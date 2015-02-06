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
package org.chtijbug.drools.platform.rules.management;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.chtijbug.drools.guvnor.rest.ChtijbugDroolsRestException;
import org.chtijbug.drools.guvnor.rest.GuvnorRepositoryConnector;
import org.chtijbug.drools.guvnor.rest.model.AssetPropertyType;
import org.chtijbug.drools.platform.rules.config.RuntimeSiteTopology;
import org.drools.guvnor.server.jaxrs.jaxb.*;
import org.drools.guvnor.server.jaxrs.jaxb.Package;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ch.lambdaj.Lambda.*;
import static com.google.common.collect.Lists.transform;
import static org.hamcrest.Matchers.equalTo;

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
        return assets;//select(assets, having(on(Asset.class).getStatus(), anyOfFilters));
    }

    public void updateAssetStatus(String packageName, String assetName, AssetStatus assetStatus) throws ChtijbugDroolsRestException {
        guvnorRepositoryConnector.changeAssetPropertyValue(packageName, assetName, AssetPropertyType.STATE, assetStatus.toString());
    }

    public void buildAndTakeSnapshot(String packageName, List<AssetStatus> buildScope, String version) throws Exception {
        if (isSnapshotVersion(version)) {
            this.guvnorRepositoryConnector.deletePackageSnapshot(packageName, version);
        }

        List<String> listSnapshots = this.guvnorRepositoryConnector.getListSnapshots(packageName);
        for (String snapshot : listSnapshots) {
            if (snapshot.equals(version)) {
                logger.warn("Cannot take a RulePackage Snapshot. Version already exists");
                throw new AdministrationBusinessProcessException(BusinessProcessError.VERSION_ALREADY_EXISTS, "Snapshot already existing");
            }
        }
        String filter = StringUtils.join(buildScope, ",");
        this.guvnorRepositoryConnector.buildRulePackageByStatus(packageName, version, filter);
    }

    private boolean isSnapshotVersion(String version) {
        return version.endsWith("-SNAPSHOT");
    }

    public List<String> getAvailableSnapshots() throws Exception {
        return guvnorRepositoryConnector.getListSnapshots();
    }


    public List<String> findAllPackages() {
        return transform(this.guvnorRepositoryConnector.getAllPackagesInGuvnorRepo(), new Function<java.lang.Package, String>() {
            @Nullable
            @Override
            public String apply(@Nullable java.lang.Package aPackage) {
                return aPackage.getName();
            }
        });
    }

    //___ Fetch all package's versions with the package's name
    public List<String> findAllPackageVersionsByName(String packageName) throws ChtijbugDroolsRestException {
        List<String> listPackages = guvnorRepositoryConnector.getListSnapshots(packageName);
        return listPackages;
    }

    //___ Fetch all package's versions with the package's name and version
    public List<String> findAllPackageVersionsByNameAndVersion(String packageName, final String version) {
        try {
            List<String> allSnapshots = findAllPackageVersionsByName(packageName);
            Collection<String> filteredSnapshots = Collections2.filter(allSnapshots, new Predicate<String>() {
                @Override
                public boolean apply(@Nullable String snapshot) {
                    return snapshot.matches(version);
                }
            });

            return Lists.newArrayList(filteredSnapshots);
        } catch (ChtijbugDroolsRestException e) {
            throw Throwables.propagate(e);
        }
    }

    public void deletePackageVersion(String packageName, String version) throws ChtijbugDroolsRestException {
        this.guvnorRepositoryConnector.deletePackageSnapshot(packageName, version);
    }
}
