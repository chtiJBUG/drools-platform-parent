package org.chtijbug.drools.platform.web;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.chtijbug.drools.guvnor.rest.model.Asset;
import org.chtijbug.drools.platform.rules.management.AssetStatus;
import org.chtijbug.drools.platform.rules.management.RuleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        AssetStatusObject prod = new AssetStatusObject(AssetStatus.DEV, "Production");
        return Arrays.asList(dev, integration, prod);
    }

    @RequestMapping(method = RequestMethod.GET)
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    public List<AssetObject> searchAllAssetsWithStatus(@RequestBody ArrayList<AssetStatus> assetStatuses) {
        logger.debug(">> searchAllAssetsWithStatus(assetStatuses={})", assetStatuses);
        try {
            //for each assetStatus, look up the related assets
            // Convert them into something readable on the front end (JSON
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


}
