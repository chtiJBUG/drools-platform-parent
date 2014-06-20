package org.chtijbug.drools.platform.web;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.chtijbug.drools.guvnor.rest.ChtijbugDroolsRestException;
import org.chtijbug.drools.guvnor.rest.model.Asset;
import org.chtijbug.drools.platform.rules.management.AssetStatus;
import org.chtijbug.drools.platform.rules.management.RuleManager;
import org.chtijbug.drools.platform.web.annotation.JsonArg;
import org.chtijbug.drools.platform.web.model.AssetObject;
import org.chtijbug.drools.platform.web.model.AssetStatusObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

import static org.chtijbug.drools.platform.rules.management.AssetStatus.*;

@Controller
@RequestMapping(value = "/rule_status")
public class RuleStatusResource {
    private static Logger logger = LoggerFactory.getLogger(RuleStatusResource.class);
    @Resource
    private RuleManager ruleManager;

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    public List<AssetStatusObject> getAllStatuses() {
        AssetStatusObject dev = new AssetStatusObject(AssetStatus.DEV, "Development");
        AssetStatusObject integration = new AssetStatusObject(AssetStatus.INT, "Integration");
        AssetStatusObject prod = new AssetStatusObject(AssetStatus.PROD, "Production");
        return Arrays.asList(dev, integration, prod);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{packageName:.+}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    public List<AssetObject> searchAllAssetsWithStatus(@PathVariable String packageName, @RequestBody List<AssetStatus> assetStatuses) {
        logger.debug(">> searchAllAssetsWithStatus(packageName={}, assetStatuses={})", packageName, assetStatuses);
        try {
            List<Asset> allAssets = ruleManager.findAllAssetsByStatus(packageName, assetStatuses);
            return Lists.transform(allAssets, new Function<Asset, AssetObject>() {
                @Nullable
                @Override
                public AssetObject apply(@Nullable Asset input) {
                    AssetObject output = new AssetObject();
                    assert input != null;
                    output.setType(input.getType());
                    output.setName(input.getName());
                    output.setStatus(input.getStatus());
                    return output;
                }
            });
        } catch (ChtijbugDroolsRestException e) {
            logger.error("<< searchAllAssetsWithStatus()", e);
            return null;
        } finally {
            logger.debug("<< searchAllAssetsWithStatus()");
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{packageName}/promote")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    public List<AssetObject> promoteAllAssets(@PathVariable String packageName, @JsonArg(value = "assetsToPromote") List<AssetObject> assetsToPromote, @JsonArg(value = "assetStatuses") List<AssetStatus> assetStatuses) {
        logger.debug(">> promoteAllAssets(packageName={}, assetStatuses={})", packageName,  assetStatuses);
        try {
            for(AssetObject assetObject : assetsToPromote) {
                AssetStatus currentStatus = getEnum(assetObject.getStatus());
                if (PROD.equals(currentStatus))
                    continue;
                this.ruleManager.updateAssetStatus(packageName, assetObject.getName(), currentStatus.getNextStatus());
            }
            return this.searchAllAssetsWithStatus(packageName, assetStatuses);
        } catch (ChtijbugDroolsRestException e) {
            logger.error("<< promoteAllAssets()", e);
            return null;
        } finally {
            logger.debug("<< promoteAllAssets()");
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{packageName}/demote")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    public List<AssetObject> demoteAllAssets(@PathVariable String packageName, @JsonArg(value = "assetsToDemote") List<AssetObject> assetsToDemote, @JsonArg(value = "assetStatuses") List<AssetStatus> assetStatuses) {
        logger.debug(">> demoteAllAssets(packageName={}, assetStatuses={})", packageName,  assetStatuses);
        try {
            for(AssetObject assetObject : assetsToDemote) {
                AssetStatus currentStatus = getEnum(assetObject.getStatus());
                    if (DEV.equals(currentStatus))
                    continue;
                this.ruleManager.updateAssetStatus(packageName, assetObject.getName(), currentStatus.getPreviousStatus());
            }
            return this.searchAllAssetsWithStatus(packageName, assetStatuses);
        } catch (ChtijbugDroolsRestException e) {
            logger.error("<< demoteAllAssets()",e);
            return null;
        } finally {
            logger.debug("<< demoteAllAssets()");
        }
    }

}
