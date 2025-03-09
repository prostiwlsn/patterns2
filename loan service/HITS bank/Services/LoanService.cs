using System.Text;
using System.Text.Json;
using AutoMapper;
using HITS_bank.Controllers.Dto;
using HITS_bank.Controllers.Dto.Common;
using HITS_bank.Controllers.Dto.Message;
using HITS_bank.Controllers.Dto.Request;
using HITS_bank.Controllers.Dto.Response;
using HITS_bank.Data.Entities;
using HITS_bank.Repositories;
using HITS_bank.Utils;
using Microsoft.Extensions.Logging.Abstractions;
using RabbitMQ.Client;
using IResult = HITS_bank.Utils.IResult;

#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

namespace HITS_bank.Services;

/// <summary>
/// Сервис кредитов
/// </summary>
public class LoanService : ILoanService
{
    private readonly ILoanRepository _loanRepository;
    private readonly IMapper _mapper;
    private readonly string _amqpConnectionString;
    private readonly string _getLoanExchange;
    
    public LoanService(ILoanRepository loanRepository, IMapper mapper)
    {
        _loanRepository = loanRepository;
        _mapper = mapper;
        
        IConfigurationRoot configurationRoot = new ConfigurationBuilder()
            .AddJsonFile("appsettings.json")
            .Build();
        
        _amqpConnectionString = configurationRoot.GetSection("ConnectionStrings")["BusConnection"] ?? throw new InvalidOperationException();
        _getLoanExchange = configurationRoot.GetSection("Exchanges")["GetLoan"] ?? throw new InvalidOperationException();
    }

    /// <summary>
    /// Создание тарифа кредита
    /// </summary>
    public async Task CreateTariff(CreateTariffRequestDto createTariffRequest)
    {
        var tariffEntity = _mapper.Map<TariffEntity>(createTariffRequest);
        await _loanRepository.AddTariff(tariffEntity);
    }

    /// <summary>
    /// Удаление тарифа кредита
    /// </summary>
    public async Task<IResult> DeleteTariff(Guid tariffId)
    {
        return await _loanRepository.DeleteTariff(tariffId);
    }

    /// <summary>
    /// Обновление тарифа
    /// </summary>
    public async Task<IResult> UpdateTariff(UpdateTariffRequestDto updatedTariff, Guid tariffId)
    {
        var updatedTariffEntity = _mapper.Map<TariffEntity>(updatedTariff);
        updatedTariffEntity.Id = tariffId;

        return await _loanRepository.UpdateTariff(updatedTariffEntity, tariffId);
    }

    /// <summary>
    /// Получение списка тарифов
    /// </summary>
    public async Task<IResult> GetTariffs(int pageNumber, int pageSize)
    {
        // Валидация
        var validationResult = ValidatePagination(pageNumber, pageSize);

        if (validationResult != null)
            return validationResult;

        // Получение списка тарифов
        var offset = (pageNumber - 1) * pageSize;
        var tariffsEntities = await _loanRepository.GetTariffs(offset: offset, limit: pageSize);

        // Получение пагинации
        var paginationResponseDto = new PaginationResponseDto
        {
            PageSize = pageSize,
            PageNumber = pageNumber,
            PagesCount = tariffsEntities.Count / pageSize + 1,
        };

        // Маппинг ответа
        var tariffsDtoList = _mapper.Map<List<TariffDto>>(tariffsEntities);
        var response = new TariffsListResponseDto
        {
            Tariffs = tariffsDtoList,
            Pagination = paginationResponseDto,
        };
        return new Success<TariffsListResponseDto>(response);
    }

    /// <summary>
    /// Создание кредита
    /// </summary>
    public async Task<IResult> CreateLoan(CreateLoanRequestDto createLoanRequest)
    {
        var selectedTariff = await _loanRepository.GetTariff(createLoanRequest.TariffId);

        if (selectedTariff == null)
            return new Error(StatusCodes.Status404NotFound, "Tariff not found");

        var loanEntity = _mapper.Map<LoanEntity>(createLoanRequest);
        loanEntity.RatePercent = selectedTariff.RatePercent;

        await _loanRepository.AddLoan(loanEntity);

        // Отправка суммы кредита на счет
        SendMoney(new GetLoanDto
        {
            Amount = loanEntity.Amount,
            AccountId = createLoanRequest.AccountId,
        });
        
        return new Success();
    }

