package com.technoetic.xplanner.actions;

import com.technoetic.xplanner.domain.DomainObject;
import com.technoetic.xplanner.domain.repository.ObjectRepository;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteObjectAction extends AbstractAction {
    public ActionForward doExecute(ActionMapping actionMapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse reply)
            throws Exception {
        Class objectClass = getObjectType(actionMapping, request);
        ObjectRepository repository = getRepository(objectClass);
        int objectIdentifier = Integer.parseInt(request.getParameter("oid"));
        getEventBus().publishDeleteEvent((DomainObject) repository.load(objectIdentifier), getLoggedInUser(request));
		repository.delete(objectIdentifier);
        String returnto = request.getParameter("returnto");
        return returnto != null ?
                new ActionForward(returnto, true)
                : actionMapping.findForward("view/projects");
    }
}
