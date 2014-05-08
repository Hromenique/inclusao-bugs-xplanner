package com.technoetic.xplanner.actions;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.Session;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.UserStory;
import com.technoetic.xplanner.domain.repository.ObjectRepository;
import com.technoetic.xplanner.forms.AbstractEditorForm;
import com.technoetic.xplanner.forms.UserStoryEditorForm;

public class EditStoryAction extends EditObjectAction
{
   public static final String CONTINUED = "continued";
   public static final String MOVED = "moved";
   public static final String OPERATION_PARAM_KEY = "operation";
   public static final String ACTION_KEY = "action";

   public void beforeObjectCommit(Object object, Session session, ActionMapping actionMapping, ActionForm actionForm,
                                  HttpServletRequest request, HttpServletResponse reply) throws Exception
   {
      UserStory story = (UserStory) object;
      ObjectRepository iterationRepository = getRepository(Iteration.class);
      int iterationId = story.getIterationId();
      Iteration iteration = (Iteration) iterationRepository.load(iterationId);
      List<UserStory> userStories = iteration.getUserStories();
      if(!userStories.contains(story)){
    	  userStories.add(story);
      }
      iteration.modifyStoryOrder(DomainOrderer.buildStoryIdNewOrderArray(userStories));
      String action = request.getParameter(ACTION_KEY);
   }

   protected void populateForm(AbstractEditorForm form, ActionMapping actionMapping, HttpServletRequest request) throws Exception
   {
      super.populateForm(form, actionMapping, request);
      if (form.getOid() == null)
      {
         UserStoryEditorForm storyForm = (UserStoryEditorForm) form;
         int iterationId = Integer.parseInt(request.getParameter("fkey"));
         ObjectRepository iterationRepository = getRepository(Iteration.class);
         Iteration iteration = (Iteration) iterationRepository.load(iterationId);
         storyForm.setDispositionName(iteration.determineNewStoryDisposition().getName());
         storyForm.setOrderNo(iteration.getUserStories().size() + 1);
      }
   }

}