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

import org.apache.commons.io.FileUtils;
import org.chtijbug.drools.platform.runtime.builder.internals.GuvnorRepositoryImpl;
import org.chtijbug.drools.platform.runtime.utils.Xsd2JarTransformer;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;
import java.net.URL;

public class BusinessPackageAuthoringManagerTest {

    @Test
    @Ignore
    public void should_get_everything_generated_on_guvnor() throws Exception {
        GuvnorRepositoryImpl guvnorRepository = new GuvnorRepositoryImpl("com.pymma.drools", "LATEST", "admin", "admin", "598");
        Xsd2JarTransformer xsd2JarTransformer = new Xsd2JarTransformer();
        BusinessPackageAuthoringManager toTest = new BusinessPackageAuthoringManager(guvnorRepository, xsd2JarTransformer, new MavenProjectFactory(), "/home/nheron/workspace-chtiJBUG/apache-maven-3.1.1", "/home/nheron/workspace-chtiJBUG/baseTestDirectory");

        URL wsdlFile = this.getClass().getResource("/newWSDL1.wsdl");
        URL modelFile = this.getClass().getResource("/model.xsd");
        InputStream wsdlContent = FileUtils.openInputStream(FileUtils.toFile(wsdlFile));
        InputStream modelContent = FileUtils.openInputStream(FileUtils.toFile(modelFile));

        toTest.createBusinessPackage(wsdlContent, modelContent);
    }

    @Test
    @Ignore
    public void should_get_webservice_project_folder_generated() throws Exception {
        GuvnorRepositoryImpl guvnorRepository = new GuvnorRepositoryImpl("com.pymma.drools", "LATEST", "admin", "admin", "598");
        guvnorRepository.setBaseUrl("http://localhost:10080");
        Xsd2JarTransformer xsd2JarTransformer = new Xsd2JarTransformer();
        BusinessPackageAuthoringManager toTest = new BusinessPackageAuthoringManager(guvnorRepository, xsd2JarTransformer, new MavenProjectFactory(), "/home/nheron/workspace-chtiJBUG/apache-maven-3.1.1", "/home/nheron/workspace-chtiJBUG/baseTestDirectory");


        URL wsdlFile = this.getClass().getResource("/newWSDL1.wsdl");
        URL modelFile = this.getClass().getResource("/model.xsd");
        InputStream wsdlContent = FileUtils.openInputStream(FileUtils.toFile(wsdlFile));
        InputStream modelContent = FileUtils.openInputStream(FileUtils.toFile(modelFile));

        toTest.generateExecutionService(wsdlContent, modelContent);
    }


}
