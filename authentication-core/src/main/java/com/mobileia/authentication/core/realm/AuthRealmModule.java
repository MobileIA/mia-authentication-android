package com.mobileia.authentication.core.realm;

import com.mobileia.authentication.core.entity.User;

import io.realm.annotations.RealmModule;

/**
 * Created by matiascamiletti on 31/1/18.
 */
@RealmModule(library = true, allClasses = false, classes = { User.class })
public class AuthRealmModule {
}
