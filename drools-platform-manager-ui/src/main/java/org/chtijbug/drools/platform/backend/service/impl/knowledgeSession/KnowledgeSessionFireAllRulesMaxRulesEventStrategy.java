package org.chtijbug.drools.platform.backend.service.impl.knowledgeSession;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.session.SessionFireAllRulesMaxNumberReachedEvent;
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
public class KnowledgeSessionFireAllRulesMaxRulesEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(KnowledgeSessionFireAllRulesMaxRulesEventStrategy.class);

    @Autowired
    FireRulesRuntimeRepository fireRulesRuntimeRepository;

    @Autowired
    SessionRuntimeRepository sessionRuntimeRepository;

    @Override
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        SessionFireAllRulesMaxNumberReachedEvent sessionFireAllRulesMaxNumberReachedEvent = (SessionFireAllRulesMaxNumberReachedEvent) historyEvent;
        FireRulesRuntime fireRulesRuntime = fireRulesRuntimeRepository.findStartedFireAllRulesBySessionID(historyEvent.getSessionId());
        fireRulesRuntime.setEventID(sessionFireAllRulesMaxNumberReachedEvent.getEventID());
        fireRulesRuntime.setEndDate(sessionFireAllRulesMaxNumberReachedEvent.getDateEvent());
        fireRulesRuntime.setFireRulesRuntimeStatus(FireRulesRuntimeStatus.MAXNBRULES);
        fireRulesRuntime.setNbreRulesFired(Long.valueOf(sessionFireAllRulesMaxNumberReachedEvent.getNumberOfRulesExecuted()));
        fireRulesRuntime.setMaxNbreRulesDefinedForSession(Long.valueOf(sessionFireAllRulesMaxNumberReachedEvent.getMaxNumberOfRulesForSession()));
        fireRulesRuntimeRepository.save(fireRulesRuntime);
        LOG.info("SessionFireAllRulesMaxNumberReachedEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof SessionFireAllRulesMaxNumberReachedEvent;
    }
}
