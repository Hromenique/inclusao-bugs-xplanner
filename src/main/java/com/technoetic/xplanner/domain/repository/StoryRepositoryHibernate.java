package com.technoetic.xplanner.domain.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.type.Type;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate.HibernateTemplate;

import com.technoetic.xplanner.util.LogUtil;

/*
 * A Hibernate implementation of the StoryRepository interface.
 * 
 * Implementation is based pretty heavily on (and should eventually supercede)
 * StoryQueryHelper.
 * 
 * @author James Beard
 * @see    com.technoetic.xplanner.db.StoryQueryHelper
 */
public class StoryRepositoryHibernate extends HibernateObjectRepository implements StoryRepository {
   private static Logger log = LogUtil.getLogger();

   public StoryRepositoryHibernate() throws HibernateException {
       super(com.technoetic.xplanner.domain.UserStory.class);
   }
   
   /*
    * Returns a collection of user stories in current and future iterations
    * where personId is the customer.
    * 
    * @param  personId    the id of the customer
    * @return             the collection of stories
    */
   public Collection getStoriesForPersonWhereCustomer(int personId) {
      return queryStories("stories.customer", personId);
   }

   /*
    * Returns a collection of user stories in current and future iterations
    * where personId is the tracker.
    * 
    * @param  personId    the id of the tracker
    * @return             the collection of stories
    */
   public Collection getStoriesForPersonWhereTracker(int personId) {
      return queryStories("stories.tracker", personId);
   }

   private List queryStories(String queryName, int personId) {
      HibernateTemplate template = getHibernateTemplate();
      return template.findByNamedQueryAndNamedParam(
            queryName,
            new String[]{"date", "personId"},
            new Object[]{new Date(), new Integer(personId)},
            new Type[]{Hibernate.DATE, Hibernate.INTEGER});
   }
}
