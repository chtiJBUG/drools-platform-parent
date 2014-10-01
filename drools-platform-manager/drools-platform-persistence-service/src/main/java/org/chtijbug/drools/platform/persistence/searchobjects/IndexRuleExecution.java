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

import org.chtijbug.drools.platform.persistence.pojo.RuleExecution;
import org.gridgain.grid.cache.query.GridCacheQuerySqlField;

import java.io.Serializable;

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
