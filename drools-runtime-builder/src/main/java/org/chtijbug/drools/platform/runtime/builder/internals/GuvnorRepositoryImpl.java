package org.chtijbug.drools.platform.runtime.builder.internals;

import com.google.common.base.Throwables;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.impl.MetadataMap;
import org.chtijbug.drools.platform.runtime.builder.GuvnorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


@Component
public class GuvnorRepositoryImpl implements GuvnorRepository {
    /**
     * Package Asset Template file
     */
    private static final String PACKAGE_ASSET_TEMPLATE = "/package.xml";
    /**
     * Package Asset Template file
     */
    private static final String PROCESS_ASSET_TEMPLATE = "/process.xml";
    /**
     * Class logger
     */
    private static Logger logger = LoggerFactory.getLogger(GuvnorRepositoryImpl.class);
    /**
     * Base 64 encoded guvnor account for using REST API
     */
    private final String auth;
    /**
     * URL to the remote Guvnor instance
     */
    private String baseUrl = "http://localhost:8080";
    /** */
    private String appName = "/drools-guvnor";
    private String packageName;
    private String packageVersion;
    private String packageUsername;
    private String packagePassword;
    private String platformServer = "localhost";
    private String ruleBaseid;
    private Integer wsPort = 8080;
    private String wsHost = "localhost";
    private String jmsServer = "localhost";
    private Integer jmsPort = 61616;


    public GuvnorRepositoryImpl() {
        this.auth = "Basic " + Base64Utility.encode((packageUsername + ":" + packagePassword).getBytes());

    }

    public GuvnorRepositoryImpl(String packageName, String packageVersion, String packageUsername, String packagePassword, String ruleBaseid) {
        this.packageName = packageName;
        this.packageVersion = packageVersion;
        this.packageUsername = packageUsername;
        this.packagePassword = packagePassword;
        this.ruleBaseid = ruleBaseid;
        this.auth = "Basic " + Base64Utility.encode((packageUsername + ":" + packagePassword).getBytes());
    }


    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAuth() {
        return auth;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getPackageVersion() {
        return packageVersion;
    }

    public String getPackageUsername() {
        return packageUsername;
    }

    public String getPackagePassword() {
        return packagePassword;
    }

    public String getPlatformServer() {
        return platformServer;
    }

    public void setPlatformServer(String platformServer) {
        this.platformServer = platformServer;
    }

    public String getRuleBaseid() {
        return ruleBaseid;
    }

    public Integer getWsPort() {
        return wsPort;
    }

    public void setWsPort(Integer wsPort) {
        this.wsPort = wsPort;
    }

    public String getWsHost() {
        return wsHost;
    }

    public void setWsHost(String wsHost) {
        this.wsHost = wsHost;
    }

    public String getJmsServer() {
        return jmsServer;
    }

    public void setJmsServer(String jmsServer) {
        this.jmsServer = jmsServer;
    }

    public Integer getJmsPort() {
        return jmsPort;
    }

    public void setJmsPort(Integer jmsPort) {
        this.jmsPort = jmsPort;
    }

    @Override
    public void createBusinessPackage(String businessPackage) {
        logger.debug(">> createBusinessPackage(businessPackage=%s)", businessPackage);
        try {
            //___ Define the Package xml Data
            String packageXml = IOUtils.toString(Class.class.getResource(PACKAGE_ASSET_TEMPLATE).openStream());
            webClient(AssetType.PACKAGE)
                    .path(AssetType.PACKAGE.buildPath(appName))
                    .post(String.format(packageXml, businessPackage));
        } catch (IOException e) {
            logger.error("Error while opening Package Template File");
            throw Throwables.propagate(e);
        } finally {
            logger.debug("<< createBusinessPackage()");
        }
    }

    @Override
    public void createBusinessModel(String businessPackage, InputStream businessModel) {
        logger.debug(">> createBusinessModel(businessPackage=%s, businessModel=%s)", businessPackage, businessModel);
        try {
            WebClient client = WebClient.create(this.baseUrl);
            MultivaluedMap<String, String> headers = new MetadataMap<>();
            headers.put("slug", Arrays.asList("model.jar"));

            headers.put("Authorization", Arrays.asList(auth));
            headers.put("Content-Type", Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
            client.headers(headers);
            client.path(AssetType.MODEL.buildPath(appName, businessPackage));
            client.type(MediaType.APPLICATION_OCTET_STREAM_TYPE);
            client.post(IOUtils.toByteArray(businessModel));
        } catch (IOException e) {
            logger.error("Error while opening input Stream %s", businessModel);
            throw Throwables.propagate(e);
        } finally {
            logger.debug("<< createBusinessModel()");
        }
    }

    @Override
    public void createBusinessProcess(String businessPackage, String processName) {
        logger.debug(">> createBusinessProcess(businessPackage=%s, processName)", businessPackage, processName);
        try {
            String processAtomXml = IOUtils.toString(Class.class.getResource(PROCESS_ASSET_TEMPLATE).openStream());
            webClient(AssetType.PROCESS)
                    .type(AssetType.PROCESS.getMediaType())
                    .path(AssetType.PROCESS.buildPath(this.appName, businessPackage))
                    .post(String.format(processAtomXml, processName));
        } catch (IOException e) {
            logger.error("Error while opening Process Template File");
            throw Throwables.propagate(e);
        } finally {
            logger.debug("<< createBusinessProcess()");
        }
    }

    private WebClient webClient(AssetType assetType) {
        WebClient client = WebClient.create(this.baseUrl);
        client.header("Authorization", auth);
        client.accept(assetType.getMediaType());
        return client;
    }
}
