package com.mobileia.authentication.core.rest.service;

import com.google.gson.JsonObject;
import com.mobileia.authentication.core.entity.AccessToken;
import com.mobileia.authentication.core.entity.User;
import com.mobileia.core.rest.RestBodyCall;

import java.util.ArrayList;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by matiascamiletti on 31/1/18.
 */

public interface UserService {

    @FormUrlEncoded
    @POST("api/me")
    RestBodyCall<User> me(
            @Field("app_id") int app_id,
            @Field("access_token") String access_token
    );

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("api/oauth")
    RestBodyCall<AccessToken> oauth(@Body JsonObject params);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("api/register")
    RestBodyCall<User> register(@Body JsonObject params);
}
