package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.knowledgeSession;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.DroolsFactObject;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.session.SessionFireAllRulesAndStartProcess;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepository;
import org.chtijbug.drools.platform.persistence.SessionExecutionRepository;
import org.chtijbug.drools.platform.persistence.pojo.Fact;
import org.chtijbug.drools.platform.persistence.pojo.FactType;
import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */

@Component
public class KnowledgeSessionFireAllRulesAndStartProcessEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(KnowledgeSessionFireAllRulesAndStartProcessEventStrategy.class);

    @Autowired
    PlatformRuntimeInstanceRepository platformRuntimeInstanceRepository;

    @Autowired
    SessionExecutionRepository sessionExecutionRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        SessionFireAllRulesAndStartProcess sessionFireAllRulesAndStartProcess = (SessionFireAllRulesAndStartProcess) historyEvent;
        SessionExecution existingSessionRutime = sessionExecutionRepository.findByRuleBaseIDAndSessionIdAndEndDateIsNull(historyEvent.getRuleBaseID(), historyEvent.getSessionId());
        if (existingSessionRutime != null) {
            if (sessionFireAllRulesAndStartProcess.getInputObject() != null) {
                DroolsFactObject inputObject = sessionFireAllRulesAndStartProcess.getInputObject();
                Fact inputFact = new Fact();
                inputFact.setEventid(sessionFireAllRulesAndStartProcess.getEventID());
                inputFact.setFactType(FactType.INPUTDATA);
                inputFact.setFullClassName(inputObject.getFullClassName());
                inputFact.setJsonFact(inputObject.getRealObject_JSON());
                inputFact.setModificationDate(sessionFireAllRulesAndStartProcess.getDateEvent());
                inputFact.setObjectVersion(inputObject.getObjectVersion());
                existingSessionRutime.getFacts().add(inputFact);
            }
            if (sessionFireAllRulesAndStartProcess.getOutputObject() != null) {
                DroolsFactObject outputObject = sessionFireAllRulesAndStartProcess.getInputObject();
                Fact outputFact = new Fact();
                outputFact.setEventid(sessionFireAllRulesAndStartProcess.getEventID());
                outputFact.setFactType(FactType.OUTPUTDATA);
                outputFact.setFullClassName(outputObject.getFullClassName());
                outputFact.setJsonFact(outputObject.getRealObject_JSON());
                outputFact.setModificationDate(sessionFireAllRulesAndStartProcess.getDateEvent());
                outputFact.setObjectVersion(outputObject.getObjectVersion());
                existingSessionRutime.getFacts().add(outputFact);
            }

            sessionExecutionRepository.save(existingSessionRutime);
        }

        LOG.debug("SessionFireAllRulesAndStartProcess " + sessionFireAllRulesAndStartProcess.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof SessionFireAllRulesAndStartProcess;
    }
}
