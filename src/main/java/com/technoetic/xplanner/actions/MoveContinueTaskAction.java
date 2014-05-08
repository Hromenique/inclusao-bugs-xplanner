package com.technoetic.xplanner.actions;

import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.Session;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.RelationshipConvertor;
import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.domain.Task;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.domain.UserStory;
import com.technoetic.xplanner.forms.MoveContinueTaskForm;
import com.technoetic.xplanner.history.HistoricalEvent;
import com.technoetic.xplanner.history.HistorySupport;
import com.technoetic.xplanner.security.SecurityHelper;

public class MoveContinueTaskAction extends EditObjectAction
{
    private final Logger log = Logger.getLogger(getClass());
    public static final RelationshipConvertor TARGET_STORY_ID_CONVERTOR =
          new RelationshipConvertor("targetStoryId", "story");
   public static final String CONTINUE_ACTION = "Continue";
   public static final String MOVE_ACTION = "Move";

   private TaskContinuer taskContinuer;

   public TaskContinuer getTaskContinuer()
   {
       return taskContinuer;
   }
   public void setTaskContinuer(TaskContinuer taskContinuer)
   {
       this.taskContinuer = taskContinuer;
   }

    public ActionForward execute(ActionMapping actionMapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse reply)
        throws Exception
    {
        MoveContinueTaskForm taskForm = (MoveContinueTaskForm) actionForm;
        Session session = getSession(request);
        try
        {
            if (taskForm.isSubmitted())
            {
                saveForm(taskForm, actionMapping, session, request, actionForm, reply);
                String returnto = request.getParameter(RETURNTO_PARAM);
                if (returnto != null)
                {
                    return new ActionForward(returnto, true);
                }
                else
                {
                    return actionMapping.findForward("view/projects");
                }
            }
            else
            {
                populateForm(taskForm, session);
                return new ActionForward(actionMapping.getInput());
            }
        }
        catch (Exception ex)
        {
            log.error("error during task move/continue action", ex);
            session.connection().rollback();
            throw new ServletException(ex);
        }
    }

    private void saveForm(MoveContinueTaskForm taskForm,
                          ActionMapping actionMapping,
                          Session session,
                          HttpServletRequest request,
                          ActionForm actionForm,
                          HttpServletResponse reply)
        throws Exception
    {
       MessageResources messageResources = (MessageResources) request.getAttribute(Globals.MESSAGES_KEY);
        String oid = taskForm.getOid();
        int targetStoryId = taskForm.getTargetStoryId();
        Class objectClass = getObjectType(actionMapping, request);
        Task task;
        if (taskForm.getAction().equals(CONTINUE_ACTION))
        {
           task = (Task) session.load(objectClass, new Integer(oid));
           taskContinuer.init(session, (MessageResources) request.getAttribute(Globals.MESSAGES_KEY), SecurityHelper.getRemoteUserId(request));
           UserStory fromStory = (UserStory) getRepository(UserStory.class).load(task.getStory().getId());
           UserStory toStory = (UserStory) getRepository(UserStory.class).load(taskForm.getTargetStoryId());
           taskContinuer.continueObject(task, fromStory, toStory);
        }
        else if (taskForm.getAction().equals(MOVE_ACTION))
        {
            task = (Task) session.load(objectClass, new Integer(oid));
            UserStory origStory = (UserStory) session.load(UserStory.class, new Integer(task.getStory().getId()));
            UserStory targetStory = (UserStory) session.load(UserStory.class, new Integer(targetStoryId));
            HistorySupport.saveEvent(session, task, HistoricalEvent.MOVED,
                                     messageResources.getMessage("task.moved.from.to", origStory.getName(), targetStory.getName()),
                                     SecurityHelper.getRemoteUserId(request), new Date());
            HistorySupport.saveEvent(session, origStory, HistoricalEvent.MOVED_OUT,
                                     messageResources.getMessage("task.moved.out.to", task.getName(), targetStory.getName()),
                                     SecurityHelper.getRemoteUserId(request), new Date());

           Iteration targetIteration = (Iteration) session.load(Iteration.class, new Integer(targetStory.getIterationId()));

           boolean isTheSameIteration = (origStory.getIterationId() == targetStory.getIterationId());

           if(!isTheSameIteration){
              if(targetIteration.isFuture() && !targetIteration.isActive()){
                 if(targetStory.getDisposition() == StoryDisposition.ADDED){
                    task.setDisposition(TaskDisposition.DISCOVERED);
                 } else if(targetStory.getDisposition() == StoryDisposition.CARRIED_OVER) {
                    task.setDisposition(TaskDisposition.CARRIED_OVER);
                 } else if(targetStory.getDisposition() == StoryDisposition.PLANNED) {
                    task.setDisposition(TaskDisposition.PLANNED);
                 }
              } else {
                 task.setDisposition(TaskDisposition.ADDED);
              }
           }

            TARGET_STORY_ID_CONVERTOR.populateDomainObject(task, taskForm);

            HistorySupport.saveEvent(session, targetStory, HistoricalEvent.MOVED_IN,
                                     messageResources.getMessage("task.moved.in.from", task.getName(), origStory.getName()),
                                     SecurityHelper.getRemoteUserId(request), new Date());
        }
        else
        {
            throw new ServletException("Unknown task editor form action: " + taskForm.getAction());
        }
        taskForm.setAction(null);
        session.flush();
        session.connection().commit();
        afterObjectCommit(actionMapping, actionForm, request, reply);
    }

    private void populateForm(MoveContinueTaskForm taskEditorForm, Session session)
        throws Exception
    {
        String oid = taskEditorForm.getOid();
        if (oid != null)
        {
            Task object = (Task) session.load(Task.class, new Integer(oid));
            populateForm(taskEditorForm, object);
        }
    }
}