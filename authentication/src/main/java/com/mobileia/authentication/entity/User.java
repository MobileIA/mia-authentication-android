package com.mobileia.authentication.entity;

import android.support.annotation.Keep;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by matiascamiletti on 31/7/17.
 */
@Keep
public class User extends RealmObject {
    @PrimaryKey
    protected int id;
    protected int app_id;
    protected String access_token;
    protected String email;
    protected String firstname;
    protected String lastname;
    protected String photo;
    protected String phone;
    protected Date created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppId() {
        return app_id;
    }

    public void setAppId(int app_id) {
        this.app_id = app_id;
    }

    public String getAccessToken() {
        return access_token;
    }

    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Date created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return firstname + " " + lastname;
    }
}
