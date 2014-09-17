package com.pymmasoftware.platform.login.loginmodule.principal;

import java.io.Serializable;
import java.security.Principal;

/**
 * Created by gjnkouyee on 3/16/14.
 */
public class DroolsPrincipal implements Principal, Serializable {

    private String name = "";

    public DroolsPrincipal(String name) {
        if (name == null) {
            throw new NullPointerException("invalid Input");
        }
        this.name = name;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return this.name;
    }

}