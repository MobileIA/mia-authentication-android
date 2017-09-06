package com.mobileia.authentication.listener;

import com.mobileia.core.entity.Error;

/**
 * Created by matiascamiletti on 5/9/17.
 */

public interface UpdateResult {
    void onSuccess(int userId);
    void onError(Error error);
}
