package org.chtijbug.drools.platform.rules.management;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import org.apache.commons.lang.StringUtils;
import org.chtijbug.drools.guvnor.rest.ChtijbugDroolsRestException;
import org.chtijbug.drools.guvnor.rest.GuvnorRepositoryConnector;
import org.chtijbug.drools.guvnor.rest.model.Asset;
import org.chtijbug.drools.guvnor.rest.model.AssetPropertyType;
import org.chtijbug.drools.guvnor.rest.model.Snapshot;
import org.chtijbug.drools.platform.rules.config.RuntimeSiteTopology;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.*;
import static com.google.common.collect.Lists.transform;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by IntelliJ IDEA.
 * Date: 05/04/13
 * Time: 11:12
 * To change this template use File | Settings | File Templates.
 */
@Component
public class RuleManager {
    private static Logger logger = LoggerFactory.getLogger(RuleManager.class);

    @VisibleForTesting
    GuvnorRepositoryConnector guvnorRepositoryConnector;


    @Autowired
    public RuleManager(RuntimeSiteTopology runtimeSiteTopology) {
        guvnorRepositoryConnector = new GuvnorRepositoryConnector(runtimeSiteTopology.buildGuvnorConfiguration());
    }

    public List<Asset> findAllAssetsByStatus(String packageName, List<AssetStatus> assetStatuses) throws ChtijbugDroolsRestException {
        //___ Fetch all assets metadata
        List<Asset> assets = guvnorRepositoryConnector.getAllBusinessAssets(packageName);
        //___ If no filter provided, then return the whole list
        if (assetStatuses == null || assetStatuses.isEmpty())
            return assets;
        //___ Create the proper matcher with the filters provided
        List<Matcher<String>> matchers = new ArrayList<>(assetStatuses.size());
        for (AssetStatus assetStatus : assetStatuses) {
            matchers.add(equalTo(assetStatus.toString()));
        }
        Matcher[] mat = new Matcher[matchers.size()];
        Matcher anyOfFilters = Matchers.anyOf(matchers.toArray(mat));
        //_____ Extract those with the expected status
        return select(assets, having(on(Asset.class).getStatus(), anyOfFilters));
    }

    public void updateAssetStatus(String packageName, String assetName, AssetStatus assetStatus) throws ChtijbugDroolsRestException {
        guvnorRepositoryConnector.changeAssetPropertyValue(packageName, assetName, AssetPropertyType.STATE, assetStatus.toString());
    }

    public void buildAndTakeSnapshot(List<AssetStatus> buildScope, String version) throws Exception {
        if (isSnapshotVersion(version)) {
            this.guvnorRepositoryConnector.deletePackageSnapshot(version);
        }

        List<Snapshot> listSnapshots = this.guvnorRepositoryConnector.getListSnapshots();
        for (Snapshot snapshot : listSnapshots) {
            if (snapshot.getName().equals(version)) {
                logger.warn("Cannot take a RulePackage Snapshot. Version already exists");
                throw new AdministrationBusinessProcessException(BusinessProcessError.VERSION_ALREADY_EXISTS, "Snapshot already existing");
            }
        }
        String filter = StringUtils.join(buildScope, ",");
        this.guvnorRepositoryConnector.buildRulePackageByStatus(version, filter);
    }

    private boolean isSnapshotVersion(String version) {
        return version.endsWith("-SNAPSHOT");
    }

    public List<Snapshot> getAvailableSnapshots() throws Exception {
        return guvnorRepositoryConnector.getListSnapshots();
    }


    public List<String> findAllPackages() {
        return transform(this.guvnorRepositoryConnector.getAllPackagesInGuvnorRepo(), new Function<Asset, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Asset asset) {
                return asset.getName();
            }
        });
    }
}
