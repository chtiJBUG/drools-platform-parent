package org.chtijbug.drools.platform.runtime.utils;

import com.google.common.base.Throwables;
import com.sun.tools.internal.xjc.Driver;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.tools.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;


@Component
public final class Xsd2JarTransformer {
    /**
     * Class logger
     */
    private static Logger logger = LoggerFactory.getLogger(Xsd2JarTransformer.class);


    public InputStream transformXsd2Jar(String packageName, File xsdFile) {
        logger.debug(">> transformXsd2Jar(xsdContent=%s", xsdFile);
        InputStream result = null;
        try {
            //___ Create tmp dir
            File targetDir = File.createTempFile("tmp-dir", "");
            targetDir.delete();
            if(!targetDir.mkdir()) {
                throw new RuntimeException("Unable to create temporary directory. Execution will stop");
            }
            //___ Run XJC
            createJavaClassesFromXsd(xsdFile, targetDir, packageName);
            //___ Generate the JAR file
            compileTarget(targetDir);
            File targetJar = File.createTempFile("business-model", ".jar");
            buildJarFile(targetJar, targetDir);
            result = FileUtils.openInputStream(targetJar);
        } catch (IOException e) {
            logger.error("An error occurred while deleting temporary folder");
            throw Throwables.propagate(e);
        } finally {
            logger.debug("<< transformXsd2Jar");
        }
        return result;
    }

    protected void compileTarget(File targetDir) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        Collection allFiles = FileUtils.listFiles(targetDir, new String[]{"java"}, true);
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(allFiles);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
        boolean status = task.call();
        if (!status) {
            /*Iterate through each compilation problem and print it*/
            for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
                logger.error("Error on line {} in {}", diagnostic.getLineNumber(), diagnostic);
            }
        }
        fileManager.close();
    }

    public void createJavaClassesFromXsd(File xsdFile, File outputDir, String packageName) {
        try {
            ClassLoader parent = this.getClass().getClassLoader();
            // SMT : Need to add the XSD file
            List<URL> urls = Arrays.asList(xsdFile.toURI().toURL());
            URLClassLoader cl = new URLClassLoader(urls.toArray(new URL[urls.size()]), parent);
            // Set the new classloader
            Thread.currentThread().setContextClassLoader(cl);
            try {
                ArrayList<String> args = getXJCArgs(xsdFile, packageName, outputDir);
               // MojoXjcListener xjcListener = new MojoXjcListener();
                // Run XJC
                if (0 != Driver.run(args.toArray(new String[args.size()]), System.out, System.out)) {
                    String msg = "Could not process schema";
                    throw new RuntimeException(msg);
                }
            } finally {
                // Set back the old classloader
                Thread.currentThread().setContextClassLoader(parent);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private ArrayList<String> getXJCArgs(File xsdFile, String packageName, File outputDirectory)
            throws RuntimeException {
        ArrayList<String> args = new ArrayList<>();

        args.add("-p");
        args.add(packageName);

        args.add("-d");
        args.add(outputDirectory.getAbsolutePath());
        // Bindings

        args.add("-b");
        args.add(Class.class.getResource("/xjc/bindings.xml").getFile());
        args.add("-extension");
        args.add(xsdFile.getAbsolutePath());

        return args;
    }


    public void buildJarFile(File targetJar, File targetDir) throws IOException {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        JarOutputStream target = new JarOutputStream(new FileOutputStream(targetJar), manifest);
        add(targetDir, target, targetDir.getAbsolutePath());
        target.close();
    }

    private void add(File source, JarOutputStream target, String rootPath) throws IOException {
        String name = source.getPath().replace("\\", "/");
        name = name.replace(rootPath, "");
        if (source.isDirectory()) {
            /* if (!name.isEmpty()) {
                if (!name.endsWith("/"))
                    name += "/";
                JarEntry entry = new JarEntry(name);
                entry.setTime(source.lastModified());
                target.putNextEntry(entry);
                target.closeEntry();
            } */
            for (File nestedFile : FileUtils.listFiles(source, new String[]{"class"}, true)) {
                add(nestedFile, target, rootPath);
            }
            return;
        }
        if (name.startsWith("/"))
            name = name.replaceFirst("/", "");
        JarEntry entry = new JarEntry(name);
        entry.setTime(source.lastModified());
        target.putNextEntry(entry);
        byte[] contentAsBytes = FileUtils.readFileToByteArray(source);
        target.write(contentAsBytes);
        target.closeEntry();
    }

    /**
     * Class to tap into Maven's logging facility
     */
    /**class MojoXjcListener
            extends XJCListener {

        private String location(SAXParseException e) {
            return e.getPublicId() + ", " + e.getSystemId() + "[" + e.getLineNumber() + ","
                    + e.getColumnNumber() + "]";
        }

        public void error(SAXParseException arg0) {
            logger.error(location(arg0), arg0);
        }

        public void fatalError(SAXParseException arg0) {
            logger.error(location(arg0), arg0);
        }

        public void warning(SAXParseException arg0) {
            logger.warn(location(arg0), arg0);
        }

        public void info(SAXParseException arg0) {
            logger.warn(location(arg0), arg0);
        }

        public void message(String arg0) {
            logger.info(arg0);
        }

    } */

}
