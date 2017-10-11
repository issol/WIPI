package com.example.isolatorv.wipi.login;

/**
 * Created by isolatorv on 2017. 9. 10..
 */

public class ServerRequest {

    private String operation;
    private User user;

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setUser(User user) {
        this.user = user;
    }
}