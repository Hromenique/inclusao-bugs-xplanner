package com.technoetic.xplanner.actions;

import com.technoetic.xplanner.domain.Person;
import com.technoetic.xplanner.domain.Project;
import com.technoetic.xplanner.domain.repository.ObjectRepository;
import com.technoetic.xplanner.forms.ProjectEditorForm;
import net.sf.hibernate.Session;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Iterator;

/**
 * User: Mateusz Prokopowicz
 * Date: Dec 30, 2004
 * Time: 10:43:51 AM
 */
public class UpdateTimeNotificationReceivers extends AbstractAction {
    public static final String ADD = "addTimeNotification";
    public static final String DELETE = "delTimeNotification";
    private static final Logger log = Logger.getLogger("UpdateTimeAction");

    protected ActionForward doExecute(ActionMapping mapping,
                                      ActionForm actionForm,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        ProjectEditorForm form = (ProjectEditorForm) actionForm;
        try {
            Session session = getSession(request);
            try {
                ActionForward forward = null;
                if (!form.isSubmitted()) {
                    forward = new ActionForward(mapping.getInput());
                } else {
                    forward = doAction(session, form, request, mapping);
                }
                populateForm(session, form, request);
                return forward;
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


    private ActionForward doAction(Session session, ProjectEditorForm form,
                                   HttpServletRequest request, ActionMapping actionMapping)
        throws Exception {
        ObjectRepository projectRepository = getRepository(Project.class);
        ObjectRepository personRepository = getRepository(Person.class);
        Project project = (Project) projectRepository.load(Integer.parseInt(form.getOid()));
        Collection receivers = project.getNotificationReceivers();
        if (form.getAction().equals(ADD)) {
            if (form.getPersonToAddId() != null && !form.getPersonToAddId().equals("0")) {
                Person person = (Person) personRepository.load(
                    Integer.parseInt(form.getPersonToAddId()));
                receivers.add(person);
                project.setNotificationReceivers(receivers);
                form.setPersonToAddId("0");
            }
        } else if (form.getAction().equals(DELETE)) {
            if (form.getPersonToDelete() != null) {
                String personToDelId = form.getPersonToDelete();
                Person person = (Person) personRepository.load(Integer.parseInt(personToDelId));
                receivers.remove(person);
                form.setPersonToDelete(null);
            }
        } else {
            throw new ServletException("Unknown action: " + form.getAction());
        }
        return new ActionForward(actionMapping.getInput());
    }

    private void populateForm(Session session, ProjectEditorForm form,
                              HttpServletRequest request) throws Exception {
        Project project = (Project) session.load(Project.class, new Integer(form.getOid()));
        Iterator itr = project.getNotificationReceivers().iterator();
        while (itr.hasNext()) {
            Person person = (Person) itr.next();
            form.addPersonInfo("" + person.getId(),
                               person.getUserId(),
                               person.getInitials(),
                               person.getName());
        }
    }
}
