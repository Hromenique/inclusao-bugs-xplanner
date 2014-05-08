/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.security.install;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.UserStory;
import com.technoetic.xplanner.upgrade.HibernateMigrationTaskSupport;

public class MigrateStoryPriorityToOrderNo extends HibernateMigrationTaskSupport {

   private static final Logger log = Logger.getLogger(MigrateStoryPriorityToOrderNo.class);
   private static final String MIGRATION_ERROR = "Error migrating story priority to order number";

  public MigrateStoryPriorityToOrderNo() {
    super("Migrate story priority to order number", 11); }

   protected void migrate() throws Exception {
      List iterations = session.find("from iteration in class " + Iteration.class.getName());
      for (Iterator IterationIterator = iterations.iterator(); IterationIterator.hasNext();) {
         Iteration iteration = (Iteration) IterationIterator.next();
         List userStories = new ArrayList(iteration.getUserStories());
         int[][] storyIdAndNewOrder = new int[userStories.size()][2];
         for (int index = 0; index < userStories.size(); index++) {
            UserStory userStory = (UserStory) userStories.get(index);
            storyIdAndNewOrder[index][Iteration.STORY_ID_INDEX] = userStory.getId();
            storyIdAndNewOrder[index][Iteration.ORDER_NO_INDEX] = userStory.getPriority();
         }
         iteration.modifyStoryOrder(storyIdAndNewOrder);
      }
   }
}
