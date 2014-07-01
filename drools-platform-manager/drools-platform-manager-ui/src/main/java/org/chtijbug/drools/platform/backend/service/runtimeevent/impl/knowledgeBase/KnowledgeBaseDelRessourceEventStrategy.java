package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.knowledgeBase;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.DrlResourceFile;
import org.chtijbug.drools.entity.history.GuvnorResourceFile;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseDelRessourceEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepository;
import org.chtijbug.drools.platform.persistence.pojo.DroolsResource;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    PlatformRuntimeInstanceRepository platformRuntimeInstanceRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        KnowledgeBaseDelRessourceEvent knowledgeBaseDelRessourceEvent = (KnowledgeBaseDelRessourceEvent) historyEvent;
        List<PlatformRuntimeInstance> existingPlatformRuntimeInstances = platformRuntimeInstanceRepository.findByRuleBaseIDAndEndDateNull(knowledgeBaseDelRessourceEvent.getRuleBaseID());

        if (existingPlatformRuntimeInstances.size() == 1) {
            PlatformRuntimeInstance existingPlatformRuntimeInstance = existingPlatformRuntimeInstances.get(0);
            DroolsResource droolsResource = null;
            if (knowledgeBaseDelRessourceEvent.getResourceFiles().size() == 1 && knowledgeBaseDelRessourceEvent.getResourceFiles().get(0) instanceof GuvnorResourceFile) {
                GuvnorResourceFile guvnorResourceFile = (GuvnorResourceFile) knowledgeBaseDelRessourceEvent.getResourceFiles().get(0);
                droolsResource = new DroolsResource(guvnorResourceFile.getGuvnor_url(), guvnorResourceFile.getGuvnor_appName(), guvnorResourceFile.getGuvnor_packageName(), guvnorResourceFile.getGuvnor_packageVersion());
            } else {
                DrlResourceFile drlResourceFile = (DrlResourceFile) knowledgeBaseDelRessourceEvent.getResourceFiles().get(0);
                droolsResource = new DroolsResource(drlResourceFile.getFileName(), drlResourceFile.getContent());
            }
            for (DroolsResource existingResource : existingPlatformRuntimeInstance.getDroolsRessources()) {
                if (existingResource.equals(droolsResource)) {
                    existingResource.setEndDate(knowledgeBaseDelRessourceEvent.getDateEvent());
                    existingPlatformRuntimeInstance.setStopEventID(knowledgeBaseDelRessourceEvent.getEventID());
                }
            }
            platformRuntimeInstanceRepository.save(existingPlatformRuntimeInstance);
        }
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {
        return historyEvent instanceof KnowledgeBaseDelRessourceEvent;
    }
}
