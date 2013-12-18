package org.chtijbug.drools.platform.runtime.builder.internals;

import javax.ws.rs.core.MediaType;

public enum AssetType {
    PACKAGE("%s/rest/packages", MediaType.APPLICATION_XML_TYPE),
    MODEL("%s/rest/packages/%s/assets", MediaType.APPLICATION_XML_TYPE),
    PROCESS("%s/rest/packages/%s/assets", MediaType.APPLICATION_ATOM_XML_TYPE);

    private String formattedPath;
    private MediaType mediaType;

    AssetType(String formattedPath, MediaType mediaType) {
        this.formattedPath = formattedPath;
        this.mediaType = mediaType;
    }

    protected String buildPath(Object... objects) {
        if(objects.length > 2)
            throw new RuntimeException("The REST Request path cannot contains more than 2 args");
        return String.format(this.formattedPath, objects);
    }

    protected MediaType getMediaType() {
        return mediaType;
    }
}
