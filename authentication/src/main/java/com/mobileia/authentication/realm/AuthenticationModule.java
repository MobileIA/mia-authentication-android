package com.mobileia.authentication.realm;

import com.mobileia.authentication.entity.User;

import io.realm.annotations.RealmModule;

/**
 * Created by matiascamiletti on 31/7/17.
 */
@RealmModule(library = true, classes = { User.class })
public class AuthenticationModule {
}
