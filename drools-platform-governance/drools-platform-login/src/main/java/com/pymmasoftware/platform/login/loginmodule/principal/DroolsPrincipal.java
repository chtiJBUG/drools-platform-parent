package com.pymmasoftware.platform.login.loginmodule.principal;

import java.io.Serializable;
import java.security.Principal;

/**
 * Created by gjnkouyee on 3/16/14.
 */
public class DroolsPrincipal implements Principal, Serializable {

    private String name = "";
    private int id;
    public DroolsPrincipal(String name) {
        if (name == null) {
            throw new NullPointerException("invalid Input");
        }
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return this.name;
    }

}