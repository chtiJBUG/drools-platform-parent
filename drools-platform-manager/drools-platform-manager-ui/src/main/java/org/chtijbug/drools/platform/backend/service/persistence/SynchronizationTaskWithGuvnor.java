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

import com.google.common.base.Throwables;
import org.chtijbug.drools.guvnor.rest.ChtijbugDroolsRestException;
import org.chtijbug.drools.guvnor.rest.GuvnorRepositoryConnector;
import org.chtijbug.drools.guvnor.rest.model.Asset;
import org.chtijbug.drools.platform.rules.config.RuntimeSiteTopology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

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


    @Scheduled(cron = "* */20 9-18 *  * MON-FRI ")
    public void SynchronizeGuvnorCategories() {
        /**
        try {

            List<Package> listPackages = guvnorRepositoryConnector.getAllPackagesInGuvnorRepo();
            for (Package packageAsset : listPackages) {
                String packageName = packageAsset.getName();
                List<Asset> assetList = guvnorRepositoryConnector.getAllBusinessAssets(packageName);
                for (Asset asset : assetList) {
                    ruleAssetManagementService.synchronizeInDBGuvnorCategories(packageName, asset);

                     if (asset.getType().equals("gdst")) {
                     try {
                     DecisionTable decisionTableGuvnorFormat = guvnorRepositoryConnector.getGuidedDecisionTable(packageName, asset.getName());
                     decisionTableAssetManagementService.SynchronizeInDBContent(packageName, asset, decisionTableGuvnorFormat);
                     } catch (GuvnorConnexionFailedException e) {
                     e.printStackTrace();
                     }

                     }


                }
            }
        } catch (ChtijbugDroolsRestException e) {
            throw Throwables.propagate(e);
        }
                **/
    }

}
