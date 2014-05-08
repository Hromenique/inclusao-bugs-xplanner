package com.technoetic.xplanner.db.hibernate;

import java.io.Serializable;
import java.util.List;

import net.sf.hibernate.Session;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.type.Type;
import org.springframework.orm.hibernate.HibernateOperations;

/**
 * User: Mateusz Prokopowicz
 * Date: Apr 21, 2005
 * Time: 11:18:09 AM
 */
public class HibernateOperationsWrapper{
   private Session session;
   private HibernateOperations hibernateOperations;

   public HibernateOperationsWrapper(Session session){
      this.session = session;
   }

   public HibernateOperationsWrapper(HibernateOperations hibernateOperations){
      this.hibernateOperations = hibernateOperations;
   }

   public Object load(Class theClass, Serializable id) throws HibernateException{
      if (session != null)
         return session.load(theClass, id);
      else
         return hibernateOperations.load(theClass, id);
   }

   public List find(String query, Object[] values, Type[] types) throws HibernateException{
      if (session != null)
         return session.find(query, values, types);
      else
         return hibernateOperations.find(query, values, types);
   }

   public Serializable save(Object object) throws HibernateException{
      if (session != null)
         return session.save(object);
      else
         return hibernateOperations.save(object);
   }   }
