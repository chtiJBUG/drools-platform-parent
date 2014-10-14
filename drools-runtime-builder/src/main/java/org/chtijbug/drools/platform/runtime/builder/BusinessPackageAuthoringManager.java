package org.chtijbug.drools.platform.runtime.builder;

import com.google.common.base.Throwables;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.common.util.StringUtils;
import org.chtijbug.drools.platform.runtime.utils.XpathQueryRunner;
import org.chtijbug.drools.platform.runtime.utils.Xsd2JarTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.xpath.XPathExpressionException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;
import static org.chtijbug.drools.platform.runtime.builder.ClassSourceCodeInjector.Keyword.PORT_TYPE;

@Service
public class BusinessPackageAuthoringManager {
    private static Logger logger = LoggerFactory.getLogger(BusinessPackageAuthoringManager.class);
    protected static final String EXECUTION_SERVICE_CLASS_NAME = "ServiceCalculate";
    protected static final String EXECUTION_SERVICE_INTERFACE_NAME = "IServiceCalculate";
    protected static final String XPATH_PACKAGE_NAME = "//wsdl:definitions/@name";
    protected static final String XPATH_PROCESSES_NAMES = "//wsdl:portType/wsdl:operation/@name";
    protected static final String XPATH_XSD_NAME = "//wsdl:types/xsd:schema/xsd:import/@schemaLocation";
    protected static final String XPATH_TARGET_NAMESPACE = "//wsdl:definitions/@targetNamespace";
    protected static final String XPATH_EXECUTION_SERVICE = "//wsdl:service/@name";

    protected static final String METHOD_SIGNATURE_REGEX = "(public).* [a-zA-Z]+ [a-zA-Z, 0-9]+\\([a-zA-Z ,\\t\\n, @,(,=,',\",0-9,:,/,\\.,)]*";
    /**
     * Guvnor Repository which is responsible of the Business Assets authoring, versioning etc...
     */
    @Resource
    private GuvnorRepository guvnorRepository;
    @Resource
    private Xsd2JarTransformer modelTransformer;
    @Resource
    private MavenProjectFactory mavenProjectFactory;

    public BusinessPackageAuthoringManager(GuvnorRepository guvnorRepository) {
        this.guvnorRepository = guvnorRepository;
    }

    public BusinessPackageAuthoringManager(GuvnorRepository guvnorRepository, Xsd2JarTransformer xsd2JarTransformer, MavenProjectFactory mavenProjectFactory) {
        this(guvnorRepository);
        this.modelTransformer = xsd2JarTransformer;
        this.mavenProjectFactory = mavenProjectFactory;
    }

    /** */
    public void createBusinessPackage(InputStream wsdlContent, InputStream businessModelAsXsd, String sourcePackageName) {
        logger.debug(">> createBusinessPackage(wsdlContent={}, businessModelAsXsd)", wsdlContent, businessModelAsXsd);
        try {
            XpathQueryRunner xpathQueryRunner = new XpathQueryRunner(wsdlContent);
            String packageName = xpathQueryRunner.executeXpath(XPATH_PACKAGE_NAME);
            List<String> allProcessesName = xpathQueryRunner.executeXpathAsList(XPATH_PROCESSES_NAMES);
            this.guvnorRepository.createBusinessPackage(packageName);
            File modelFile = File.createTempFile("model", ".xsd");

            FileUtils.writeLines(modelFile, IOUtils.readLines(businessModelAsXsd));
            InputStream modelInputStream = modelTransformer.transformXsd2Jar(sourcePackageName, modelFile);
            this.guvnorRepository.createBusinessModel(packageName, modelInputStream);
            for (String processName : allProcessesName) {
                this.guvnorRepository.createBusinessProcess(packageName, processName);
            }
            // Everything should be created on guvnor
        } catch (XPathExpressionException e) {
            logger.error("An error occurred while running an XPath Expression.");
            throw Throwables.propagate(e);
        } catch (IOException e) {
            logger.error("An error occurred while reading the content of the input stream {} (businessModelAsXsd)", businessModelAsXsd);
            throw Throwables.propagate(e);
        } finally {
            logger.debug("<< createBusinessPackage()");
        }
    }

