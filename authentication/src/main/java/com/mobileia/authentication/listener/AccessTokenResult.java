package com.mobileia.authentication.listener;

import android.support.annotation.Keep;

import com.mobileia.core.entity.Error;

/**
 * Created by matiascamiletti on 1/8/17.
 */
@Keep
public interface AccessTokenResult {
    void onSuccess(String accessToken);
    void onError(Error error);
}
