package org.chtijbug.drools.platform.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 06/05/14
 * Time: 16:16
 * To change this template use File | Settings | File Templates.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Heartbeat {
    Date lastAlive;

    public Heartbeat() {
    }

    public Heartbeat(Date lastAlive) {
        this.lastAlive = lastAlive;
    }

    public Date getLastAlive() {
        return lastAlive;
    }

    public void setLastAlive(Date lastAlive) {
        this.lastAlive = lastAlive;
    }
}
