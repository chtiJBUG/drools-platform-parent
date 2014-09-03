package org.chtijbug.drools.platform.web;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.SessionExecutionRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.pojo.*;
import org.chtijbug.drools.platform.web.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
    PlatformRuntimeInstanceRepositoryCacheService platformRuntimeInstanceRepository;
    @Autowired
    SessionExecutionRepositoryCacheService sessionExecutionRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/{packageName:.+}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Transactional
    public List<RuntimeInstance> findActivePlatformRuntimeInstance(@PathVariable String packageName) {
        return Lists.transform(platformRuntimeInstanceRepository.findByPackageNameActiveRuntime(packageName),
                new Function<PlatformRuntimeInstance, RuntimeInstance>() {
                    @Nullable
                    @Override
                    public RuntimeInstance apply(@Nullable PlatformRuntimeInstance platformRuntimeInstance) {
                        String url = "http://" + platformRuntimeInstance.getHostname() + ":" + platformRuntimeInstance.getPort() + platformRuntimeInstance.getEndPoint();
                        String rulePackage = null;
                        String version = null;
                        PlatformRuntimeDefinition platformRuntimeDefinition = platformRuntimeInstance.getPlatformRuntimeDefinition();
                        if (!platformRuntimeDefinition.getDroolsRessourcesDefinition().isEmpty()) {
                            DroolsResource guvnorResource = platformRuntimeDefinition.getDroolsRessourcesDefinition().get(0);
                            rulePackage = guvnorResource.getGuvnor_packageName();
                            version = guvnorResource.getGuvnor_packageVersion();
                        }
                        return new RuntimeInstance(platformRuntimeInstance.getId(), platformRuntimeInstance.getRuleBaseID(), url, rulePackage, version);
                    }
                }
        );
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all/{packageName:.+}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Transactional
    public List<PlatformRuntimeInstance> findAllPlatformRuntimeInstance(@PathVariable String packageName) {
        return platformRuntimeInstanceRepository.findByPackageNameAllRuntime(packageName);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/session/{Id}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Transactional
    public SessionExecutionDetailsResource findSessionExecutionDetails(@PathVariable Long Id) {
        logger.debug(">> findSessionExecutionDetails(sessionId= {})", Id);
        try {
            //____ Data from Database
            final SessionExecution sessionExecution = sessionExecutionRepository.findByIdForUI(Id);
            // final SessionExecution sessionExecution = sessionExecutionRepository.findDetailsBySessionId(sessionID);
            SessionExecutionDetailsResource executionDetailsResource = new SessionExecutionDetailsResource();
            ProcessExecution processExecution = sessionExecution.getProcessExecutions().get(0);
            ProcessDetails processDetails = new ProcessDetails();

            List<Fact> inputFactList = Lists.newArrayList(sessionExecution.getFactsByType(FactType.INPUTDATA));
            List<Fact> outputFactList = Lists.newArrayList(sessionExecution.getFactsByType(FactType.OUTPUTDATA));

            if (sessionExecution.getProcessExecutions().size() != 0) {
                processDetails.setProcessName(processExecution.getProcessName());

                if (processExecution.getProcessVersion() != null) {
                    processDetails.setProcessVersion(processExecution.getProcessVersion());
                } else {
                    processDetails.setProcessVersion("");
                }

                processDetails.setProcessExecutionStatus(processExecution.getProcessExecutionStatus().toString());
                processDetails.setProcessType(processExecution.getProcessType());

                executionDetailsResource.setProcessDetails(processDetails);

                for (RuleflowGroup ruleFlowGroup : processExecution.getRuleflowGroups()) {
                    RuleFlowGroupDetails ruleFlowGroupDetails = new RuleFlowGroupDetails();
                    ruleFlowGroupDetails.setRuleflowGroup(ruleFlowGroup.getRuleflowGroup());
                    //___ Add rule execution details list
                    for (RuleExecution ruleExecution : ruleFlowGroup.getRuleExecutionList()) {
                        RuleExecutionDetails ruleExecutionDetails = new RuleExecutionDetails();
                        ruleExecutionDetails.setPackageName(ruleExecution.getPackageName());
                        ruleExecutionDetails.setRuleName(ruleExecution.getRuleName());
                        ruleExecutionDetails.setWhenFacts(ruleExecution.getWhenFacts());
                        ruleExecutionDetails.setThenFacts(ruleExecution.getThenFacts());

                        RuleAssetDetails ruleAssetDetails = new RuleAssetDetails();
                        ruleAssetDetails.setAssetName(ruleExecution.getRuleAsset().getAssetName());
                        ruleAssetDetails.setVersionNumber(ruleExecution.getRuleAsset().getVersionNumber());
                        ruleAssetDetails.setRuleAssetCategory(ruleExecution.getRuleAsset().getRuleAssetCategory());

                        ruleExecutionDetails.setRuleAsset(ruleAssetDetails);

                        ruleFlowGroupDetails.addRuleExecution(ruleExecutionDetails); //Ajout de la ruleExecutionDetails dans la liste
                    }
                    executionDetailsResource.addRuleFlowGroup(ruleFlowGroupDetails);
                }
                for (Fact inputFact : inputFactList) {
                    executionDetailsResource.setInputObject(inputFact.getJsonFact());
                }
                for (Fact outputFact : outputFactList) {
                    executionDetailsResource.setOutputObject(outputFact.getJsonFact());
                }
                //logger.debug("Skipping this entry {}", sessionId);
            }
            return executionDetailsResource;
        } finally {
            logger.debug("<< findSessionExecutionDetails()");
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @ResponseBody
    public Integer countPlatformRuntimeInstanceByFilters(@RequestBody final PlatformRuntimeFilter runtimeFilter) {
        logger.debug(">> countPlatformRuntimeInstanceByFilters(runtimeFilter= {})", runtimeFilter);
        try {
            return platformRuntimeInstanceRepository.countAllPlatformRuntimeInstanceByFilter(runtimeFilter);
        } finally {
            logger.debug("<< countPlatformRuntimeInstanceByFilters()");
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/filter")
    @Consumes(MediaType.APPLICATION_JSON)
    @ResponseBody
    @Transactional
    public List<SessionExecutionResource> findPlatformRuntimeInstanceByFilters(@RequestBody final PlatformRuntimeFilter runtimeFilter) {
        logger.debug(">> findAllPlatformRuntimeInstanceByFilter(runtimeFilter= {})", runtimeFilter);
        try {
            //____ Extract data from database
            final List<SessionExecution> allSessionExecutions = platformRuntimeInstanceRepository.findAllPlatformRuntimeInstanceByFilter(runtimeFilter);
            //___ TODO pour chacun de ces enregistrements, le convertir en objet JSON
            List<SessionExecutionResource> result = Lists.transform(allSessionExecutions, new Function<SessionExecution, SessionExecutionResource>() {
                @Nullable
                @Override
                public SessionExecutionResource apply(@Nullable SessionExecution sessionExecution) {
                    // TODO
                    SessionExecutionResource output = new SessionExecutionResource();
                    output.setId(sessionExecution.getId());
                    PlatformRuntimeInstance runtimeInstance = sessionExecution.getPlatformRuntimeInstance();
                    DroolsResource guvnorResource = sessionExecution.getPlatformRuntimeInstance().getPlatformRuntimeDefinition().getDroolsRessourcesDefinition().get(0);
                    assert sessionExecution != null;
                    output.setRuleBaseID(sessionExecution.getPlatformRuntimeInstance().getRuleBaseID());
                    output.setSessionId(sessionExecution.getSessionId());

                    if (guvnorResource.getEndDate() == null) {

                        String guvnorUrl = guvnorResource.getGuvnor_url() + guvnorResource.getGuvnor_appName();
                        output.setGuvnorUrl(guvnorUrl);
                        output.setRulePackage(guvnorResource.getGuvnor_packageName());
                        output.setVersion(guvnorResource.getGuvnor_packageVersion());

                    }

                    //___ Différence entre runtimeURL et hostname par rapport aux filtres ?
                    output.setRuntimeURL(runtimeInstance.getHostname() + ":" + runtimeInstance.getPort() + runtimeInstance.getEndPoint());
                    output.setHostname(runtimeInstance.getHostname() + ":" + runtimeInstance.getPort() + runtimeInstance.getEndPoint());

                    output.setStatus(runtimeInstance.getStatus().toString());
                    //output.setStatus(sessionExecution.getSessionExecutionStatus().toString());
                    output.setStartDate(sessionExecution.getStartDate().toString());
                    //sessionExecution.getFacts();
                    if (sessionExecution.getEndDate() != null)
                        output.setEndDate(sessionExecution.getEndDate().toString());
                    return output;
                }
            });

            int page = 1;
            int pageSize = 10;

            Pageable pageable = new PageRequest(page, pageSize);

            /*model.addAttribute("users", result.get(pageable));
            return "users";*/

            return Lists.newArrayList(Iterables.filter(result, Predicates.notNull()));
        } finally {
            logger.debug("<< findAllPlatformRuntimeInstanceByFilter()");
        }
    }

    // Is it possible to simplify those requests?

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


    @RequestMapping(method = RequestMethod.GET, value = "/all/facttypes")
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    public List<String> getAllFactTypes() {
        /*FactTypeObject when = new FactTypeObject();
        FactTypeObject inserted = new FactTypeObject();
        FactTypeObject updated_oldvalue = new FactTypeObject();
        FactTypeObject updated_newvalue = new FactTypeObject();
        FactTypeObject deleted = new FactTypeObject();
        FactTypeObject inputdata = new FactTypeObject();
        FactTypeObject outputdata = new FactTypeObject();*/

        return Arrays.asList(FactType.WHEN.toString(), FactType.INSERTED.toString(), FactType.UPDATED_OLDVALUE.toString(), FactType.UPDATED_NEWVALUE.toString(), FactType.DELETED.toString(), FactType.INPUTDATA.toString(), FactType.OUTPUTDATA.toString());
    }


}