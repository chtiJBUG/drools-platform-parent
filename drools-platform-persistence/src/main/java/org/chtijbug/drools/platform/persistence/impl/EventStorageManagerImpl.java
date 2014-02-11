package org.chtijbug.drools.platform.persistence.impl;

import org.chtijbug.drools.entity.history.fact.DeletedFactHistoryEvent;
import org.chtijbug.drools.entity.history.fact.InsertedFactHistoryEvent;
import org.chtijbug.drools.entity.history.fact.UpdatedFactHistoryEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseCreateSessionEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseCreatedEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseInitialLoadEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseReloadedEvent;
import org.chtijbug.drools.entity.history.process.NodeInstanceAfterHistoryEvent;
import org.chtijbug.drools.entity.history.process.NodeInstanceBeforeHistoryEvent;
import org.chtijbug.drools.entity.history.process.ProcessEndHistoryEvent;
import org.chtijbug.drools.entity.history.process.ProcessStartHistoryEvent;
import org.chtijbug.drools.entity.history.rule.AfterRuleFiredHistoryEvent;
import org.chtijbug.drools.entity.history.rule.AfterRuleFlowActivatedHistoryEvent;
import org.chtijbug.drools.entity.history.rule.AfterRuleFlowDeactivatedHistoryEvent;
import org.chtijbug.drools.entity.history.rule.BeforeRuleFiredHistoryEvent;
import org.chtijbug.drools.entity.history.session.*;
import org.chtijbug.drools.platform.persistence.EventStorageManager;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 07/01/14
 * Time: 16:03
 * To change this template use File | Settings | File Templates.
 */
@Component
public class EventStorageManagerImpl implements EventStorageManager{


    //@Autowired
    //OrientDBConnector orientDBConnector ;

    @Override
    public void storeEvent(KnowledgeBaseCreatedEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void storeEvent(KnowledgeBaseCreateSessionEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void storeEvent(KnowledgeBaseInitialLoadEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void storeEvent(KnowledgeBaseReloadedEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void storeEvent(DeletedFactHistoryEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void storeEvent(InsertedFactHistoryEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void storeEvent(UpdatedFactHistoryEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void storeEvent(SessionCreatedEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void storeEvent(SessionDisposedEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void storeEvent(SessionFireAllRulesBeginEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void storeEvent(SessionFireAllRulesEndEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void storeEvent(SessionFireAllRulesMaxNumberReachedEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void storeEvent(SessionStartProcessBeginEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void storeEvent(SessionStartProcessEndEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void storeEvent(NodeInstanceAfterHistoryEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void storeEvent(NodeInstanceBeforeHistoryEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void storeEvent(ProcessEndHistoryEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void storeEvent(ProcessStartHistoryEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void storeEvent(AfterRuleFiredHistoryEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void storeEvent(AfterRuleFlowActivatedHistoryEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void storeEvent(AfterRuleFlowDeactivatedHistoryEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void storeEvent(BeforeRuleFiredHistoryEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
