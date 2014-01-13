package org.chtijbug.drools.platform.entity.event;

import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseCreatedEvent;
import org.chtijbug.drools.platform.entity.PlatformRuntime;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/01/14
 * Time: 20:52
 * To change this template use File | Settings | File Templates.
 */
public class PlatformKnowledgeBaseCreatedEvent extends KnowledgeBaseCreatedEvent {
    private PlatformRuntime platformRuntime;

    public PlatformKnowledgeBaseCreatedEvent(int eventID, Date dateEvent, int ruleBaseID, PlatformRuntime platformRuntime) {
        super(eventID, dateEvent, ruleBaseID);
        this.platformRuntime = platformRuntime;
    }

    public PlatformRuntime getPlatformRuntime() {
        return platformRuntime;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PlatformKnowledgeBaseCreatedEvent{");
        sb.append("platformRuntime=").append(platformRuntime);
        sb.append('}');
        return sb.toString();
    }
}
