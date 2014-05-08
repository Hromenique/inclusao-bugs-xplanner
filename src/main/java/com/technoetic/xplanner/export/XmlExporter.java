package com.technoetic.xplanner.export;

import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.Person;
import com.technoetic.xplanner.domain.Project;
import com.technoetic.xplanner.domain.Role;
import com.technoetic.xplanner.domain.Task;
import com.technoetic.xplanner.domain.TimeEntry;
import com.technoetic.xplanner.domain.UserStory;
import net.sf.hibernate.Session;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.expression.Context;
import org.apache.commons.betwixt.expression.Expression;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.strategy.DecapitalizeNameMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class XmlExporter implements Exporter {
    private Logger log = Logger.getLogger(getClass());

    public void initializeHeaders(HttpServletResponse response) {
        response.setHeader("Content-type", "text/xml");
        response.setHeader("Content-disposition", "inline; filename=export.xml");
    }

    public byte[] export(Session session, Object object) throws ExportException {
        try {
            StringWriter outputWriter = new StringWriter();
            outputWriter.write("<?xml version='1.0' ?>");
            BeanWriter beanWriter = new BeanWriter(outputWriter);
            beanWriter.getXMLIntrospector().setAttributesForPrimitives(false);
            beanWriter.setWriteIDs(false);
            beanWriter.enablePrettyPrint();

            beanWriter.getXMLIntrospector().setElementNameMapper(new DecapitalizeNameMapper());
            configureBindings(beanWriter);
            installCircularRelationshipsHack(beanWriter);
            ElementDescriptor descriptor = getElementDescriptor(
                    beanWriter.getXMLIntrospector(), XPlannerData.class, "objects");
            String collectionName = "objects";
            if (object instanceof Project) {
                collectionName = "projects";
            } else if (object instanceof Iteration) {
                collectionName = "iterations";
            }
            descriptor.setLocalName(collectionName);
            List people = session.find("from person in class " + Person.class.getName());
            XPlannerData data = new XPlannerData();
            data.setPeople(people);
            data.setObjects(new ArrayList());
            data.getObjects().add(object);

            beanWriter.write("xplanner", data);
            return outputWriter.toString().getBytes();
        } catch (Exception e) {
            log.error("error formatting XML export", e);
            return null;
        }
    }

    // todo - Find a way to eliminate this hack as Hibernate dependencies are added.
    private void installCircularRelationshipsHack(BeanWriter beanWriter) throws IntrospectionException {
        installRelationshipMapper(beanWriter, Task.class, "story", "story.id");
//        installRelationshipMapper(beanWriter, Iteration.class, "project", "project.id");
    }

    private void installRelationshipMapper(BeanWriter beanWriter, Class aClass, String property, final String propertyPath) throws IntrospectionException {
        ElementDescriptor taskParentDescriptor = getElementDescriptor(
                beanWriter.getXMLIntrospector(), aClass, property);
        taskParentDescriptor.setContextExpression(new Expression() {
            public Object evaluate(Context context) {
                try {
                    return PropertyUtils.getProperty(context.getBean(), propertyPath);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            public void update(Context context, String s) {
                // no op
            }
        });
    }

    private ElementDescriptor getElementDescriptor(XMLIntrospector xmlIntrospector, Class beanClass,
            String property) throws IntrospectionException {
        ElementDescriptor[] descriptors = xmlIntrospector.introspect(beanClass).getElementDescriptor().getElementDescriptors();
        ElementDescriptor descriptor = null;
        for (int i = 0; i < descriptors.length; i++) {
            if (descriptors[i].getPropertyName().equals(property)) {
                descriptor = descriptors[i];
            }
        }
        return descriptor;
    }

    private void configureBindings(BeanWriter beanWriter) throws IntrospectionException {
        XMLBeanInfo beanInfo;
        beanInfo = configureBeanInfo(beanWriter, Project.class);
        hideProperty(beanInfo, "currentIteration");
        beanInfo = configureBeanInfo(beanWriter, Iteration.class);
        beanInfo = configureBeanInfo(beanWriter, UserStory.class);
        beanInfo = configureBeanInfo(beanWriter, Task.class);
        beanInfo = configureBeanInfo(beanWriter, TimeEntry.class);
        beanInfo = configureBeanInfo(beanWriter, Person.class);
        hideProperty(beanInfo, "lastUpdateTime");
        hideProperty(beanInfo, "password");
        beanInfo = configureBeanInfo(beanWriter, Role.class);
        hideProperty(beanInfo, "personId");
        hideProperty(beanInfo, "id");
    }

    private XMLBeanInfo configureBeanInfo(BeanWriter beanWriter, Class beanClass) throws IntrospectionException {
        XMLBeanInfo beanInfo;
        beanInfo = beanWriter.getXMLIntrospector().introspect(beanClass);
        hideProperty(beanInfo, "lastUpdateTime");
        return beanInfo;
    }

//    private void renderAsAttribute(XMLBeanInfo beanInfo, String property) {
//        hideProperty(beanInfo, property);
//        AttributeDescriptor[] originalAttributeDescriptors = beanInfo.getElementDescriptor().getAttributeDescriptors();
//        AttributeDescriptor[] attributeDescriptors = new AttributeDescriptor[originalAttributeDescriptors.length+1];
//        System.arraycopy(originalAttributeDescriptors, 0, attributeDescriptors, 0, originalAttributeDescriptors.length);
//        AttributeDescriptor attributeDescriptor = new AttributeDescriptor(property);
//        attributeDescriptor.setPropertyName(property);
//        attributeDescriptor.setPropertyType(Object.class);
//        BeanInfo bi = null;
//        try {
//            bi = Introspector.getBeanInfo(beanInfo.getBeanClass());
//        } catch (IntrospectionException e) {
//            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
//        }
//        PropertyDescriptor propertyDescriptor = null;
//        PropertyDescriptor propertyDescriptors[] = bi.getPropertyDescriptors();
//        for (int i = 0; i < propertyDescriptors.length; i++) {
//            if (propertyDescriptors[i].fromNameKey().equals(property)) {
//                 propertyDescriptor = propertyDescriptors[i];
//                break;
//            }
//        }
//        attributeDescriptor.setTextExpression(new MethodExpression(propertyDescriptor.getReadMethod()));
//        attributeDescriptors[originalAttributeDescriptors.length] = attributeDescriptor;
//        beanInfo.getElementDescriptor().setAttributeDescriptors(attributeDescriptors);
//
//    }

    private static void hideProperty(XMLBeanInfo beanInfo, final String property) {
        ArrayList elementDescriptors = new ArrayList();
        ElementDescriptor elementDescriptor = beanInfo.getElementDescriptor();
        CollectionUtils.addAll(elementDescriptors, elementDescriptor.getElementDescriptors());
        elementDescriptors.remove(CollectionUtils.find(elementDescriptors, new Predicate() {
            public boolean evaluate(Object o) {
                return ((ElementDescriptor)o).getPropertyName().equals(property);
            }
        }));
        elementDescriptor.setElementDescriptors((ElementDescriptor[])elementDescriptors.
                toArray(new ElementDescriptor[elementDescriptors.size()]));
    }

    public static class XPlannerData {
        private List people;
        private List objects;

        public List getPeople() {
            return people;
        }

        public void setPeople(List people) {
            this.people = people;
        }

        public void setObjects(List objects) {
            this.objects = objects;
        }

        public List getObjects() {
            return objects;
        }
    }
}
