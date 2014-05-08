package com.technoetic.xplanner.actions;

import java.util.Iterator;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.DomainSpecificPropertiesFactory;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.Attribute;
import com.technoetic.xplanner.domain.DomainObject;
import com.technoetic.xplanner.domain.Person;
import com.technoetic.xplanner.domain.Project;
import com.technoetic.xplanner.domain.repository.ObjectRepository;
import com.technoetic.xplanner.forms.AbstractEditorForm;
import com.technoetic.xplanner.forms.ProjectEditorForm;
import com.technoetic.xplanner.wiki.WikiFormat;

/**
 * Created by IntelliJ IDEA.
 * User: sg897500
 * Date: Dec 2, 2004
 * Time: 3:26:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class EditProjectAction extends EditObjectAction {

   DomainSpecificPropertiesFactory domainSpecificPropertiesFactory;

   public void setDomainSpecificPropertiesFactory(DomainSpecificPropertiesFactory domainSpecificPropertiesFactory) {
      this.domainSpecificPropertiesFactory = domainSpecificPropertiesFactory;
   }

   protected ActionForward doExecute(ActionMapping actionMapping,
                                      ActionForm actionForm,
                                      HttpServletRequest request,
                                      HttpServletResponse reply) throws Exception {
        ProjectEditorForm pef = (ProjectEditorForm) actionForm;
        //DEBT Move the notification management actions to its own action: NotificationAction.add() & delete().
        if (pef.getAction() != null &&
            (pef.getAction().equals(UpdateTimeNotificationReceivers.ADD) ||
             pef.getAction().equals(UpdateTimeNotificationReceivers.DELETE))) {

            //return actionMapping.findForward("project/notification");
            return new ActionForward("/do/edit/project/notification", false);
        }
        return super.doExecute(actionMapping, actionForm, request, reply);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void saveForm(AbstractEditorForm form,
                            ActionMapping actionMapping,
                            HttpServletRequest request)
        throws Exception {
        String oid = form.getOid();
        Class objectClass = getObjectType(actionMapping, request);
        ObjectRepository objectRepository = getRepository(objectClass);
        DomainObject object;
        String action = form.getAction();
        if (action.equals(UPDATE_ACTION)) {
            object = (DomainObject) objectRepository.load(Integer.parseInt(oid));
            populateObject(request, object, form);
            objectRepository.update(object);
        } else if (action.equals(CREATE_ACTION)) {
            object = createObject(objectClass, request, form, objectRepository);
        } else {
            throw new ServletException("Unknown editor action: " + action);
        }
       setTargetObject(request, object);
        form.setAction(null);
        saveOrUpdateAttribute(XPlannerProperties.WIKI_URL_KEY,
                              object,
                              ((ProjectEditorForm) form).getWikiUrl());
        saveOrUpdateAttribute(XPlannerProperties.SEND_NOTIFICATION_KEY,
                              object,
                              (new Boolean(((ProjectEditorForm) form).isSendingMissingTimeEntryReminderToAcceptor())).toString());
        saveOrUpdateAttribute(WikiFormat.ESCAPE_BRACKETS_KEY,
                              object,
                              (new Boolean(((ProjectEditorForm) form).isOptEscapeBrackets())).toString());
    }

    private void saveOrUpdateAttribute(String attributeName,
                                       DomainObject object,
                                       String currentAttributeValue)
        throws Exception {
        String attr = object.getAttribute(attributeName);
        if (attr != null) {
            Attribute attribute = new Attribute(object.getId(),
                                                attributeName,
                                                currentAttributeValue);
            ThreadSession.get().update(attribute);
        } else {
           String existingAttributeValue = new XPlannerProperties().getProperty(attributeName);
            if (existingAttributeValue != null &&
                !existingAttributeValue.equals(currentAttributeValue) &&
                !currentAttributeValue.equals("")) {
                Attribute attribute = new Attribute(object.getId(),
                                                    attributeName,
                                                    currentAttributeValue);
                ThreadSession.get().save(attribute);
            }
            if (existingAttributeValue == null && !currentAttributeValue.equals("")) {
                Attribute attribute = new Attribute(object.getId(),
                                                    attributeName,
                                                    currentAttributeValue);
                ThreadSession.get().save(attribute);
            }
        }
    }

    protected void populateForm(AbstractEditorForm form, DomainObject object) throws Exception {
        super.populateForm(form, object);
        ProjectEditorForm pef = (ProjectEditorForm) form;

        Properties properties = domainSpecificPropertiesFactory.createPropertiesFor(object);
        pef.setWikiUrl(properties.getProperty(XPlannerProperties.WIKI_URL_KEY, "http://"));
        pef.setSendemail(
              Boolean.valueOf(properties.getProperty(XPlannerProperties.SEND_NOTIFICATION_KEY, "true")).booleanValue());
        pef.setOptEscapeBrackets(
              Boolean.valueOf(properties.getProperty(WikiFormat.ESCAPE_BRACKETS_KEY, "true")).booleanValue());
        Project project = (Project) object;
        ProjectEditorForm projectEditorForm = (ProjectEditorForm) form;
        Iterator itr = project.getNotificationReceivers().iterator();
        while (itr.hasNext()) {
            Person person = (Person) itr.next();
            projectEditorForm.addPersonInfo("" + person.getId(),
                                            person.getUserId(),
                                            person.getInitials(),
                                            person.getName());
        }

    }
}
