package org.chtijbug.drools.platform.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.chtijbug.drools.platform.entity.coder.JSONCoder;
import org.chtijbug.drools.runtime.DroolsChtijbugException;

import javax.websocket.DecodeException;
import javax.websocket.EncodeException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
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

        @Override
        public void encode(PlatformManagementKnowledgeBean object, Writer writer) throws EncodeException, IOException {
            super.encode(object, writer);
        }

        @Override
        public PlatformManagementKnowledgeBean decode(Reader reader) throws DecodeException, IOException {
            StringBuilder builder = new StringBuilder();
            int charsRead = -1;
            char[] chars = new char[100];
            do {
                charsRead = reader.read(chars, 0, chars.length);
                //if we have valid chars, append them to end of string.
                if (charsRead > 0)
                    builder.append(chars, 0, charsRead);
            } while (charsRead > 0);
            String stringReadFromReader = builder.toString();
            return super.decode(reader);
        }

        public void PlatformManagementKnowledgeBeanCode() {
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
