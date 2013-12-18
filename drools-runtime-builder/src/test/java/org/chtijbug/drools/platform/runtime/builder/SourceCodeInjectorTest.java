package org.chtijbug.drools.platform.runtime.builder;

import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.chtijbug.drools.platform.runtime.builder.SourceCodeInjector.Keyword;
import static org.apache.commons.io.FileUtils.openInputStream;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.ResourceUtils.getFile;

public class SourceCodeInjectorTest {

    @Test
    public void should_get_6_keywords_fully_extracted() throws Exception {

        InputStream wsdlContent = openInputStream(getFile("classpath:newWSDL1.wsdl"));
        SourceCodeInjector sci = new SourceCodeInjector();
        sci.extractAllKeywords(wsdlContent, "com.pymma.drools");
        assertThat(sci.getKeywords()).hasSize(6);

        assertThat(sci.getKeywordValue(Keyword.PORT_TYPE)).isEqualTo("newWSDL1PortType");
        assertThat(sci.getKeywordValue(Keyword.SERVICE_PORT_BINDING_NAME)).isEqualTo("newWSDL1PortTypeBindingPort");
        assertThat(sci.getKeywordValue(Keyword.PACKAGE_NAME)).isEqualTo("com.pymma.drools");
        assertThat(sci.getKeywordValue(Keyword.SERVICE_NAME)).isEqualTo("newWSDL1Service");
        assertThat(sci.getKeywordValue(Keyword.ENDPOINT_INTERFACE_CLASSNAME)).isEqualTo("com.pymma.drools.NewWSDL1PortType");
        assertThat(sci.getKeywordValue(Keyword.TARGET_NAMESPACE)).isEqualTo("http://j2ee.netbeans.org/wsdl/BpelModule1/src/newWSDL1");
    }


    @Test
    public void should_get_Impl_class_customized() throws Exception {

       String expectedContent = readFileToString(getFile("classpath:expected-generated-class"));
        InputStream wsdlContent = openInputStream(getFile("classpath:newWSDL1.wsdl"));

        File tmpFile = File.createTempFile("Service.java", "");
        SourceCodeInjector sourceCodeInjector = new SourceCodeInjector(tmpFile,wsdlContent, "com.pymma.drools.runtime");

        List<ProcessStructure> processes = Arrays.asList(new ProcessStructure("mainProcess", "Input", "Output"));
        sourceCodeInjector.customize(processes);

        String toBeTest = readFileToString(tmpFile);

        assertThat(toBeTest).isEqualTo(expectedContent);

    }

}
