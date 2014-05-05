package org.chtijbug.drools.platform.entity;

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
public class PlatformManagementKnowledgeBean {


    public static class PlatformManagementKnowledgeBeanCode extends
            JSONCoder<PlatformManagementKnowledgeBean> {
     }

    private boolean isAlive=false;

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    private RequestRuntimePlarform requestRuntimePlarform;


    private RequestStatus requestStatus;

    private JMXInfo jmxInfo;

    private List<PlatformResourceFile> resourceFileList = new ArrayList<>();

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

    public void setResourceFileList(List<PlatformResourceFile> resourceFileList) {
        this.resourceFileList = resourceFileList;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PlatformManagementKnowledgeBean{");
        sb.append("isAlive=").append(isAlive);
        sb.append(", requestRuntimePlarform=").append(requestRuntimePlarform);
        sb.append(", requestStatus=").append(requestStatus);
        sb.append(", jmxInfo=").append(jmxInfo);
        sb.append(", resourceFileList=").append(resourceFileList);
        sb.append(", droolsChtijbugException=").append(droolsChtijbugException);
        sb.append('}');
        return sb.toString();
    }
}
