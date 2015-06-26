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
package org.chtijbug.drools.platform.persistence.pojo;

import org.chtijbug.drools.platform.persistence.utility.StringJsonUserType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;


@Entity
@Table(name = "ruleflowgroup_execution_record")
@TypeDefs({@TypeDef(name = "jsonb", typeClass = StringJsonUserType.class)})
@Cacheable(value = true)
public class RuleFlowExecutionRecord {
    @Id
    @SequenceGenerator(name = "ruleflowgroup_execution_record_id_seq", sequenceName = "ruleflowgroup_execution_record_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ruleflowgroup_execution_record_id_seq")
    private Long id;

    private String processInstanceId;

    @ManyToOne
    @JoinColumn(name = "session_execution_record_id", referencedColumnName = "id")
    private SessionExecutionRecord sessionExecutionRecord;

    @Type(type = "jsonb")
    private String jsonRuleFlowExecution;

    private String ruleFlowName;

    public RuleFlowExecutionRecord() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJsonRuleFlowExecution() {
        return jsonRuleFlowExecution;
    }

    public void setJsonRuleFlowExecution(String jsonRuleFlowExecution) {
        this.jsonRuleFlowExecution = jsonRuleFlowExecution;
    }


    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getRuleFlowName() {
        return ruleFlowName;
    }

    public void setRuleFlowName(String ruleFlowName) {
        this.ruleFlowName = ruleFlowName;
    }

    public SessionExecutionRecord getSessionExecutionRecord() {
        return sessionExecutionRecord;
    }

    public void setSessionExecutionRecord(SessionExecutionRecord sessionExecutionRecord) {
        this.sessionExecutionRecord = sessionExecutionRecord;
    }
}
