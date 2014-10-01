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
package org.chtijbug.drools.platform.backend.service.persistence;

import org.chtijbug.drools.guvnor.rest.dt.*;
import org.chtijbug.drools.guvnor.rest.model.Asset;
import org.chtijbug.drools.platform.persistence.DTRuleAssetRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.RuleAssetRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class DecisionTableAssetManagementService {


    @Autowired
    DTRuleAssetRepositoryCacheService dtRuleAssetRepository;

    @Autowired
    RuleAssetRepositoryCacheService ruleAssetRepository;

    @Transactional
    public void SynchronizeInDBContent(String packageName, Asset asset, DecisionTable decisionTableGuvnorFormat) {

        DTRuleAsset dtruleAsset = dtRuleAssetRepository.findByPackageNameAndAssetName(packageName, asset.getName());
        if (dtruleAsset != null) {
            Integer assetVersionNumberFromGuvnor = new Integer(asset.getVersionNumber());
            if (dtruleAsset.getRuleAsset().getVersionNumber() < assetVersionNumberFromGuvnor) {
                /** Synchronozation is needed
                 *  Guvnor is the master so delete it
                 */
                dtRuleAssetRepository.delete(dtruleAsset);
                //this.createDTTableInDB(packageName, asset, decisionTableGuvnorFormat);
            }
        } else {
            //this.createDTTableInDB(packageName, asset, decisionTableGuvnorFormat);


        }
    }

    private void createDTTableInDB(String packageName, Asset asset, DecisionTable decisionTableGuvnorFormat) {
        /**
         *  Create a copy of the Decision Table in the Database
         **/
        RuleAsset ruleAsset = ruleAssetRepository.findByPackageNameAndAssetName(packageName, asset.getName());
        if (ruleAsset != null) {
            DTRuleAsset dtRuleAsset = new DTRuleAsset();
            ruleAsset.setVersionNumber(new Integer(asset.getVersionNumber()));
            dtRuleAsset.setRuleAsset(ruleAsset);
            for (ColumnDefinition columnDefinition : decisionTableGuvnorFormat.getColumnDefinitionList()) {

                if (columnDefinition.isHideColumn() == false && (columnDefinition.getColumnDefinition() == ColumnType.condition || columnDefinition.getColumnDefinition() == ColumnType.action)) {
                    DTColumnDefinition dtColumnDefinition = new DTColumnDefinition();
                    dtColumnDefinition.setDataType(columnDefinition.getFieldType());
                    dtColumnDefinition.setName(columnDefinition.getHeader());
                    dtRuleAsset.getDtColumnDefinitions().add(dtColumnDefinition);
                }
            }
            for (Row row : decisionTableGuvnorFormat.getRows()) {
                DTRowData dtRowData = new DTRowData();
                dtRuleAsset.getDtRowDatas().add(dtRowData);
                int i = 0;
                for (RowElement rowElement : row.getRowElements()) {
                    if (i == 0) { //First column = RowNumber
                        dtRowData.setRowNumber(new Integer(rowElement.getValue()));
                    } else if (i == 1) {  //second column is the description of the row
                        dtRowData.setRowDescription(rowElement.getValue());
                    } else if (i > 1) { //Real Data column
                        ColumnDefinition columnDefinition = rowElement.getColumnDefinition();
                        if (columnDefinition.isHideColumn() == false && (columnDefinition.getColumnDefinition() == ColumnType.condition || columnDefinition.getColumnDefinition() == ColumnType.action)) {  // Only keep conditon and action
                            String elementValue = null;
                            switch (rowElement.getDtCellValue52().getDataType()) {
                                case NUMERIC_LONG:
                                case NUMERIC:
                                    if (rowElement.getDtCellValue52().getNumericValue() != null) {
                                        elementValue = rowElement.getDtCellValue52().getNumericValue().toString();
                                    }
                                    break;
                                case STRING:
                                    elementValue = rowElement.getDtCellValue52().getStringValue();
                                    break;
                                case DATE:
                                    if (rowElement.getDtCellValue52().getDateValue() != null) {
                                        elementValue = rowElement.getDtCellValue52().getDateValue().toString();
                                    }
                                    break;
                                case BOOLEAN:
                                    if (rowElement.getDtCellValue52().getBooleanValue() != null) {
                                        elementValue = rowElement.getDtCellValue52().getBooleanValue().toString();
                                    }
                                    break;
                            }
                            DTColData dtColData = new DTColData(rowElement.getColumnDefinition().getHeader(), elementValue, rowElement.getColumnDefinition().getFieldType());
                            dtRowData.getDtColDatas().add(dtColData);
                        }
                    }
                    i++;
                }
            }
            dtRuleAssetRepository.save(dtRuleAsset);

        }
    }

}

