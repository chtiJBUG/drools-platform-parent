package org.chtijbug.drools.platform.backend.topic.pojo;

import org.chtijbug.drools.platform.persistence.pojo.RealTimeParameters;

/**
 * Created by IntelliJ IDEA.
 * Date: 12/05/14
 * Time: 10:11
 * To change this template use File | Settings | File Templates.
 */
public class RealTimeParametersData extends BaseTopicData {

    private RealTimeParameters realTimeParameters;

    public RealTimeParametersData() {
        super();
    }

    public RealTimeParameters getRealTimeParameters() {
        return realTimeParameters;
    }

    public void setRealTimeParameters(RealTimeParameters realTimeParameters) {
        this.realTimeParameters = realTimeParameters;
    }
}
