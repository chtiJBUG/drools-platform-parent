package org.chtijbug.drools.platform.entity.event;

import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseCreatedEvent;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 21/01/14
 * Time: 18:12
 * To change this template use File | Settings | File Templates.
 */
public class PlatformKnowledgeBaseShutdownEvent  extends KnowledgeBaseCreatedEvent {
    private Date endDate;

    public PlatformKnowledgeBaseShutdownEvent(int eventID, Date dateEvent, int ruleBaseID, Date endDate) {
        super(eventID, dateEvent, ruleBaseID);
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PlatformKnowledgeBaseShutdownEvent{");
        sb.append("endDate=").append(endDate);
        sb.append('}');
        return sb.toString();
    }
}
