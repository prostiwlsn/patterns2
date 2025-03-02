namespace patterns2_infoauth.Model
{
    public class GetSessionStatusRequest
    {
        public Guid SessionId { get; set; }
    }

    public class GetSessionStatusResponse
    {
        public bool IsSessionActive { get; set; }
    }
}
