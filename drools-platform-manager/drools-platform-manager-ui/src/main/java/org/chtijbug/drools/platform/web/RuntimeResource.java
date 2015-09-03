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

import com.carrotsearch.sizeof.RamUsageEstimator;
import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.chtijbug.drools.platform.backend.wsclient.WebSocketSessionManager;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeDefinitionRepository;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepository;
import org.chtijbug.drools.platform.persistence.SessionExecutionRecordRepository;
import org.chtijbug.drools.platform.persistence.pojo.*;
import org.chtijbug.drools.platform.persistence.utility.JSONHelper;
import org.chtijbug.drools.platform.web.model.*;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * Created by IntelliJ IDEA.
 * Date: 02/06/14
 * Time: 13:53
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping(value = "/runtime")
public class RuntimeResource extends CacheLoader<Long, SessionExecutionDetailsResource> {
    private static Logger logger = LoggerFactory.getLogger(RuntimeResource.class);

    @Autowired
    PlatformRuntimeInstanceRepository platformRuntimeInstanceRepository;
    @Autowired
    SessionExecutionRecordRepository sessionExecutionRepository;
    @Autowired
    PlatformRuntimeDefinitionRepository platformRuntimeDefinitionRepositoryCacheService;

    @Autowired
    WebSocketSessionManager webSocketSessionManager;

    @Autowired
    RuntimeDisplayCache runtimeDisplayCache;

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Transactional
    public List<RuntimeInstance> findAllPlatformRuntimeInstance() {
        return Lists.transform(platformRuntimeDefinitionRepositoryCacheService.findAll(),
                new Function<PlatformRuntimeDefinition, RuntimeInstance>() {
                    @Nullable
                    @Override
                    public RuntimeInstance apply(@Nullable PlatformRuntimeDefinition platformRuntimeDefinition) {
                        String hostname = platformRuntimeDefinition.getDeploymentHost().getHostname();
                        Integer portNumber = platformRuntimeDefinition.getWebsocketPort();
                        String endPointName = platformRuntimeDefinition.getWebsocketEndpoint();
                        String url = "http://" + hostname + ":" + portNumber + endPointName;
                        String rulePackage = null;
                        String version = null;
                        String environment = null;
                        String mode = null;
                        String status = null;

                        if (!platformRuntimeDefinition.getDroolsRessourcesDefinition().isEmpty()) {
                            DroolsResource guvnorResource = platformRuntimeDefinition.getDroolsRessourcesDefinition().get(0);
                            rulePackage = guvnorResource.getGuvnor_packageName();
                            version = guvnorResource.getGuvnor_packageVersion();
                        }
                        if (platformRuntimeDefinition.getPlatformRuntimeEnvironment() != null) {
                            environment = platformRuntimeDefinition.getPlatformRuntimeEnvironment().name();
                        } else {
                            environment = PlatformRuntimeEnvironment.PROD.name();
                        }
                        if (platformRuntimeDefinition.getPlatformRuntimeMode() != null) {
                            mode = platformRuntimeDefinition.getPlatformRuntimeMode().name();
                        } else {
                            mode = PlatformRuntimeMode.Debug.name();
                        }
                        Boolean webSocketSession = webSocketSessionManager.exists(platformRuntimeDefinition.getRuleBaseID());
                        if (webSocketSession) {
                            status = "Alive";
                        } else {
                            status = "Not Running/Not Reachable";
                        }
                        return new RuntimeInstance(platformRuntimeDefinition.getId(), platformRuntimeDefinition.getRuleBaseID(), url, rulePackage, version, environment, mode, status);
                    }
                }
        );
    }

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
                        String hostname = platformRuntimeInstance.getPlatformRuntimeDefinition().getDeploymentHost().getHostname();
                        Integer portNumber = platformRuntimeInstance.getPlatformRuntimeDefinition().getWebsocketPort();
                        String endPointName = platformRuntimeInstance.getPlatformRuntimeDefinition().getWebsocketEndpoint();
                        String url = "http://" + hostname + ":" + portNumber + endPointName;
                        String rulePackage = null;
                        String version = null;
                        String environment = null;
                        String mode = null;
                        String status = null;
                        PlatformRuntimeDefinition platformRuntimeDefinition = platformRuntimeInstance.getPlatformRuntimeDefinition();
                        if (!platformRuntimeDefinition.getDroolsRessourcesDefinition().isEmpty()) {
                            DroolsResource guvnorResource = platformRuntimeDefinition.getDroolsRessourcesDefinition().get(0);
                            rulePackage = guvnorResource.getGuvnor_packageName();
                            version = guvnorResource.getGuvnor_packageVersion();
                        }
                        if (platformRuntimeDefinition.getPlatformRuntimeEnvironment() != null) {
                            environment = platformRuntimeDefinition.getPlatformRuntimeEnvironment().name();
                        } else {
                            environment = PlatformRuntimeEnvironment.PROD.name();
                        }
                        if (platformRuntimeDefinition.getPlatformRuntimeMode() != null) {
                            mode = platformRuntimeDefinition.getPlatformRuntimeMode().name();
                        } else {
                            mode = PlatformRuntimeMode.Debug.name();
                        }
                        Boolean webSocketSessionExist = webSocketSessionManager.exists(platformRuntimeInstance.getRuleBaseID());
                        if (webSocketSessionExist) {
                            status = "Alive";
                        } else {
                            status = "Not Running/Not Reachable";
                        }
                        return new RuntimeInstance(platformRuntimeInstance.getId(), platformRuntimeInstance.getRuleBaseID(), url, rulePackage, version, environment, mode, status);
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

