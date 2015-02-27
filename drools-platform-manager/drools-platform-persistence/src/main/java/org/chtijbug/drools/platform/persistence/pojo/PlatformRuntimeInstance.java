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

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "platform_runtime_instance", indexes = {@Index(columnList = "ruleBaseID")})
@Cacheable(value = true)
public class PlatformRuntimeInstance implements Serializable {

    @Id
    @SequenceGenerator(name = "platform_runtime_instance_id_seq", sequenceName = "platform_runtime_instance_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "platform_runtime_instance_id_seq")
    private Long id;
    @Column(nullable = false)
    private Date startDate;
    private Date endDate;
    private Date shutdowDate;
    @Enumerated(EnumType.STRING)
    private PlatformRuntimeInstanceStatus status;
    private Integer startEventID;
    private Integer stopEventID;
    private Integer ruleBaseID;
    @Enumerated(EnumType.STRING)
    private PlatformRuntimeMode platformRuntimeMode = PlatformRuntimeMode.Debug;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DroolsResource> droolsRessources = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "platformRuntimeInstance")
    private List<SessionExecution> sessionExecutions = new ArrayList<SessionExecution>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "platform_runtime_instance_id_fk", referencedColumnName = "id")
    private PlatformRuntimeDefinition platformRuntimeDefinition;


    public PlatformRuntimeInstance() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getShutdowDate() {
        return shutdowDate;
    }

    public void setShutdowDate(Date shutdowDate) {
        this.shutdowDate = shutdowDate;
    }

    public PlatformRuntimeInstanceStatus getStatus() {
        return status;
    }

    public void setStatus(PlatformRuntimeInstanceStatus status) {
        this.status = status;
    }

    public Integer getStartEventID() {
        return startEventID;
    }

    public void setStartEventID(Integer startEventID) {
        this.startEventID = startEventID;
    }

    public Integer getStopEventID() {
        return stopEventID;
    }

    public void setStopEventID(Integer stopEventID) {
        this.stopEventID = stopEventID;
    }

    public Integer getRuleBaseID() {
        return ruleBaseID;
    }

    public void setRuleBaseID(Integer ruleBaseID) {
        this.ruleBaseID = ruleBaseID;
    }

    public PlatformRuntimeMode getPlatformRuntimeMode() {
        return platformRuntimeMode;
    }

    public void setPlatformRuntimeMode(PlatformRuntimeMode platformRuntimeMode) {
        this.platformRuntimeMode = platformRuntimeMode;
    }

    public List<DroolsResource> getDroolsRessources() {
        return droolsRessources;
    }

    public void setDroolsRessources(List<DroolsResource> droolsResources) {
        this.droolsRessources = droolsResources;
    }

    public List<SessionExecution> getSessionExecutions() {
        return sessionExecutions;
    }

    public void setSessionExecutions(List<SessionExecution> sessionExecutions) {
        this.sessionExecutions = sessionExecutions;
    }

    public PlatformRuntimeDefinition getPlatformRuntimeDefinition() {
        return platformRuntimeDefinition;
    }

    public void setPlatformRuntimeDefinition(PlatformRuntimeDefinition platformRuntimeDefinition) {
        this.platformRuntimeDefinition = platformRuntimeDefinition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlatformRuntimeInstance)) return false;

        PlatformRuntimeInstance that = (PlatformRuntimeInstance) o;

        if (!startEventID.equals(that.startEventID)) return false;
        if (!ruleBaseID.equals(that.ruleBaseID)) return false;
        if (!startDate.equals(that.startDate)) return false;

        return true;
    }

    @Override
    public String toString() {
        return "PlatformRuntimeInstance{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", shutdowDate=" + shutdowDate +
                ", status=" + status +
                ", startEventID=" + startEventID +
                ", stopEventID=" + stopEventID +
                ", ruleBaseID=" + ruleBaseID +
                ", platformRuntimeMode=" + platformRuntimeMode +
                '}';
    }
}
