using System.Text.Json.Serialization;

namespace HITS_bank.Controllers.Dto.Message.AccountDto;
#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

public class AccountDto
{
    [JsonPropertyName("id")]
    public Guid Id { get; set; }
    
    [JsonPropertyName("accountNumber")]
    public string AccountNumber { get; set; }
    
    [JsonPropertyName("balance")]
    public float Balance { get; set; }
    
    [JsonPropertyName("currency")]
    public CurrencyEnum Currency { get; set; }
    
    [JsonPropertyName("userId")]
    public Guid UserId { get; set; }
    
    [JsonPropertyName("isDeleted")]
    public bool IsDeleted { get; set; }
    
    [JsonPropertyName("createdDateTime")]
    public DateTime CreateDateTime { get; set; }
}