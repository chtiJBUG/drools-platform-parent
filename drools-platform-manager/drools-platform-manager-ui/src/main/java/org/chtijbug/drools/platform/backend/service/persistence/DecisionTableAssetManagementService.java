package org.chtijbug.drools.platform.backend.service.persistence;

import org.chtijbug.drools.guvnor.rest.dt.*;
import org.chtijbug.drools.guvnor.rest.model.Asset;
import org.chtijbug.drools.platform.persistence.DTRuleAssetRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.RuleAssetRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * Date: 03/07/14
 * Time: 16:04
 * To change this template use File | Settings | File Templates.
 */
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
