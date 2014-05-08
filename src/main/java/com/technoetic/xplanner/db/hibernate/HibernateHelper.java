package com.technoetic.xplanner.db.hibernate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import javax.servlet.ServletRequest;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.connection.ConnectionProviderFactory;
import net.sf.hibernate.dialect.Dialect;
import net.sf.hibernate.util.DTDEntityResolver;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.actions.XPlannerInitializationPlugin;
import com.technoetic.xplanner.util.LogUtil;

/** @noinspection ClassNamePrefixedWithPackageName*/
public class HibernateHelper {
   private static final Logger LOG = LogUtil.getLogger();
   private static final String SESSION_ATTRIBUTE_KEY = "HibernateSession";

  private HibernateHelper() {}

  public static void initializeHibernate() throws HibernateException {
     if (GlobalSessionFactory.get() == null) {
        Configuration cfg = initializeConfiguration();
        SessionFactory sessionFactory = cfg.buildSessionFactory();
        // XPlanner uses a custom session factory so that all sessions will be
        // automatically configured with an XPlanner-related Hibernate interceptor.
        GlobalSessionFactory.set(new XPlannerSessionFactory(sessionFactory));
     }
  }

   public static SessionFactory getSessionFactory()
      throws HibernateException
   {
      initializeHibernate();
      return GlobalSessionFactory.get();
   }

   public static Configuration initializeConfiguration() throws HibernateException {
      Transformer transformer = null;
      Properties properties = getProperties();
        try {
            if (properties.containsKey("xplanner.hibernate.mappingtransform")) {
                String transformerFileName = properties.getProperty("xplanner.hibernate.mappingtransform");
                LOG.info("Using Hibernate mapping transformer: " + transformerFileName);
                transformer = createTransformer(transformerFileName);
            }
            Configuration cfg = new Configuration();
            cfg.addInputStream(getMappingStream("mappings/Attribute.xml", transformer));
            cfg.addInputStream(getMappingStream("mappings/Permission.xml", transformer));
            cfg.addInputStream(getMappingStream("mappings/RoleAssociation.xml", transformer));
            cfg.addInputStream(getMappingStream("mappings/DataSample.xml", transformer));
            cfg.addInputStream(getMappingStream("mappings/Project.xml", transformer));
            cfg.addInputStream(getMappingStream("mappings/Iteration.xml", transformer));
            cfg.addInputStream(getMappingStream("mappings/UserStory.xml", transformer));
            cfg.addInputStream(getMappingStream("mappings/Task.xml", transformer));
            cfg.addInputStream(getMappingStream("mappings/TimeEntry.xml", transformer));
            cfg.addInputStream(getMappingStream("mappings/Integration.xml", transformer));
            cfg.addInputStream(getMappingStream("mappings/Note.xml", transformer));
            cfg.addInputStream(getMappingStream("mappings/Role.xml", transformer));
            cfg.addInputStream(getMappingStream("mappings/Person.xml", transformer));
            cfg.addInputStream(getMappingStream("mappings/HistoricalEvent.xml", transformer));
            cfg.addInputStream(getMappingStream("mappings/File.xml", transformer));
            cfg.addInputStream(getMappingStream("mappings/Directory.xml", transformer));
            cfg.addInputStream(getMappingStream("mappings/Metrics.xml", transformer));
//FEATURE:
//            cfg.addInputStream(getMappingStream("mappings/Feature.xml", transformer));
            cfg.addProperties(properties);
            EHCacheHelper.configure(cfg);
            return cfg;
        } catch (Exception e) {
            throw new HibernateException(e);
        }
    }

  public static Properties getProperties() {return new XPlannerProperties().get();}

  private static Transformer createTransformer(String path) throws TransformerConfigurationException {
      InputStream xsltStream = XPlannerInitializationPlugin.class.getClassLoader().getResourceAsStream(path);
      Source xsltSource = new StreamSource(xsltStream);
      TransformerFactory tf = TransformerFactory.newInstance();
      Templates transformation = tf.newTemplates(xsltSource);
      return transformation.newTransformer();
  }

    private static InputStream getMappingStream(String path, Transformer transformer) throws TransformerException, SAXException {
        InputStream in = XPlannerInitializationPlugin.class.getClassLoader().getResourceAsStream(path);
        if (transformer != null) {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setEntityResolver(new DTDEntityResolver());
            SAXSource saxSource = new SAXSource(reader, new InputSource(in));
            ByteArrayOutputStream transformedStream = new ByteArrayOutputStream();
            transformer.transform(saxSource, new StreamResult(transformedStream));
            return new ByteArrayInputStream(transformedStream.toByteArray());
        } else {
            return in;
        }
    }

    public static Session getSession(ServletRequest request) {
       Session session = (Session)request.getAttribute(SESSION_ATTRIBUTE_KEY);
       if (LOG.isDebugEnabled()) {
         if (session != ThreadSession.get()) {
           LOG.info("Session storage mismatch thread=" + ThreadSession.get() + " HBHelper=" + session);
         }
       }
       return session;
    }

    public static void setSession(ServletRequest request, Session session) {
        request.setAttribute(SESSION_ATTRIBUTE_KEY, session);
    }

  public static Dialect getDialect() throws HibernateException {
    return Dialect.getDialect(getProperties());
  }

  static public Connection getConnection() {
    try {
      Properties properties = new XPlannerProperties().get();
      return ConnectionProviderFactory.newConnectionProvider(properties).getConnection();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
