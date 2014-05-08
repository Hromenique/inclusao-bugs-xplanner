package com.technoetic.xplanner.tags;

import com.technoetic.xplanner.domain.UserStory;
import com.technoetic.xplanner.domain.repository.UserStoryRepository;
import com.technoetic.xplanner.security.AuthenticationException;
import net.sf.hibernate.HibernateException;

import java.util.ArrayList;
import java.util.List;

public class StoryOptionsTag extends OptionsTag
{
   private int actualStoryId;

   public void setActualStoryId(int actualStoryId) {
      this.actualStoryId = actualStoryId;
   }

   protected UserStoryRepository getUserStoryRepository() {
        return new UserStoryRepository(getSession(), getAuthorizer(), getLoggedInUserId());
    }

    protected List getOptions() throws HibernateException, AuthenticationException {
        UserStoryRepository userStoryRepository = getUserStoryRepository();
       List stories = userStoryRepository.fetchStoriesWeCanMoveTasksTo(actualStoryId);
        List options = new ArrayList();
        for (int i = 0; i < stories.size(); i++) {
            UserStory s = (UserStory) stories.get(i);
            options.add(new StoryModel(new IterationModel(userStoryRepository.getIteration(s)), s));
        }
        return options;
    }
}
