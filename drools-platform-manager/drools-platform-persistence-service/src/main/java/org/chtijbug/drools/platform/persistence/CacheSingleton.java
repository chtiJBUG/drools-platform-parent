package org.chtijbug.drools.platform.persistence;

import org.gridgain.grid.Grid;
import org.gridgain.grid.GridException;
import org.gridgain.grid.GridGain;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by IntelliJ IDEA.
 * Date: 01/08/14
 * Time: 10:32
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CacheSingleton {


    private static Logger logger = getLogger(CacheSingleton.class);

    private Grid grid;

    public CacheSingleton() {
        try {
            grid = GridGain.start("example-cache.xml");
        } catch (GridException e) {
            logger.error("Could not create Cache", e);
        }
    }

    public Grid getGrid() {
        return grid;
    }
}
