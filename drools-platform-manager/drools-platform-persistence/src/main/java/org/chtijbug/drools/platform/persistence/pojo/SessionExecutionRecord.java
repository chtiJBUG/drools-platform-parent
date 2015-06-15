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
import java.util.Date;


@Entity
@Table(name = "session_execution_record", indexes = {@Index(columnList = "sessionId")})
@TypeDefs({@TypeDef(name = "jsonb", typeClass = StringJsonUserType.class)})
@Cacheable(value = true)
public class SessionExecutionRecord {
    @Id
    @SequenceGenerator(name = "session_execution_record_id_seq", sequenceName = "session_execution_record_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "session_execution_record_id_seq")
    private Long id;

    private Integer sessionId;
    @Column(nullable = false)
    private Date startDate;
    private Date endDate;
    @ManyToOne
    @JoinColumn(name = "platform_runtime_instance_id", referencedColumnName = "id")
    private PlatformRuntimeInstance platformRuntimeInstance;
    @Enumerated(EnumType.STRING)
    private PlatformRuntimeMode platformRuntimeMode = PlatformRuntimeMode.Debug;
    @Enumerated(EnumType.STRING)
    private SessionExecutionStatus sessionExecutionStatus;

    private Date processingStartDate;
    private Date processingStopDate;

    @Type(type = "jsonb")
    private String jsonSessionExecution;


    public SessionExecutionRecord() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public SessionExecutionStatus getSessionExecutionStatus() {
        return sessionExecutionStatus;
    }

    public void setSessionExecutionStatus(SessionExecutionStatus sessionExecutionStatus) {
        this.sessionExecutionStatus = sessionExecutionStatus;
    }

    public Date getProcessingStartDate() {
        return processingStartDate;
    }

    public void setProcessingStartDate(Date processionStartDate) {
        this.processingStartDate = processionStartDate;
    }

    public Date getProcessingStopDate() {
        return processingStopDate;
    }

    public void setProcessingStopDate(Date processingStopDate) {
        this.processingStopDate = processingStopDate;
    }

    public PlatformRuntimeMode getPlatformRuntimeMode() {
        return platformRuntimeMode;
    }

    public void setPlatformRuntimeMode(PlatformRuntimeMode platformRuntimeMode) {
        this.platformRuntimeMode = platformRuntimeMode;
    }

    public String getJsonSessionExecution() {
        return jsonSessionExecution;
    }

    public void setJsonSessionExecution(String jsonSessionExecution) {
        this.jsonSessionExecution = jsonSessionExecution;
    }

    public PlatformRuntimeInstance getPlatformRuntimeInstance() {
        return platformRuntimeInstance;
    }

    public void setPlatformRuntimeInstance(PlatformRuntimeInstance platformRuntimeInstance) {
        this.platformRuntimeInstance = platformRuntimeInstance;
    }
}
