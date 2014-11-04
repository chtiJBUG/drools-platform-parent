package org.chtijbug.drools.platform.runtime;

/**
 * Created by IntelliJ IDEA.
 * Date: 03/11/14
 * Time: 10:34
 * To change this template use File | Settings | File Templates.
 */
public class CodeGeneratorService {


    public void createGuvnorPackage() {

    }

    public void createWarRuntime() {

    }


    /**
     * public void should_get_everything_generated_on_guvnor() throws Exception {
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
     @Ignore public void should_get_webservice_project_folder_generated() throws Exception {
     GuvnorRepositoryImpl guvnorRepository = new GuvnorRepositoryImpl("com.pymma.drools", "LATEST", "admin", "admin", "598");
     Xsd2JarTransformer xsd2JarTransformer = new Xsd2JarTransformer();
     BusinessPackageAuthoringManager toTest = new BusinessPackageAuthoringManager(guvnorRepository, xsd2JarTransformer, new MavenProjectFactory(), "/home/nheron/workspace-chtiJBUG/apache-maven-3.1.1", "/home/nheron/workspace-chtiJBUG/baseTestDirectory");


     URL wsdlFile = this.getClass().getResource("/newWSDL1.wsdl");
     URL modelFile = this.getClass().getResource("/model.xsd");
     InputStream wsdlContent = FileUtils.openInputStream(FileUtils.toFile(wsdlFile));
     InputStream modelContent = FileUtils.openInputStream(FileUtils.toFile(modelFile));

     toTest.generateExecutionService(wsdlContent, modelContent);
     */
}
