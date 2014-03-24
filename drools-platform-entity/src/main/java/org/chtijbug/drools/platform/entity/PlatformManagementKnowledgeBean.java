package org.chtijbug.drools.platform.entity;

import org.chtijbug.drools.platform.entity.coder.JSONCoder;
import org.chtijbug.drools.runtime.DroolsChtijbugException;

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

    private GuvnorVersion guvnorVersion=null;

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

    public GuvnorVersion getGuvnorVersion() {
        return guvnorVersion;
    }

    public void setGuvnorVersion(GuvnorVersion guvnorVersion) {
        this.guvnorVersion = guvnorVersion;
    }

    public DroolsChtijbugException getDroolsChtijbugException() {
        return droolsChtijbugException;
    }

    public void setDroolsChtijbugException(DroolsChtijbugException droolsChtijbugException) {
        this.droolsChtijbugException = droolsChtijbugException;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PlatformManagementKnowledgeBean{");
        sb.append("isAlive=").append(isAlive);
        sb.append(", requestRuntimePlarform=").append(requestRuntimePlarform);
        sb.append(", requestStatus=").append(requestStatus);
        sb.append(", jmxInfo=").append(jmxInfo);
        sb.append(", guvnorVersion=").append(guvnorVersion);
        sb.append('}');
        return sb.toString();
    }
}
