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
package org.chtijbug.drools.platform.rules.management;

import org.chtijbug.drools.platform.rules.config.RuntimeSiteTopology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;

@Component
public class RuntimeManager {
    private static final String REST_SERVICE_PATH = "rest/rule-engine/version";
    private static Logger logger = LoggerFactory.getLogger(RuntimeManager.class);

    @Resource
    RuntimeSiteTopology runtimeSiteTopology;

    public void updateSettings(String envName, String version) {
        try {
            logger.debug("calling server to update package version");
            Response response = runtimeSiteTopology.webClient(envName, REST_SERVICE_PATH)
                    .accept(MediaType.TEXT_PLAIN_TYPE)
                    .post(version);
            if (OK.getStatusCode() != response.getStatus()) {
                throw new AdministrationBusinessProcessException(BusinessProcessError.ENVIRONMENT_UPDATE_ERROR, "Unable to update Environment status : " + response.getStatus());
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating setting on {} with version {}.", envName, version);
            throw new AdministrationBusinessProcessException(BusinessProcessError.ENVIRONMENT_UPDATE_ERROR, "Unable to update Environment " + envName);
        }
    }

    public String getDeployedPackageVersion(String envName) {
        try {
            //___ Get the deployed rulebase version on the target environment
            return runtimeSiteTopology.webClient(envName, REST_SERVICE_PATH)
                    .accept(MediaType.TEXT_PLAIN_TYPE)
                    .get(String.class);
        } catch (Exception e) {
            logger.error("Error occurred while looking up the deployed rulePackage version on {} .\nCause : {}", envName, e);
            throw new AdministrationBusinessProcessException(BusinessProcessError.DEPLOYED_VERSION_FETCH_ERROR, "Unable to resolve deployed Rule Package on Environment " + envName);
        }
    }

}
