package com.mobileia.authentication.listener;

import com.mobileia.core.entity.Error;

/**
 * Created by matiascamiletti on 1/8/17.
 */

public interface RegisterResult {
    void onSuccess(int userId);
    void onError(Error error);
}
