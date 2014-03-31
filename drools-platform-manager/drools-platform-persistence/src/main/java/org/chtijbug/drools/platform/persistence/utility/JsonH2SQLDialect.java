package org.chtijbug.drools.platform.persistence.utility;

/**
 * Created by IntelliJ IDEA.
 * Date: 31/03/14
 * Time: 11:37
 * To change this template use File | Settings | File Templates.
 */

import org.hibernate.dialect.H2Dialect;

import java.sql.Types;

/**
 * Wrap default PostgreSQL9Dialect with 'json' type.
 *
 * @author timfulmer
 */
public class JsonH2SQLDialect extends H2Dialect {

    public JsonH2SQLDialect() {

        super();

        this.registerColumnType(Types.JAVA_OBJECT, "json");
    }
}