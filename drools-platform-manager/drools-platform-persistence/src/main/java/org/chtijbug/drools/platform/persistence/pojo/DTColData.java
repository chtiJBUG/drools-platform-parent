package org.chtijbug.drools.platform.persistence.pojo;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * Date: 03/07/14
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "dt_coldata_rule_asset")
public class DTColData {
    @Id
    @SequenceGenerator(name = "dt_coldata_rule_asset_id_seq", sequenceName = "dt_colwdata_rule_asset_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dt_coldata_rule_asset_id_seq")
    private Long id;

    private String columnName;

    private String valueString;

    private String dataType;

    public DTColData() {
    }

    public DTColData(String columnName, String valueString, String dataType) {
        this.columnName = columnName;
        this.valueString = valueString;
        this.dataType = dataType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
