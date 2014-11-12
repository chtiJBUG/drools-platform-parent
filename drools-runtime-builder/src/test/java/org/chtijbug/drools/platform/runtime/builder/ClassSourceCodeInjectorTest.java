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
package org.chtijbug.drools.platform.runtime.builder;

import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.io.FileUtils.openInputStream;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.chtijbug.drools.platform.runtime.builder.ClassSourceCodeInjector.Keyword;
import static org.springframework.util.ResourceUtils.getFile;

public class ClassSourceCodeInjectorTest {

    @Test
    public void should_get_6_keywords_fully_extracted() throws Exception {

        InputStream wsdlContent = openInputStream(getFile("classpath:newWSDL1.wsdl"));
        ClassSourceCodeInjector sci = new ClassSourceCodeInjector();
        sci.extractAllKeywords(wsdlContent, "com.pymma.drools");
        assertThat(sci.getKeywords()).hasSize(6);

        assertThat(sci.getKeywordValue(Keyword.PORT_TYPE)).isEqualTo("newWSDL1PortType");
        assertThat(sci.getKeywordValue(Keyword.SERVICE_PORT_BINDING_NAME)).isEqualTo("newWSDL1PortTypeBindingPort");
        assertThat(sci.getKeywordValue(Keyword.PACKAGE_NAME)).isEqualTo("com.pymma.drools");
        assertThat(sci.getKeywordValue(Keyword.SERVICE_NAME)).isEqualTo("newWSDL1Service");
        assertThat(sci.getKeywordValue(Keyword.ENDPOINT_INTERFACE_CLASSNAME)).isEqualTo("com.pymma.drools.NewWSDL1PortType");
        assertThat(sci.getKeywordValue(Keyword.TARGET_NAMESPACE)).isEqualTo("http://j2ee.netbeans.org/wsdl/BpelModule1/src/newWSDL1");
    }


    @Test
    public void should_get_Impl_class_customized() throws Exception {

        String expectedContent = readFileToString(getFile("classpath:expected-generated-class"));
        InputStream wsdlContent = openInputStream(getFile("classpath:newWSDL1.wsdl"));

        File tmpFile = File.createTempFile("Service.java", "");
        ClassSourceCodeInjector classSourceCodeInjector = new ClassSourceCodeInjector(tmpFile, wsdlContent, "com.pymma.drools.runtime");

        List<ProcessStructure> processes = Arrays.asList(new ProcessStructure("mainProcess", "Input", "Output"));
        classSourceCodeInjector.customize(processes, "com.pymma.drools.runtime");

        String toBeTest = readFileToString(tmpFile);

        assertThat(toBeTest).isEqualTo(expectedContent);

    }

}
