/*
 * Copyright 2014 Pymma Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.chtijbug.drools.platform.persistence.utility;


import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author timfulmer
 */
public class StringJsonUserType implements UserType {


    @Override
    public int[] sqlTypes() {
        return new int[]{Types.JAVA_OBJECT};
    }


    @Override
    public Class returnedClass() {
        return String.class;
    }


    @Override
    public boolean equals(Object x, Object y) throws HibernateException {

        if (x == null) {

            return y == null;
        }

        return x.equals(y);
    }


    @Override
    public int hashCode(Object x) throws HibernateException {

        return x.hashCode();
    }


    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        if (rs.getString(names[0]) == null) {
            return null;
        }
        return rs.getString(names[0]);
    }


    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
            return;
        }

        st.setObject(index, value, Types.OTHER);
    }


    @Override
    public Object deepCopy(Object value) throws HibernateException {

        return value;
    }


    @Override
    public boolean isMutable() {
        return true;
    }


    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (String) this.deepCopy(value);
    }


    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return this.deepCopy(cached);
    }



    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}
