/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.actions;

import java.util.*;

import com.technoetic.xplanner.domain.*;
import com.technoetic.xplanner.history.HistoricalEvent;

public class TestStoryContinuer extends ContinuerBaseTestCase {

   protected void setUp() throws Exception {
      super.setUp();
      setUpTasksStoryAndIteration();
      setUpTargetIteration();
      setUpContinuers();
      support.hibernateSession.save(targetIteration);

   }

   public void testStoryContinuation() throws Exception {

      putContentOnMockQuery();

      UserStory continuedStory = (UserStory) storyContinuer.continueObject(story, iteration, targetIteration);
      List tasks = new ArrayList(continuedStory.getTasks());
      assertEquals("continued tasks in continued story", 2, tasks.size());
      Task continuedTask1 = (Task) tasks.get(0);
      Task continuedTask2 = (Task) tasks.get(1);

      assertHistoricalEventInObject(incompleteTask1, "Continued as task:" + continuedTask1.getId() + " in story:" +  continuedStory.getId(),HistoricalEvent.CONTINUED);
      assertHistoricalEventInObject(incompleteTask2, "Continued as task:" + continuedTask2.getId() + " in story:" +  continuedStory.getId(),HistoricalEvent.CONTINUED);
      assertHistoricalEventInObject(story, "Continued task:" + incompleteTask1.getId() + " as task:" + continuedTask1.getId() + " in story:" + continuedStory.getId(), HistoricalEvent.CONTINUED);
      assertHistoricalEventInObject(story, "Continued task:" + incompleteTask2.getId() + " as task:" + continuedTask2.getId() + " in story:" + continuedStory.getId(), HistoricalEvent.CONTINUED);
      assertHistoricalEventInObject(iteration, "Continued story:" + story.getId() + " as story:" + continuedStory.getId() + " in iteration:" + targetIteration.getId(), HistoricalEvent.CONTINUED);

      assertHistoricalEventInObject(targetIteration, "Continued story:" + continuedStory.getId() + " from story:" + story.getId() + " in iteration:" + iteration.getId(), HistoricalEvent.CONTINUED);
      assertHistoricalEventInObject(continuedStory,  "Continued task:" + continuedTask1.getId() + " from task:" + incompleteTask1.getId() + " in story:" +  story.getId(),HistoricalEvent.CONTINUED);
      assertHistoricalEventInObject(continuedStory,  "Continued task:" + continuedTask2.getId() + " from task:" + incompleteTask2.getId() + " in story:" +  story.getId(),HistoricalEvent.CONTINUED);
      assertHistoricalEventInObject(continuedTask1, "Continued from task:" + incompleteTask1.getId() + " in story:" +  story.getId(),HistoricalEvent.CONTINUED);
      assertHistoricalEventInObject(continuedTask2, "Continued from task:" + incompleteTask2.getId() + " in story:" +  story.getId(),HistoricalEvent.CONTINUED);

      assertFalse("Story should not been completed", story.isCompleted());
      verifyNotesBeingContinued(2);
      verifyContinuedStory(continuedStory);
   }

}