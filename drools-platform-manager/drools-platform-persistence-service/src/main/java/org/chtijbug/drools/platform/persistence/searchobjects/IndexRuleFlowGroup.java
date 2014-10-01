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
package org.chtijbug.drools.platform.persistence.searchobjects;

import org.chtijbug.drools.platform.persistence.pojo.RuleflowGroup;
import org.gridgain.grid.cache.query.GridCacheQuerySqlField;

import java.io.Serializable;


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
