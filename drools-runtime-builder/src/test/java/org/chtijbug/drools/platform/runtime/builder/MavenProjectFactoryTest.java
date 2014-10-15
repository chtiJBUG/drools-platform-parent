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

import org.chtijbug.drools.platform.runtime.builder.internals.GuvnorRepositoryImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.apache.commons.io.FileUtils.openInputStream;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.ResourceUtils.getFile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/spring/spring-test-context.xml")
public class MavenProjectFactoryTest {
    private static final String BASE_PACKAGE_NAME = "com.pymma.drools.test";

    @Resource
    private MavenProjectFactory mavenProjectFactory;

    @Test
    public void should_get_maven_directories_properly_created() throws Exception {
        File originalPomXml = getFile("classpath:file-templates/pom.xml");
        String originalPomFileContent = readFileToString(originalPomXml);
        originalPomFileContent = originalPomFileContent.replaceAll("#basePackageName#", BASE_PACKAGE_NAME);

        //____ Test project structures and folders
        MavenProject mavenProject = mavenProjectFactory.createEmptyMavenProject(BASE_PACKAGE_NAME, "", "");
        assertThat(mavenProject).isNotNull();
        assertThat(mavenProject.srcFolder).exists();
        assertThat(mavenProject.resourcesFolder).exists();
        assertThat(mavenProject.webappFolder).exists();
        assertThat(mavenProject.webinfFolder).exists();
        assertThat(mavenProject.wsdlFolder).exists();
        assertThat(mavenProject.pomFile).exists();

        //_____ Check that pom file is properly generated
        String generatedPomFileContent = readFileToString(mavenProject.pomFile);
        assertThat(generatedPomFileContent).isEqualTo(originalPomFileContent);
    }

    @Test
    public void should_get_all_webservice_artifacts_copied() throws Exception {
        MavenProject mavenProject = mavenProjectFactory.createEmptyMavenProject(BASE_PACKAGE_NAME, "", "");
        assertThat(mavenProject).isNotNull();

        File originalWsdl = getFile("classpath:newWSDL1.wsdl");
        File originalXsd = getFile("classpath:model.xsd");
        File originalBinding = getFile("classpath:xjc/bindings.xml");
        File originalWebXml = getFile("classpath:file-templates/web.xml");

        InputStream wsdlContent = openInputStream(originalWsdl);
        InputStream xsdContent = openInputStream(originalXsd);
        mavenProjectFactory.addWebServicesResources(mavenProject, wsdlContent, xsdContent);

        assertThat(mavenProject.resourcesFolder).isDirectory();
        File wsdlFile = new File(mavenProject.resourcesFolder, "wsdl/executionService.wsdl");
        assertThat(wsdlFile).exists();
        assertThat(wsdlFile).hasContentEqualTo(originalWsdl);

        File xsdFile = new File(mavenProject.resourcesFolder, "wsdl/model.xsd");
        assertThat(xsdFile).exists();
        assertThat(xsdFile).hasContentEqualTo(originalXsd);

        File bindingFile = new File(mavenProject.resourcesFolder, "xjc/bindings.xml");
        assertThat(bindingFile).exists();
        assertThat(bindingFile).hasContentEqualTo(originalBinding);

        File webXmlFile = new File(mavenProject.webinfFolder, "web.xml");
        assertThat(webXmlFile).exists();
        assertThat(webXmlFile).hasContentEqualTo(originalWebXml);

    }

    @Test
    public void should_get_all_spring_resources_copied() throws Exception {
        MavenProject mavenProject = mavenProjectFactory.createEmptyMavenProject(BASE_PACKAGE_NAME, "", "");
        assertThat(mavenProject).isNotNull();

        File originalBeansFile = getFile("classpath:file-templates/chtijbug-spring.xml");
        File originalServletFile = getFile("classpath:file-templates/rule-engine-servlet.xml");

        String originalBeansContent = readFileToString(originalBeansFile);
        originalBeansContent = originalBeansContent.replaceAll("#basePackageName#", "com.pymma.drools");
        String originalServletContent = readFileToString(originalServletFile);
        originalServletContent = originalServletContent.replaceAll("#targetNamespace#", "http://pymma.com/drools");
        originalServletContent = originalServletContent.replaceAll("#wsdlServiceName#", "testExecutionService");

        GuvnorRepositoryImpl guvnorRepository = new GuvnorRepositoryImpl("loyalty", "latest", "admin", "admin", "500");

        mavenProjectFactory.addSpringResources(mavenProject, guvnorRepository, "http://pymma.com/drools", "testExecutionService");

        assertThat(mavenProject.resourcesFolder).isDirectory();
        File beansFile = new File(mavenProject.resourcesFolder, "spring/chtijbug-spring.xml");
        assertThat(beansFile).exists();
        assertThat(readFileToString(beansFile)).isEqualTo(originalBeansContent);

        File servletFile = new File(mavenProject.webinfFolder, "spring/rule-engine-servlet.xml");
        assertThat(servletFile).exists();
        assertThat(readFileToString(servletFile)).isEqualTo(originalServletContent);
    }


    @Test
    @Ignore
    public void should_get_full_maven_project_generated() throws IOException {
        File originalWsdl = getFile("classpath:newWSDL1.wsdl");
        File originalXsd = getFile("classpath:model.xsd");
        GuvnorRepositoryImpl guvnorRepository = new GuvnorRepositoryImpl("loyalty", "latest", "admin", "admin", "500");
        MavenProject mavenProject = mavenProjectFactory.createNewWarMavenProject(openInputStream(originalWsdl), openInputStream(originalXsd), guvnorRepository, "", "");
        assertThat(mavenProject).isNotNull();

        mavenProject.generateSources();

        File interfaceClass = mavenProject.getSourceFile("NewWSDL1PortType.java");
        assertThat(interfaceClass).isNotNull();
        assertThat(interfaceClass).exists();

    }
}
