package org.chtijbug.drools.platform.persistence.impl.dao;

/**
 * Created by IntelliJ IDEA.
 * Date: 11/02/14
 * Time: 23:48
 * To change this template use File | Settings | File Templates.
 */
public interface IAbstractJpaDAO<T> {
    public void save( T entity );
    public void update( T entity );
    public void delete( T entity );
    public T findOne( Long id )  ;
}
