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

package org.chtijbug.drools.platform.runtime.utils;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class XpathQueryRunnerTest {
    protected static final String XPATH_PACKAGE_NAME = "//wsdl:definitions/@name";
    protected static final String XPATH_PROCESSES_NAMES = "//wsdl:portType/wsdl:operation/@name";
    protected static final String XPATH_XSD_NAME = "//wsdl:types/xsd:schema/xsd:import/@schemaLocation";
    protected static final String XPATH_TARGET_NAMESPACE = "//wsdl:definitions/@targetNamespace";
    protected static final String XPATH_EXECUTION_SERVICE = "//wsdl:service/@name";

    @Test
    public void should_get_package_name_equals_to_name1() throws Exception {
        URL wsdlFile = this.getClass().getResource("/newWSDL1.wsdl");
        FileInputStream wsdlContent = FileUtils.openInputStream(FileUtils.toFile(wsdlFile));
        XpathQueryRunner queryRunner = new XpathQueryRunner(wsdlContent);
        String packageName = queryRunner.executeXpath(XPATH_PACKAGE_NAME);
        assertThat(packageName).isEqualTo("newWSDL1");
    }

    @Test
    public void should_get_all_processes_from_wsdl() throws Exception {
        URL wsdlFile = this.getClass().getResource("/newWSDL1.wsdl");
        FileInputStream wsdlContent = FileUtils.openInputStream(FileUtils.toFile(wsdlFile));
        XpathQueryRunner queryRunner = new XpathQueryRunner(wsdlContent);
        List<String> allProcessesName = queryRunner.executeXpathAsList(XPATH_PROCESSES_NAMES);
        assertThat(allProcessesName).hasSize(1);
        assertThat(allProcessesName.get(0)).isEqualTo("newWSDL1Operation");

    }

    @Test
    public void should_get_xsd_filename_equals_to_model_xsd() throws Exception {
        URL wsdlFile = this.getClass().getResource("/newWSDL1.wsdl");
        FileInputStream wsdlContent = FileUtils.openInputStream(FileUtils.toFile(wsdlFile));
        XpathQueryRunner queryRunner = new XpathQueryRunner(wsdlContent);
        String xsdFilename = queryRunner.executeXpath(XPATH_XSD_NAME);
        assertThat(xsdFilename).isEqualTo("model.xsd");
    }

    @Test
    public void should_get_target_namespace() throws Exception {
        URL wsdlFile = this.getClass().getResource("/newWSDL1.wsdl");
        FileInputStream wsdlContent = FileUtils.openInputStream(FileUtils.toFile(wsdlFile));
        XpathQueryRunner queryRunner = new XpathQueryRunner(wsdlContent);
        String xsdFilename = queryRunner.executeXpath(XPATH_TARGET_NAMESPACE);
        assertThat(xsdFilename).isEqualTo("http://j2ee.netbeans.org/wsdl/BpelModule1/src/newWSDL1");
    }

    @Test
    public void should_get_service_name() throws Exception {
        URL wsdlFile = this.getClass().getResource("/newWSDL1.wsdl");
        FileInputStream wsdlContent = FileUtils.openInputStream(FileUtils.toFile(wsdlFile));
        XpathQueryRunner queryRunner = new XpathQueryRunner(wsdlContent);
        String xsdFilename = queryRunner.executeXpath(XPATH_EXECUTION_SERVICE);
        assertThat(xsdFilename).isEqualTo("newWSDL1Service");
    }
}
