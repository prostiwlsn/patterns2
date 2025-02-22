namespace patterns2_infoauth.Model
{
    public class GetUserResponse
    {
        public bool Success { get; set; }
        public UserInfoDto? Data { get; set; } = null;
    }
}
