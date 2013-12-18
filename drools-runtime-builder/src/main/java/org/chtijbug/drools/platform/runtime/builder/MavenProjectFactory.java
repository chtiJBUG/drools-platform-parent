package org.chtijbug.drools.platform.runtime.builder;

import com.google.common.base.Throwables;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.chtijbug.drools.platform.runtime.utils.XpathQueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

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

    public MavenProject createNewWarMavenProject(InputStream wsdlContent, InputStream businessModelAsXsd, String basePackageName) {
        logger.debug(">> createNewWarMavenProject(wsdlContent={}, businessModelAsXsd={})", wsdlContent, businessModelAsXsd);
        try {
            byte[] wsdlContentAsBytes = IOUtils.toByteArray(wsdlContent);
            MavenProject mavenProject = createEmptyMavenProject(basePackageName);
            //___ Copy file into src/main/resources
            addWebServicesResources(mavenProject, new ByteArrayInputStream(wsdlContentAsBytes), businessModelAsXsd);
            //____ Copy chtijbug-spring.xml file
            XpathQueryRunner xpathQueryRunner = new XpathQueryRunner(wsdlContentAsBytes);
            String executionService;
            executionService = xpathQueryRunner.executeXpath(BusinessPackageAuthoringManager.XPATH_EXECUTION_SERVICE);
            String targetNamespace = xpathQueryRunner.executeXpath(XPATH_TARGET_NAMESPACE);
            addSpringResources(mavenProject, basePackageName, targetNamespace, executionService);
            File configFile = mavenProject.createSourceFile("com.pymma.drools.config", "RuleEngineConfig.java");
            File originalConfigFile = ResourceUtils.getFile("classpath:file-templates/RuleEngineConfig");
            FileUtils.copyFile(originalConfigFile, configFile);
            return mavenProject;
        } catch (IOException e) {
            throw Throwables.propagate(e);
        } finally {
            logger.debug("<< createNewWarMavenProject()");
        }
    }

    protected void addSpringResources(MavenProject mavenProject, String basePackageName, String targetNamespace, String executionService) {
        try {
            //____ src/main/resources
            File springBeansConfFile = getFile("classpath:file-templates/chtijbug-spring.xml");
            String springBeansConfContent = readFileToString(springBeansConfFile);
            springBeansConfContent = springBeansConfContent.replaceAll("#basePackageName#", basePackageName);
            File springResourcesFolder = new File(mavenProject.resourcesFolder, "spring");
            if (!springResourcesFolder.exists() && !springResourcesFolder.mkdir())
                throw new RuntimeException("Unable to create spring resources directory. Execution will stop");
            mavenProject.addSpringConfigurationFile(springResourcesFolder, "chtijbug-spring.xml", springBeansConfContent);
            //____ src/main/webapp/WEB-INF/spring
            File springWebInfFile = getFile("classpath:file-templates/rule-engine-servlet.xml");
            String springWebInfContent = readFileToString(springWebInfFile);
            springWebInfContent = springWebInfContent.replaceAll("#targetNamespace#", targetNamespace);
            springWebInfContent = springWebInfContent.replaceAll("#wsdlServiceName#", executionService);
            File springWebInfFolder = new File(mavenProject.webinfFolder, "spring");
            if (!springWebInfFolder.exists() && !springWebInfFolder.mkdir())
                throw new RuntimeException("Unable to create spring resources directory. Execution will stop");
            mavenProject.addSpringConfigurationFile(springWebInfFolder, "rule-engine-servlet.xml", springWebInfContent);
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

    protected MavenProject createEmptyMavenProject(String basePackageName) {
        try {
            File projectFolder = File.createTempFile("tmp-dir", "");
            if (projectFolder.delete() && !projectFolder.mkdir())
                throw new RuntimeException("Unable to create temporary directory. Execution will stop");
            //_____ Create standard maven structure for web application
            File pomFile = getFile("classpath:file-templates/pom.xml");
            String pomFileContent = readFileToString(pomFile);
            pomFileContent = pomFileContent.replaceAll("#basePackageName#", basePackageName);
            return new MavenProject(projectFolder, pomFileContent);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

}
