package org.chtijbug.drools.platform.backend.wsclient.listener;

import org.chtijbug.drools.platform.entity.PlatformResourceFile;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 11/05/14
 * Time: 11:46
 * To change this template use File | Settings | File Templates.
 */
public interface VersionInfosListener {
    public void messageReceived(Integer ruleBaseID,List<PlatformResourceFile> platformResourceFiles);
}
