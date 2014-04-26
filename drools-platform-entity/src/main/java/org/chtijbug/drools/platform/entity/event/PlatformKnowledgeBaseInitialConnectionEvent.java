package org.chtijbug.drools.platform.entity.event;

import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseEvent;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/01/14
 * Time: 20:52
 * To change this template use File | Settings | File Templates.
 */
public class PlatformKnowledgeBaseInitialConnectionEvent extends KnowledgeBaseEvent {
    private String hostname;
    private int port;
    private Date startDate;

    public PlatformKnowledgeBaseInitialConnectionEvent(int eventID, Date dateEvent, int ruleBaseID) {
        super(eventID, dateEvent, ruleBaseID);
        this.startDate = dateEvent;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PlatformKnowledgeBaseCreatedEvent{");
        sb.append("ws_hostname='").append(hostname).append('\'');
        sb.append(", ws_port=").append(port);
        sb.append(", date=").append(startDate);
        sb.append('}');
        return sb.toString();
    }
}
