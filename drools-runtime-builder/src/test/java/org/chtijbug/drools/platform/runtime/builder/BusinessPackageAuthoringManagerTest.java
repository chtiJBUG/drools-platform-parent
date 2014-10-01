package org.chtijbug.drools.platform.runtime.builder;

import org.apache.commons.io.FileUtils;
import org.chtijbug.drools.platform.runtime.builder.internals.GuvnorRepositoryImpl;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;
import java.net.URL;

public class BusinessPackageAuthoringManagerTest {

    @Test
    @Ignore
    public void should_get_everything_generated_on_guvnor() throws Exception {
        GuvnorRepositoryImpl guvnorRepository = new GuvnorRepositoryImpl("http://localhost:8080", "/drools-guvnor", "admin", "admin");
        BusinessPackageAuthoringManager toTest = new BusinessPackageAuthoringManager(guvnorRepository);


        URL wsdlFile = this.getClass().getResource("/newWSDL1.wsdl");
        URL modelFile = this.getClass().getResource("/model.xsd");
        InputStream wsdlContent = FileUtils.openInputStream(FileUtils.toFile(wsdlFile));
        InputStream modelContent = FileUtils.openInputStream(FileUtils.toFile(modelFile));

        toTest.createBusinessPackage(wsdlContent, modelContent, "com.pymma.drools.runtime");
    }

    @Test
    @Ignore
    public void should_get_webservice_project_folder_generated() throws Exception {
        GuvnorRepositoryImpl guvnorRepository = new GuvnorRepositoryImpl("http://localhost:8080", "/drools-guvnor", "admin", "admin");
        BusinessPackageAuthoringManager toTest = new BusinessPackageAuthoringManager(guvnorRepository, null, new MavenProjectFactory());


        URL wsdlFile = this.getClass().getResource("/newWSDL1.wsdl");
        URL modelFile = this.getClass().getResource("/model.xsd");
        InputStream wsdlContent = FileUtils.openInputStream(FileUtils.toFile(wsdlFile));
        InputStream modelContent = FileUtils.openInputStream(FileUtils.toFile(modelFile));

        toTest.generateExecutionService(wsdlContent, modelContent, "com.pymma.drools");
    }


}
