package com.example.isolatorv.wipi;

import java.io.Serializable;

/**
 * Created by isolatorv on 2017. 10. 25..
 */

public class UserData implements Serializable {

    public String u_name, u_email;
    public int u_sno;

    public String p_name, p_type, p_age;

    public UserData(String u_name, String u_email, int u_sno, String p_name, String p_type, String p_age){
        this.u_name = u_name;
        this.u_email = u_email;
        this.u_sno = u_sno;
        this.p_name = p_name;
        this.p_type = p_type;
        this.p_age = p_age;
    }

}
