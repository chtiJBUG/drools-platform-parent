package org.chtijbug.drools.platform.persistence.searchobjects;

import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;
import org.gridgain.grid.cache.query.GridCacheQuerySqlField;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * Date: 31/07/14
 * Time: 11:23
 * To change this template use File | Settings | File Templates.
 */
public class IndexSessionExecution implements Serializable {

    @GridCacheQuerySqlField
    private Integer rulebaseid;

    @GridCacheQuerySqlField
    private Integer sessionid;


    private SessionExecution sessionExecution;

    public IndexSessionExecution() {
    }

    public Integer getRulebaseid() {
        return rulebaseid;
    }

    public void setRulebaseid(Integer rulebaseid) {
        this.rulebaseid = rulebaseid;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("IndexSessionExecution{");
        sb.append("rulebaseid=").append(rulebaseid);
        sb.append(", sessionid=").append(sessionid);
        sb.append(", sessionExecution=").append(sessionExecution);
        sb.append('}');
        return sb.toString();
    }

    public Integer getSessionid() {
        return sessionid;
    }

    public void setSessionid(Integer sessionid) {
        this.sessionid = sessionid;
    }


    public SessionExecution getSessionExecution() {
        return sessionExecution;
    }

    public void setSessionExecution(SessionExecution sessionExecution) {
        this.sessionExecution = sessionExecution;
    }
}
