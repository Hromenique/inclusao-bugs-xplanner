package com.technoetic.xplanner.actions;

import java.util.HashSet;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;

import com.technoetic.xplanner.domain.DomainObject;
import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.domain.Task;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.domain.UserStory;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;

public class StoryContinuer extends Continuer {
   private TaskContinuer taskContinuer;

   //DEBT 3LAYERCONTEXT remove dependency on request and move this class into the session context.
   public void init(Session session, HttpServletRequest request) throws AuthenticationException {
      init(session,
           (MessageResources) request.getAttribute(Globals.MESSAGES_KEY),
           SecurityHelper.getRemoteUserId(request));
      taskContinuer.init(session, (MessageResources) request.getAttribute(Globals.MESSAGES_KEY), currentUserId);
   }

   protected void doContinueObject(DomainObject fromObject, DomainObject toParent, DomainObject toObject)
         throws HibernateException {

      UserStory fromStory = (UserStory) fromObject;
      Iteration toIteration = (Iteration) toParent;
      UserStory toStory = (UserStory) toObject;

      fromStory.postponeRemainingHours();

      toStory.setIterationId(toIteration.getId());
      toStory.setEstimatedHours(fromStory.getTaskBasedRemainingHours());
      toStory.setDisposition(determineContinuedStoryDisposition(toIteration));
      toStory.setEstimatedOriginalHours(null);
      toStory.setTasks(new HashSet());
      continueTasks(fromStory, toStory, determineTaskDisposition(toIteration));
   }

   private TaskDisposition determineTaskDisposition(Iteration iteration) {
      return iteration.isActive() ? TaskDisposition.ADDED : TaskDisposition.CARRIED_OVER;
   }

   private void continueTasks(UserStory fromStory, UserStory toStory, TaskDisposition taskDisposition)
         throws HibernateException {
      Iterator taskIterator = fromStory.getTasks().iterator();
      taskContinuer.setDispositionOfContinuedTasks(taskDisposition);
      while (taskIterator.hasNext()) {
         Task task = (Task) taskIterator.next();
         if (!task.isCompleted()) {
            taskContinuer.continueObject(task, fromStory, toStory);
         }
      }
   }


   public StoryDisposition determineContinuedStoryDisposition(Iteration iteration) {
      return iteration.isActive() ? StoryDisposition.ADDED : StoryDisposition.CARRIED_OVER;
   }

   public void setTaskContinuer(TaskContinuer taskContinuer) {
      this.taskContinuer = taskContinuer;
   }

   public TaskContinuer getTaskContinuer() {
      return taskContinuer;
   }
}
