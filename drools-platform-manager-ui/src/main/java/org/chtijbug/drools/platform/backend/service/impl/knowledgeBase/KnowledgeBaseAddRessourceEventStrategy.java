package org.chtijbug.drools.platform.backend.service.impl.knowledgeBase;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseAddRessourceEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseDelRessourceEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.DroolsRessource;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 14:56
 * To change this template use File | Settings | File Templates.
 */
@Component
public class KnowledgeBaseAddRessourceEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(KnowledgeBaseAddRessourceEventStrategy.class);


    @Autowired
    PlatformRuntimeRepository platformRuntimeRepository;

    @Override
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        KnowledgeBaseAddRessourceEvent knowledgeBaseAddRessourceEvent = (KnowledgeBaseAddRessourceEvent) historyEvent;
        PlatformRuntime existingPlatformRuntime = platformRuntimeRepository.findByRuleBaseIDAndEndDateNull(knowledgeBaseAddRessourceEvent.getRuleBaseID());

         DroolsRessource droolsRessource = null;
         if (knowledgeBaseAddRessourceEvent.getDrlRessourceFiles().size() == 0) {
             droolsRessource = new DroolsRessource(knowledgeBaseAddRessourceEvent.getGuvnor_url(), knowledgeBaseAddRessourceEvent.getGuvnor_appName(), knowledgeBaseAddRessourceEvent.getGuvnor_packageName(), knowledgeBaseAddRessourceEvent.getGuvnor_packageVersion());
         } else {
             droolsRessource = new DroolsRessource(knowledgeBaseAddRessourceEvent.getDrlRessourceFiles().get(0).getFileName(), knowledgeBaseAddRessourceEvent.getDrlRessourceFiles().get(0).getContent());
         }
         droolsRessource.setStartDate(new Date());
         existingPlatformRuntime.getDroolsRessources().add(droolsRessource);

         platformRuntimeRepository.save(existingPlatformRuntime);
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {
        return historyEvent instanceof KnowledgeBaseAddRessourceEvent;
    }
}
