package com.mobileia.authentication.listener;

/**
 * Created by matiascamiletti on 1/8/17.
 */

public interface AccessTokenResult {
    void onSuccess(String accessToken);
    void onError();
}
