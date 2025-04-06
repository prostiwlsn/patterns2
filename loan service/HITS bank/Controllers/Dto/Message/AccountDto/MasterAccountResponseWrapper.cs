using System.Text.Json.Serialization;

namespace HITS_bank.Controllers.Dto.Message.AccountDto;
#pragma warning disable CS1591 // Missing XML comment for publicly visible type or member

public class MasterAccountResponseWrapper
{
    [JsonPropertyName("success")]
    public bool Success { get; set; }
    
    [JsonPropertyName("data")]
    public AccountDto Data { get; set; }
    
    [JsonPropertyName("errorMessage")]
    public string ErrorMessage { get; set; }
}