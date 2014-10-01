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
package org.chtijbug.drools.platform.web;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.chtijbug.drools.guvnor.rest.ChtijbugDroolsRestException;
import org.chtijbug.drools.platform.rules.config.Environment;
import org.chtijbug.drools.platform.rules.config.RuntimeSiteTopology;
import org.chtijbug.drools.platform.rules.management.AssetStatus;
import org.chtijbug.drools.platform.rules.management.RuleManager;
import org.chtijbug.drools.platform.rules.management.RuntimeManager;
import org.chtijbug.drools.platform.web.model.PackageSnapshot;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/rules_package")
public class RulesPackageResource {
    @Resource
    private RuleManager ruleManager;
    @Resource
    private RuntimeManager runtimeManager;
    @Resource
    private RuntimeSiteTopology runtimeSiteTopology;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    public List<String> getAllPackages() throws Exception {
        return this.ruleManager.findAllPackages();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{packageName:.+}/{version:.+}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    public List<PackageSnapshot> getAllPackageVersions(@PathVariable final String packageName, @PathVariable String version) throws Exception {
        List<String> result;
        if (version.equals("default")) {
            result = this.ruleManager.findAllPackageVersionsByName(packageName);
        } else if (version.equals("default-SNAPSHOT")) {
            version = ".*-SNAPSHOT";
            result = this.ruleManager.findAllPackageVersionsByNameAndVersion(packageName, version);
        } else {
            if (version.equals("*")) {
                version = ".*";
            } else if (version.equals("*-SNAPSHOT")) {
                version = ".*-SNAPSHOT";
            }
            result = this.ruleManager.findAllPackageVersionsByNameAndVersion(packageName, version);
        }
        return Lists.transform(result, new Function<String, PackageSnapshot>() {
            @Nullable
            @Override
            public PackageSnapshot apply(@Nullable String snapshot) {
                return new PackageSnapshot(packageName, snapshot);
            }
        });
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{packageName:.+}/{version:.+}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    public void setPackageVersion(@PathVariable final String packageName, @PathVariable String version, @RequestBody List<AssetStatus> buildScope) throws Exception {
        try {
            this.ruleManager.buildAndTakeSnapshot(packageName, buildScope, version);
        } catch (ChtijbugDroolsRestException e) {
            // TODO
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delete/{packageName:.+}/{version:.+}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    public void deleteVersion(@PathVariable final String packageName, @PathVariable String version) throws Exception {
        this.ruleManager.deletePackageVersion(packageName, version);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/rebuild/{packageName:.+}/{version:.+}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    public void rebuildPackageVersion(@PathVariable final String packageName, @PathVariable String version, @RequestBody List<AssetStatus> buildScope) throws Exception {
        try {
            this.ruleManager.buildAndTakeSnapshot(packageName, buildScope, version);
        } catch (ChtijbugDroolsRestException e) {
            // TODO
        }
    }

    @RequestMapping(value = "/envs", method = RequestMethod.GET)
    @Produces(value = MediaType.APPLICATION_JSON)
    public List<Environment> findAllEnvironments() {
        return new ArrayList<>(runtimeSiteTopology.getEnvironments());
    }

    @RequestMapping(value = "/envs/{environmentName:.+}", method = RequestMethod.GET)
    @ResponseBody
    public String getDeployedPackageVersion(@PathVariable String environmentName) {
        return runtimeManager.getDeployedPackageVersion(environmentName);
    }

    @RequestMapping(value = "/deploy/{environmentName}/{version:.+}", method = RequestMethod.POST)
    @ResponseBody
    public void updateEnvironment(@PathVariable String environmentName, @PathVariable String version) {
        runtimeManager.updateSettings(environmentName, version);
    }

    @RequestMapping(value = "/snapshots", method = RequestMethod.GET)
    @Produces(value = MediaType.APPLICATION_JSON)
    public List<String> getAvailableSnapshots() throws Exception {
        return Lists.transform(ruleManager.getAvailableSnapshots(), new Function<String, String>() {
            @Nullable
            @Override
            public String apply(@Nullable String input) {
                if (input != null) {
                    return input;
                }
                return null;
            }
        });
    }

}
