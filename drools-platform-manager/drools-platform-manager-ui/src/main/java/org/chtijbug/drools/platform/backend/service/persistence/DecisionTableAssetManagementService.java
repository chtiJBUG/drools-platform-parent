package org.chtijbug.drools.platform.backend.service.persistence;

import org.chtijbug.drools.guvnor.rest.dt.*;
import org.chtijbug.drools.guvnor.rest.model.Asset;
import org.chtijbug.drools.platform.persistence.DTRuleAssetRepository;
import org.chtijbug.drools.platform.persistence.RuleAssetRepository;
import org.chtijbug.drools.platform.persistence.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 03/07/14
 * Time: 16:04
 * To change this template use File | Settings | File Templates.
 */
@Component
public class DecisionTableAssetManagementService {


    @Autowired
    DTRuleAssetRepository dtRuleAssetRepository;

    @Autowired
    RuleAssetRepository ruleAssetRepository;


    public void SynchronizeInDBContent(String packageName, Asset asset, DecisionTable decisionTableGuvnorFormat) {
        DTRuleAsset dtruleAsset = dtRuleAssetRepository.findByPackageNameAndAssetName(packageName, asset.getName());
        if (dtruleAsset != null) {
            Integer assetVersionNumberFromGuvnor = new Integer(asset.getVersionNumber());
            if (dtruleAsset.getRuleAsset().getVersionNumber() < assetVersionNumberFromGuvnor) {
                /** Synchronozation is needed
                 *
                 */

            }
        } else {
            /** Create the Decision Table in the Database
             *
             */
            RuleAsset ruleAsset = ruleAssetRepository.findByPackageNameAndAssetName(packageName, asset.getName());
            if (ruleAsset != null) {
                DTRuleAsset dtRuleAsset = new DTRuleAsset();
                dtRuleAsset.setRuleAsset(ruleAsset);
                for (ColumnDefinition columnDefinition : decisionTableGuvnorFormat.getColumnDefinitionList()) {

                    if (columnDefinition.getColumnDefinition() == ColumnType.condition || columnDefinition.getColumnDefinition() == ColumnType.action) {
                        DTColumnDefinition dtColumnDefinition = new DTColumnDefinition();
                        dtColumnDefinition.setName(columnDefinition.getFieldType());
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
                            if (columnDefinition.getColumnDefinition() == ColumnType.condition || columnDefinition.getColumnDefinition() == ColumnType.action) {  // Only keep conditon and action
                                DTColData dtColData = new DTColData(rowElement.getColumnDefinition().getHeader(), rowElement.getValue(), rowElement.getColumnDefinition().getFieldType());
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
}

