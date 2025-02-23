﻿using System.ComponentModel.DataAnnotations;

#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member
namespace HITS_bank.Controllers.Dto.Request;

/// <summary>
/// Создание кредита
/// </summary>
public class CreateLoanRequestDto
{
    public Guid TariffId { get; set; }
    
    public Guid UserId { get; set; }
    
    [Range(1, 5)]
    public int DurationInYears { get; set; }
    
    [Range(1, int.MaxValue)]
    public int Amount { get; set; }
}