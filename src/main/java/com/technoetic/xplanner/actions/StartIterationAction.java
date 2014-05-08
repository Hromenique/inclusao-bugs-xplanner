package com.technoetic.xplanner.actions;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.Session;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.charts.DataSampler;
import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.Project;
import com.technoetic.xplanner.domain.repository.ObjectRepository;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.forms.IterationStatusEditorForm;
import com.technoetic.xplanner.forms.TimeEditorForm;
import com.technoetic.xplanner.history.HistoricalEvent;
import com.technoetic.xplanner.history.HistorySupport;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.util.RequestUtils;
import com.technoetic.xplanner.util.TimeGenerator;

public class StartIterationAction extends AbstractIterationAction {
   private TimeGenerator timeGenerator;

   public void setTimeGenerator(TimeGenerator timeGenerator) {
      this.timeGenerator = timeGenerator;
   }

   public void beforeObjectCommit(Object object,
                                  Session session,
                                  ActionMapping actionMapping,
                                  ActionForm actionForm,
                                  HttpServletRequest request,
                                  HttpServletResponse reply) throws Exception {
      Iteration iteration = (Iteration) object;
      iteration.start();
      dataSampler.generateOpeningDataSamples(iteration);
      HistorySupport.saveEvent(session,
                               iteration,
                               HistoricalEvent.ITERATION_STARTED,
                               null,
                               SecurityHelper.getRemoteUserId(request),
                               timeGenerator.getCurrentTime());
   }

    protected ActionForward doExecute(ActionMapping mapping,
                                      ActionForm actionForm,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
       IterationStatusEditorForm form = (IterationStatusEditorForm) actionForm;
       ObjectRepository iterationRepository = getRepository(Iteration.class);
       ObjectRepository projectRepository = getRepository(Project.class);
       Iteration iteration;

       ActionForward nextPage = mapping.getInputForward();
       if(!form.isIterationStartConfirmed()) {
          if(UpdateTimeAction.isFromUpdateTime(request)){
             //DEBT Extract constants for all these strings. from_edit/time -> UpdateTimeAction, edit/iteration -> this class
             iteration = (Iteration)request.getAttribute("edit/iteration");
          } else {
             String iterationId = form.getOid();
             iteration = (Iteration) iterationRepository.load(Integer.parseInt(iterationId));
          }
          if (iteration != null) {
             request.setAttribute("edit/iteration", iteration);
             Project project = (Project) projectRepository.load(iteration.getProjectId());
             Collection startedIterationsList = getStartedIterations(project);
             int startedIterationsCount = startedIterationsList.size();
             request.setAttribute("startedIterationsNr", new Integer(startedIterationsCount));
             if (startedIterationsCount > 0) {
                //DEBT These should not be strings but constant used both in java code and jsp.
                nextPage = mapping.findForward("start/iteration");
             } else if (!UpdateTimeAction.isFromUpdateTime(request)){
                iteration.close();
                iterationRepository.update(iteration);
                setTargetObject(request, iteration);
                String returnto = request.getParameter(EditObjectAction.RETURNTO_PARAM);
                nextPage = returnto != null ? new ActionForward(returnto, true) : mapping.findForward("view/projects");
             }
          }
       } else {
          String iterationId = form.getOid();
          iteration = (Iteration) iterationRepository.load(Integer.parseInt(iterationId));
          if(form.isCloseIterations()){
             Project project = (Project) projectRepository.load(iteration.getProjectId());
             closeStartedIterations(project, iterationRepository);
          }
          iteration.close();
          iterationRepository.update(iteration);

          setTargetObject(request, iteration);

          // DEBT: Use the normal returnto mechanism to send the control back to edit/time w/o embedding knowledge of it inside this action
          if (RequestUtils.isParameterTrue(request, IterationStatusEditorForm.SAVE_TIME_ATTR)) {
             request.setAttribute(TimeEditorForm.WIZARD_MODE_ATTR, Boolean.TRUE);
             nextPage = mapping.findForward("edit/time");
          } else {
             String returnto = request.getParameter(EditObjectAction.RETURNTO_PARAM);
             nextPage = returnto != null ? new ActionForward(returnto, true) : mapping.findForward("view/projects");
          }
       }
       return nextPage;
    }

   private void closeStartedIterations(Project project, ObjectRepository iterationObjectRepository) throws RepositoryException {
      Collection startedIterationsList = getStartedIterations(project);
      Iterator iterator = startedIterationsList.iterator();
      while(iterator.hasNext()){
         Iteration iteration = (Iteration) iterator.next();
         iteration.close();
         iterationObjectRepository.update(iteration);
      }
   }

   private Collection getStartedIterations(Project project) {
     Collection startedIterationList = new ArrayList();
     Collection iterationList = project.getIterations();
     Iterator iterator = iterationList.iterator();
      while(iterator.hasNext()){
         Iteration iteration = (Iteration)iterator.next();
         if(iteration.isActive()){
            startedIterationList.add(iteration);
         }
     }
     return startedIterationList;
  }


    public void setDataSampler(DataSampler dataSampler)
    {
        this.dataSampler = dataSampler;
    }

   public DataSampler getDataSampler()
   {
      return dataSampler;
   }
}