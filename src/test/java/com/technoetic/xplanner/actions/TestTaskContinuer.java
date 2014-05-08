package com.technoetic.xplanner.actions;

import java.util.ArrayList;
import java.util.List;

import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.Person;
import com.technoetic.xplanner.domain.Task;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.domain.UserStory;
import com.technoetic.xplanner.history.HistoricalEvent;
import com.technoetic.xplanner.security.SecurityHelper;

public class TestTaskContinuer extends ContinuerBaseTestCase {
   private UserStory story1;
   private UserStory story2;
   private Task taskToMoveOrContinue;
   private static final String TASK_NAME = "Test Task";

   protected void setUp() throws Exception {
      super.setUp();
      setUpContinuers();
      taskToMoveOrContinue =
            createTask(((Integer) (support.hibernateSession.saveIds[0])).intValue(),
                       1010,
                       5.0,
                       2.0,
                       createTimeEntries());
      taskToMoveOrContinue.setName(TASK_NAME);
      support.hibernateSession.save(taskToMoveOrContinue);
      ArrayList tasks = new ArrayList();
      tasks.add(taskToMoveOrContinue);

//       setUpThreadSession(); // AbstractUnitTestCase.mockSession
      /**
       * use hibernate support.hibernate session instead of AbstractUnitTestCase.mockSession
       */
//       ThreadSession.set(support.hibernateSession); // hibernate.session

      story1 = new UserStory();
      story1.setTasks(tasks);
      story1.setName("name");
      story1.setCustomer(new Person());
      story1.setDescription("A description.");
      support.hibernateSession.save(story1);

      story2 = new UserStory();
      story2.setName("story2 name");
      support.hibernateSession.save(story2);
      support.hibernateSession.loadAddReturnByClassById(story2.getId(), story2);
      assertTrue(story2.getId() != 0);
      Iteration iteration = new Iteration();
      ArrayList stories = new ArrayList();
      stories.add(story1);
      stories.add(story2);
      iteration.setUserStories(stories);
      support.hibernateSession.save(iteration);
      story1.setIterationId(iteration.getId());
      story2.setIterationId(iteration.getId());
      support.hibernateSession.loadAddReturnByClassById(iteration.getId(), iteration);
   }

   public void testTaskContinuation() throws Exception {
      taskToMoveOrContinue.setCompleted(false);
      taskToMoveOrContinue.setStory(story1);

      createNotesListFor(taskToMoveOrContinue);

      putContentOnMockQuery();

      Iteration iteration = (Iteration) metaDataRepository.getParent(story2);
      assertNotNull("Test not set up correctly", iteration);

      taskContinuer.init(support.hibernateSession, support.resources, SecurityHelper.getRemoteUserId(support.request));
      Task continuedTask = (Task) taskContinuer.continueObject(taskToMoveOrContinue, story1, story2);

      assertHistoricalEventInObject(continuedTask,
                                    "Continued from task:" +
                                    taskToMoveOrContinue.getId() +
                                    " in story:" +
                                    story1.getId(),
                                    HistoricalEvent.CONTINUED);
      assertHistoricalEventInObject(taskToMoveOrContinue,
                                    "Continued as task:" + continuedTask.getId() + " in story:" + story2.getId(),
                                    HistoricalEvent.CONTINUED);
      assertHistoricalEventInObject(story2,
                                    "Continued task:" +
                                    continuedTask.getId() +
                                    " from task:" +
                                    taskToMoveOrContinue.getId() +
                                    " in story:" +
                                    story1.getId(),
                                    HistoricalEvent.CONTINUED);
      assertHistoricalEventInObject(story1,
                                    "Continued task:" +
                                    taskToMoveOrContinue.getId() +
                                    " as task:" +
                                    continuedTask.getId() +
                                    " in story:" +
                                    story2.getId(),
                                    HistoricalEvent.CONTINUED);

      List tasks = getSavedInstancesOf(Task.class);
      assertEquals("# of tasks", 2, tasks.size());

      verifyNotesBeingContinued(1);
      assertTaskProperties(taskToMoveOrContinue, continuedTask);
      assertEquals("original task disposition", TaskDisposition.PLANNED, taskToMoveOrContinue.getDisposition());
      assertEquals("continued task disposition", TaskDisposition.CARRIED_OVER, continuedTask.getDisposition());
   }

}
