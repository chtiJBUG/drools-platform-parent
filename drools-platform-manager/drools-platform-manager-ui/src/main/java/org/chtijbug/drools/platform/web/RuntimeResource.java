package org.chtijbug.drools.platform.web;

import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepository;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public List<PlatformRuntimeInstance> findActivePlatformRuntimeInstance(@PathVariable String packageName) {
        return platformRuntimeInstanceRepository.findByPackageNameActiveRuntime(packageName);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all/{packageName:.+}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    public List<PlatformRuntimeInstance> findAllPlatformRuntimeInstance(@PathVariable String packageName) {
        return platformRuntimeInstanceRepository.findByPackageNameAllRuntime(packageName);
    }

}
