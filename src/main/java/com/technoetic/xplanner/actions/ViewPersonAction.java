package com.technoetic.xplanner.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

import com.technoetic.xplanner.domain.repository.TaskRepository;
import com.technoetic.xplanner.domain.repository.StoryRepository;

/**
 * User: Mateusz Prokopowicz
 * Date: Jul 5, 2005
 * Time: 2:45:56 PM
 */
public class ViewPersonAction extends ViewObjectAction{
   TaskRepository taskRepository;
   StoryRepository storyRepository;

   public void setStoryRepository(StoryRepository storyRepository) {
      this.storyRepository = storyRepository;
   }

   public void setTaskRepository(TaskRepository taskRepository) {
      this.taskRepository = taskRepository;
   }

   protected ActionForward doExecute(ActionMapping actionMapping,
                                     ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse reply) throws Exception {
       
      int personId = Integer.parseInt(request.getParameter("oid"));
      
      // grab a list of all active tasks
      Collection tasks = taskRepository.getCurrentTasksForPerson(personId);
      
      // use the utility methods on the task repository to filter the results
      request.setAttribute("currentActiveTasksForPerson", taskRepository.getCurrentActiveTasks(tasks));
      request.setAttribute("currentPendingTasksForPerson", taskRepository.getCurrentPendingTasks(tasks));
      request.setAttribute("currentCompletedTasksForPerson", taskRepository.getCurrentCompletedTasks(tasks));
      request.setAttribute("futureTasksForPerson", taskRepository.getFutureTasksForPerson(personId));
      request.setAttribute("storiesForCustomer", storyRepository.getStoriesForPersonWhereCustomer(personId));
      request.setAttribute("storiesForTracker", storyRepository.getStoriesForPersonWhereTracker(personId));
      
      return super.doExecute(actionMapping, form, request, reply);
   }
}
