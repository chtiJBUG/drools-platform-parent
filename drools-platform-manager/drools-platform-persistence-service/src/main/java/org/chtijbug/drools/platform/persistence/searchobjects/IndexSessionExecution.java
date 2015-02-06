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

import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;
import org.gridgain.grid.cache.query.GridCacheQuerySqlField;

import java.io.Serializable;


public class IndexSessionExecution implements Serializable {

    @GridCacheQuerySqlField
    private Long rulebaseid;

    @GridCacheQuerySqlField
    private Long sessionid;


    private SessionExecution sessionExecution;

    public IndexSessionExecution() {
    }

    public Long getRulebaseid() {
        return rulebaseid;
    }

    public void setRulebaseid(Long rulebaseid) {
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

    public Long getSessionid() {
        return sessionid;
    }

    public void setSessionid(Long sessionid) {
        this.sessionid = sessionid;
    }


    public SessionExecution getSessionExecution() {
        return sessionExecution;
    }

    public void setSessionExecution(SessionExecution sessionExecution) {
        this.sessionExecution = sessionExecution;
    }
}
