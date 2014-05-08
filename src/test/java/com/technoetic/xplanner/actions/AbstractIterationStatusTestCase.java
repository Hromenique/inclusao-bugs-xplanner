/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Dec 23, 2005
 * Time: 12:01:09 PM
 */
package com.technoetic.xplanner.actions;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;

import com.technoetic.xplanner.charts.DataSampler;
import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.domain.DomainMetaDataRepository;
import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.Project;
import com.technoetic.xplanner.domain.Task;
import com.technoetic.xplanner.domain.UserStory;
import com.technoetic.xplanner.forms.IterationStatusEditorForm;
import com.technoetic.xplanner.history.HistoricalEvent;

public class AbstractIterationStatusTestCase extends AbstractActionTestCase {
   public static final int ITERATION_ID = 99;
   public static final int PROJECT_ID = 100;

   Project project;
   Iteration iteration;
   UserStory story;
   IterationStatusEditorForm editorForm;
   DataSampler mockDataSampler;
   public HistoricalEvent event;

   public void setUp() throws Exception
   {
      super.setUp();
      setUpProjectAndIterationData();
      support.setUpSubjectInRole("editor");
      support.setForward(AbstractAction.TYPE_KEY, Iteration.class.getName());
      support.setForward("view/projects", "projects.jsp");
      editorForm = new IterationStatusEditorForm();
      editorForm.setOid(""+ITERATION_ID);
      support.form = editorForm;
      AbstractIterationAction iterationAction = (AbstractIterationAction) action;
      mockDataSampler = createMock(DataSampler.class);
      iterationAction.setDataSampler(mockDataSampler);
      event = mom.newHistoricalEvent(project.getId(),
                                     iteration.getId(),
                                     DomainMetaDataRepository.ITERATION_TYPE_NAME,
                                     null,
                                     null);
      event.setId(0);


      expectObjectRepositoryAccess(Iteration.class);
      expectObjectRepositoryAccess(Project.class);
      expect(mockObjectRepository.load(ITERATION_ID)).andReturn(iteration);
      expect(mockObjectRepository.load(PROJECT_ID)).andReturn(project);
      mockObjectRepository.update(iteration);

   }

   private void setUpProjectAndIterationData() {
      project = new Project();
      project.setId(PROJECT_ID);
      iteration = new Iteration();
      iteration.setId(ITERATION_ID);
      iteration.setProjectId(PROJECT_ID);
      story = new UserStory();
      story.setEstimatedHours(3.0);
      Task task = new Task();
      task.setEstimatedHours(4.0);
      List tasks = new ArrayList();
      tasks.add(task);
      story.setTasks(tasks);
      List stories = new ArrayList();
      stories.add(story);
      iteration.setUserStories(stories);
      List iterations = new ArrayList();
      iterations.add(iteration);
      project.setIterations(iterations);
   }

   public void tearDown() throws Exception
   {
      super.tearDown();
      GlobalSessionFactory.set(null);
   }

   protected void resetHibernateSession()
   {
//      mockSession = new MockSession()
//      {
//         public Transaction beginTransaction()
//         {
//            return mockTransaction;
//         }
//      };
//      support.hibernateSession = (MockSession) mockSession;
//      ThreadSession.set(mockSession);
   }
}