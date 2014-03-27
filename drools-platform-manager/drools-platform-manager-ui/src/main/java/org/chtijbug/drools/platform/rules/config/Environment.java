package org.chtijbug.drools.platform.rules.config;

import java.net.URL;

public class Environment {
    private String name;
    private URL url;

    public Environment(String name, URL url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

}
