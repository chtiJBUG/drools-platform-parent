package org.chtijbug.drools.platform.entity;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 06/05/14
 * Time: 16:16
 * To change this template use File | Settings | File Templates.
 */
public class Heartbeat {
    Date lastAlive;

    public Heartbeat() {
    }

    public Date getLastAlive() {
        return lastAlive;
    }

    public void setLastAlive(Date lastAlive) {
        this.lastAlive = lastAlive;
    }
}
