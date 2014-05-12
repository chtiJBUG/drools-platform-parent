package org.chtijbug.drools.platform.backend.topic.pojo;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 12/05/14
 * Time: 10:01
 * To change this template use File | Settings | File Templates.
 */
public class HeartBeatData extends BaseTopicData {

    private Date lastTimeAlive;

    public HeartBeatData() {
        super();
    }

    public Date getLastTimeAlive() {
        return lastTimeAlive;
    }

    public void setLastTimeAlive(Date lastTimeAlive) {
        this.lastTimeAlive = lastTimeAlive;
    }
}
