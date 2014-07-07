package org.chtijbug.drools.platform.persistence.pojo;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * Date: 03/07/14
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "dt_columndefinition_rule_asset")
public class DTColumnDefinition {
    @Id
    @SequenceGenerator(name = "dt_columndefinition_rule_asset_id_seq", sequenceName = "dt_columndefinition_rule_asset_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dt_columndefinition_rule_asset_id_seq")
    private Long id;

    private String name;

    private String dataType;

    public DTColumnDefinition() {
    }

    public DTColumnDefinition(String name, String dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
