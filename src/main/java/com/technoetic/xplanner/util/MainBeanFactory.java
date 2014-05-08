package com.technoetic.xplanner.util;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.beans.BeansException;
import org.springframework.core.io.ClassPathResource;
import org.apache.log4j.Logger;

public class MainBeanFactory
{
   public static BeanFactory factory = createDefaultFactory();
   public static final String DEFAULT_BEANS_REGISTRY_PATH = "/spring-beans.xml";
   protected static final Logger LOG = LogUtil.getLogger();

   private static XmlBeanFactory createDefaultFactory()
   {
      try
      {
         return new XmlBeanFactory(new ClassPathResource(DEFAULT_BEANS_REGISTRY_PATH));
      }
      catch (Throwable e)
      {
         e.printStackTrace();
         return null;
      }
   }

   public static void reset()
   {
      factory = createDefaultFactory();
   }

   public static Object getBean(Class type)
   {
      return getBean(type.getName());
   }

   public static Object getBean(Class type, boolean logFailures)
   {
      return getBean(type.getName(), logFailures);
   }

   public static Object getBean(String name)
   {
      return factory.getBean(name);
   }
   public static Object getBean(String name, boolean logFailures)
   {
      try {
         return getBean(name);
      } catch (BeansException e) {
         if (logFailures) LOG.error(e.getMessage(), e);
         throw e;
      }
   }

   public static void setBean(Class type, Object singleton)
   {
      ((ConfigurableBeanFactory) factory).registerSingleton(type.getName(), singleton);
   }

   public static void setBean(String name, Object singleton)
   {
      ((ConfigurableBeanFactory) factory).registerSingleton(name, singleton);
   }

   public static void initBeanProperties(Object bean)
   {
      ((AutowireCapableBeanFactory) factory).autowireBeanProperties(bean,
                                                                    AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE,
                                                                    true);
   }

   public static void registerBeanDefinition(Class typeName, Class beanClass)
   {
      ((DefaultListableBeanFactory) factory).registerBeanDefinition(typeName.getName(),
                                                                    new RootBeanDefinition(beanClass,
                                                                                           RootBeanDefinition.AUTOWIRE_AUTODETECT));
   }
}
