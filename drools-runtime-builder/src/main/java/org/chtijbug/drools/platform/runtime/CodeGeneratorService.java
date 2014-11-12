package org.chtijbug.drools.platform.runtime;

import org.apache.commons.io.FileUtils;
import org.chtijbug.drools.platform.runtime.builder.BusinessPackageAuthoringManager;
import org.chtijbug.drools.platform.runtime.builder.MavenProjectFactory;
import org.chtijbug.drools.platform.runtime.builder.internals.GuvnorRepositoryImpl;
import org.chtijbug.drools.platform.runtime.utils.Xsd2JarTransformer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Created by IntelliJ IDEA.
 * Date: 03/11/14
 * Time: 10:34
 * To change this template use File | Settings | File Templates.
 */
public class CodeGeneratorService {


    private BusinessPackageAuthoringManager businessPackageAuthoringManager;

    private URL wsdlFile;

    private URL modelFile;

    public static void main(String args[]) throws Exception {
        CodeGeneratorService codeGeneratorService = new CodeGeneratorService();
        codeGeneratorService.createProject();
    }


    public void createProject() throws IOException {

        String guvnorUrl = getStringFromCommandLine("Guvnor url [http://localhost:8080]");
        if (guvnorUrl == null || guvnorUrl.length() == 0) {
            guvnorUrl = "http://localhost:8080";
        }
        String packageName = getStringFromCommandLine("Enter Package name [test.test]");
        if (packageName == null || packageName.length() == 0) {
            packageName = "test.test";
        }
        String packageVersion = getStringFromCommandLine("Enter Package Version [LATEST]");
        if (packageVersion == null || packageVersion.length() == 0) {
            packageVersion = "LATEST";
        }
        String userName = getStringFromCommandLine("Enter guvnor admin username [admin]");
        if (userName == null || userName.length() == 0) {
            userName = "admin";
        }
        String password = getStringFromCommandLine("Enter guvnor admin password [admin]");
        if (password == null || password.length() == 0) {
            password = "admin";
        }
        String ruleBaseId = getStringFromCommandLine("Enter ruleBaseId [101]");
        if (ruleBaseId == null || ruleBaseId.length() == 0) {
            ruleBaseId = "101";
        }
        String wsPort = getStringFromCommandLine("Enter wsport [8080]");
        if (wsPort == null || wsPort.length() == 0) {
            wsPort = "8080";
        }
        String wsHost = getStringFromCommandLine("Enter wsHost [localhost]");
        if (wsHost == null || wsHost.length() == 0) {
            wsHost = "localhost";
        }
        String jmsHost = getStringFromCommandLine("Enter jmsHost [localhost]");
        if (jmsHost == null || jmsHost.length() == 0) {
            jmsHost = "localhost";
        }
        String mavenInstallDir = getStringFromCommandLine("Enter Maven Install Dir ");

        String generateBaseDirectory = getStringFromCommandLine("Enter base directory  [~/temp]");
        if (generateBaseDirectory == null || generateBaseDirectory.length() == 0) {
            generateBaseDirectory = "~/temp";
        }


        GuvnorRepositoryImpl guvnorRepository = new GuvnorRepositoryImpl(packageName, packageVersion, userName, password, ruleBaseId);
        guvnorRepository.setBaseUrl(guvnorUrl);
        guvnorRepository.setWsPort(Integer.valueOf(wsPort));
        guvnorRepository.setWsHost(wsHost);
        guvnorRepository.setJmsServer(jmsHost);
        Xsd2JarTransformer xsd2JarTransformer = new Xsd2JarTransformer();
        businessPackageAuthoringManager = new BusinessPackageAuthoringManager(guvnorRepository, xsd2JarTransformer, new MavenProjectFactory(), mavenInstallDir, generateBaseDirectory);
        String wsdlFileString = getStringFromCommandLine("Enter wsdl");
        String xsdFileString = getStringFromCommandLine("Enter xsd");
        wsdlFile = Paths.get(wsdlFileString).toUri().toURL();
        modelFile = Paths.get(xsdFileString).toUri().toURL();
        createWarApp();
        createGuvnorProject();
    }

    private void createWarApp() throws IOException {
        InputStream wsdlContentStep2 = FileUtils.openInputStream(FileUtils.toFile(wsdlFile));
        InputStream modelContentStep2 = FileUtils.openInputStream(FileUtils.toFile(modelFile));

        businessPackageAuthoringManager.generateExecutionService(wsdlContentStep2, modelContentStep2);

    }

    private void createGuvnorProject() throws IOException {
        InputStream wsdlContentStep1 = FileUtils.openInputStream(FileUtils.toFile(wsdlFile));
        InputStream modelContentStep1 = FileUtils.openInputStream(FileUtils.toFile(modelFile));
        businessPackageAuthoringManager.createBusinessPackage(wsdlContentStep1, modelContentStep1);

    }

    private String getStringFromCommandLine(String textToDisplay) {
        System.out.print(textToDisplay + ": ");

        //  open up standard input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String userEnteredText = null;

        //  read the username from the command-line; need to use try/catch with the
        //  readLine() method
        try {
            userEnteredText = br.readLine();
        } catch (IOException ioe) {
            System.out.println("IO error trying to read " + textToDisplay + " !");
            System.exit(1);
        }


        return userEnteredText;
    }


}
