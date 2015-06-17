package org.chtijbug.drools.platform.web;

import org.chtijbug.drools.platform.web.model.RuleFlowGroupDetails;
import org.chtijbug.drools.platform.web.model.SessionExecutionDetailsResource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nheron on 16/06/15.
 */
@Component
public class RuntimeDisplayCache {


    private Map<Long, SessionExecutionDetailsResource> sessionsCache = new HashMap<>();


    public void storeSession(Long id, SessionExecutionDetailsResource session) {
        sessionsCache.put(id, session);
    }

    public SessionExecutionDetailsResource updateView(Long id, String ruleflowName, Long direction) {

        SessionExecutionDetailsResource result = null;
        if (sessionsCache.containsKey(id)) {
            SessionExecutionDetailsResource dbElement = sessionsCache.get(id);
            for (RuleFlowGroupDetails detail : dbElement.getAllRuleFlowGroupDetails()) {
                if (detail.getRuleflowGroup().equals(ruleflowName)) {
                    if (direction > 0) {
                        int offset = detail.getPosition() + detail.getNbRuleToDisplay();
                        if (offset < detail.getAllRuleExecutionDetails().size()) {
                            detail.setPosition(detail.getPosition() + detail.getNbRuleToDisplay());
                        }
                    } else if (direction < 0) {
                        int offset = detail.getPosition() - detail.getNbRuleToDisplay();
                        if (offset >= 0) {
                            detail.setPosition(detail.getPosition() - detail.getNbRuleToDisplay());
                        }
                    }
                }

            }
            result = sessionsCache.get(id).duplicate();
        }
        return result;
    }

    public SessionExecutionDetailsResource getView(Long id) {
        SessionExecutionDetailsResource result = null;
        if (sessionsCache.containsKey(id)) {
            result = sessionsCache.get(id).duplicate();
        }
        return result;
    }

}
