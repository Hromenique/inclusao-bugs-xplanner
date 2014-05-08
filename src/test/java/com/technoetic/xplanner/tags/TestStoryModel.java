package com.technoetic.xplanner.tags;

import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.Project;
import com.technoetic.xplanner.domain.UserStory;
import junit.framework.TestCase;

public class TestStoryModel extends TestCase {
    StoryModel model;

    public void testGetName() throws Exception {
        UserStory story = new UserStory();
        story.setName("story");
        Iteration iteration = new Iteration();
        iteration.setName("iteration");
        IterationModel model = new IterationModel(iteration){
            protected Project getProject() {
                Project project = new Project();
                project.setName("project");
                return project;
            }
        };
        this.model = new StoryModel(model,story );
        assertEquals("project :: iteration :: story",this.model.getName());
    }
}