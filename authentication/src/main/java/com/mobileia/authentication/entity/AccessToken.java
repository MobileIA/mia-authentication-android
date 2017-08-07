package com.mobileia.authentication.entity;

import io.realm.annotations.PrimaryKey;

/**
 * Created by matiascamiletti on 7/8/17.
 */

public class AccessToken {
    @PrimaryKey
    public int id;
    public int app_id;
    public int user_id;
    public String access_token;
    public String expires;
    public String scope;
}
