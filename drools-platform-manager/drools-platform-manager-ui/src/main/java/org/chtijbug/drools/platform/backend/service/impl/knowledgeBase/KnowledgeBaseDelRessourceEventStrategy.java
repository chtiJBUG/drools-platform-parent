package org.chtijbug.drools.platform.backend.service.impl.knowledgeBase;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseDelRessourceEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.DroolsRessource;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 14:56
 * To change this template use File | Settings | File Templates.
 */
@Component
public class KnowledgeBaseDelRessourceEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(KnowledgeBaseDelRessourceEventStrategy.class);



    @Autowired
    PlatformRuntimeRepository platformRuntimeRepository;

    @Override
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        KnowledgeBaseDelRessourceEvent knowledgeBaseDelRessourceEvent = (KnowledgeBaseDelRessourceEvent) historyEvent;
        PlatformRuntime existingPlatformRuntime = platformRuntimeRepository.findByRuleBaseIDAndEndDateNull(knowledgeBaseDelRessourceEvent.getRuleBaseID());

              DroolsRessource droolsRessource = null;
              if (knowledgeBaseDelRessourceEvent.getDrlRessourceFiles().size() == 0) {
                  droolsRessource = new DroolsRessource(knowledgeBaseDelRessourceEvent.getGuvnor_url(), knowledgeBaseDelRessourceEvent.getGuvnor_appName(), knowledgeBaseDelRessourceEvent.getGuvnor_packageName(), knowledgeBaseDelRessourceEvent.getGuvnor_packageVersion());
              } else {
                  droolsRessource = new DroolsRessource(knowledgeBaseDelRessourceEvent.getDrlRessourceFiles().get(0).getFileName(), knowledgeBaseDelRessourceEvent.getDrlRessourceFiles().get(0).getContent());
              }
              for (DroolsRessource toto : existingPlatformRuntime.getDroolsRessources()) {
                  if (toto.equals(droolsRessource)) {
                      toto.setEndDate(knowledgeBaseDelRessourceEvent.getDateEvent());
                  }
              }
              platformRuntimeRepository.save(existingPlatformRuntime);
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {
        return historyEvent instanceof KnowledgeBaseDelRessourceEvent;
    }
}
