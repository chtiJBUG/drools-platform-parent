package org.chtijbug.drools.platform.persistence.impl.dao.impl;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

public abstract class AbstractJpaDAO< T extends Serializable> {

   private Class< T > clazz;

   @PersistenceContext
   EntityManager entityManager;

   public void setClazz( Class< T > clazzToSet ){
      this.clazz = clazzToSet;
   }

   public T findOne( Long id ){
      return this.entityManager.find( this.clazz, id );
   }
   public List< T > findAll(){
      return this.entityManager.createQuery( "from " + this.clazz.getName() )
       .getResultList();
   }
   @Transactional
   public void save( T entity ){
      this.entityManager.persist(entity);
   }
    @Transactional
   public void update( T entity ){
      this.entityManager.merge( entity );
   }
   @Transactional
   public void delete( T entity ){
      this.entityManager.remove( entity );
   }
   @Transactional
   public void deleteById( Long entityId ){
      T entity = this.findOne(entityId);

      this.delete( entity );
   }
}