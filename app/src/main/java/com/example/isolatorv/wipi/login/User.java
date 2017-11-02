package com.example.isolatorv.wipi.login;

/**
 * Created by isolatorv on 2017. 9. 10..
 */
public class User {

    private String name;
    private String email;
    private String unique_id;
    private String password;
    private String old_password;
    private String new_password;
    private Boolean is_logined;
    private int sno;

    public int getSno() {
        return sno;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public Boolean getIs_logined() {
        return is_logined;
    }

    public void setIs_logined(Boolean is_logined) {
        this.is_logined = is_logined;
    }
}