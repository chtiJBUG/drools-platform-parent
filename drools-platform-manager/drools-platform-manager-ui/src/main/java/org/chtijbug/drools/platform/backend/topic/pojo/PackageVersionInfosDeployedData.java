package org.chtijbug.drools.platform.backend.topic.pojo;

import org.chtijbug.drools.platform.entity.PlatformResourceFile;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 12/05/14
 * Time: 10:18
 * To change this template use File | Settings | File Templates.
 */
public class PackageVersionInfosDeployedData extends BaseTopicData{

    private List<PlatformResourceFile> platformResourceFiles ;


    public PackageVersionInfosDeployedData() {
        super();
    }


    public List<PlatformResourceFile> getPlatformResourceFiles() {
        return platformResourceFiles;
    }

    public void setPlatformResourceFiles(List<PlatformResourceFile> platformResourceFiles) {
        this.platformResourceFiles = platformResourceFiles;
    }
}
