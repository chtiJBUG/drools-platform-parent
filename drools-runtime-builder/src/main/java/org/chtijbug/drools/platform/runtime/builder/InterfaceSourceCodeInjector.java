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
import static org.chtijbug.drools.platform.runtime.builder.ClassSourceCodeInjector.Keyword.PORT_TYPE;
import static org.springframework.util.ResourceUtils.getFile;
import static org.springframework.util.StringUtils.capitalize;

public class InterfaceSourceCodeInjector {
    protected static final String EXECUTION_SERVICE_TEMPLATE_RESOURCE = "classpath:file-templates/interface-class-template";
    protected static final String METHOD_TEMPLATE_RESOURCE = "classpath:file-templates/interface-method-impl-template";


    private final File toBeCodeInjected;
    private Map<Keyword2, String> keywords2;

    protected InterfaceSourceCodeInjector() {
        // nop
        this.toBeCodeInjected = null;
    }


    public InterfaceSourceCodeInjector(File toBeCodeInjected, InputStream wsdlContent, String basePackageName) {
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

    public void customize(List<ProcessStructure> processes) {
        try {
            String fileContent = readFileToString(this.toBeCodeInjected);
            for (Entry<Keyword2, String> entry : keywords2.entrySet()) {
                fileContent = fileContent.replaceAll(entry.getKey().getToken(), entry.getValue());
            }
            //____ Add methods for each process
            StringBuilder methodBuilder = new StringBuilder();
            for (ProcessStructure processStructure : processes) {
                String methodTemplate = readFileToString(getFile(METHOD_TEMPLATE_RESOURCE));
                methodTemplate = methodTemplate.replaceAll("#methodName#", processStructure.getProcessName());
                methodTemplate = methodTemplate.replaceAll("#inputClass#", processStructure.getInputClassname());
                methodTemplate = methodTemplate.replaceAll("#outputClass#", processStructure.getOutputClassname());
                methodBuilder.append(methodTemplate).append("\n\n");
            }
            fileContent = fileContent.replaceAll("#METHODS#", methodBuilder.toString());

            writeStringToFile(this.toBeCodeInjected, fileContent, false);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public String getKeywordValue(Keyword2 keyword) {
        if (this.keywords2 == null || this.keywords2.isEmpty())
            throw new RuntimeException("Source code injector not properly created. Bad usage of the SourceCodeInjector");
        return this.keywords2.get(keyword);
    }

    protected void extractAllKeywords(InputStream wsdlContent, String basePackageName) throws IOException {
        XpathQueryRunner xpathQueryRunner = new XpathQueryRunner(wsdlContent);
        this.keywords2 = new HashMap<>();
        for (Keyword2 keyword : Keyword2.values()) {
            if (keyword.getXpathExpr() != null) {
                keywords2.put(keyword, xpathQueryRunner.executeXpath(keyword.getXpathExpr()));
            }
        }
        keywords2.put(Keyword2.PACKAGE_NAME, basePackageName);
        //___ Compute the endpointInterfaceClassname
        keywords2.put(Keyword2.ENDPOINT_INTERFACE_CLASSNAME, basePackageName + "." + capitalize(keywords2.get(PORT_TYPE)));
    }

    protected Map<Keyword2, String> getKeywords() {
        return keywords2;
    }


    protected enum Keyword2 {
        PACKAGE_NAME(null, "#packageName#"),
        TARGET_NAMESPACE("//wsdl:definitions/@targetNamespace", "#targetNamespace#"),
        SERVICE_NAME("//wsdl:service/@name", "#serviceName#"),
        SERVICE_PORT_BINDING_NAME("//wsdl:service/wsdl:port/@name", "#portName#"),
        PORT_TYPE("//wsdl:portType/@name", "#portType#"),
        ENDPOINT_INTERFACE_CLASSNAME(null, "#endpointInterface#");

        private String xpathExpr;
        private String token;

        private Keyword2(String xpathExpr, String token) {
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
