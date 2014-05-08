package com.technoetic.xplanner.domain.ext;

import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.UserStory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

//DEBT: We may need to fold this into Iteration. See if there are other places that use this otherwise keep it this way
public class IterationTasks extends Iteration {

    Iteration iteration;

    public IterationTasks(Iteration iteration){
        this.iteration = iteration;
    }

    public Collection getIterationTasks(){
       Collection iterationTasks = new HashSet();
       Iterator storyIterator = iteration.getUserStories().iterator();
       while(storyIterator.hasNext()){
          UserStory userStory = (UserStory)storyIterator.next();
          Collection storyTasks = userStory.getTasks();
          iterationTasks.addAll(storyTasks);
       }
       return iterationTasks;
    }
}
