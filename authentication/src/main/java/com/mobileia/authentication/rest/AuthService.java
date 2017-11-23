package com.mobileia.authentication.rest;

import com.google.gson.JsonObject;
import com.mobileia.authentication.entity.AccessToken;
import com.mobileia.authentication.entity.User;
import com.mobileia.core.rest.RestBodyCall;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by matiascamiletti on 31/7/17.
 */

public interface AuthService {

    @FormUrlEncoded
    @POST("api/oauth")
    RestBodyCall<AccessToken> oauth(
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

    @FormUrlEncoded
    @POST("api/oauth")
    RestBodyCall<AccessToken> oauthWithFacebook(
            @Field("app_id") int app_id,
            @Field("grant_type") String grant_type,
            @Field("facebook_id") String facebook_id,
            @Field("facebook_access_token") String facebook_access_token,
            @Field("device_token") String device_token,
            @Field("device_model") String device_model,
            @Field("platform") int platform,
            @Field("language") String language,
            @Field("version") String version
    );

    @FormUrlEncoded
    @POST("api/oauth")
    RestBodyCall<AccessToken> oauthWithGoogle(
            @Field("app_id") int app_id,
            @Field("grant_type") String grant_type,
            @Field("google_token") String google_token,
            @Field("device_token") String device_token,
            @Field("device_model") String device_model,
            @Field("platform") int platform,
            @Field("language") String language,
            @Field("version") String version
    );

    @FormUrlEncoded
    @POST("api/oauth")
    RestBodyCall<AccessToken> oauthWithTwitter(
            @Field("app_id") int app_id,
            @Field("grant_type") String grant_type,
            @Field("twitter_token") String twitter_token,
            @Field("twitter_secret") String twitter_secret,
            @Field("device_token") String device_token,
            @Field("device_model") String device_model,
            @Field("platform") int platform,
            @Field("language") String language,
            @Field("version") String version
    );

    @FormUrlEncoded
    @POST("api/register")
    RestBodyCall<User> register(
            @Field("app_id") int app_id,
            @Field("register_type") String grant_type,
            @Field("email") String email,
            @Field("password") String password,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("photo") String photo,
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST("api/register")
    RestBodyCall<User> registerWithFacebook(
            @Field("app_id") int app_id,
            @Field("register_type") String grant_type,
            @Field("facebook_id") String facebook_id,
            @Field("facebook_access_token") String facebook_access_token
    );

    @FormUrlEncoded
    @POST("api/register")
    RestBodyCall<User> registerWithGoogle(
            @Field("app_id") int app_id,
            @Field("register_type") String grant_type,
            @Field("google_token") String google_token
    );

    @FormUrlEncoded
    @POST("api/register")
    RestBodyCall<User> registerWithTwitter(
            @Field("app_id") int app_id,
            @Field("register_type") String grant_type,
            @Field("twitter_token") String twitter_token,
            @Field("twitter_secret") String twitter_secret
    );

    @FormUrlEncoded
    @POST("api/me")
    RestBodyCall<User> me(
            @Field("app_id") int app_id,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST("api/recovery")
    RestBodyCall<Boolean> recovery(
            @Field("app_id") int app_id,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api/location/register")
    RestBodyCall<Boolean> locationRegister(
            @Field("app_id") int app_id,
            @Field("access_token") String access_token,
            @Field("lat") double latitude,
            @Field("lng") double longitude
    );

    @FormUrlEncoded
    @POST("api/logout")
    RestBodyCall<Boolean> logout(
            @Field("app_id") int app_id,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST("api/update")
    RestBodyCall<User> update(
            @Field("app_id") int app_id,
            @Field("access_token") String access_token,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("photo") String photo,
            @Field("phone") String phone
    );
}
