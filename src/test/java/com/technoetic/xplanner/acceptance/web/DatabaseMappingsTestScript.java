package com.technoetic.xplanner.acceptance.web;

import com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript;
import com.technoetic.xplanner.db.hibernate.IdGenerator;
import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.Person;
import com.technoetic.xplanner.domain.Project;
import com.technoetic.xplanner.domain.UserStory;

public class DatabaseMappingsTestScript extends AbstractDatabaseTestScript {
   public void testMappings() throws Exception {
      Project project = newProject();
      Iteration iteration = newIteration(project);
      UserStory story = newUserStory(iteration);
      Person person = newPerson(IdGenerator.getUniqueId("UserId"));
      story.setCustomer(person);
      commitCloseAndOpenSession();
      UserStory savedStory = (UserStory) getSession().load(UserStory.class, new Integer(story.getId()));
      assertEquals(person, savedStory.getCustomer());
   }

}