    @RequestMapping(method = RequestMethod.GET, value = "/sessionbrowser/{Id}/{ruleFlowName}/{direction}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Transactional
    public SessionExecutionDetailsResource findSessionExecutionDetails(@PathVariable Long Id, @PathVariable String ruleFlowName, @PathVariable Long direction) {

        SessionExecutionDetailsResource result = runtimeDisplayCache.updateView(Id, ruleFlowName, direction);


        return result;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/session/{Id}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Transactional
    public SessionExecutionDetailsResource findSessionExecutionDetails(@PathVariable Long Id) {
        logger.debug(">> findSessionExecutionDetails(sessionId= {})", Id);
        try {
            SessionExecutionDetailsResource executionDetailsResource = runtimeDisplayCache.getView(Id);

            return executionDetailsResource;
        } catch (Exception e) {
            DroolsChtijbugException ee = new DroolsChtijbugException("", "", e);
            logger.error("   public SessionExecutionDetailsResource findSessionExecutionDetails(@PathVariable Long Id) {", ee);
        } finally {
            logger.debug("<< findSessionExecutionDetails()");
        }
        return null;
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
        logger.debug(">> findAllSessionExecutionByFilter(runtimeFilter= {})", runtimeFilter);
        try {
            //____ Extract data from database
            final List<SessionExecutionRecord> allSessionExecutionsrecord = platformRuntimeInstanceRepository.findAllSessionExecutionByFilter(runtimeFilter);
            List<SessionExecution> lists = new ArrayList<>();
            for (SessionExecutionRecord record : allSessionExecutionsrecord) {
                //SessionExecution sessionExecution = (SessionExecution) JSONHelper.getObjectFromJSONString(record.getJsonSessionExecution(), SessionExecution.class);
                SessionExecution sessionExecution = new SessionExecution();
                sessionExecution.setPlatformRuntimeInstance(record.getPlatformRuntimeInstance());
                sessionExecution.setId(record.getId());
                sessionExecution.setStartDate(record.getStartDate());
                sessionExecution.setEndDate(record.getEndDate());
                lists.add(sessionExecution);
            }
            final List<SessionExecution> allSessionExecutions = lists;
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
                    String hostname = runtimeInstance.getPlatformRuntimeDefinition().getDeploymentHost().getHostname();
                    Integer portNumber = runtimeInstance.getPlatformRuntimeDefinition().getWebsocketPort();
                    String endPointName = runtimeInstance.getPlatformRuntimeDefinition().getWebsocketEndpoint();

                    //___ Différence entre runtimeURL et hostname par rapport aux filtres ?
                    output.setRuntimeURL(hostname + ":" + portNumber + endPointName);
                    output.setHostname(hostname + ":" + portNumber + endPointName);

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
            logger.debug("<< findAllSessionExecutionByFilter()");
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

    @RequestMapping(method = RequestMethod.POST, value = "/mode/{ruleBaseId}/{newMode}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    public void changeRuntimeMode(@PathVariable Integer ruleBaseId, @PathVariable String newMode) {
        logger.debug(">> changeRuntimeMode(ruleBaseId={}, newMode={})", ruleBaseId, newMode);
        try {
            PlatformRuntimeDefinition targetInstance = platformRuntimeDefinitionRepositoryCacheService.findByRuleBaseID(ruleBaseId);
            if (targetInstance != null) {
                PlatformRuntimeMode realNewMode = PlatformRuntimeMode.valueOf(newMode);
                if (realNewMode != null) {
                    targetInstance.setPlatformRuntimeMode(realNewMode);
                    platformRuntimeDefinitionRepositoryCacheService.save(targetInstance);
                }

            }
        } catch (Exception e) {
            logger.error("<< changeRuntimeMode()", e);
        } finally {
            logger.debug("<< changeRuntimeMode()");
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/env/{ruleBaseId}/{newEnv}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @ResponseBody
    public void changeRuntimeEnv(@PathVariable Integer ruleBaseId, @PathVariable String newEnv) {
        logger.debug(">> changeRuntimeEnv(ruleBaseId={}, newMode={})", ruleBaseId, newEnv);
        try {
            PlatformRuntimeDefinition targetInstance = platformRuntimeDefinitionRepositoryCacheService.findByRuleBaseID(ruleBaseId);
            if (targetInstance != null) {
                PlatformRuntimeEnvironment realNewEnv = PlatformRuntimeEnvironment.valueOf(newEnv);
                if (realNewEnv != null) {
                    targetInstance.setPlatformRuntimeEnvironment(realNewEnv);
                    platformRuntimeDefinitionRepositoryCacheService.save(targetInstance);
                }

            }
        } catch (Exception e) {
            logger.error("<< changeRuntimeEnv()", e);
        } finally {
            logger.debug("<< changeRuntimeEnv()");
        }
    }

    @Override
    public SessionExecutionDetailsResource load(Long Id) {

        logger.debug(">> SessionExecutionDetailsResource(sessionId= {})", Id);
        try {
            SessionExecutionDetailsResource executionDetailsResource;
            //____ Data from Database
            SessionExecutionRecord sessionExecutionRecord = sessionExecutionRepository.findOne(Id);
            SessionExecution sessionExecution = null;
            List<Long> listRuleFlows = platformRuntimeInstanceRepository.findAllRuleFlowGroupRecordId(Id);

            if (sessionExecutionRecord.getJsonSessionExecution() != null) {
                sessionExecution = (SessionExecution) JSONHelper.getObjectFromJSONString(sessionExecutionRecord.getJsonSessionExecution(), SessionExecution.class);
                long sizeOf = RamUsageEstimator.sizeOf(sessionExecution);
                logger.info("sizeof sessionExecution:{}", sizeOf);
            }
            sessionExecutionRecord = null;
            // final SessionExecution sessionExecution = sessionExecutionRepository.findDetailsBySessionId(sessionID);
            executionDetailsResource = new SessionExecutionDetailsResource();
            ProcessExecution processExecution = null;
            ProcessDetails processDetails = new ProcessDetails();
            if (sessionExecution != null) {
                List<Fact> inputFactList = Lists.newArrayList(sessionExecution.getFactsByType(FactType.INPUTDATA));
                List<Fact> outputFactList = Lists.newArrayList(sessionExecution.getFactsByType(FactType.OUTPUTDATA));
                for (Fact inputFact : inputFactList) {
                    executionDetailsResource.setInputObject(inputFact.getJsonFact());
                }
                for (Fact outputFact : outputFactList) {
                    executionDetailsResource.setOutputObject(outputFact.getJsonFact());
                }
            }

            if (sessionExecution != null && sessionExecution.getProcessExecutions().size() != 0) {
                processExecution = sessionExecution.getProcessExecutions().get(0);
                processDetails.setProcessName(processExecution.getProcessName());

                if (processExecution.getProcessVersion() != null) {
                    processDetails.setProcessVersion(processExecution.getProcessVersion());
                } else {
                    processDetails.setProcessVersion("");
                }

                processDetails.setProcessExecutionStatus(processExecution.getProcessExecutionStatus().toString());
                processDetails.setProcessType(processExecution.getProcessType());

                executionDetailsResource.setProcessDetails(processDetails);
                for (Long idrfg : listRuleFlows) {
                    RuleFlowExecutionRecord ruleFlowExecutionRecord = platformRuntimeInstanceRepository.findRuleFlowGroupRecordById(idrfg);
                    logger.info("lenghth jsonruleFlowExecutionRecord:{}", ruleFlowExecutionRecord.getJsonRuleFlowExecution().length());
                    long sizeOf = RamUsageEstimator.sizeOf(ruleFlowExecutionRecord.getJsonRuleFlowExecution());
                    logger.info("sizeof jsonruleFlowExecutionRecord:{}", sizeOf);
                    RuleflowGroup ruleFlowGroup = (RuleflowGroup) JSONHelper.getObjectFromJSONString(ruleFlowExecutionRecord.getJsonRuleFlowExecution(), RuleflowGroup.class);
                    ruleFlowExecutionRecord = null;
                    sizeOf = RamUsageEstimator.sizeOf(ruleFlowGroup);
                    logger.info("sizeof ruleFlowGroup:{}", sizeOf);
                    RuleFlowGroupDetails ruleFlowGroupDetails = new RuleFlowGroupDetails();
                    ruleFlowGroupDetails.setRuleflowGroup(ruleFlowGroup.getRuleflowGroup());
                    //___ Add rule execution details list
                    Queue<RuleExecution> executionQueue = ruleFlowGroup.getRuleExecutionList();
                    while (!executionQueue.isEmpty()) {
                        RuleExecution ruleExecution = executionQueue.poll();
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
                        ruleExecution = null;
                    }
                    sizeOf = RamUsageEstimator.sizeOf(ruleFlowGroupDetails);
                    logger.info("sizeof ruleFlowGroupDetails:{}", sizeOf);
                    executionDetailsResource.addRuleFlowGroup(ruleFlowGroupDetails);
                }


            }
            long sizeOf = RamUsageEstimator.sizeOf(executionDetailsResource);
            logger.info("sizeof executionDetailsResource:{}", sizeOf);


            return executionDetailsResource;
        } catch (Exception e) {
            DroolsChtijbugException ee = new DroolsChtijbugException("", "", e);
            logger.error("   public SessionExecutionDetailsResource findSessionExecutionDetails(@PathVariable Long Id) {", ee);
        } finally {
            logger.debug("<< SessionExecutionDetailsResource()");
        }
        return null;
    }
}