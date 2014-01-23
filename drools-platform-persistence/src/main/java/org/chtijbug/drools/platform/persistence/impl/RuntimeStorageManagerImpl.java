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

import java.util.*;

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

    @Override
    public List<PlatformRuntime> findRunningPlatformRuntime(String hostname) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select from platformruntime where  enddate=startdate and ");
        stringBuilder.append(" hostname=\":hostname\"");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("hostname", hostname);
        OSQLSynchQuery<ODocument> query = new OSQLSynchQuery<ODocument>(stringBuilder.toString());
        List<ODocument> result = orientDBConnector.getDatabase().command(query).execute(params);
        return transformFrom(result);
    }

    @Override
    public List<PlatformRuntime> findRunningPlatformRuntime(int ruleBaseID) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select from platformruntime where enddate=startdate ");
        stringBuilder.append("and rulebaseid=:rulebaseid");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("rulebaseid", ruleBaseID);
        OSQLSynchQuery<ODocument> query = new OSQLSynchQuery<ODocument>(stringBuilder.toString());
        List<ODocument> result = orientDBConnector.getDatabase().command(query).execute(params);
        return transformFrom(result);
    }

    @Override
    public List<PlatformRuntime> findRunningPlatformRuntime(int ruleBaseID, String hostname) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select from platformruntime where  enddate=startdate and ");
        stringBuilder.append("rulebaseid=:rulebaseid");
        stringBuilder.append(" and hostname=\":hostname\"");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("rulebaseid", ruleBaseID);
        params.put("hostname", hostname);
        OSQLSynchQuery<ODocument> query = new OSQLSynchQuery<ODocument>(stringBuilder.toString());
        List<ODocument> result = orientDBConnector.getDatabase().command(query).execute(params);
        return transformFrom(result);
    }

    @Override
    public void deletePlatformRuntime(String orientdbID) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select from platformruntime where @rid=:orientdbid ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orientdbid", orientdbID);
        OSQLSynchQuery<ODocument> query = new OSQLSynchQuery<ODocument>(stringBuilder.toString());
        List<ODocument> result = orientDBConnector.getDatabase().command(query).execute(params);
        for (ODocument oDocument : result) {
            oDocument.delete();
        }
    }
    @Override
    public PlatformRuntime getPlatformRuntime(String orientdbID) {
        return getFrom(getPatformRuntime_ODocument(orientdbID));
    }

    private ODocument getPatformRuntime_ODocument(String orientdbID) {
        ODocument resultODocument = null;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select from platformruntime where @rid=:orientdbid ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orientdbid", orientdbID);
        OSQLSynchQuery<ODocument> query = new OSQLSynchQuery<ODocument>(stringBuilder.toString());
        List<ODocument> result = orientDBConnector.getDatabase().command(query).execute(params);
        for (ODocument oDocument : result) {
            resultODocument = oDocument;
        }
        return resultODocument;
    }

    @Override
    public void updatePlatformRuntime(String orientdbID, PlatformRuntime platformRuntime) {
        ODocument oDocument = this.getPatformRuntime_ODocument(orientdbID);
        this.save(platformRuntime, oDocument);
    }

    @Override
    public void save(PlatformRuntime platformRuntime) {
        //TODO first test if there is a running instance before saving it
        ODatabaseRecordThreadLocal.INSTANCE.set(this.orientDBConnector.getDatabase());
        ODocument platformRuntimeDocument = new ODocument(PlatformRuntime.class_name);
        platformRuntimeDocument.field(PlatformRuntime.var_hostname, platformRuntime.getHostname());
        platformRuntimeDocument.field(PlatformRuntime.var_port, platformRuntime.getPort());
        platformRuntimeDocument.field(PlatformRuntime.var_endpoint, platformRuntime.getEndPoint());
        platformRuntimeDocument.field(PlatformRuntime.var_startdate, platformRuntime.getStartDate());
        platformRuntimeDocument.field(PlatformRuntime.var_enddate, platformRuntime.getEndDate());
        platformRuntimeDocument.field(PlatformRuntime.var_status, platformRuntime.getStatus());
        platformRuntimeDocument.field(PlatformRuntime.var_eventid, platformRuntime.getEventID());
        platformRuntimeDocument.field(PlatformRuntime.var_rulebaseid, platformRuntime.getRuleBaseID());
        platformRuntimeDocument.save();

    }


    public void save(PlatformRuntime platformRuntime, ODocument platformRuntimeDocument) {
        //TODO first test if there is a running instance before saving it
        ODatabaseRecordThreadLocal.INSTANCE.set(this.orientDBConnector.getDatabase());
        platformRuntimeDocument.field(PlatformRuntime.var_hostname, platformRuntime.getHostname());
        platformRuntimeDocument.field(PlatformRuntime.var_port, platformRuntime.getPort());
        platformRuntimeDocument.field(PlatformRuntime.var_endpoint, platformRuntime.getEndPoint());
        platformRuntimeDocument.field(PlatformRuntime.var_startdate, platformRuntime.getStartDate());
        platformRuntimeDocument.field(PlatformRuntime.var_enddate, platformRuntime.getEndDate());
        platformRuntimeDocument.field(PlatformRuntime.var_status, platformRuntime.getStatus());
        platformRuntimeDocument.field(PlatformRuntime.var_eventid, platformRuntime.getEventID());
        platformRuntimeDocument.field(PlatformRuntime.var_rulebaseid, platformRuntime.getRuleBaseID());
        platformRuntimeDocument.save();

    }

    private List<PlatformRuntime> transformFrom(List<ODocument> inputList) {
        List<PlatformRuntime> outputList = new ArrayList<PlatformRuntime>();
        for (ODocument oDocument : inputList) {
            outputList.add(this.getFrom(oDocument));
        }
        return outputList;
    }

    private PlatformRuntime getFrom(ODocument input) {
        PlatformRuntime output = new PlatformRuntime();
        output.setOrientdbId(input.getIdentity().toString());
        if (input.field(PlatformRuntime.var_hostname) != null) {
            output.setHostname((String) input.field(PlatformRuntime.var_hostname));
        }
        if (input.field(PlatformRuntime.var_port) != null) {
            output.setPort((Integer) input.field(PlatformRuntime.var_port));
        }
        if (input.field(PlatformRuntime.var_endpoint) != null) {
            output.setEndPoint((String) input.field(PlatformRuntime.var_endpoint));
        }
        if (input.field(PlatformRuntime.var_startdate) != null) {
            output.setStartDate((Date) input.field(PlatformRuntime.var_startdate));
        }
        if (input.field(PlatformRuntime.var_enddate) != null) {
            output.setEndDate((Date) input.field(PlatformRuntime.var_enddate));
        }
        String statusString = (String) input.field(PlatformRuntime.var_status);
        if (PlatformRuntimeStatus.STARTED.equals(statusString)) {
            output.setStatus(PlatformRuntimeStatus.STARTED);
        } else if (PlatformRuntimeStatus.STOPPED.equals(statusString)) {
            output.setStatus(PlatformRuntimeStatus.STOPPED);
        } else {
            output.setStatus(PlatformRuntimeStatus.NOT_JOINGNABLE);
        }
        if (input.field(PlatformRuntime.var_eventid) != null) {
            output.setEventID((Integer) input.field(PlatformRuntime.var_eventid));
        }
        if (input.field(PlatformRuntime.var_rulebaseid) != null) {
            output.setRuleBaseID((Integer) input.field(PlatformRuntime.var_rulebaseid));
        }
        return output;
    }
}
