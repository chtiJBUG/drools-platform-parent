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


import org.chtijbug.drools.platform.persistence.pojo.FireAllRulesExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class FireAllRulesExecutionRepositoryCacheService {
    @Autowired
    FireAllRulesExecutionRepository fireAllRulesExecutionRepository;

    public List<FireAllRulesExecution> findAllStartedFireAllRulesBySessionID(Integer sessionID) {

        return fireAllRulesExecutionRepository.findAllStartedFireAllRulesBySessionID(sessionID);
    }


    public FireAllRulesExecution findStartedFireAllRulesBySessionID(Integer sessionID) {
        return fireAllRulesExecutionRepository.findStartedFireAllRulesBySessionID(sessionID);
    }


    public void save(FireAllRulesExecution fireAllRulesExecution) {
        this.fireAllRulesExecutionRepository.save(fireAllRulesExecution);
    }
}
