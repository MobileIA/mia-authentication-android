package com.mobileia.authentication.listener;

import android.support.annotation.Keep;

import com.mobileia.core.entity.Error;

/**
 * Created by matiascamiletti on 15/8/17.
 */
@Keep
public interface RecoveryResult {
    void onSuccess();
    void onError(Error error);
}
