package com.technoetic.xplanner.db;

import java.util.Collection;
import java.util.List;
import java.util.Date;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.CollectionUtils;

import com.technoetic.xplanner.domain.Task;

import net.sf.hibernate.type.Type;
import net.sf.hibernate.Hibernate;

/**
 * User: Mateusz Prokopowicz
 * Date: Aug 24, 2005
 * Time: 6:22:01 PM
 */
public class TaskQueryDao extends HibernateDaoSupport implements TaskQuery {

   public Collection query(Collection cachedTasks, int personId, Boolean completed, Boolean active) {
      return CollectionUtils.select(getCurrentTasks(cachedTasks, personId),
                                    new TaskStatusFilter(completed, active));
   }

   private Collection getCurrentTasks(Collection cache, int personId) {
      if (cache == null) {
         cache = queryTasks("tasks.current.accepted", personId);
         cache.addAll(queryTasks("tasks.current.worked", personId));
      }
      return cache;
   }

   public List queryTasks(String queryName, int personId) {
      return getHibernateTemplate().findByNamedQueryAndNamedParam(queryName,
                                                                  new String[]{"now", "personId"},
                                                                  new Object[]{new Date(), new Integer(personId)},
                                                                  new Type[]{Hibernate.DATE, Hibernate.INTEGER});
   }

   public List queryTasks(String queryName, Date date){
      return getHibernateTemplate().findByNamedQuery(queryName, date);
   }

   private class TaskStatusFilter implements Predicate {
      Boolean isCompleted;
      Boolean isActive;

      public TaskStatusFilter(Boolean isCompleted, Boolean isActive) {
         this.isCompleted = isCompleted;
         this.isActive = isActive;
      }

      public boolean evaluate(Object o) {
         final Task task = ((Task) o);
         return (isCompleted == null || (isCompleted.booleanValue() == task.isCompleted())) &&
                (isActive == null || (isActive.booleanValue() == task.getTimeEntries().size() > 0));
      }
   }
}
