namespace patterns2_infoauth.Model
{
    public class TokenDto
    {
        public string AccessToken { get; set; }
        public string RefreshToken { get; set; }
        public int ExpiresIn { get; set; } = 3600;
    }
}
