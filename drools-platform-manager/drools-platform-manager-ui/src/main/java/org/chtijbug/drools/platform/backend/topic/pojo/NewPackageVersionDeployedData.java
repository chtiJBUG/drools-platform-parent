package org.chtijbug.drools.platform.backend.topic.pojo;

import org.chtijbug.drools.platform.entity.PlatformResourceFile;
import org.chtijbug.drools.platform.entity.RequestStatus;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 12/05/14
 * Time: 10:18
 * To change this template use File | Settings | File Templates.
 */
public class NewPackageVersionDeployedData extends BaseTopicData{

    private RequestStatus state;

    private List<PlatformResourceFile> platformResourceFiles ;


    public NewPackageVersionDeployedData() {
        super();
    }

    public NewPackageVersionDeployedData(RequestStatus state) {
        this.state = state;
    }

    public NewPackageVersionDeployedData(RequestStatus state, List<PlatformResourceFile> platformResourceFiles) {
        this.state = state;
        this.platformResourceFiles = platformResourceFiles;
    }

    public RequestStatus getState() {
        return state;
    }

    public void setState(RequestStatus state) {
        this.state = state;
    }

    public List<PlatformResourceFile> getPlatformResourceFiles() {
        return platformResourceFiles;
    }

    public void setPlatformResourceFiles(List<PlatformResourceFile> platformResourceFiles) {
        this.platformResourceFiles = platformResourceFiles;
    }
}
