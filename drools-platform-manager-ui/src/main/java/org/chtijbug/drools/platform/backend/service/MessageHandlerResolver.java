package org.chtijbug.drools.platform.backend.service;

import org.chtijbug.drools.entity.history.HistoryEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 14:52
 * To change this template use File | Settings | File Templates.
 */
@Component
public class MessageHandlerResolver {
    @Resource
    private List<AbstractEventHandlerStrategy> allStrategies;

    public AbstractEventHandlerStrategy resolveMessageHandler(HistoryEvent historyEvent) {
        for(AbstractEventHandlerStrategy strategy : allStrategies) {
            if (strategy.isEventSupported(historyEvent))
                return strategy;
        }
        return null;
    }
}
