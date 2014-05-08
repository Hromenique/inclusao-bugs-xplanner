package com.technoetic.xplanner.domain.repository;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;
import org.springframework.orm.hibernate.HibernateObjectRetrievalFailureException;
import org.springframework.dao.DataAccessException;

import com.technoetic.xplanner.domain.DomainObject;
import com.technoetic.xplanner.domain.NoteAttachable;

public class HibernateObjectRepository extends HibernateDaoSupport implements ObjectRepository {
   private Class objectClass;
   private final String deletionQuery;

   public HibernateObjectRepository(Class objectClass) throws HibernateException {
      this.objectClass = objectClass;
      deletionQuery = "from object in " + objectClass + " where id = ?";
   }

   public void delete(final int objectIdentifier) throws RepositoryException {
      try {
         if (NoteAttachable.class.isAssignableFrom(objectClass)) {
            // FIXME This unfortunately is not enough. we have cascade delete on from project down to time entry. Any of these contained entity could have notes that have files. These files won't be deleted
//                  NoteHelper.deleteNotesFor(objectClass, objectIdentifier, getHibernateTemplate());
         }
         getHibernateTemplate().delete(deletionQuery, new Integer(objectIdentifier), Hibernate.INTEGER);
      } catch (HibernateObjectRetrievalFailureException e) {
         throw new ObjectNotFoundException(e.getMessage());
      } catch (DataAccessException ex) {
         throw new RepositoryException(ex);
      }
   }

   public Object load(final int objectIdentifier) throws RepositoryException {
      try {
         return getHibernateTemplate().load(objectClass, new Integer(objectIdentifier));

      } catch (HibernateObjectRetrievalFailureException e) {
         throw new ObjectNotFoundException(e.getMessage());
      } catch (DataAccessException ex) {
         throw new RepositoryException(ex);
      }
   }

   public int insert(final DomainObject object) throws RepositoryException {
      try {
         Integer id = (Integer) getHibernateTemplate().save(object);
         return id.intValue();

      } catch (HibernateObjectRetrievalFailureException e) {
         throw new ObjectNotFoundException(e.getMessage());
      } catch (DataAccessException ex) {
         throw new RepositoryException(ex);
      }
   }

   public void update(DomainObject object) throws DuplicateUserIdException {
      // empty
   }
}
