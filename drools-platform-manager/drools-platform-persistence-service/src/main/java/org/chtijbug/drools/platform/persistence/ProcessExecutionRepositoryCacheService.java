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
package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.ProcessExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ProcessExecutionRepositoryCacheService {
    @Autowired
    ProcessExecutionRepository processExecutionRepository;

    public List<ProcessExecution> findAllStartedProcessByRuleBaseIDAndSessionIDAndProcessInstanceId(Long ruleBaseID, Long sessionID, String processInstanceId) {
        return this.processExecutionRepository.findAllStartedProcessByRuleBaseIDAndSessionIDAndProcessInstanceId(ruleBaseID, sessionID, processInstanceId);
    }


    public ProcessExecution findStartedProcessByRuleBaseIDBySessionIDAndProcessInstanceId(Long ruleBaseID, Long sessionID, String processInstanceId) {

        return this.processExecutionRepository.findStartedProcessByRuleBaseIDBySessionIDAndProcessInstanceId(ruleBaseID, sessionID, processInstanceId);

    }

    public void save(ProcessExecution processExecution) {
        this.processExecutionRepository.save(processExecution);
    }
}
