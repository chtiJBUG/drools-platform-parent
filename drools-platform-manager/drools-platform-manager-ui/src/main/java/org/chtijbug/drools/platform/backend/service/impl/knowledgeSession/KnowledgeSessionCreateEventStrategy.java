package org.chtijbug.drools.platform.backend.service.impl.knowledgeSession;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.session.SessionCreatedEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepository;
import org.chtijbug.drools.platform.persistence.SessionRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.chtijbug.drools.platform.persistence.pojo.SessionRuntime;
import org.chtijbug.drools.platform.persistence.pojo.SessionRuntimeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
    PlatformRuntimeInstanceRepository platformRuntimeInstanceRepository;

    @Autowired
    SessionRuntimeRepository sessionRuntimeRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        SessionCreatedEvent sessionCreatedEvent = (SessionCreatedEvent) historyEvent;
        SessionRuntime existingSessionRutime = sessionRuntimeRepository.findByRuleBaseIDAndSessionIdAndEndDateIsNull(historyEvent.getRuleBaseID(), historyEvent.getSessionId());
        if (existingSessionRutime != null) {
            existingSessionRutime.setEndDate(new Date());
            existingSessionRutime.setSessionRuntimeStatus(SessionRuntimeStatus.CRASHED);
            sessionRuntimeRepository.save(existingSessionRutime);
        }
        List<PlatformRuntimeInstance> platformRuntimeInstances = platformRuntimeInstanceRepository.findByRuleBaseIDAndEndDateNull(sessionCreatedEvent.getRuleBaseID());
        if (platformRuntimeInstances.size()==1) {
            SessionRuntime sessionRuntime = new SessionRuntime();
            sessionRuntime.setPlatformRuntimeInstance(platformRuntimeInstances.get(0));
            sessionRuntime.setStartDate(sessionCreatedEvent.getDateEvent());
            sessionRuntime.setSessionId(sessionCreatedEvent.getSessionId());
            sessionRuntime.setEventID(sessionCreatedEvent.getEventID());
            sessionRuntime.setSessionRuntimeStatus(SessionRuntimeStatus.STARTED);
            sessionRuntimeRepository.save(sessionRuntime);
        }
        LOG.debug("SessionCreatedEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof SessionCreatedEvent;
    }
}
