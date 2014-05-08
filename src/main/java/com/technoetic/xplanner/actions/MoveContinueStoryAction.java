package com.technoetic.xplanner.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.RelationshipConvertor;
import com.technoetic.xplanner.domain.RelationshipMappingRegistry;
import com.technoetic.xplanner.domain.UserStory;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.forms.MoveContinueStoryForm;
import com.technoetic.xplanner.history.Historian;
import com.technoetic.xplanner.history.HistoricalEvent;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;

public class MoveContinueStoryAction extends EditObjectAction {
   public static final String CONTINUE_ACTION = "Continue";
   public static final String MOVE_ACTION = "Move";

   private StoryContinuer storyContinuer;

   public void setStoryContinuer(StoryContinuer storyContinuer) { this.storyContinuer = storyContinuer; }

   public StoryContinuer getStoryContinuer() { return storyContinuer; }

   public ActionForward doExecute(ActionMapping actionMapping,
                                  ActionForm actionForm,
                                  HttpServletRequest request,
                                  HttpServletResponse reply)
         throws Exception {
      Session session = getSession(request);
      MoveContinueStoryForm storyForm = (MoveContinueStoryForm) actionForm;
      if (storyForm.isSubmitted()) {
         saveForm(storyForm, session, request);
         String returnto = request.getParameter(RETURNTO_PARAM);
         if (returnto != null) {
            return new ActionForward(returnto, true);
         } else {
            return actionMapping.findForward("view/projects");
         }
      } else {
         populateForm(storyForm, session);
         return new ActionForward(actionMapping.getInput());
      }
   }

   private void saveForm(MoveContinueStoryForm storyForm, Session session, HttpServletRequest request)
         throws Exception {
      UserStory story = getStory(Integer.parseInt(storyForm.getOid()));
      populateObject(request, story, storyForm);
      Iteration originalIteration = getIteration(story.getIterationId());
      Iteration targetIteration = getIteration(storyForm.getTargetIterationId());

      if (storyForm.getAction().equals(MOVE_ACTION)) {
         moveStory(story, targetIteration, originalIteration, request, session);
      } else if (storyForm.getAction().equals(CONTINUE_ACTION)) {
         continueStory(story, originalIteration, targetIteration, request, session);
      } else {
         throw new ServletException("Unknown editor action: " + storyForm.getAction());
      }
      reorderIterationStories(originalIteration);
      reorderIterationStories(targetIteration);
      storyForm.setAction(null);
   }

   private void continueStory(UserStory story,
                              Iteration originalIteration,
                              Iteration targetIteration,
                              HttpServletRequest request, Session session
   ) throws AuthenticationException, HibernateException {
      storyContinuer.init(session, request);
      UserStory targetStory = (UserStory) storyContinuer.continueObject(story, originalIteration, targetIteration);
      updateStoryOrderNoInTargetIteration(targetStory, originalIteration, targetIteration);
   }

   private void moveStory(UserStory story,
                          Iteration targetIteration,
                          Iteration originalIteration,
                          HttpServletRequest request, Session session) throws AuthenticationException {
      originalIteration.getUserStories().remove(story);
      story.moveTo(targetIteration);
      updateStoryOrderNoInTargetIteration(story, originalIteration, targetIteration);
      saveMoveHistory(story, originalIteration, targetIteration, session, SecurityHelper.getRemoteUserId(request));
   }

   private UserStory getStory(int id) throws RepositoryException {
      return (UserStory) getRepository(UserStory.class).load(id);
   }

   private void reorderIterationStories(Iteration iteration) throws Exception {
      Collection stories = iteration.getUserStories();
      iteration.modifyStoryOrder(DomainOrderer.buildStoryIdNewOrderArray(stories));
   }

    private void saveMoveHistory(UserStory story,
                                 Iteration originIteration,
                                 Iteration targetIteration,
                                 Session session,
                                 int currentUserId) {
       Historian historian = new Historian(session, currentUserId);

       Date now = new Date();
       historian.saveEvent(story,
                           HistoricalEvent.MOVED,
                           "from " + originIteration.getName() + " to " + targetIteration.getName(),
                           now);
       historian.saveEvent(originIteration,
                           HistoricalEvent.MOVED_OUT,
                           story.getName() + " to " + targetIteration.getName(),
                           now);
       historian.saveEvent(targetIteration,
                           HistoricalEvent.MOVED_IN,
                           story.getName() + " from " + originIteration.getName(),
                           now);
    }

   private void updateStoryOrderNoInTargetIteration(UserStory story,
                                                    Iteration originalIteration,
                                                    Iteration targetIteration
   ) {
      if (originalIteration.getStartDate().compareTo(targetIteration.getStartDate()) <= 0) {
         story.setOrderNo(0);
      } else {
         story.setOrderNo(Integer.MAX_VALUE);
      }
   }

   private void populateForm(MoveContinueStoryForm storyForm, Session session)
         throws Exception {
      String oid = storyForm.getOid();
      if (oid != null) {
         UserStory story = (UserStory) session.load(UserStory.class, new Integer(oid));
         Iteration iteration = (Iteration) session.load(Iteration.class, new Integer(story.getIterationId()));
         storyForm.setFutureIteration(iteration.isFuture());
         PropertyUtils.copyProperties(storyForm, story);
         populateManyToOneIds(storyForm, story);
      }
   }

   private void populateManyToOneIds(ActionForm form, UserStory story)
         throws IllegalAccessException,
                NoSuchMethodException,
                InvocationTargetException {
      Collection mappings = RelationshipMappingRegistry.getInstance().getRelationshipMappings(story);
      for (Iterator iterator = mappings.iterator(); iterator.hasNext();) {
         RelationshipConvertor convertor = (RelationshipConvertor) iterator.next();
         convertor.populateAdapter(form, story);
      }
   }

}

