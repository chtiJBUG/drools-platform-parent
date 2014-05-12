package org.chtijbug.drools.platform.backend.wsclient.listener;

import org.chtijbug.drools.platform.entity.PlatformResourceFile;
import org.chtijbug.drools.platform.entity.RequestStatus;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 12/05/14
 * Time: 09:26
 * To change this template use File | Settings | File Templates.
 */
public interface LoadNewRuleVersionListener {
    public void messageReceived(Integer ruleBaseID,RequestStatus state,List<PlatformResourceFile> platformResourceFiles);
}
