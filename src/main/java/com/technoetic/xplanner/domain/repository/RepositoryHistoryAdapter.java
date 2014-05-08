package com.technoetic.xplanner.domain.repository;

import java.util.Date;

import net.sf.hibernate.Session;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import com.technoetic.xplanner.domain.DomainObject;
import com.technoetic.xplanner.filters.ThreadServletRequest;
import com.technoetic.xplanner.history.HistoricalEvent;
import com.technoetic.xplanner.history.HistorySupport;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;

public class RepositoryHistoryAdapter extends HibernateDaoSupport implements ObjectRepository {
   private Class objectClass;
   private ObjectRepository delegate;

   public RepositoryHistoryAdapter(Class objectClass, ObjectRepository delegate) {
      this.objectClass = objectClass;
      this.delegate = delegate;
   }

   public void delete(final int objectIdentifier) throws RepositoryException {
      // todo How should the project ID be obtained with refs to Hibernate?

      final int remoteUserId = getRemoteUserId();
      DomainObject object =
            (DomainObject) getHibernateTemplate().load(objectClass, new Integer(objectIdentifier));
      saveHistoryEvent(object, HistoricalEvent.DELETED, object.getName(), remoteUserId);
      delegate.delete(objectIdentifier);
   }

   public int insert(final DomainObject object) throws RepositoryException {
      int id = delegate.insert(object);
      final int remoteUserId;
      remoteUserId = getRemoteUserId();
      saveHistoryEvent(object, HistoricalEvent.CREATED, object.getName(), remoteUserId);
      return id;
   }

   public Object load(int objectIdentifier) throws RepositoryException {
      // no load history
      return delegate.load(objectIdentifier);
   }

   public void update(final DomainObject object) throws RepositoryException {
      final int remoteUserId;
      remoteUserId = getRemoteUserId();
      saveHistoryEvent(object, HistoricalEvent.UPDATED, object.getName(), remoteUserId);
      delegate.update(object);
   }

   private void saveHistoryEvent(final DomainObject object,
                                 final String eventType,
                                 final String description,
                                 final int remoteUserId) {
      getHibernateTemplate().execute(new SaveEventHibernateCallback(object, eventType, description, remoteUserId));

   }

   private int getRemoteUserId() throws RepositoryException {
      int remoteUserId;
      try {
         remoteUserId = SecurityHelper.getRemoteUserId(ThreadServletRequest.get());
      } catch (AuthenticationException e) {
         throw new RepositoryException(e);
      }
      return remoteUserId;
   }

   class SaveEventHibernateCallback implements HibernateCallback {
      private final DomainObject object;
      private final String eventType;
      private final String description;
      private final int remoteUserId;

      public SaveEventHibernateCallback(DomainObject object, String eventType, String description, int remoteUserId) {
         this.object = object;
         this.eventType = eventType;
         this.description = description;
         this.remoteUserId = remoteUserId;
      }

      public Object doInHibernate(Session session) {
         HistorySupport.saveEvent(session, object, eventType,
                                  description, remoteUserId, new Date());
         return null;
      }

      public boolean equals(Object o) {
         if (this == o) return true;
         if (o == null || getClass() != o.getClass()) return false;

         final SaveEventHibernateCallback that = (SaveEventHibernateCallback) o;

         if (remoteUserId != that.remoteUserId) return false;
         if (description != null ? !description.equals(that.description) : that.description != null) return false;
         if (eventType != null ? !eventType.equals(that.eventType) : that.eventType != null) return false;
         if (object != null ? !object.equals(that.object) : that.object != null) return false;

         return true;
      }

      public int hashCode() {
         return 0;
      }
   }
}
