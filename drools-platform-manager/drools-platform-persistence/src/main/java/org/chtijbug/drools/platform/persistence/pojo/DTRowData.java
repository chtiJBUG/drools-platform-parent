package org.chtijbug.drools.platform.persistence.pojo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 03/07/14
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "dt_rowdata_rule_asset")
public class DTRowData {
    @Id
    @SequenceGenerator(name = "dt_rowdata_rule_asset_id_seq", sequenceName = "dt_romwdata_rule_asset_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dt_rowdata_rule_asset_id_seq")
    private Long id;

    private Integer rowNumber;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "dt_colData_asset_id_fk")
    private List<DTColData> dtColDatas = new ArrayList<DTColData>();

    public DTRowData() {
    }

    public DTRowData(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public List<DTColData> getDtColDatas() {
        return dtColDatas;
    }

    public void setDtColDatas(List<DTColData> dtColDatas) {
        this.dtColDatas = dtColDatas;
    }
}
