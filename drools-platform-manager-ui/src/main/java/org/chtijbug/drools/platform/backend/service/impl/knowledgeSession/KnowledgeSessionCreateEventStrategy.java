package org.chtijbug.drools.platform.backend.service.impl.knowledgeSession;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.session.SessionCreatedEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.entity.SessionRuntimeStatus;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeRepository;
import org.chtijbug.drools.platform.persistence.SessionRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;
import org.chtijbug.drools.platform.persistence.pojo.SessionRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */

@Component
public class KnowledgeSessionCreateEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(KnowledgeSessionCreateEventStrategy.class);

    @Autowired
    PlatformRuntimeRepository platformRuntimeRepository;

    @Autowired
    SessionRuntimeRepository sessionRuntimeRepository;

    @Override
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        SessionCreatedEvent sessionCreatedEvent = (SessionCreatedEvent) historyEvent;
        SessionRuntime existingSessionRutime = sessionRuntimeRepository.findBySessionIdAndStartDateIsNull(historyEvent.getSessionId());
        if (existingSessionRutime != null) {
            existingSessionRutime.setEndDate(new Date());
            existingSessionRutime.setSessionRuntimeStatus(SessionRuntimeStatus.CRASHED);
            sessionRuntimeRepository.save(existingSessionRutime);
        }
        PlatformRuntime platformRuntime = platformRuntimeRepository.findByRuleBaseIDAndEndDateNull(sessionCreatedEvent.getRuleBaseID());
        SessionRuntime sessionRuntime = new SessionRuntime();
        sessionRuntime.setPlatformRuntime(platformRuntime);
        sessionRuntime.setStartDate(sessionCreatedEvent.getDateEvent());
        sessionRuntime.setSessionId(sessionCreatedEvent.getSessionId());
        sessionRuntime.setEventID(sessionCreatedEvent.getEventID());
        existingSessionRutime.setSessionRuntimeStatus(SessionRuntimeStatus.STARTED);
        sessionRuntimeRepository.save(sessionRuntime);
        LOG.info("SessionCreatedEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof SessionCreatedEvent;
    }
}
