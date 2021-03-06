package com.mobileia.authentication.core.realm;

import android.content.Context;

import com.mobileia.authentication.core.entity.User;
import com.mobileia.core.Mobileia;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;

/**
 * Created by matiascamiletti on 31/1/18.
 */

public class AuthBaseRealm {
    /**
     * Almacena la unica instancia de la libreria
     */
    private static AuthBaseRealm sOurInstance = new AuthBaseRealm();
    /**
     * Almacena el contexto
     */
    protected Context mContext;

    /**
     * Obtiene la instancia creada
     * @return
     */
    public static AuthBaseRealm getInstance() {
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
     * Se encarga de actualizar los datos del usuario
     * @param user
     */
    public void update(final User user){
        // Obtenemos instancia de realm
        Realm realm = getRealm();
        // Obtener usuario logueado
        final User current = fetchUser();
        // Ejecutamos transaccion para guardar
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Asignamos nuevos datos
                current.setFirstname(user.getFirstname());
                current.setLastname(user.getLastname());
                current.setPhoto(user.getPhoto());
                current.setPhone(user.getPhone());
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
                .name("mobileia_authentication_" + Mobileia.getInstance().getAppId() + ".realm")
                //.schemaVersion(1)
                .modules(new AuthRealmModule())
                .build();

        return Realm.getInstance(config);
    }
    /**
     * Constructor del singleton
     */
    private AuthBaseRealm() {
    }
}
