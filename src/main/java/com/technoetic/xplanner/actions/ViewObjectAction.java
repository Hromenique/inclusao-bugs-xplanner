package com.technoetic.xplanner.actions;

import com.technoetic.xplanner.domain.repository.ObjectRepository;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewObjectAction extends AbstractAction {
    private boolean authorizationRequired = true;

    protected ActionForward doExecute(ActionMapping actionMapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse reply)
            throws Exception {
        ObjectRepository objectRepository = getRepository(actionMapping, request);
        String forwardPath = getForwardPath(actionMapping, request);
        if (isSecure(actionMapping)) {
            Object object = objectRepository.load(Integer.parseInt(request.getParameter("oid")));
            setDomainContext(request, object, actionMapping);
        }
        return new ActionForward(forwardPath);
    }

    private String getForwardPath(ActionMapping actionMapping, HttpServletRequest request)
        throws UnsupportedEncodingException {
        String forwardPath = actionMapping.findForward("display").getPath();
        String returnto = request.getParameter("returnto");
        if (returnto != null) {
            forwardPath +=
                    (forwardPath.indexOf("?") != -1 ? "&" : "?") +
                    "returnto=" + URLEncoder.encode(returnto, "UTF-8");
        }
        return forwardPath;
    }

    protected ObjectRepository getRepository(ActionMapping actionMapping,
                                             HttpServletRequest request)
        throws ClassNotFoundException, ServletException {
        Class objectClass = getObjectType(actionMapping, request);
        ObjectRepository objectRepository = getRepository(objectClass);
        return objectRepository;
    }

    private boolean isSecure(ActionMapping actionMapping) {
        return authorizationRequired;
    }

    public void setAuthorizationRequired(boolean authorizationRequired) {
        this.authorizationRequired = authorizationRequired;
    }
}