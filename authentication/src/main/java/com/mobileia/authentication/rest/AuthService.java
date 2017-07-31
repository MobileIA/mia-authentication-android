package com.mobileia.authentication.rest;

import com.mobileia.authentication.entity.OAuth;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by matiascamiletti on 31/7/17.
 */

public interface AuthService {

    @FormUrlEncoded
    @POST("oauth")
    Call<OAuth> createAccessToken(
            @Field("app_id") int app_id,
            @Field("grant_type") String grant_type,
            @Field("email") String email,
            @Field("password") String password,
            @Field("device_token") String device_token,
            @Field("device_model") String device_model,
            @Field("platform") int platform,
            @Field("language") String language,
            @Field("version") String version
    );
}