    /// <summary>
    /// Отправка суммы кредита на счет
    /// </summary>
    private void SendMoney(GetLoanDto getLoanMessage)
    {
        var factory = new ConnectionFactory{ HostName = _amqpConnectionString };
        
        using (var connection = factory.CreateConnection())
        using (var channel = connection.CreateModel())
        {
            channel.ExchangeDeclare(exchange: _getLoanExchange, type: ExchangeType.Direct, durable: true);

            var queueName = "GetLoan";
            var routingKey = "GetLoan";
            channel.QueueDeclare(queue: queueName, durable: true, exclusive: false, autoDelete: false);
            channel.QueueBind(queue: queueName,
                exchange: _getLoanExchange,
                routingKey: routingKey);
            
            var message = JsonSerializer.Serialize(getLoanMessage);
            var body = Encoding.UTF8.GetBytes(message);
            
            var properties = channel.CreateBasicProperties();
            properties.Persistent = true;

            channel.BasicPublish(exchange: _getLoanExchange,
                routingKey: routingKey,
                basicProperties: properties,
                body: body);
        }
    }
    
    /// <summary>
    /// Получение списка кредитов пользователя
    /// </summary>
    public async Task<IResult> GetUserLoansList(Guid userId, int pageNumber, int pageSize)
    {
        // Валидация
        var validationResult = ValidatePagination(pageNumber, pageSize);

        if (validationResult != null)
            return validationResult;

        // Получение списка кредитов
        var offset = (pageNumber - 1) * pageSize;
        var loanEntities = await _loanRepository.GetUserLoansList(userId, offset, pageSize);

        // Получение пагинации
        var pagination = new PaginationResponseDto
        {
            PageNumber = pageNumber,
            PageSize = pageSize,
            PagesCount = loanEntities.Count / pageSize + 1,
        };

        // Маппинг ответа
        var loansListDto = _mapper.Map<List<LoanDto>>(loanEntities);
        var response = new LoansListResponseDto
        {
            Loans = loansListDto,
            Pagination = pagination,
        };

        return new Success<LoansListResponseDto>(response);
    }

    /// <summary>
    /// Оплатить кредит
    /// </summary>
    public async Task<LoanPaymentResultMessage> PayForLoan(LoanPaymentDto loanPayment)
    {
        // Получение кредита
        LoanEntity? loan = await _loanRepository.GetLoan(loanPayment.RecipientAccountId);

        if (loan == null)
            return new LoanPaymentResultMessage
            {
                Success = false,
                Data = new LoanPaymentResultData
                {
                    SenderAccountId = loanPayment.SenderAccountId,
                    ReturnedAmount = loanPayment.Amount,
                },
                ErrorMessage = "Loan not found",
                ErrorStatusCode = StatusCodes.Status404NotFound,
            };

        // Оплата кредита
        var debtPayment = Math.Min(loanPayment.Amount, loan.Debt);
        loan.Debt -= debtPayment;

        var result = await _loanRepository.UpdateLoan(loan);

        // Возврат лишней суммы
        if (result is Success)
        {
            var returnAmount = loanPayment.Amount - debtPayment;

            return new LoanPaymentResultMessage
            {
                Success = true,
                Data = new LoanPaymentResultData
                {
                    SenderAccountId = loanPayment.SenderAccountId,
                    ReturnedAmount = returnAmount,
                },
            };
        }
        if (result is Error error)
        {
            return new LoanPaymentResultMessage
            {
                Success = false,
                Data = new LoanPaymentResultData
                {
                    SenderAccountId = loanPayment.SenderAccountId,
                    ReturnedAmount = loanPayment.Amount,
                },
                ErrorMessage = error.Message,
                ErrorStatusCode = error.StatusCode,
            };
        }
        
        return new LoanPaymentResultMessage
        {
            Success = false,
            Data = new LoanPaymentResultData
            {
                SenderAccountId = loanPayment.SenderAccountId,
                ReturnedAmount = loanPayment.Amount,
            },
            ErrorMessage = "Что-то пошло не так",
            ErrorStatusCode = StatusCodes.Status500InternalServerError,
        };
    }

    /// <summary>
    /// Валидация пагинации
    /// </summary>
    private static IResult? ValidatePagination(int pageNumber, int pageSize)
    {
        if (pageNumber < 1)
            return new Error(StatusCodes.Status400BadRequest, "Page number cannot be less than 1");

        if (pageSize < 0)
            return new Error(StatusCodes.Status400BadRequest, "Page size cannot be less than 0");

        return null;
    }

    /// <summary>
    /// Валидация номера счета
    /// </summary>
    private static IResult? ValidateAccountNumber(string accountNumber)
    {
        if (accountNumber.Length != 20)
            return new Error(StatusCodes.Status400BadRequest, "Account number must be 20 characters long");

        if (accountNumber.Any(x => !char.IsDigit(x)))
            return new Error(StatusCodes.Status400BadRequest, "Account number must contain only digits");

        return null;
    }
}