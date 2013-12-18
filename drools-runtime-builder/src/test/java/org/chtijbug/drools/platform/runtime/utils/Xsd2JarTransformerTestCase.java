package org.chtijbug.drools.platform.runtime.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import static org.assertj.core.api.Assertions.assertThat;

public class Xsd2JarTransformerTestCase {

    @Test
    public void should_get_5_java_classes_generated() throws IOException {
        Xsd2JarTransformer toTest = new Xsd2JarTransformer();

        //____ Create Temp directory wher classes will be generated
        File outputDir = File.createTempFile("test-dir", "");
        outputDir.delete();
        outputDir.mkdir();
        //____ Load the testing XSD
        URL xsdFile = this.getClass().getResource("/model.xsd");
        //____ Generate Java classes
        toTest.createJavaClassesFromXsd(new File(xsdFile.getFile()), outputDir, "org.pymma.drools");
        //____ Check that the counting of generated classes is the one expected.
        Collection javaFiles = FileUtils.listFiles(outputDir, FileFilterUtils.suffixFileFilter(".java"), DirectoryFileFilter.DIRECTORY);
        assertThat(javaFiles).hasSize(5);
    }

    @Test
    public void should_get_5_java_classes_compiled() throws IOException {
        Xsd2JarTransformer toTest = new Xsd2JarTransformer();

        //____ Create Temp directory wher classes will be generated
        File outputDir = File.createTempFile("test-dir", "");
        outputDir.delete();
        outputDir.mkdir();
        //____ Load the testing XSD
        URL xsdFile = this.getClass().getResource("/model.xsd");
        //____ Generate Java classes
        toTest.createJavaClassesFromXsd(new File(xsdFile.getFile()), outputDir, "org.pymma.drools");
        toTest.compileTarget(outputDir);
        //____ Check that the counting of generated classes is the one expected.
        Collection javaFiles = FileUtils.listFiles(outputDir, FileFilterUtils.suffixFileFilter(".class"), DirectoryFileFilter.DIRECTORY);
        assertThat(javaFiles).hasSize(5);

    }

    @Test
    public void should_get_all_expected_entries_from_generated_jar_file() throws IOException {
        Xsd2JarTransformer toTest = new Xsd2JarTransformer();

        URL xsdFile = this.getClass().getResource("/model.xsd");

        InputStream modelJarStream = toTest.transformXsd2Jar("org.pymma.drools", new File(xsdFile.getFile()));

        File modelJarFile = File.createTempFile("model", ".jar");
        IOUtils.copy(modelJarStream, FileUtils.openOutputStream(modelJarFile));

        JarInputStream inputStream = new JarInputStream(FileUtils.openInputStream(modelJarFile));
        assertThat(inputStream.getManifest()).isNotNull();

        List<ZipEntry> allJarEntries = new ArrayList<ZipEntry>();

        ZipEntry entry;
        while((entry = inputStream.getNextEntry()) != null)
            allJarEntries.add(entry);

        assertThat(allJarEntries).hasSize(5);
    }

}
