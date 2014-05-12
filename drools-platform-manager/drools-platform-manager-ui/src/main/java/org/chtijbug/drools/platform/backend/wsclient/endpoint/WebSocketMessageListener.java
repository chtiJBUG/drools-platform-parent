package org.chtijbug.drools.platform.backend.wsclient.endpoint;

import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;

/**
 * Created by IntelliJ IDEA.
 * Date: 06/05/14
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
public interface WebSocketMessageListener {
    public void beanReceived(PlatformManagementKnowledgeBean platformManagementKnowledgeBean) ;

}
