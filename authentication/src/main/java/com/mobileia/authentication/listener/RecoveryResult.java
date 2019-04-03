package com.mobileia.authentication.listener;

import com.mobileia.core.entity.Error;

/**
 * Created by matiascamiletti on 15/8/17.
 */
public interface RecoveryResult {
    void onSuccess();
    void onError(Error error);
}
