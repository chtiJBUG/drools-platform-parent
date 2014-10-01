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
package org.chtijbug.drools.platform.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.chtijbug.drools.platform.entity.coder.JSONCoder;
import org.chtijbug.drools.runtime.DroolsChtijbugException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 10/01/14
 * Time: 15:12
 * To change this template use File | Settings | File Templates.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlatformManagementKnowledgeBean {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PlatformManagementKnowledgeBeanCode extends JSONCoder<PlatformManagementKnowledgeBean> {

        public PlatformManagementKnowledgeBeanCode() {
            set_type(PlatformManagementKnowledgeBean.class);
        }
    }

    private Heartbeat heartbeat;


    private boolean isAlive = false;


    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    private RequestRuntimePlarform requestRuntimePlarform;


    private RequestStatus requestStatus;

    private JMXInfo jmxInfo;

    private ArrayList<PlatformResourceFile> resourceFileList = new ArrayList<>();

    private DroolsChtijbugException droolsChtijbugException;

    public RequestRuntimePlarform getRequestRuntimePlarform() {
        return requestRuntimePlarform;
    }

    public void setRequestRuntimePlarform(RequestRuntimePlarform requestRuntimePlarform) {
        this.requestRuntimePlarform = requestRuntimePlarform;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public JMXInfo getJmxInfo() {
        return jmxInfo;
    }

    public void setJmxInfo(JMXInfo jmxInfo) {
        this.jmxInfo = jmxInfo;
    }


    public DroolsChtijbugException getDroolsChtijbugException() {
        return droolsChtijbugException;
    }

    public void setDroolsChtijbugException(DroolsChtijbugException droolsChtijbugException) {
        this.droolsChtijbugException = droolsChtijbugException;
    }

    public List<PlatformResourceFile> getResourceFileList() {
        return resourceFileList;
    }

    public void setResourceFileList(ArrayList<PlatformResourceFile> resourceFileList) {
        this.resourceFileList = resourceFileList;
    }

    public Heartbeat getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(Heartbeat heartbeat) {
        this.heartbeat = heartbeat;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PlatformManagementKnowledgeBean{");
        sb.append("heartbeat=").append(heartbeat);
        sb.append(", isAlive=").append(isAlive);
        sb.append(", requestRuntimePlarform=").append(requestRuntimePlarform);
        sb.append(", requestStatus=").append(requestStatus);
        sb.append(", jmxInfo=").append(jmxInfo);
        if (resourceFileList != null && resourceFileList.size() > 0) {
            sb.append(", resourceFileList=").append(resourceFileList);
            for (PlatformResourceFile element : resourceFileList) {
                sb.append(",   PlatformResourceFile=").append(element);
            }
        }

        sb.append(", droolsChtijbugException=").append(droolsChtijbugException);
        sb.append('}');
        return sb.toString();
    }
}
