package com.technoetic.xplanner.acceptance.web;

import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.Project;
import com.technoetic.xplanner.domain.Task;
import com.technoetic.xplanner.domain.UserStory;
import com.technoetic.xplanner.tags.DomainContext;

public class DomainContextTestScript extends com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript {
    DomainContext context;

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testPopulateTask() throws Exception {
        Project project = newProject();
        Iteration iteration = newIteration(project);
        UserStory story = newUserStory(iteration);
        Task task = newTask(story);
        commitCloseAndOpenSession();
        DomainContext context = new DomainContext();
        context.populate(task);
        assertEquals(project.getId(), context.getProjectId());
        assertEquals(project.getName(), context.getProjectName());
        assertEquals(iteration.getId(), context.getIterationId());
        assertEquals(iteration.getName(), context.getIterationName());
        assertEquals(story.getId(), context.getStoryId());
        assertEquals(story.getName(), context.getStoryName());
        assertSame(task, context.getTargetObject());
    }

    public void testPopulateStory() throws Exception {
        Project project = newProject();
        Iteration iteration = newIteration(project);
        UserStory story = newUserStory(iteration);
        commitCloseAndOpenSession();
        DomainContext context = new DomainContext();
        context.populate(story);
        assertEquals(project.getId(), context.getProjectId());
        assertEquals(project.getName(), context.getProjectName());
        assertEquals(iteration.getId(), context.getIterationId());
        assertEquals(iteration.getName(), context.getIterationName());
        assertEquals(story.getId(), context.getStoryId());
        assertEquals(story.getName(), context.getStoryName());
        assertSame(story, context.getTargetObject());
    }

}