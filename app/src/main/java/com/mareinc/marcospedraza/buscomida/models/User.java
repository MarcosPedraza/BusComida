package com.mareinc.marcospedraza.buscomida.models;

public class User {

    String user_name;
    String tel;
    String profile_url;
    String email;

    public User() {
    }

    public User(String user_name, String tel, String profile_url, String email) {
        this.user_name = user_name;
        this.tel = tel;
        this.profile_url = profile_url;
        this.email = email;
    }


    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getTel() {
        return tel;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public String getEmail() {
        return email;
    }
}
