package org.chtijbug.drools.platform.persistence.utility;

/**
 * Created by IntelliJ IDEA.
 * Date: 31/03/14
 * Time: 11:37
 * To change this template use File | Settings | File Templates.
 */

import org.hibernate.dialect.DerbyTenSevenDialect;

import java.sql.Types;

/**
 * Wrap default PostgreSQL9Dialect with 'json' type.
 *
 * @author timfulmer
 */
public class JsonDerbytQLDialect extends DerbyTenSevenDialect {

    public JsonDerbytQLDialect() {

        super();

        this.registerColumnType(Types.JAVA_OBJECT, "json");
    }
}