package org.chtijbug.drools.platform.backend.service.persistence;

import com.google.common.base.Throwables;
import org.chtijbug.drools.guvnor.rest.ChtijbugDroolsRestException;
import org.chtijbug.drools.guvnor.rest.GuvnorRepositoryConnector;
import org.chtijbug.drools.guvnor.rest.model.Asset;
import org.chtijbug.drools.platform.rules.config.RuntimeSiteTopology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 03/07/14
 * Time: 15:38
 * To change this template use File | Settings | File Templates.
 */
@Component
public class SynchronizationTaskWithGuvnor {

    @Autowired
    private DecisionTableAssetManagementService decisionTableAssetManagementService;

    @Autowired
    private RuleAssetManagementService ruleAssetManagementService;

    GuvnorRepositoryConnector guvnorRepositoryConnector;

    @Autowired
    public SynchronizationTaskWithGuvnor(RuntimeSiteTopology runtimeSiteTopology) {
        guvnorRepositoryConnector = new GuvnorRepositoryConnector(runtimeSiteTopology.buildGuvnorConfiguration());
    }


    //@Scheduled(cron = "* */15 9-18 *  * MON-FRI ")
    public void SynchronizeGuvnorCategories() {
        try {
            List<Asset> listPackages = guvnorRepositoryConnector.getAllPackagesInGuvnorRepo();
            for (Asset packageAsset : listPackages) {
                String packageName = packageAsset.getName();
                List<Asset> assetList = guvnorRepositoryConnector.getAllBusinessAssets(packageName);
                for (Asset asset : assetList) {
                    ruleAssetManagementService.synchronizeInDBGuvnorCategories(packageName, asset);
                    /**
                     if (asset.getType().equals("gdst")) {
                     try {
                     DecisionTable decisionTableGuvnorFormat = guvnorRepositoryConnector.getGuidedDecisionTable(packageName, asset.getName());
                     decisionTableAssetManagementService.SynchronizeInDBContent(packageName, asset, decisionTableGuvnorFormat);
                     } catch (GuvnorConnexionFailedException e) {
                     e.printStackTrace();
                     }

                     }

                     **/
                }
            }
        } catch (ChtijbugDroolsRestException e) {
            throw Throwables.propagate(e);
        }
    }

}
