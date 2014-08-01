package org.chtijbug.drools.platform.persistence.searchobjects;

import org.chtijbug.drools.platform.persistence.pojo.RuleflowGroup;
import org.gridgain.grid.cache.query.GridCacheQuerySqlField;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * Date: 31/07/14
 * Time: 11:23
 * To change this template use File | Settings | File Templates.
 */
public class IndexRuleFlowGroup implements Serializable {

    @GridCacheQuerySqlField
    private Integer rulebaseid;

    @GridCacheQuerySqlField
    private Integer sessionid;

    @GridCacheQuerySqlField
    private Integer processintanceid;

    @GridCacheQuerySqlField
    private String ruleflowgroupname;

    private RuleflowGroup ruleflowGroup;

    public IndexRuleFlowGroup() {
    }

    public Integer getRulebaseid() {
        return rulebaseid;
    }

    public void setRulebaseid(Integer rulebaseid) {
        this.rulebaseid = rulebaseid;
    }

    public Integer getSessionid() {
        return sessionid;
    }

    public void setSessionid(Integer sessionid) {
        this.sessionid = sessionid;
    }

    public Integer getProcessintanceid() {
        return processintanceid;
    }

    public void setProcessintanceid(Integer processintanceid) {
        this.processintanceid = processintanceid;
    }

    public String getRuleflowgroupname() {
        return ruleflowgroupname;
    }

    public void setRuleflowgroupname(String ruleflowgroupname) {
        this.ruleflowgroupname = ruleflowgroupname;
    }

    public RuleflowGroup getRuleflowGroup() {
        return ruleflowGroup;
    }

    public void setRuleflowGroup(RuleflowGroup ruleflowGroup) {
        this.ruleflowGroup = ruleflowGroup;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("IndexRuleFlowGroup{");
        sb.append("rulebaseid=").append(rulebaseid);
        sb.append(", sessionid=").append(sessionid);
        sb.append(", processintanceid=").append(processintanceid);
        sb.append(", ruleflowgroupname='").append(ruleflowgroupname).append('\'');
        sb.append(", ruleflowGroup=").append(ruleflowGroup);
        sb.append('}');
        return sb.toString();
    }
}
