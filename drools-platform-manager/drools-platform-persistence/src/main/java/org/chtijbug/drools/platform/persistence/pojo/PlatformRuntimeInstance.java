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
@Table(name = "platform_runtime_instance")
public class PlatformRuntimeInstance implements Serializable {

    @Id
    @SequenceGenerator(name = "platform_runtime_instance_id_seq", sequenceName = "platform_runtime_instance_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "platform_runtime_instance_id_seq")
    private Long id;

    @Column(nullable = false)
    private Integer ruleBaseID;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "platform_instance_id_fk")
    private List<PlatformRuntime> platformRuntimes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "platform_instance_id_fk")
    private List<DroolsResource> droolsRessourcesDefinition = new ArrayList<>();


    public PlatformRuntimeInstance() {
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

    public List<PlatformRuntime> getPlatformRuntimes() {
        return platformRuntimes;
    }

    public void setPlatformRuntimes(List<PlatformRuntime> platformRuntimes) {
        this.platformRuntimes = platformRuntimes;
    }

    public List<DroolsResource> getDroolsRessourcesDefinition() {
        return droolsRessourcesDefinition;
    }

    public void setDroolsRessourcesDefinition(List<DroolsResource> droolsRessourcesDefinition) {
        this.droolsRessourcesDefinition = droolsRessourcesDefinition;
    }
}
