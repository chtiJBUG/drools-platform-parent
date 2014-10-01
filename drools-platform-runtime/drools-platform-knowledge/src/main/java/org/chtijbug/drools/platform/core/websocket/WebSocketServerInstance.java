/*
 * Copyright 2014 Pymma Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.chtijbug.drools.platform.core.websocket;

import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.runtime.DroolsChtijbugException;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/06/14
 * Time: 13:15
 * To change this template use File | Settings | File Templates.
 */
public interface WebSocketServerInstance {
    public void end();

    public void run();

    public void sendMessage(PlatformManagementKnowledgeBean platformManagementKnowledgeBean) throws DroolsChtijbugException;

    public abstract String getHostName();

    public abstract int getPort();


    public abstract String getEndPoint();
}
