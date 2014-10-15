package org.chtijbug.drools.platform.runtime.builder;

import com.google.common.base.Throwables;
import org.apache.commons.io.IOUtils;
import org.chtijbug.drools.platform.runtime.builder.internals.GuvnorRepositoryImpl;
import org.chtijbug.drools.platform.runtime.utils.XpathQueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.apache.commons.io.FileUtils.readFileToString;
import static org.chtijbug.drools.platform.runtime.builder.BusinessPackageAuthoringManager.XPATH_TARGET_NAMESPACE;
import static org.chtijbug.drools.platform.runtime.builder.BusinessPackageAuthoringManager.XPATH_XSD_NAME;
import static org.springframework.util.ResourceUtils.getFile;

@Component
public class MavenProjectFactory {
    private static Logger logger = LoggerFactory.getLogger(MavenProjectFactory.class);

    public MavenProject createNewWarMavenProject(InputStream wsdlContent, InputStream businessModelAsXsd, GuvnorRepositoryImpl guvnorRepository, String mavenPath, String baseGeneratedPath) {
        logger.debug(">> createNewWarMavenProject(wsdlContent={}, businessModelAsXsd={})", wsdlContent, businessModelAsXsd);
        try {
            byte[] wsdlContentAsBytes = IOUtils.toByteArray(wsdlContent);
            MavenProject mavenProject = createEmptyMavenProject(guvnorRepository.getPackageName(), mavenPath, baseGeneratedPath);
            //___ Copy file into src/main/resources
            addWebServicesResources(mavenProject, new ByteArrayInputStream(wsdlContentAsBytes), businessModelAsXsd);
            //____ Copy chtijbug-spring.xml file
            XpathQueryRunner xpathQueryRunner = new XpathQueryRunner(wsdlContentAsBytes);
            String executionService;
            executionService = xpathQueryRunner.executeXpath(BusinessPackageAuthoringManager.XPATH_EXECUTION_SERVICE);
            String targetNamespace = xpathQueryRunner.executeXpath(XPATH_TARGET_NAMESPACE);
            addSpringResources(mavenProject, guvnorRepository, targetNamespace, executionService);
            // File configFile = mavenProject.createSourceFile("com.pymma.drools.config", "RuleEngineConfig.java");
            // File originalConfigFile = ResourceUtils.getFile("classpath:file-templates/RuleEngineConfig");
            // FileUtils.copyFile(originalConfigFile, configFile);
            return mavenProject;
        } catch (IOException e) {
            throw Throwables.propagate(e);
        } finally {
            logger.debug("<< createNewWarMavenProject()");
        }
    }

