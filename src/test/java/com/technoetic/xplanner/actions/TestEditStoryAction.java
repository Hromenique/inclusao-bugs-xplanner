package com.technoetic.xplanner.actions;

import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;

import com.technoetic.xplanner.domain.Feature;
import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.domain.Person;
import com.technoetic.xplanner.domain.Task;
import com.technoetic.xplanner.domain.TimeEntry;
import com.technoetic.xplanner.domain.UserStory;
import com.technoetic.xplanner.forms.UserStoryEditorForm;

public class TestEditStoryAction extends AbstractActionTestCase {
    private Iteration iteration;
    private List<UserStory> stories;
    private UserStory story;
    private Task task1;
    private Task task2;
    private Task task3;
    private Feature feature1;
    private Feature feature2;
    private ArrayList tasks;
    private ArrayList features;
   public static final int STORY_ID = 111;
   public static final int ITERATION_ID = 33;

   @Before
   public void setUp() throws Exception {
      action = new EditStoryAction();
      super.setUp();

       support.setForward(AbstractAction.TYPE_KEY, UserStory.class.getName());

       long now = new Date().getTime();
       TimeEntry t1 = new TimeEntry();
       t1.setStartTime(new Date(now - 3600000));
       t1.setEndTime(new Date(now));
       TimeEntry t2 = new TimeEntry();
       t2.setStartTime(new Date(now + 3600000));
       t2.setEndTime(new Date(now + 7200000));
       ArrayList timeEntries = new ArrayList();
       timeEntries.add(t1);
       timeEntries.add(t2);

       task1 = new Task();
       task1.setAcceptorId(1010);
       task1.setEstimatedHours(4.0);
       task1.setEstimatedOriginalHours(2.0);
       task1.setTimeEntries(timeEntries);
       task1.setCreatedDate(new Date(0));
       task2 = new Task();
       task2.setAcceptorId(1212);
       task2.setEstimatedHours(4.0);
       task2.setEstimatedOriginalHours(3.0);
       task2.setTimeEntries(timeEntries);
       task2.setCreatedDate(new Date(0));
       task3 = new Task();
       task3.setAcceptorId(1313);
       task3.setEstimatedHours(6.0);
       task3.setEstimatedOriginalHours(4.0);
       task3.setTimeEntries(new ArrayList());
       task3.setCreatedDate(new Date(0));
       tasks = new ArrayList();
       tasks.add(task1);
       tasks.add(task2);
       tasks.add(task3);
       feature1 = new Feature();
       feature1.setName("feature 1");
       feature2 = new Feature();
       feature2.setName("feature 2");
       features = new ArrayList();
       features.add(feature1);
       features.add(feature2);
       story = new UserStory();
       story.setTasks(tasks);
       story.setFeatures(features);
       iteration = new Iteration();
       iteration.setStatus(IterationStatus.INACTIVE);
       stories = new ArrayList<UserStory>();
       stories.add(story);
       iteration.setUserStories(stories);
   }

    public void testStoryCustomerIdToObjectMappingAfterSubmit() throws Exception {
        UserStoryEditorForm form = new UserStoryEditorForm();
        form.setOid("" + STORY_ID);
        form.setIterationId(ITERATION_ID);
        form.setAction(EditStoryAction.UPDATE_ACTION);
        support.form = form;
        form.setCustomerId(1);
        Person person1 = new Person();
        person1.setId(1);
        Person person2 = new Person();
        person2.setId(2);
        story.setId(STORY_ID);

        expectObjectRepositoryAccess(Iteration.class);
        expectObjectRepositoryAccess(UserStory.class);

        expect(mockObjectRepository.load(STORY_ID)).andReturn(story);
        expect(mockObjectRepository.load(ITERATION_ID)).andReturn(iteration);
        mockObjectRepository.update(story);
    	expect(mockSession.load(Person.class, new Integer(1))).andReturn(person1);
    	eventBus.publishUpdateEvent(form, story, null);
        replay();
        
        support.executeAction(action);

        verify();
        assertSame(person1, story.getCustomer());
    }

    public void testStoryCustomerIdToObjectMappingBeforeSubmit() throws Exception {
        UserStoryEditorForm form = new UserStoryEditorForm();
        form.setOid("" + STORY_ID);
        form.setCustomerId(1);
        support.form = form;
        Person person1 = new Person();
        person1.setId(1);
        Person person2 = new Person();
        person2.setId(2);
        UserStory story = new UserStory();
        story.setId(STORY_ID);
        story.setCustomer(person1);

        expectObjectRepositoryAccess(UserStory.class);
        expect(mockObjectRepository.load(STORY_ID)).andReturn(story);
        replay();

        support.executeAction(action);

        // dao - this test is probably wrong
        verify();
        assertEquals(1, form.getCustomerId());
    }

    public void testReorderOfStories() throws Exception
    {
        UserStoryEditorForm form = new UserStoryEditorForm();
        form.setOid("" + STORY_ID);
        form.setOrderNo(3);
        form.setAction(EditStoryAction.UPDATE_ACTION);
        support.form = form;
//        UserStory story = new UserStory();
        story.setId(STORY_ID);
        story.setIterationId(iteration.getId());
        expectObjectRepositoryAccess(UserStory.class);
        expect(mockObjectRepository.load(STORY_ID)).andReturn(story);
        mockObjectRepository.update(story);
        expectObjectRepositoryAccess(Iteration.class);
        expect(mockObjectRepository.load(iteration.getId())).andReturn(iteration);
    	eventBus.publishUpdateEvent(form, story, null);
        replay();
        assertEquals(1,iteration.getUserStories().size());
        support.executeAction(action);
        assertEquals(1, story.getOrderNo());
    }
}
