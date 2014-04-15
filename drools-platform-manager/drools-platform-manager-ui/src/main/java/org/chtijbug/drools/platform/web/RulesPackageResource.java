package org.chtijbug.drools.platform.web;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.chtijbug.drools.guvnor.rest.model.Snapshot;
import org.chtijbug.drools.platform.rules.config.Environment;
import org.chtijbug.drools.platform.rules.config.RuntimeSiteTopology;
import org.chtijbug.drools.platform.rules.management.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
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
        // TODO Need to unmock the following result...
        // this.ruleManager.findAllPackages();
        // ruleManager.getAvailableSnapshots()
        return Arrays.asList("package 1", "package 2", "package 3");

    }

    @RequestMapping(value = "/build", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> createRulePackageVersion(@RequestBody PackageSnapshotRequest packageSnapshotRequest) throws Exception {
        try {
            this.ruleManager.buildAndTakeSnapshot(packageSnapshotRequest.getAssetStatuses(), packageSnapshotRequest.constructVersionName());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AdministrationBusinessProcessException e) {
            if(BusinessProcessError.VERSION_ALREADY_EXISTS.equals(e.getError())) {
                return new ResponseEntity<>("This snapshot name does already exists.", HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>("Not supported case", HttpStatus.INTERNAL_SERVER_ERROR);
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
        return Lists.transform(ruleManager.getAvailableSnapshots(), new Function<Snapshot, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Snapshot input) {
                if (input != null) {
                    return input.getName();
                }
                return null;
            }
        });
    }

}
