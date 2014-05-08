package com.technoetic.xplanner.actions;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.technoetic.xplanner.domain.DomainObject;
import com.technoetic.xplanner.domain.RelationshipConvertor;
import com.technoetic.xplanner.domain.RelationshipMappingRegistry;
import com.technoetic.xplanner.domain.repository.ObjectRepository;
import com.technoetic.xplanner.forms.AbstractEditorForm;
import com.technoetic.xplanner.util.LogUtil;

public class EditObjectAction<T> extends AbstractAction {
   protected static final Logger log = LogUtil.getLogger();

   public static final String UPDATE_ACTION = "Update";
    public static final String CREATE_ACTION = "Create";

    public static final String RETURNTO_PARAM = "returnto";
    public static final String MERGE_PARAM = "merge";

    @Override
	protected ActionForward doExecute(ActionMapping actionMapping,
                                      ActionForm actionForm,
                                      HttpServletRequest request,
                                      HttpServletResponse reply)
            throws Exception {
        AbstractEditorForm form = (AbstractEditorForm)actionForm;
        if (form.isSubmitted()) {
            saveForm(form, actionMapping, request);
            setCookies(form, actionMapping, request, reply);
            String returnto = request.getParameter(RETURNTO_PARAM);
            return returnto != null ? new ActionForward(returnto, true) : actionMapping.findForward("view/projects");
        } else {
            populateForm(form, actionMapping, request);
            return new ActionForward(actionMapping.getInput());
        }
    }

   protected void setCookies(AbstractEditorForm form,
                             ActionMapping mapping,
                             HttpServletRequest request,
                             HttpServletResponse response)
   {
   }

   protected void saveForm(AbstractEditorForm form, ActionMapping actionMapping, HttpServletRequest request)
            throws Exception {
        String oid = form.getOid();
        Class objectClass = getObjectType(actionMapping, request);
        ObjectRepository objectRepository = getRepository(objectClass);
        DomainObject object;
        String action = form.getAction();
        if (action.equals(UPDATE_ACTION)) {
            object = updateObject(oid, request, form, objectRepository);
        } else if (action.equals(CREATE_ACTION)) {
            object = createObject(objectClass, request, form, objectRepository);
        } else {
            throw new ServletException("Unknown editor action: " + action);
        }
      setTargetObject(request, object);
        form.setAction(null);
    }

   protected DomainObject updateObject(String oid,
                                     HttpServletRequest request,
                                     AbstractEditorForm form,
                                     ObjectRepository objectRepository) throws Exception {DomainObject object;
      object = (DomainObject)objectRepository.load(Integer.parseInt(oid));
      getEventBus().publishUpdateEvent(form, object, getLoggedInUser(request));
      populateObject(request, object, form);
      objectRepository.update(object);
      return object;
   }

   protected final DomainObject createObject(Class<? extends DomainObject> objectClass,
                                       HttpServletRequest request,
                                       AbstractEditorForm form,
                                       ObjectRepository objectRepository)
       throws Exception
   {
       DomainObject object = objectClass.newInstance();
       
       populateObject(request, object, form);
       int savedObjectId = objectRepository.insert(object);
       form.setId(savedObjectId);
       getEventBus().publishCreateEvent(object, getLoggedInUser(request));
       return object;
   }

   protected void populateForm(AbstractEditorForm form, ActionMapping actionMapping, HttpServletRequest request)
        throws Exception {
        String oid = form.getOid();
        if (oid != null) {
            Class objectClass = getObjectType(actionMapping, request);
            ObjectRepository objectRepository = getRepository(objectClass);
            DomainObject object = (DomainObject) objectRepository.load(Integer.parseInt(oid));
            populateForm(form, object);
        }
    }

    protected void populateForm(AbstractEditorForm form, DomainObject object) throws Exception {
        copyProperties(form, object);
        populateManyToOneIds(form, object);
    }

    protected final void copyProperties(Object destination, Object source) throws Exception {
        BeanInfo info = Introspector.getBeanInfo(source.getClass());
        PropertyDescriptor[] properties = info.getPropertyDescriptors();
        for (int i = 0; i < properties.length; i++) {
            PropertyDescriptor sourceProperty = properties[i];
            PropertyDescriptor destinationProperty = findProperty(destination, sourceProperty.getName());
            if (destinationProperty != null && destinationProperty.getWriteMethod() != null &&
                sourceProperty.getReadMethod() != null) {
               Object value = sourceProperty.getReadMethod().invoke(source, new Object[0]);
               log.debug("  " + destinationProperty.getName() + "=" + value);
               destinationProperty.getWriteMethod().invoke(destination,
                                                           new Object[]{ value});
            }
        }
    }

    private PropertyDescriptor findProperty(Object object, String name) throws IntrospectionException {
        BeanInfo info = Introspector.getBeanInfo(object.getClass());
        PropertyDescriptor[] properties = info.getPropertyDescriptors();
        for (int i = 0; i < properties.length; i++) {
            PropertyDescriptor property = properties[i];
            if (property.getName().equals(name)) {
                return property;
            }
        }
        return null;
    }

    protected void populateManyToOneIds(ActionForm form, DomainObject object) throws IllegalAccessException,
                                                                                     NoSuchMethodException,
                                                                                     InvocationTargetException {
        Collection mappings = RelationshipMappingRegistry.getInstance().getRelationshipMappings(object);
        for (Iterator iterator = mappings.iterator(); iterator.hasNext();) {
            RelationshipConvertor convertor = (RelationshipConvertor) iterator.next();
            convertor.populateAdapter(form, object);
        }
    }

    protected void populateObject(HttpServletRequest request, Object object, ActionForm form) throws Exception {
       log.debug("Populating object " + object.getClass().getName() + " " + ((DomainObject)object).getId());
       if ("true".equals(request.getParameter(MERGE_PARAM))) {
          RequestUtils.populate(object, request);
          //TODO: should we populate many-to-one rels in this mode?
       } else {
    	  copyProperties(object, form);
          populateManyToOneRelationships((DomainObject) object, form);
       }
    }
    protected void populateManyToOneRelationships(DomainObject object, ActionForm form)
        throws Exception{
        Collection mappings = RelationshipMappingRegistry.getInstance().getRelationshipMappings(object);
        for (Iterator iterator = mappings.iterator(); iterator.hasNext();) {
            RelationshipConvertor convertor = (RelationshipConvertor) iterator.next();
            convertor.populateDomainObject(object, form);
        }
    }
}