    protected void addSpringResources(MavenProject mavenProject, GuvnorRepositoryImpl guvnorRepository, String targetNamespace, String executionService) {
        try {
            /**
             * src/main/resources/spring
             */
            File springBeansConfFile = getFile("classpath:file-templates/application-context.xml");
            String springBeansConfContent = readFileToString(springBeansConfFile);
            // springBeansConfContent = springBeansConfContent.replaceAll("#basePackageName#", basePackageName);
            File springResourcesFolder = new File(mavenProject.resourcesFolder, "spring");
            if (!springResourcesFolder.exists() && !springResourcesFolder.mkdir())
                throw new RuntimeException("Unable to create spring resources directory. Execution will stop");
            mavenProject.addSpringConfigurationFile(springResourcesFolder, "application-context.xml", springBeansConfContent);
            /**
             * _ src/main/webapp/WEB-INF
             */
            File springWebInfFile = getFile("classpath:file-templates/soap-servlet.xml");
            String springWebInfContent = readFileToString(springWebInfFile);
            //springWebInfContent = springWebInfContent.replaceAll("#targetNamespace#", targetNamespace);
            //springWebInfContent = springWebInfContent.replaceAll("#wsdlServiceName#", executionService);
            File springWebInfFolder = new File(mavenProject.webinfFolder, "");
            if (!springWebInfFolder.exists() && !springWebInfFolder.mkdir())
                throw new RuntimeException("Unable to create spring resources directory. Execution will stop");
            mavenProject.addSpringConfigurationFile(springWebInfFolder, "soap-servlet.xml", springWebInfContent);
            /**
             *  src/main/webapp/WEB-INF
             */
            File springWebInfFile2 = getFile("classpath:file-templates/websocket-servlet.xml");
            String springWebInfContent2 = readFileToString(springWebInfFile2);
            //springWebInfContent = springWebInfContent.replaceAll("#targetNamespace#", targetNamespace);
            //springWebInfContent = springWebInfContent.replaceAll("#wsdlServiceName#", executionService);
            File springWebInfFolder2 = new File(mavenProject.webinfFolder, "");
            if (!springWebInfFolder2.exists() && !springWebInfFolder2.mkdir())
                throw new RuntimeException("Unable to create spring resources directory. Execution will stop");
            mavenProject.addSpringConfigurationFile(springWebInfFolder2, "websocket-servlet.xml", springWebInfContent2);
            /**
             * src/main/resources/spring/
             */
            File springWebInfFile3 = getFile("classpath:file-templates/platform-knowledge.properties");
            String springWebInfContent3 = readFileToString(springWebInfFile3);
            springWebInfContent3 = springWebInfContent3.replaceAll("#baseUrl#", guvnorRepository.getBaseUrl());

            springWebInfContent3 = springWebInfContent3.replaceAll("#packageName#", guvnorRepository.getPackageName());
            springWebInfContent3 = springWebInfContent3.replaceAll("#packageVersion#", guvnorRepository.getPackageVersion());
            springWebInfContent3 = springWebInfContent3.replaceAll("#packageUserName#", guvnorRepository.getPackageUsername());
            springWebInfContent3 = springWebInfContent3.replaceAll("#packagePassword#", guvnorRepository.getPackagePassword());
            springWebInfContent3 = springWebInfContent3.replaceAll("#wsport#", guvnorRepository.getWsPort().toString());
            springWebInfContent3 = springWebInfContent3.replaceAll("#wsHost#", guvnorRepository.getWsHost());
            springWebInfContent3 = springWebInfContent3.replaceAll("#jmsServer#", guvnorRepository.getJmsServer());
            springWebInfContent3 = springWebInfContent3.replaceAll("#jmsPort#", guvnorRepository.getJmsPort().toString());

            springWebInfContent3 = springWebInfContent3.replaceAll("#rulebaseid#", guvnorRepository.getRuleBaseid());
            springWebInfContent3 = springWebInfContent3.replaceAll("#platformServer#", guvnorRepository.getPlatformServer());
            File springWebInfFolder3 = new File(mavenProject.resourcesFolder, "spring");
            if (!springWebInfFolder3.exists() && !springWebInfFolder3.mkdir())
                throw new RuntimeException("Unable to create spring resources directory. Execution will stop");
            mavenProject.addSpringConfigurationFile(springWebInfFolder3, "platform-knowledge.properties", springWebInfContent3);

        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    protected void addWebServicesResources(MavenProject mavenProject, InputStream wsdlContent, InputStream businessModelAsXsd) {
        try {
            byte[] wsdlContentAsByteArray = IOUtils.toByteArray(wsdlContent);
            XpathQueryRunner xpathQueryRunner = new XpathQueryRunner(wsdlContentAsByteArray);
            mavenProject.addWSDL("executionService.wsdl", wsdlContentAsByteArray);
            String modelFilename = xpathQueryRunner.executeXpath(XPATH_XSD_NAME);
            mavenProject.addXSD(modelFilename, businessModelAsXsd);
            mavenProject.addXJCBinding(getFile("classpath:xjc/bindings.xml"));
            mavenProject.addWebXml(getFile("classpath:file-templates/web.xml"));
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    protected MavenProject createEmptyMavenProject(String basePackageName, String mavenPath, String baseGeneratedPath) {
        try {

            File projectFolder = null;
            if (baseGeneratedPath != null && baseGeneratedPath.length() > 0) {
                projectFolder = new File(baseGeneratedPath + "/" + basePackageName);
            } else {
                projectFolder = File.createTempFile("tmp-dir", "");
            }
            if (projectFolder.delete() && !projectFolder.mkdir())
                throw new RuntimeException("Unable to create temporary directory. Execution will stop");
            //_____ Create standard maven structure for web application
            File pomFile = getFile("classpath:file-templates/pom.xml");
            String pomFileContent = readFileToString(pomFile);
            pomFileContent = pomFileContent.replaceAll("#basePackageName#", basePackageName);
            return new MavenProject(projectFolder, pomFileContent, mavenPath, baseGeneratedPath);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

}
