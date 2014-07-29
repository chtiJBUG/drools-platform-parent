package org.chtijbug.drools.platform.web.model;

import org.chtijbug.drools.platform.persistence.pojo.FactType;

/**
 * Created by alexandre on 24/07/2014.
 */
public class FactTypeObject {
    private FactType type;
    private String description;

    public FactTypeObject(FactType type, String description) {
        this.type = type;
        this.description = description;
    }

    public FactType getType() {
        return type;
    }

    public void setType(FactType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
