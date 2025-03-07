using System.Text.Json.Serialization;

namespace HITS_bank.Services.idk;

public class Message
{
    //[JsonPropertyName("Id")]
    public Guid Id { get; set; }
}