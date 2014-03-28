package org.chtijbug.drools.platform.backend.service.impl.knowledgeSession;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.session.SessionFireAllRulesEndEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.pojo.FireRulesRuntimeStatus;
import org.chtijbug.drools.platform.persistence.FireRulesRuntimeRepository;
import org.chtijbug.drools.platform.persistence.SessionRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.FireRulesRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */

@Component
public class KnowledgeSessionFireAllRulesEndEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(KnowledgeSessionFireAllRulesEndEventStrategy.class);

    @Autowired
    FireRulesRuntimeRepository fireRulesRuntimeRepository;

    @Autowired
    SessionRuntimeRepository sessionRuntimeRepository;

    @Override
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        SessionFireAllRulesEndEvent sessionFireAllRulesEndEvent = (SessionFireAllRulesEndEvent) historyEvent;
        FireRulesRuntime fireRulesRuntime = fireRulesRuntimeRepository.findStartedFireAllRulesBySessionID(historyEvent.getSessionId());
        fireRulesRuntime.setEventID(sessionFireAllRulesEndEvent.getEventID());
        fireRulesRuntime.setEndDate(sessionFireAllRulesEndEvent.getDateEvent());
        fireRulesRuntime.setFireRulesRuntimeStatus(FireRulesRuntimeStatus.STOPPED);
        fireRulesRuntime.setNbreRulesFired(Long.valueOf(sessionFireAllRulesEndEvent.getNumberRulesExecuted()));
        fireRulesRuntime.setExecutionTime(Long.valueOf(sessionFireAllRulesEndEvent.getExecutionTime()));
        fireRulesRuntimeRepository.save(fireRulesRuntime);
        LOG.info("SessionFireAllRulesEndEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof SessionFireAllRulesEndEvent;
    }
}