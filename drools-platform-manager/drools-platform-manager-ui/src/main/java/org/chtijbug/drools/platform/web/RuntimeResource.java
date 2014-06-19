package org.chtijbug.drools.platform.web;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepository;
import org.chtijbug.drools.platform.persistence.pojo.DroolsResource;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.chtijbug.drools.platform.web.model.RuntimeFilter;
import org.chtijbug.drools.platform.web.model.RuntimeInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 02/06/14
 * Time: 13:53
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping(value = "/runtime")
public class RuntimeResource {

    @Autowired
    PlatformRuntimeInstanceRepository platformRuntimeInstanceRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/{packageName:.+}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    public List<RuntimeInstance> findActivePlatformRuntimeInstance(@PathVariable String packageName) {
        return Lists.transform(platformRuntimeInstanceRepository.findByPackageNameActiveRuntime(packageName),
                new Function<PlatformRuntimeInstance, RuntimeInstance>() {
                    @Nullable
                    @Override
                    public RuntimeInstance apply(@Nullable PlatformRuntimeInstance platformRuntimeInstance) {
                        String url = "http://"+platformRuntimeInstance.getHostname()+":"+platformRuntimeInstance.getPort()+platformRuntimeInstance.getEndPoint();
                        //___ Weak code.... TODO
                        DroolsResource guvnorResource = platformRuntimeInstance.getDroolsRessources().get(0);
                        String rulePackage =guvnorResource.getGuvnor_packageName();
                        String version = guvnorResource.getGuvnor_packageVersion();
                        return new RuntimeInstance(platformRuntimeInstance.getId(), url, rulePackage, version);
                    }
                }
        );
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all/{packageName:.+}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    public List<PlatformRuntimeInstance> findAllPlatformRuntimeInstance(@PathVariable String packageName) {
        return platformRuntimeInstanceRepository.findByPackageNameAllRuntime(packageName);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/filter")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    public List<PlatformRuntimeInstance> findPlatformRuntimeInstanceByFilters(@RequestBody RuntimeFilter runtimeFilter) {
        return platformRuntimeInstanceRepository.findByPackageNameAllRuntime(runtimeFilter.getpackageName());
    }

}
