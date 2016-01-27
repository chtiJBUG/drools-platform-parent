package org.chtijbug.drools.platform.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by nheron on 26/01/2016.
 */
@Component
public class GuvnorData {

    @Value(value = "${guvnor.url}")
    private String guvnorUrl;
    @Value(value ="${guvnor.appName}")
    private String guvnorApp;
    @Value(value ="${guvnor.username}")
    private String guvnorUserName;
    @Value(value ="${guvnor.password}")
    private String guvnorPassword;

    public String getGuvnorUrl() {
        return guvnorUrl;
    }

    public void setGuvnorUrl(String guvnorUrl) {
        this.guvnorUrl = guvnorUrl;
    }

    public String getGuvnorApp() {
        return guvnorApp;
    }

    public void setGuvnorApp(String guvnorApp) {
        this.guvnorApp = guvnorApp;
    }

    public String getGuvnorUserName() {
        return guvnorUserName;
    }

    public void setGuvnorUserName(String guvnorUserName) {
        this.guvnorUserName = guvnorUserName;
    }

    public String getGuvnorPassword() {
        return guvnorPassword;
    }

    public void setGuvnorPassword(String guvnorPassword) {
        this.guvnorPassword = guvnorPassword;
    }
}
