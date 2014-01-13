package org.chtijbug.drools.platform.persistence.impl;

import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import org.chtijbug.drools.platform.entity.PlatformRuntime;
import org.chtijbug.drools.platform.entity.PlatformRuntimeStatus;
import org.chtijbug.drools.platform.persistence.RuntimeStorageManager;
import org.chtijbug.drools.platform.persistence.impl.db.OrientDBConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/01/14
 * Time: 21:07
 * To change this template use File | Settings | File Templates.
 */
@Component
public class RuntimeStorageManagerImpl implements RuntimeStorageManager {

    @Autowired
    OrientDBConnector orientDBConnector;


    public List<PlatformRuntime>findRunningPlatformRuntime(String hostname) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select from PlatformRuntime where endDate is null and ");
        stringBuilder.append(" hostname=\"" + hostname + "\"");
        OSQLSynchQuery<ODocument> query = new OSQLSynchQuery<ODocument>(stringBuilder.toString());
        List<ODocument> result = orientDBConnector.getDatabase().command(query).execute();
        return transformFrom(result);
    }

    public List<PlatformRuntime> findRunningPlatformRuntime(int ruleBaseID) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select from PlatformRuntime where endDate is null and ");
        stringBuilder.append("ruleBaseID=" + ruleBaseID);
        OSQLSynchQuery<ODocument> query = new OSQLSynchQuery<ODocument>(stringBuilder.toString());
        List<ODocument> result = orientDBConnector.getDatabase().command(query).execute();
        return transformFrom(result);
    }

    public List<PlatformRuntime> findRunningPlatformRuntime(int ruleBaseID, String hostname) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select from PlatformRuntime where endDate is null and ");
        stringBuilder.append("ruleBaseID=" + ruleBaseID);
        stringBuilder.append(" and hostname=\"" + hostname + "\"");
        OSQLSynchQuery<ODocument> query = new OSQLSynchQuery<ODocument>(stringBuilder.toString());
        List<ODocument> result = orientDBConnector.getDatabase().command(query).execute();
        return transformFrom(result);
    }


    @Override
    public void save(PlatformRuntime platformRuntime) {
        //TODO first test if there is a running instance before saving it
        ODatabaseRecordThreadLocal.INSTANCE.set(this.orientDBConnector.getDatabase());
        ODocument platformRuntimeDocument = new ODocument("PlatformRuntime");
        platformRuntimeDocument.field("hostname", platformRuntime.getHostname());
        platformRuntimeDocument.field("port", platformRuntime.getPort());
        platformRuntimeDocument.field("endPoint", platformRuntime.getEndPoint());
        platformRuntimeDocument.field("startDate", platformRuntime.getStartDate());
        platformRuntimeDocument.field("endDate", platformRuntime.getEndDate());
        platformRuntimeDocument.field("Status", platformRuntime.getStatus());
        platformRuntimeDocument.field("eventID", platformRuntime.getEventID());
        platformRuntimeDocument.field("ruleBaseID", platformRuntime.getRuleBaseID());
        platformRuntimeDocument.save();

    }

    private List<PlatformRuntime> transformFrom(List<ODocument> inputList) {
        List<PlatformRuntime> outputList = new ArrayList<PlatformRuntime>();
        for (ODocument oDocument: inputList) {
            outputList.add(this.getFrom(oDocument));
        }
        return outputList;
    }

    private PlatformRuntime getFrom(ODocument input) {
        PlatformRuntime output = new PlatformRuntime();
        output.setHostname((String) input.field("hostname"));
        output.setPort((Integer) input.field("port"));
        output.setEndPoint((String) input.field("endPoint"));
        output.setStartDate((Date) input.field("startDate"));
        output.setEndDate((Date) input.field("endDate"));
        String statusString = (String) input.field("Status");
        if (PlatformRuntimeStatus.STARTED.equals(statusString)) {
            output.setStatus(PlatformRuntimeStatus.STARTED);
        } else if (PlatformRuntimeStatus.STOPPED.equals(statusString)) {
            output.setStatus(PlatformRuntimeStatus.STOPPED);
        } else {
            output.setStatus(PlatformRuntimeStatus.NOT_JOINGNABLE);
        }
        output.setEventID((Integer) input.field("eventID"));
        output.setRuleBaseID((Integer) input.field("ruleBaseID"));
        return output;
    }
}
