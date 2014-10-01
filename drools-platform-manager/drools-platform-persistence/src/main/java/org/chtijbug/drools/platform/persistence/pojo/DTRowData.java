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

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dt_rowdata_rule_asset")
public class DTRowData {
    @Id
    @SequenceGenerator(name = "dt_rowdata_rule_asset_id_seq", sequenceName = "dt_romwdata_rule_asset_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dt_rowdata_rule_asset_id_seq")
    private Long id;

    private Integer rowNumber;

    private String rowDescription;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "dtrowdata_id")
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

    public String getRowDescription() {
        return rowDescription;
    }

    public void setRowDescription(String rowDescription) {
        this.rowDescription = rowDescription;
    }

    public List<DTColData> getDtColDatas() {
        return dtColDatas;
    }

    public void setDtColDatas(List<DTColData> dtColDatas) {
        this.dtColDatas = dtColDatas;
    }
}
