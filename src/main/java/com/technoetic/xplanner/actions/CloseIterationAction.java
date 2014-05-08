package com.technoetic.xplanner.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.technoetic.xplanner.domain.*;
import com.technoetic.xplanner.domain.repository.ObjectRepository;
import com.technoetic.xplanner.forms.IterationStatusEditorForm;
import com.technoetic.xplanner.history.HistoricalEvent;
import com.technoetic.xplanner.history.HistorySupport;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.util.TimeGenerator;

import net.sf.hibernate.Session;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.*;
import org.apache.struts.action.ActionMapping;

public class CloseIterationAction extends AbstractIterationAction {
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
      closeIteration(request, session, iteration);
      String event = HistoricalEvent.ITERATION_CLOSED;
      HistorySupport.saveEvent(session,
                               iteration,
                               event,
                               null,
                               SecurityHelper.getRemoteUserId(request),
                               timeGenerator.getCurrentTime());
   }

    protected ActionForward doExecute(ActionMapping mapping,
                                      ActionForm actionForm,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        IterationStatusEditorForm form = (IterationStatusEditorForm) actionForm;
        if (StringUtils.isEmpty(form.getOid())) return mapping.getInputForward();

        String iterationId = form.getOid();
        ObjectRepository objectRepository = getRepository(Iteration.class);
        Iteration iteration = (Iteration) objectRepository.load(Integer.parseInt(iterationId));
        if (iteration.isActive()) {
           iteration.setStatus(IterationStatus.INACTIVE);
           setTargetObject(request, iteration);
        }
        objectRepository.update(iteration);
        String returnto = request.getParameter(EditObjectAction.RETURNTO_PARAM);
        ActionForward forward = mapping.findForward("onclose");
        //DEBT refactor the creation of the url get parameter to reuse what the LinkTag is doing.
        //DEBT We need to move to encapsulate how the links are built so we eventually "invoke" new screens like functions and not build link by hand
        return new ActionForward(forward.getPath() + "?iterationId=" + iterationId + "&" +
                                 returnto + "?oid=" + iterationId + "&fkey=" + iterationId,
                                 forward.getRedirect());
    }

    public void closeIteration(HttpServletRequest request, Session session, Iteration iteration)
        throws Exception {
        iteration.setStatus(IterationStatus.INACTIVE);
        dataSampler.generateClosingDataSamples(iteration);
    }}