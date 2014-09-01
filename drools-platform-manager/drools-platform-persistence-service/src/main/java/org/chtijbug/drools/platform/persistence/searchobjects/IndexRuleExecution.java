package org.chtijbug.drools.platform.persistence.searchobjects;

import org.chtijbug.drools.platform.persistence.pojo.RuleExecution;
import org.gridgain.grid.cache.query.GridCacheQuerySqlField;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * Date: 31/07/14
 * Time: 11:23
 * To change this template use File | Settings | File Templates.
 */
public class IndexRuleExecution implements Serializable {

    @GridCacheQuerySqlField
    private Integer rulebaseid;

    @GridCacheQuerySqlField
    private Integer sessionid;

    @GridCacheQuerySqlField
    private String ruleflowgroup;

    @GridCacheQuerySqlField
    private String rulename;

    private RuleExecution ruleExecution;

    public IndexRuleExecution() {
    }

    public Integer getrulebaseid() {
        return rulebaseid;
    }

    public void setrulebaseid(Integer rulebaseid) {
        this.rulebaseid = rulebaseid;
    }

    public Integer getsessionid() {
        return sessionid;
    }

    public void setsessionid(Integer sessionid) {
        this.sessionid = sessionid;
    }

    public String getruleflowgroup() {
        return ruleflowgroup;
    }

    public void setruleflowgroup(String ruleFlowGroup) {
        this.ruleflowgroup = ruleFlowGroup;
    }

    public String getrulename() {
        return rulename;
    }

    public void setrulename(String rulename) {
        this.rulename = rulename;
    }

    public RuleExecution getRuleExecution() {
        return ruleExecution;
    }


    public void setRuleExecution(RuleExecution ruleExecution) {
        this.ruleExecution = ruleExecution;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("IndexRuleExecution{");
        sb.append("ruleBaseID=").append(rulebaseid);
        sb.append(", sessionId=").append(sessionid);
        sb.append(", ruleflowgroup='").append(ruleflowgroup).append('\'');
        sb.append(", rulename='").append(rulename).append('\'');
        sb.append(", ruleExecution=").append(ruleExecution);
        sb.append('}');
        return sb.toString();
    }
}
