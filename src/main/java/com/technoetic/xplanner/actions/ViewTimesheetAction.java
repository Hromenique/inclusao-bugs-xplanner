package com.technoetic.xplanner.actions;

import com.technoetic.xplanner.db.PersonTimesheetQuery;
import com.technoetic.xplanner.forms.PersonTimesheetForm;
import net.sf.hibernate.Session;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;

public class ViewTimesheetAction extends AbstractAction {
    private static final Logger log = Logger.getLogger("ViewTimesheetAction");

    protected ActionForward doExecute(ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest request,
            HttpServletResponse reply) throws Exception {
        PersonTimesheetForm form = (PersonTimesheetForm)actionForm;
        try {
            Session session = getSession(request);
            try {
                PersonTimesheetQuery query = new PersonTimesheetQuery(getSession(request));
                query.setPersonId(form.getPersonId());
                query.setStartDate(form.getStartDate());
                query.setEndDate(form.getEndDate());
                form.setTimesheet(query.getTimesheet());
                if (form.getDateFormat() == null) {
                    String format = getResources(request).getMessage("format.date");
                    form.setDateFormat(new SimpleDateFormat(format));
                }

                return actionMapping.findForward("view/timesheet");
            } catch (Exception ex) {
                session.connection().rollback();
                log.error("error", ex);
                throw new ServletException(ex);
            }
        } catch (ServletException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("error", ex);
            throw new ServletException(ex);
        }
    }
}
