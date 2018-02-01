package com.mobileia.authentication.core.listener;

import android.support.annotation.Keep;

import com.mobileia.core.entity.Error;

/**
 * Created by matiascamiletti on 31/1/18.
 */

@Keep
public interface RegisterResult {
    void onSuccess(int userId);
    void onError(Error error);
}
