package org.chtijbug.drools.platform.runtime.builder;

import com.google.common.base.Throwables;
import org.chtijbug.drools.platform.runtime.utils.XpathQueryRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.Entry;
import static org.apache.commons.io.FileUtils.*;
import static org.chtijbug.drools.platform.runtime.builder.ClassSourceCodeInjector.Keyword.ENDPOINT_INTERFACE_CLASSNAME;
import static org.chtijbug.drools.platform.runtime.builder.ClassSourceCodeInjector.Keyword.PORT_TYPE;
import static org.springframework.util.ResourceUtils.getFile;
import static org.springframework.util.StringUtils.capitalize;

public class ClassSourceCodeInjector {
    protected static final String EXECUTION_SERVICE_TEMPLATE_RESOURCE = "classpath:file-templates/java-class-template";
    protected static final String METHOD_TEMPLATE_RESOURCE = "classpath:file-templates/method-impl-template";


    private final File toBeCodeInjected;
    private Map<Keyword, String> keywords;

    protected ClassSourceCodeInjector() {
        // nop
        this.toBeCodeInjected = null;
    }


    public ClassSourceCodeInjector(File toBeCodeInjected, InputStream wsdlContent, String basePackageName) {
        try {
            this.toBeCodeInjected = toBeCodeInjected;
            copyFile(
                    getFile(EXECUTION_SERVICE_TEMPLATE_RESOURCE),
                    toBeCodeInjected);
            extractAllKeywords(wsdlContent, basePackageName);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public void customize(List<ProcessStructure> processes, String basePackageName) {
        try {
            String fileContent = readFileToString(this.toBeCodeInjected);
            for (Entry<Keyword, String> entry : keywords.entrySet()) {
                fileContent = fileContent.replaceAll(entry.getKey().getToken(), entry.getValue());
            }
            //____ Add methods for each process
            StringBuilder methodBuilder = new StringBuilder();
            for (ProcessStructure processStructure : processes) {
                String methodTemplate = readFileToString(getFile(METHOD_TEMPLATE_RESOURCE));
                methodTemplate = methodTemplate.replaceAll("#methodName#", processStructure.getProcessName());
                methodTemplate = methodTemplate.replaceAll("#inputClass#", processStructure.getInputClassname());
                methodTemplate = methodTemplate.replaceAll("#outputClass#", processStructure.getOutputClassname());
                methodTemplate = methodTemplate.replaceAll("#packageName#", basePackageName);
                methodBuilder.append(methodTemplate).append("\n\n");
            }
            fileContent = fileContent.replaceAll("#METHODS#", methodBuilder.toString());

            writeStringToFile(this.toBeCodeInjected, fileContent, false);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public String getKeywordValue(Keyword keyword) {
        if (this.keywords == null || this.keywords.isEmpty())
            throw new RuntimeException("Source code injector not properly created. Bad usage of the SourceCodeInjector");
        return this.keywords.get(keyword);
    }

    protected void extractAllKeywords(InputStream wsdlContent, String basePackageName) throws IOException {
        XpathQueryRunner xpathQueryRunner = new XpathQueryRunner(wsdlContent);
        this.keywords = new HashMap<>();
        for (Keyword keyword : Keyword.values()) {
            if (keyword.getXpathExpr() != null) {
                keywords.put(keyword, xpathQueryRunner.executeXpath(keyword.getXpathExpr()));
            }
        }
        keywords.put(Keyword.PACKAGE_NAME, basePackageName);
        //___ Compute the endpointInterfaceClassname
        keywords.put(ENDPOINT_INTERFACE_CLASSNAME, basePackageName + "." + capitalize(keywords.get(PORT_TYPE)));
    }

    protected Map<Keyword, String> getKeywords() {
        return keywords;
    }


    protected enum Keyword {
        PACKAGE_NAME(null, "#packageName#"),
        TARGET_NAMESPACE("//wsdl:definitions/@targetNamespace", "#targetNamespace#"),
        SERVICE_NAME("//wsdl:service/@name", "#serviceName#"),
        SERVICE_PORT_BINDING_NAME("//wsdl:service/wsdl:port/@name", "#portName#"),
        PORT_TYPE("//wsdl:portType/@name", "#portType#"),
        ENDPOINT_INTERFACE_CLASSNAME(null, "#endpointInterface#");

        private String xpathExpr;
        private String token;

        private Keyword(String xpathExpr, String token) {
            this.xpathExpr = xpathExpr;
            this.token = token;
        }

        public String getXpathExpr() {
            return xpathExpr;
        }

        private String getToken() {
            return token;
        }
    }

}
