package org.chtijbug.drools.platform.backend.service.impl.knowledgeSession;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.session.SessionFireAllRulesBeginEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.pojo.FireRulesRuntimeStatus;
import org.chtijbug.drools.platform.persistence.FireRulesRuntimeRepository;
import org.chtijbug.drools.platform.persistence.SessionRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.FireRulesRuntime;
import org.chtijbug.drools.platform.persistence.pojo.SessionRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */

@Component
public class KnowledgeSessionFireAllRulesBeginEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(KnowledgeSessionFireAllRulesBeginEventStrategy.class);

    @Autowired
    FireRulesRuntimeRepository fireRulesRuntimeRepository;

    @Autowired
    SessionRuntimeRepository sessionRuntimeRepository;

    @Override
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        SessionFireAllRulesBeginEvent sessionFireAllRulesBeginEvent = (SessionFireAllRulesBeginEvent) historyEvent;

        List<FireRulesRuntime> fireRulesRuntimes = fireRulesRuntimeRepository.findAllStartedFireAllRulesBySessionID(historyEvent.getSessionId());
        for (FireRulesRuntime runningFireRulesRuntime : fireRulesRuntimes){
            runningFireRulesRuntime.setEndDate(new Date());
            runningFireRulesRuntime.setFireRulesRuntimeStatus(FireRulesRuntimeStatus.CRASHED);
            fireRulesRuntimeRepository.save(runningFireRulesRuntime);
        }
        SessionRuntime existingSessionRutime = sessionRuntimeRepository.findBySessionIdAndStartDateIsNull(historyEvent.getSessionId());

        FireRulesRuntime fireRulesRuntime = new FireRulesRuntime();
        fireRulesRuntime.setSessionRuntime(existingSessionRutime);
        fireRulesRuntime.setEventID(sessionFireAllRulesBeginEvent.getEventID());
        fireRulesRuntime.setStartDate(sessionFireAllRulesBeginEvent.getDateEvent());
        fireRulesRuntime.setFireRulesRuntimeStatus(FireRulesRuntimeStatus.STARTED);
        fireRulesRuntimeRepository.save(fireRulesRuntime);
        LOG.info("SessionFireAllRulesBeginEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof SessionFireAllRulesBeginEvent;
    }
}
