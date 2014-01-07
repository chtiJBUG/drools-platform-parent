package org.chtijbug.drools.platform.persistence;

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

/**
 * Created by IntelliJ IDEA.
 * Date: 07/01/14
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */
public interface EventStorageManager {


    public void storeEvent(KnowledgeBaseCreatedEvent event);

    public void storeEvent(KnowledgeBaseCreateSessionEvent event);

    public void storeEvent(KnowledgeBaseInitialLoadEvent event);

    public void storeEvent(KnowledgeBaseReloadedEvent event);

    public void storeEvent(DeletedFactHistoryEvent event);

    public void storeEvent(InsertedFactHistoryEvent event);

    public void storeEvent(UpdatedFactHistoryEvent event);

    public void storeEvent(SessionCreatedEvent event);

    public void storeEvent(SessionDisposedEvent event);

    public void storeEvent(SessionFireAllRulesBeginEvent event);

    public void storeEvent(SessionFireAllRulesEndEvent event);


    public void storeEvent(SessionFireAllRulesMaxNumberReachedEvent event);

    public void storeEvent(SessionStartProcessBeginEvent event);

    public void storeEvent(SessionStartProcessEndEvent event);

    public void storeEvent(NodeInstanceAfterHistoryEvent event);

    public void storeEvent(NodeInstanceBeforeHistoryEvent event);

    public void storeEvent(ProcessEndHistoryEvent event);

    public void storeEvent(ProcessStartHistoryEvent event);

    public void storeEvent(AfterRuleFiredHistoryEvent event);

    public void storeEvent(AfterRuleFlowActivatedHistoryEvent event);

    public void storeEvent(AfterRuleFlowDeactivatedHistoryEvent event);

    public void storeEvent(BeforeRuleFiredHistoryEvent event);


}
