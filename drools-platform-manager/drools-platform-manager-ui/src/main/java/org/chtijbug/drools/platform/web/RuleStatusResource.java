package org.chtijbug.drools.platform.web;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.chtijbug.drools.guvnor.rest.model.Asset;
import org.chtijbug.drools.platform.rules.management.AssetStatus;
import org.chtijbug.drools.platform.rules.management.RuleManager;
import org.chtijbug.drools.platform.web.annotation.JsonArg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

import static org.chtijbug.drools.platform.rules.management.AssetStatus.DEV;
import static org.chtijbug.drools.platform.rules.management.AssetStatus.PROD;
import static org.chtijbug.drools.platform.rules.management.AssetStatus.getEnum;

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

    @RequestMapping(method = RequestMethod.POST)
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    public List<AssetObject> searchAllAssetsWithStatus(@RequestBody List<AssetStatus> assetStatuses) {
        logger.debug(">> searchAllAssetsWithStatus(assetStatuses={})", assetStatuses);
        try {
            List<Asset> allAssets = ruleManager.findAllAssetsByStatus(assetStatuses);
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
        } finally {
            logger.debug("<< searchAllAssetsWithStatus()");
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/promote")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    public List<AssetObject> promoteAllAssets(@JsonArg(value = "assetsToPromote") List<AssetObject> assetsToPromote, @JsonArg(value = "assetStatuses") List<AssetStatus> assetStatuses) {
        logger.debug(">> promoteAllAssets(assetStatuses={})", assetsToPromote);
        try {
            for(AssetObject assetObject : assetsToPromote) {
                AssetStatus currentStatus = getEnum(assetObject.getStatus());
                if (PROD.equals(currentStatus))
                    continue;
                this.ruleManager.updateAssetStatus(assetObject.getName(), currentStatus.getNextStatus());
            }
            return this.searchAllAssetsWithStatus(assetStatuses);
        } finally {
            logger.debug("<< promoteAllAssets()");
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/demote")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    public List<AssetObject> demoteAllAssets(@JsonArg(value = "assetsToDemote") List<AssetObject> assetsToDemote, @JsonArg(value = "assetStatuses") List<AssetStatus> assetStatuses) {
        logger.debug(">> demoteAllAssets(assetStatuses={})", assetsToDemote);
        try {
            for(AssetObject assetObject : assetsToDemote) {
                AssetStatus currentStatus = getEnum(assetObject.getStatus());
                    if (DEV.equals(currentStatus))
                    continue;
                this.ruleManager.updateAssetStatus(assetObject.getName(), currentStatus.getPreviousStatus());
            }
            return this.searchAllAssetsWithStatus(assetStatuses);
        } finally {
            logger.debug("<< demoteAllAssets()");
        }
    }

}