package org.chtijbug.drools.platform.rules.management;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang.StringUtils;
import org.chtijbug.drools.guvnor.rest.GuvnorRepositoryConnector;
import org.chtijbug.drools.guvnor.rest.model.Asset;
import org.chtijbug.drools.guvnor.rest.model.AssetPropertyType;
import org.chtijbug.drools.guvnor.rest.model.Snapshot;
import org.chtijbug.drools.platform.rules.config.RuntimeSiteTopology;
import org.hamcrest.Matchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.*;

/**
 * Created by IntelliJ IDEA.
 * Date: 05/04/13
 * Time: 11:12
 * To change this template use File | Settings | File Templates.
 */
@Service
public class RuleManager {
    private static Logger logger = LoggerFactory.getLogger(RuleManager.class);

    @VisibleForTesting
    GuvnorRepositoryConnector guvnorRepositoryConnector;


    @Autowired
    public RuleManager(RuntimeSiteTopology runtimeSiteTopology) {
        guvnorRepositoryConnector = new GuvnorRepositoryConnector(runtimeSiteTopology.buildGuvnorConfiguration());
    }

    public List<String> findAllAssetsByStatus(AssetStatus assetStatus) {
        List<String> result = new ArrayList<String>();
        List<Asset> assets = guvnorRepositoryConnector.getAllBusinessAssets();

        //_____ If NONE then return the whole list
        if (AssetStatus.NONE.equals(assetStatus)) {
            return result;
        }
        //_____ Extract those with the expected status
        List<Asset> resultAssets =
                select(assets, having(on(Asset.class).getStatus(), Matchers.equalTo(assetStatus.toString())));
        result = extract(resultAssets, on(Asset.class).getName());
        return result;
    }

    public void updateAllAssetsWithStatus(List<String> assets, final AssetStatus assetStatus) {
        AssetStatus nextStatus;
        if (assetStatus == null)
            nextStatus = AssetStatus.DEV;
        else
            nextStatus = assetStatus.getNextStatus();

        for (String assetName : assets) {
            guvnorRepositoryConnector.changeAssetPropertyValue(assetName, AssetPropertyType.STATE, nextStatus.toString());

        }
    }

    public void buildAndTakeSnapshot(AssetStatus status, String version) throws Exception {
        List<Snapshot> listSnapshots = this.guvnorRepositoryConnector.getListSnapshots();
        for (Snapshot snapshot : listSnapshots) {
            if (snapshot.getName().equals(version)) {
                logger.warn("Cannot take a RulePackage Snapshot. Version already exists");
                throw new AdministrationBusinessProcessException(BusinessProcessError.VERSION_ALREADY_EXISTS, "Snapshot already existing");
            }
        }
        AssetStatus toEvaluate = status;
        List<AssetStatus> buildScope = new ArrayList<AssetStatus>();
        while (toEvaluate != null) {
            buildScope.add(toEvaluate);
            toEvaluate = toEvaluate.getNextStatus();
        }
        String filter = StringUtils.join(buildScope, ",");
        this.guvnorRepositoryConnector.buildRulePackageByStatus(version, filter);
    }

    public void revertAllAssetsToStatus(List<String> assets, AssetStatus assetStatus) {
        for (String assetName : assets) {
            guvnorRepositoryConnector.changeAssetPropertyValue(assetName, AssetPropertyType.STATE, assetStatus.toString());
        }
    }

    public List<Snapshot> getAvailableSnapshots() throws Exception {
        return guvnorRepositoryConnector.getListSnapshots();
    }
}
