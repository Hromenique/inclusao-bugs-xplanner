package com.technoetic.xplanner.actions;

import com.technoetic.xplanner.db.NoteHelper;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.DomainObject;
import com.technoetic.xplanner.domain.Note;
import com.technoetic.xplanner.domain.repository.ObjectRepository;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: sg897500
 * Date: Nov 26, 2004
 * Time: 12:27:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeleteNoteAction extends DeleteObjectAction {

    public ActionForward doExecute(ActionMapping actionMapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse reply)
        throws Exception {
        Class objectClass = getObjectType(actionMapping, request);
        ObjectRepository repository = getRepository(objectClass);

        Note note = (Note) repository.load((new Integer(request.getParameter("oid")).intValue()));
        getEventBus().publishDeleteEvent(note, getLoggedInUser(request));
        NoteHelper.deleteNote(note, ThreadSession.get());

        String returnto = request.getParameter("returnto");
        return returnto != null ?
               new ActionForward(returnto, true)
               : actionMapping.findForward("view/projects");
    }


}
