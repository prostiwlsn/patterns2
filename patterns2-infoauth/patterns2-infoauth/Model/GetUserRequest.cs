namespace patterns2_infoauth.Model
{
    public class GetUserRequest : RequestWithToken
    {
        public Guid Id { get; set; }
    }
}
