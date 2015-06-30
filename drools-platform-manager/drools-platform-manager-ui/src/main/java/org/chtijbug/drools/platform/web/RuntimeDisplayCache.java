package org.chtijbug.drools.platform.web;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.chtijbug.drools.platform.web.model.RuleFlowGroupDetails;
import org.chtijbug.drools.platform.web.model.SessionExecutionDetailsResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by nheron on 16/06/15.
 */
@Component
public class RuntimeDisplayCache {
    private static Logger logger = LoggerFactory.getLogger(RuntimeDisplayCache.class);

    private LoadingCache<Long, SessionExecutionDetailsResource> sessionsCache;

    @Autowired
    private CacheLoader<Long, SessionExecutionDetailsResource> handler;

    public RuntimeDisplayCache() {

    }


    public SessionExecutionDetailsResource updateView(Long id, String ruleflowName, Long direction) {


        SessionExecutionDetailsResource dbElement = null;
        try {
            dbElement = getCache().get(id);
        } catch (ExecutionException e) {
            logger.error("updateView-getCache", e);
        }
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
        SessionExecutionDetailsResource result = dbElement.duplicate();

        return result;
    }

    public SessionExecutionDetailsResource getView(Long id) {

        SessionExecutionDetailsResource result = null;

        try {
            result = getCache().get(id).duplicate();
        } catch (ExecutionException e) {

            logger.error("updateView-getView", e);
        }

        return result;
    }

    private LoadingCache<Long, SessionExecutionDetailsResource> getCache() {
        if (sessionsCache == null) {
            sessionsCache = CacheBuilder.newBuilder()
                    .expireAfterAccess(5, TimeUnit.MINUTES)
                    .maximumSize(30)
                    .build(handler);
        }
        return sessionsCache;
    }

}
