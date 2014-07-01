package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.knowledgeBase;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.DrlResourceFile;
import org.chtijbug.drools.entity.history.GuvnorResourceFile;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseAddRessourceEvent;
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
public class KnowledgeBaseAddRessourceEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(KnowledgeBaseAddRessourceEventStrategy.class);


    @Autowired
    PlatformRuntimeInstanceRepository platformRuntimeInstanceRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        KnowledgeBaseAddRessourceEvent knowledgeBaseAddRessourceEvent = (KnowledgeBaseAddRessourceEvent) historyEvent;
        List<PlatformRuntimeInstance> existingPlatformRuntimeInstances = platformRuntimeInstanceRepository.findByRuleBaseIDAndEndDateNull(knowledgeBaseAddRessourceEvent.getRuleBaseID());

        DroolsResource droolsResource = null;
        if (knowledgeBaseAddRessourceEvent.getResourceFiles().size() == 1 && knowledgeBaseAddRessourceEvent.getResourceFiles().get(0) instanceof GuvnorResourceFile) {
            GuvnorResourceFile guvnorResourceFile = (GuvnorResourceFile) knowledgeBaseAddRessourceEvent.getResourceFiles().get(0);
            droolsResource = new DroolsResource(guvnorResourceFile.getGuvnor_url(), guvnorResourceFile.getGuvnor_appName(), guvnorResourceFile.getGuvnor_packageName(), guvnorResourceFile.getGuvnor_packageVersion());
        } else {
            DrlResourceFile drlResourceFile = (DrlResourceFile) knowledgeBaseAddRessourceEvent.getResourceFiles().get(0);
            droolsResource = new DroolsResource(drlResourceFile.getFileName(), drlResourceFile.getContent());
        }
        droolsResource.setStartDate(knowledgeBaseAddRessourceEvent.getDateEvent());
        droolsResource.setStartEventID(knowledgeBaseAddRessourceEvent.getEventID());
        if (existingPlatformRuntimeInstances.size() == 1) {
            existingPlatformRuntimeInstances.get(0).getDroolsRessources().add(droolsResource);
            platformRuntimeInstanceRepository.save(existingPlatformRuntimeInstances.get(0));
        }
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {
        return historyEvent instanceof KnowledgeBaseAddRessourceEvent;
    }
}
