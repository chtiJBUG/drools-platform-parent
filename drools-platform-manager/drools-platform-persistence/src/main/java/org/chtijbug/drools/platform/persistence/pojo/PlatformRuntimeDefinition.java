package org.chtijbug.drools.platform.persistence.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/01/14
 * Time: 20:20
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "platform_runtime_definition")
public class PlatformRuntimeDefinition implements Serializable {

    @Id
    @SequenceGenerator(name = "platform_runtime_definition_id_seq", sequenceName = "platform_runtime_definition_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "platform_runtime_definition_id_seq")
    private Long id;

    @Column(nullable = false)
    private Integer ruleBaseID;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "platformRuntimeDefinition")
    private List<PlatformRuntimeInstance> platformRuntimeInstances = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DroolsResource> droolsRessourcesDefinition = new ArrayList<>();

    private String couldInstanceStartWithNewRuleVersion;

    public PlatformRuntimeDefinition() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Integer getRuleBaseID() {
        return ruleBaseID;
    }

    public void setRuleBaseID(Integer ruleBaseID) {
        this.ruleBaseID = ruleBaseID;
    }

    public List<PlatformRuntimeInstance> getPlatformRuntimeInstances() {
        return platformRuntimeInstances;
    }

    public void setPlatformRuntimeInstances(List<PlatformRuntimeInstance> platformRuntimeInstances) {
        this.platformRuntimeInstances = platformRuntimeInstances;
    }

    public List<DroolsResource> getDroolsRessourcesDefinition() {
        return droolsRessourcesDefinition;
    }

    public void setDroolsRessourcesDefinition(List<DroolsResource> droolsRessourcesDefinition) {
        this.droolsRessourcesDefinition = droolsRessourcesDefinition;
    }

    public String getCouldInstanceStartWithNewRuleVersion() {
        return couldInstanceStartWithNewRuleVersion;
    }

    public void setCouldInstanceStartWithNewRuleVersion(String couldInstanceStartWithNewRuleVersion) {
        this.couldInstanceStartWithNewRuleVersion = couldInstanceStartWithNewRuleVersion;
    }
}