    public void generateExecutionService(InputStream wsdlContent, InputStream businessModelAsXsd, String basePackageName, String mavenPath) {
        logger.debug(">> generateExecutionService(wsdlContent={}, businessModelAsXsd={}, basePackageName={})", wsdlContent, businessModelAsXsd, basePackageName);
        try {
            //___ Create target project folder in maven style
            byte[] wsdlBytes = IOUtils.toByteArray(wsdlContent);
            MavenProject mavenProject = mavenProjectFactory.createNewWarMavenProject(new ByteArrayInputStream(wsdlBytes), businessModelAsXsd, basePackageName, mavenPath);
            //___ Generate all files based on maven-cxf-plugin
            mavenProject.generateSources();
            //___ Create the Impl file according to the Port name from the wsdl
            File executionServiceClassFile = mavenProject.createSourceFile(basePackageName, EXECUTION_SERVICE_CLASS_NAME);

            ClassSourceCodeInjector classSourceCodeInjector = new ClassSourceCodeInjector(executionServiceClassFile, new ByteArrayInputStream(wsdlBytes), basePackageName);

            File classFile = mavenProject.getSourceFile(StringUtils.capitalize(classSourceCodeInjector.getKeywordValue(PORT_TYPE)).concat(".java"));
            List<ProcessStructure> processStructures = extractMethodStructures(classFile);
            classSourceCodeInjector.customize(processStructures);


            File executionServiceInterfaceFile = mavenProject.createSourceFile(basePackageName, EXECUTION_SERVICE_INTERFACE_NAME);

            InterfaceSourceCodeInjector interfaceSourceCodeInjector = new InterfaceSourceCodeInjector(executionServiceInterfaceFile, new ByteArrayInputStream(wsdlBytes), basePackageName);
            File interfaceFile = mavenProject.getSourceFile(StringUtils.capitalize(interfaceSourceCodeInjector.getKeywordValue(InterfaceSourceCodeInjector.Keyword2.PORT_TYPE)).concat(".java"));
            List<ProcessStructure> processStructures2 = extractMethodStructures(interfaceFile);
            interfaceSourceCodeInjector.customize(processStructures2);


            //___ Package all these into a war file...
            InputStream warFileInputStream = mavenProject.buildUpPackage();

            //___ Deploy it somehow, somewhere....
            // TODO
        } catch (Exception e) {
            throw Throwables.propagate(e);
        } finally {
            logger.debug("<< generateExecutionService()");
        }
    }

    protected List<ProcessStructure> extractMethodStructures(File interfaceFile) {
        List<ProcessStructure> result = newArrayList();
        try {
            String interfaceContent = FileUtils.readFileToString(interfaceFile);
            Pattern methodPattern = Pattern.compile(METHOD_SIGNATURE_REGEX);
            Matcher matcher = methodPattern.matcher(interfaceContent);
            while (matcher.find()) {
                String method = matcher.group();
                // Remove the public field
                method = method.replaceAll("public ", "");
                method = method.replaceAll("@WebParam\\(.*\\)", "");
                method = method.replaceAll("\\(", "");
                method = method.replaceAll("\\)", "");
                method = method.replaceAll("\n", "");
                method = method.replaceAll("   ", "");
                String[] methodSignatureItems = method.split(" ");
                if (methodSignatureItems.length == 0 || methodSignatureItems.length > 4)
                    throw new RuntimeException("The current version of the Execution Service Generator only supports 1 parameter per method");
                String ouputClass = methodSignatureItems[0];
                String methodName = methodSignatureItems[1];
                String inputClass = methodSignatureItems[2];
                if (!inputClass.equals(ouputClass))
                    throw new RuntimeException("The current version only supports identical input and output");
                result.add(new ProcessStructure(methodName, inputClass, ouputClass));
            }

            return result;
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

}
