package com.technoetic.xplanner.domain.repository;

import java.util.Collection;

/*
 * A repository that can be used to access collections of user stories
 * based on certain criteria.
 * 
 * @author James Beard
 */
public interface StoryRepository extends ObjectRepository {
   
   /*
    * Returns a collection of user stories in current and future iterations
    * where personId is the customer.
    * 
    * @param  personId    the id of the customer
    * @return             the collection of stories
    */
   public Collection getStoriesForPersonWhereCustomer(int personId);

   /*
    * Returns a collection of user stories in current and future iterations
    * where personId is the tracker.
    * 
    * @param  personId    the id of the tracker
    * @return             the collection of stories
    */
   public Collection getStoriesForPersonWhereTracker(int personId);

}
