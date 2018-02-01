package com.mobileia.authentication.core.entity;

import io.realm.annotations.PrimaryKey;

/**
 * Created by matiascamiletti on 1/2/18.
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
