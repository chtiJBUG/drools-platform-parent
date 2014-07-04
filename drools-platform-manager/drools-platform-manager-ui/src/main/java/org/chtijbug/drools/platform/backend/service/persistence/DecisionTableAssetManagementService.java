package org.chtijbug.drools.platform.backend.service.persistence;

import org.chtijbug.drools.guvnor.rest.dt.DecisionTable;
import org.chtijbug.drools.guvnor.rest.model.Asset;
import org.chtijbug.drools.platform.persistence.DTRuleAssetRepository;
import org.chtijbug.drools.platform.persistence.RuleAssetRepository;
import org.chtijbug.drools.platform.persistence.pojo.DTRuleAsset;
import org.chtijbug.drools.platform.persistence.pojo.RuleAsset;
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

            }


        }


    }
}
