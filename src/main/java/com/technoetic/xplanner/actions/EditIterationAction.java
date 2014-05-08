package com.technoetic.xplanner.actions;

import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.charts.DataSampler;

import net.sf.hibernate.Session;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditIterationAction extends EditObjectAction {
    public static final String ACTION_KEY = "action";
    private DataSampler dataSampler;

   public DataSampler getDataSampler()
   {
      return dataSampler;
   }

   public void setDataSampler(DataSampler dataSampler)
   {
      this.dataSampler = dataSampler;
   }

    public void beforeObjectCommit(Object object, Session session, ActionMapping actionMapping, ActionForm actionForm,
                                   HttpServletRequest request, HttpServletResponse reply)
        throws Exception {
        Iteration iteration = (Iteration) object;
        String action = request.getParameter(ACTION_KEY);
        try {
            if (StringUtils.equals(action, EditObjectAction.CREATE_ACTION)) {
                iteration.setStatus(IterationStatus.INACTIVE);
                iteration.setDaysWorked(0.0d);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}