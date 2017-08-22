package com.mobileia.authentication.realm;

import android.content.Context;

import com.mobileia.authentication.entity.User;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;

/**
 * Created by matiascamiletti on 31/7/17.
 */

public class AuthenticationRealm {
    /**
     * Almacena la unica instancia de la libreria
     */
    private static AuthenticationRealm sOurInstance = new AuthenticationRealm();
    /**
     * Almacena el contexto
     */
    protected Context mContext;

    /**
     * Obtiene la instancia creada
     * @return
     */
    public static AuthenticationRealm getInstance() {
        return sOurInstance;
    }

    /**
     * Guardar el objeto usuario en al DB
     * @param user
     */
    public void save(final User user){
        // Obtenemos instancia de realm
        Realm realm = getRealm();
        // Ejecutamos transaccion para guardar
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Copiamos objeto del usuario
                realm.copyToRealmOrUpdate(user);
            }
        });
    }

    /**
     * Devuelve el usuario guardado si existe
     * @return
     */
    public User fetchUser(){
        // Obtenemos instancia de realm
        Realm realm = getRealm();
        // Construimos query para buscar todos los usuarios
        RealmQuery<User> query = realm.where(User.class);
        // Devolvemos el usuario si existe
        return query.findFirst();
    }

    /**
     * Elimina el usuario pasado por parametro
     * @param user
     */
    public void deleteUser(final User user){
        // Obtenemos instancia de realm
        Realm realm = getRealm();
        // Ejecutamos transaccion para guardar
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Eliminamos usuario
                user.deleteFromRealm();
            }
        });
    }
    /**
     * Obtiene la instancia de Realm
     * @return
     */
    protected Realm getRealm(){
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("mobileia_authentication.realm")
                //.schemaVersion(1)
                .modules(new AuthenticationModule())
                .build();

        return Realm.getInstance(config);
    }
    /**
     * Constructor del singleton
     */
    private AuthenticationRealm() {
    }
}
