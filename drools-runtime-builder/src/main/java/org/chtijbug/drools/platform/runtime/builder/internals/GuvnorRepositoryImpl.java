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
    /** Class logger */
    private static Logger logger = LoggerFactory.getLogger(GuvnorRepositoryImpl.class);
    /** Package Asset Template file */
    private static final String PACKAGE_ASSET_TEMPLATE = "/package.xml";
    /** Package Asset Template file */
    private static final String PROCESS_ASSET_TEMPLATE = "/process.xml";
    /** URL to the remote Guvnor instance */
    private String baseUrl = "http://localhost:8080";
    /** */
    private String appName = "/drools-guvnor";
    /** Base 64 encoded guvnor account for using REST API */
    private final String auth;

    public GuvnorRepositoryImpl(String baseUrl, String appName, String username, String password) {
        this.baseUrl = baseUrl;
        this.appName = appName;
        this.auth = "Basic " + Base64Utility.encode((username + ":" + password).getBytes());
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
