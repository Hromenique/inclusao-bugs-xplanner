package com.technoetic.xplanner.actions;

import com.technoetic.xplanner.metrics.IterationMetrics;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewIterationMetricsAction extends ViewObjectAction{
    private IterationMetrics iterationMetrics;

    protected ActionForward doExecute(ActionMapping actionMapping,
                                      ActionForm form,
                                      HttpServletRequest request,
                                      HttpServletResponse reply) throws Exception {
       //DEBT(SPRING) Should have been injected directly from the spring context file
        iterationMetrics.setIterationRepository(getRepository(actionMapping,request));
        iterationMetrics.analyze(Integer.parseInt(request.getParameter("oid")));
        request.setAttribute("metrics", iterationMetrics);
        return super.doExecute(actionMapping, form, request, reply);
    }

    public void setIterationMetrics(IterationMetrics iterationMetrics) {
        this.iterationMetrics = iterationMetrics;
    }
}
