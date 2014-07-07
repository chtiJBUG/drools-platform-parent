package org.chtijbug.drools.platform.persistence.pojo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 03/07/14
 * Time: 15:14
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "dt_rule_asset")
public class DTRuleAsset {
    @Id
    @SequenceGenerator(name = "dt_rule_asset_id_seq", sequenceName = "dt_rule_asset_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dt_rule_asset_id_seq")
    private Long id;


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private RuleAsset ruleAsset;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "dtruleasset_id")
    private List<DTColumnDefinition> dtColumnDefinitions = new ArrayList<DTColumnDefinition>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "dtruleasset_id")
    private List<DTRowData> dtRowDatas = new ArrayList<DTRowData>();

    public DTRuleAsset() {
    }


    public RuleAsset getRuleAsset() {
        return ruleAsset;
    }

    public void setRuleAsset(RuleAsset ruleAsset) {
        this.ruleAsset = ruleAsset;
    }

    public List<DTColumnDefinition> getDtColumnDefinitions() {
        return dtColumnDefinitions;
    }

    public void setDtColumnDefinitions(List<DTColumnDefinition> dtColumnDefinitions) {
        this.dtColumnDefinitions = dtColumnDefinitions;
    }

    public List<DTRowData> getDtRowDatas() {
        return dtRowDatas;
    }

    public void setDtRowDatas(List<DTRowData> dtRowDatas) {
        this.dtRowDatas = dtRowDatas;
    }
}
