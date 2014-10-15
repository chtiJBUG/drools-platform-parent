package org.chtijbug.drools.platform.runtime.builder;

import com.google.common.base.Throwables;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collection;

import static org.apache.commons.io.FileUtils.copyFileToDirectory;
import static org.apache.commons.io.FileUtils.write;

public class MavenProject {
    private static final String JAVA_EXT = ".java";
    private static Logger logger = LoggerFactory.getLogger(MavenProject.class);
    protected final File pomFile;
    protected final File srcFolder;
    protected final File resourcesFolder;
    protected final File webappFolder;
    protected final File webinfFolder;
    protected final File wsdlFolder;
    protected String mavenPath;
    protected String baseGeneratedPath;

    public MavenProject(File projectFolder, String pomFileContent, String mavenPath, String baseGeneratedPath) {
        this.mavenPath = mavenPath;
        this.baseGeneratedPath = baseGeneratedPath;
        this.srcFolder = addSubFolder(projectFolder, "src/main/java");
        this.resourcesFolder = addSubFolder(projectFolder, "src/main/resources");
        this.wsdlFolder = addSubFolder(resourcesFolder, "wsdl");
        this.webappFolder = addSubFolder(projectFolder, "src/main/webapp");
        this.webinfFolder = addSubFolder(webappFolder, "WEB-INF");
        this.pomFile = new File(projectFolder, "pom.xml");
        try {
            if (!pomFile.exists() && !pomFile.createNewFile())
                throw new RuntimeException("Unable to create temporary directory. Execution will stop");
            write(pomFile, pomFileContent);
        } catch (IOException e) {
            logger.error("Exception thrown while copying content into the destination file pom.xml");
            throw Throwables.propagate(e);
        }
    }

    public void generateSources() {
        launchMavenCommand("generate-sources");
    }

    public InputStream buildUpPackage() {
        launchMavenCommand("package");
        //___ TODO Look up for the generated war file and open an input stream on it.
        return null; // TODO

    }

    private void launchMavenCommand(String lifeCycle) {
        try {
            String localM2Repo = "-Dmaven.repo.local=" + this.mavenPath + "/m2";
            String mvnCommand = "mvn";
            if (this.mavenPath != null && this.mavenPath.length() > 0) {
                mvnCommand = this.mavenPath + "/bin/mvn";
            }
            ProcessBuilder builder = new ProcessBuilder(mvnCommand, "-X", "-f", this.pomFile.getAbsolutePath(), lifeCycle, localM2Repo);

            Process process = builder.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = br.readLine()) != null) {
                logger.debug(line);
            }
            int exitStatus = process.waitFor();
            if (exitStatus != 0)
                throw new RuntimeException("Error while running mvn " + lifeCycle + " command");
        } catch (IOException | InterruptedException e) {
            logger.error("Error while running mvn clean command");
            throw Throwables.propagate(e);
        }
    }

    private File addSubFolder(File parentFolder, String folderName) {
        File subFolder = new File(parentFolder, folderName);
        if (!subFolder.mkdirs())
            throw new RuntimeException("Unable to create temporary directory. Execution will stop");
        return subFolder;
    }

    public void addWSDL(String filename, byte[] wsdlContentAsByteArray) {
        createNewFile(wsdlFolder, filename, new ByteArrayInputStream(wsdlContentAsByteArray));
    }

    public void addXSD(String modelFilename, InputStream businessModelAsXsd) {
        createNewFile(wsdlFolder, modelFilename, businessModelAsXsd);
    }

    public void addXJCBinding(File bindingFile) {
        try {
            // Create new xjc folder
            File xjcFolder = new File(resourcesFolder, "xjc");
            copyFileToDirectory(bindingFile, xjcFolder);
        } catch (IOException e) {
            logger.error("Exception thrown while copying content of the file {} into the destination file", bindingFile);
            throw Throwables.propagate(e);
        }
    }

    public void addWebXml(File webXml) {
        try {
            copyFileToDirectory(webXml, webinfFolder);
        } catch (IOException e) {
            logger.error("Exception thrown while copying content of the file {} into the destination file", webXml);
            throw Throwables.propagate(e);
        }
    }

    protected static void createNewFile(File targetDir, String filename, InputStream content) {
        logger.debug(">> createNewFile(targetDir={}, filename={}, content={}", targetDir, filename, content);
        try {
            File file = new File(targetDir, filename);
            if (!file.createNewFile())
                throw new RuntimeException("Unable to create temporary directory. Execution will stop");
            FileUtils.copyInputStreamToFile(content, file);
        } catch (IOException e) {
            logger.error("Exception thrown while copying content into the destination file {}", filename);
            throw Throwables.propagate(e);
        } finally {
            logger.debug("<< createNewFile");
        }
    }

    protected void addSpringConfigurationFile(File springFolder, String filename, String springConfContent) {
        logger.debug(">> addSpringConfigurationFile(springFolder={}, filename={}, springConfContent={})", springFolder, filename, springConfContent);
        try {
            File springFile = new File(springFolder, filename);
            if (!springFile.exists() && !springFile.createNewFile())
                throw new RuntimeException("Unable to create spring resource file with name " + filename);
            write(springFile, springConfContent);
        } catch (IOException e) {
            logger.error("Exception thrown while copying content into the destination file pom.xml");
            throw Throwables.propagate(e);
        } finally {
            logger.debug("<< addSpringConfigurationFile");
        }
    }

    public File createSourceFile(String basePackageName, String executionServiceClassName) {
        try {
            String packageDirectoryName = basePackageName;
            if (packageDirectoryName.contains("."))
                packageDirectoryName = packageDirectoryName.replaceAll("\\.", File.separator);
            String javaSourceFilename = executionServiceClassName;
            if (!javaSourceFilename.endsWith(JAVA_EXT))
                javaSourceFilename = javaSourceFilename.concat(JAVA_EXT);
            File packageDirectory = new File(this.srcFolder, packageDirectoryName);
            if (!packageDirectory.exists() && !packageDirectory.mkdirs())
                throw new RuntimeException("Unable to create folder into " + this.srcFolder.getAbsolutePath());
            File javaSourceFile = new File(packageDirectory, javaSourceFilename);
            if (!javaSourceFile.exists() && !javaSourceFile.createNewFile())
                throw new RuntimeException("Unable to create Java source file into " + packageDirectory.getAbsolutePath());
            return javaSourceFile;
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }


    public File getSourceFile(String filename) {
        Collection<File> files = FileUtils.listFiles(this.srcFolder, null, true);
        for (File child : files) {
            if (child.getName().equals(filename))
                return child;
        }
        return null;
    }
}
