package com.pymmasoftware.platform.login.loginmodule.realm;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.realm.JAASRealm;

/**
 * Created by gjnkouyee on 3/16/14.
 */
public class DroolsJaasRealm extends JAASRealm {

    private String jaasConfig;

    public String getJaasConfig() {
        return jaasConfig;
    }

    public void setJaasConfig(String jaasConfig) {
        this.jaasConfig = jaasConfig;
    }

    public void initialize() throws LifecycleException {
        System.setProperty("javax.security.auth.login.Configuration", jaasConfig);
        super.init();
    }
}
