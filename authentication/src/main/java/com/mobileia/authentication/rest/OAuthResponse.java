package com.mobileia.authentication.rest;

/**
 * Created by matiascamiletti on 31/7/17.
 */

public class OAuthResponse {
    public int id;
    public String access_token;
    public int expires_in;
    public String token_type;
    public String scope;
    public String refresh_token;
    public int user_id;
}
