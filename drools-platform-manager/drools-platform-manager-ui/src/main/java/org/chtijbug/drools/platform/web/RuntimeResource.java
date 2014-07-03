package org.chtijbug.drools.platform.web;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepository;
import org.chtijbug.drools.platform.persistence.pojo.*;
import org.chtijbug.drools.platform.web.model.RuntimeInstance;
import org.chtijbug.drools.platform.web.model.RuntimeStatusObject;
import org.chtijbug.drools.platform.web.model.SessionExecutionResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
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
    private static Logger logger = LoggerFactory.getLogger(RuntimeResource.class);

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
                        String url = "http://" + platformRuntimeInstance.getHostname() + ":" + platformRuntimeInstance.getPort() + platformRuntimeInstance.getEndPoint();
                        String rulePackage = null;
                        String version = null;
                        if (!platformRuntimeInstance.getDroolsRessources().isEmpty()) {
                            DroolsResource guvnorResource = platformRuntimeInstance.getDroolsRessources().get(0);
                            rulePackage = guvnorResource.getGuvnor_packageName();
                            version = guvnorResource.getGuvnor_packageVersion();
                        }
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

    @RequestMapping(method = RequestMethod.POST, value = "/filter")
    @Consumes(MediaType.APPLICATION_JSON)
    @ResponseBody
    public List<SessionExecutionResource> findPlatformRuntimeInstanceByFilters(@RequestBody final PlatformRuntimeFilter runtimeFilter) {
        logger.debug(">> findAllPlatformRuntimeInstanceByFilter(runtimeFilter= {})", runtimeFilter);
        try {
            //____ On récupere les données depuis la BDD
            final List<SessionExecution> allSessionExecutions = platformRuntimeInstanceRepository.findAllPlatformRuntimeInstanceByFilter(runtimeFilter);
            //___ TODO pour chacun de ces enregistrements, le convertir en objet JSON
            return Lists.transform(allSessionExecutions, new Function<SessionExecution, SessionExecutionResource>() {
                @Nullable
                @Override
                public SessionExecutionResource apply(@Nullable SessionExecution sessionExecution) {
                    // TODO
                    SessionExecutionResource output = new SessionExecutionResource();
                    PlatformRuntimeInstance runtimeInstance = sessionExecution.getPlatformRuntimeInstance();
                    DroolsResource guvnorResource = sessionExecution.getPlatformRuntimeInstance().getDroolsRessources().get(0);
                    assert sessionExecution != null;

                    /*

                        ruleBase ID (runtime instance)
                        sessionId (session)
                        status (runtime instance)
                        status (session)

                        start Date (session)
                        end Date (session)

                        facts (session) ?
                        startEvent (session) ?
                        endEvent (session) ?

                        rulesExecution (session)

                    */

                    output.setRuleBaseID(sessionExecution.getPlatformRuntimeInstance().getRuleBaseID());
                    output.setSessionId(sessionExecution.getSessionId());

                    if (guvnorResource.getEndDate() == null) {

                        String guvnorUrl = guvnorResource.getGuvnor_url() + guvnorResource.getGuvnor_appName();
                        output.setGuvnorUrl(guvnorUrl);
                        output.setRulePackage(guvnorResource.getGuvnor_packageName());
                        output.setVersion(guvnorResource.getGuvnor_packageVersion());

                    } else {
                        logger.debug("Skipping this entry {}", sessionExecution);
                        return null;
                    }

                    //___ Différence entre runtimeURL et hostname par rapport aux filtres ?
                    output.setRuntimeURL(runtimeInstance.getHostname()+":"+runtimeInstance.getPort()+runtimeInstance.getEndPoint());
                    output.setHostname(runtimeInstance.getHostname()+":"+runtimeInstance.getPort()+runtimeInstance.getEndPoint());

                    output.setStatus(runtimeInstance.getStatus().toString());
                    //output.setStatus(sessionExecution.getSessionExecutionStatus().toString());
                    output.setStartDate(sessionExecution.getStartDate().toString());
                    //sessionExecution.getFacts();
                    if (sessionExecution.getEndDate() != null)
                        output.setEndDate(sessionExecution.getEndDate().toString());
                    return output;
                }
            });
        } finally {
            logger.debug("<< findAllPlatformRuntimeInstanceByFilter()");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all/statuses")
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    public List<RuntimeStatusObject> getAllStatuses() {
        RuntimeStatusObject initmode = new RuntimeStatusObject(PlatformRuntimeInstanceStatus.INITMODE, "INITMODE");
        RuntimeStatusObject started = new RuntimeStatusObject(PlatformRuntimeInstanceStatus.STARTED, "STARTED");
        RuntimeStatusObject notJoignable = new RuntimeStatusObject(PlatformRuntimeInstanceStatus.NOT_JOINGNABLE, "NOT_JOIGNABLE");
        RuntimeStatusObject stopped = new RuntimeStatusObject(PlatformRuntimeInstanceStatus.STOPPED, "STOPPED");
        RuntimeStatusObject crashed = new RuntimeStatusObject(PlatformRuntimeInstanceStatus.CRASHED, "CRASHED");

        return Arrays.asList(initmode, started, notJoignable, stopped, crashed);
    }

}
