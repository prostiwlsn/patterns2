﻿using System.Text;
using Microsoft.IdentityModel.Tokens;

namespace HITS_bank.Data;

public class AuthenticationOptions
{
    public const string ISSUER = "MyAuthServer";
    public const string AUDIENCE = "MyAuthClient";
    const string KEY = "mysupersecret_secretkey!123";
    public const int LIFETIME = 1;
    
    public static SymmetricSecurityKey GetSymmetricSecurityKey()
    {
        return new SymmetricSecurityKey(Encoding.ASCII.GetBytes(KEY));
    }
}