package com.technoetic.xplanner.db.hibernate;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;
import javax.naming.NamingException;
import javax.naming.Reference;

import net.sf.hibernate.Databinder;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Interceptor;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.exception.SQLExceptionConverter;
import net.sf.hibernate.metadata.ClassMetadata;
import net.sf.hibernate.metadata.CollectionMetadata;
import org.apache.log4j.Logger;
import com.thoughtworks.proxy.Invoker;
import com.thoughtworks.proxy.factory.StandardProxyFactory;
import com.thoughtworks.proxy.toys.echo.Echoing;

import com.technoetic.xplanner.util.LogUtil;

public class XPlannerSessionFactory implements SessionFactory {
   protected static final Logger LOG = LogUtil.getLogger();

    private SessionFactory delegate;

    public XPlannerSessionFactory(SessionFactory delegate) {
        this.delegate = delegate;
    }

    public void close() throws HibernateException {
        delegate.close();
    }

    public void evict(Class persistentClass) throws HibernateException {
        delegate.evict(persistentClass);
    }

    public void evict(Class persistentClass, Serializable id) throws HibernateException {
        delegate.evict(persistentClass, id);
    }

    public void evictCollection(String roleName) throws HibernateException {
        delegate.evictCollection(roleName);
    }

    public void evictCollection(String roleName, Serializable id) throws HibernateException {
        delegate.evictCollection(roleName, id);
    }

    public void evictQueries() throws HibernateException {
        delegate.evictQueries();
    }

    public void evictQueries(String cacheRegion) throws HibernateException {
        delegate.evictQueries(cacheRegion);
    }

    public SQLExceptionConverter getSQLExceptionConverter() {
        return delegate.getSQLExceptionConverter();
    }

    public Map getAllClassMetadata() throws HibernateException {
        return delegate.getAllClassMetadata();
    }

    public Map getAllCollectionMetadata() throws HibernateException {
        return delegate.getAllCollectionMetadata();
    }

    public ClassMetadata getClassMetadata(Class persistentClass) throws HibernateException {
        return delegate.getClassMetadata(persistentClass);
    }

    public CollectionMetadata getCollectionMetadata(String roleName) throws HibernateException {
        return delegate.getCollectionMetadata(roleName);
    }

    public Databinder openDatabinder() throws HibernateException {
        return delegate.openDatabinder();
    }

    public Session openSession() throws HibernateException {
       Session session = delegate.openSession(new XPlannerInterceptor());
       return logAndWrapNewSessionIfDebug(session);
    }

   public Session openSession(Connection connection) {
      Session session = delegate.openSession(connection, new XPlannerInterceptor());
      return logAndWrapNewSessionIfDebug(session);
    }

    public Session openSession(Connection connection, Interceptor interceptor) {
       Session session = delegate.openSession(connection, interceptor);
       return logAndWrapNewSessionIfDebug(session);
    }

    public Session openSession(Interceptor interceptor) throws HibernateException {
       Session session = delegate.openSession(interceptor);
       return logAndWrapNewSessionIfDebug(session);
    }

   private Session logAndWrapNewSessionIfDebug(final Session session) {
      if (LOG.isDebugEnabled()) {
         PrintWriter out = new PrintWriter(new Log4JDebugLoggerWriter());
         return (Session) Echoing.object(Session.class, session, out, new StandardProxyFactory() {
            public boolean canProxy(Class type) {
               return Session.class.isAssignableFrom(type);
            }

            public Object createProxy(Class[] types, Invoker invoker) {
               return super.createProxy(types, new SessionCountingInvoker(session, invoker));
            }
         });
      }
      return session;
   }

   public Reference getReference() throws NamingException {
       return delegate.getReference();
   }

   private static int lastId = 0;
   private static int sessionCount = 0;

   class SessionCountingInvoker implements Invoker {
      private Session session;
      private int id;
      private Invoker invoker;
      private StackTraceElement[] creationStack;

      public SessionCountingInvoker(Session session, Invoker invoker) {
         this.session = session;
         this.invoker = invoker;
         this.creationStack = getStackTraceForMethod("openSession");
         this.id = ++lastId;
         LOG.debug("Session.new() -> " + ++sessionCount + " opened. " +
                   this + " was opened:\n" + getStackTraceString(creationStack));
      }

      public String toString() {
         return "session #" + id + " (" + session + ")";
      }

      private StackTraceElement[] getStackTraceForMethod(String methodName) {
         StackTraceElement[] stackTrace = new Throwable().getStackTrace();
         stackTrace = pruneTopDownToOpenSessionFrame(stackTrace, methodName);
         stackTrace = pruneBottomUpToFirstXPlannerFrame(stackTrace);
         return stackTrace;
      }

      private StackTraceElement[] pruneBottomUpToFirstXPlannerFrame(StackTraceElement[] stackTrace) {
         int firstXPlannerFrameIndex = findFirstXPlannerFrameIndex(stackTrace);
         StackTraceElement[] prunedStackTrace = new StackTraceElement[firstXPlannerFrameIndex+1];
         System.arraycopy(stackTrace, 0, prunedStackTrace, 0, firstXPlannerFrameIndex+1);
         return prunedStackTrace;
      }

      private int findFirstXPlannerFrameIndex(StackTraceElement[] stackTrace) {
         for (int i = stackTrace.length - 1; i >= 0; i--) {
            StackTraceElement frame = stackTrace[i];
            if (frame.getClassName().indexOf("xplanner") != -1 ||
                frame.getClassName().indexOf("springframework") != -1) {
               return i;
            }
         }
         return stackTrace.length-1;
      }

      private StackTraceElement[] pruneTopDownToOpenSessionFrame(StackTraceElement[] stackTrace, String methodName) {
         int openSessionFrameIndex = findFirstTopFrameIndexForMethod(stackTrace, methodName);
         StackTraceElement[] prunedStackTrace = new StackTraceElement[stackTrace.length - openSessionFrameIndex];
         System.arraycopy(stackTrace, openSessionFrameIndex, prunedStackTrace, 0, prunedStackTrace.length);
         return prunedStackTrace;
      }

      private int findFirstTopFrameIndexForMethod(StackTraceElement[] stackTrace, String methodName) {
         for (int i = 0; i < stackTrace.length; i++) {
            StackTraceElement frame = stackTrace[i];
            if (methodName.equals(frame.getMethodName())) {
               return i;
            }
         }
         return 0;
      }

      private String getStackTraceString(StackTraceElement[] stackTrace) {
         StringBuffer buf = new StringBuffer();
         buf.append("\n");
         for (int i = 0; i < stackTrace.length; i++) {
            StackTraceElement frame = stackTrace[i];
            buf.append("\tat " + frame + "\n");
         }
         buf.append("\n");
         return buf.toString();
      }

      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
         Object result = null;
         boolean connected = session.isConnected();
         try {
            return invoker.invoke(proxy, method, args);
         } finally {
            if (method.getName().equals("close")) {
               if (connected) {
                  --sessionCount;
               }
               LOG.debug("Session.close() -> " + (!connected?"did not close session. ":" ") + sessionCount + " still opened. " +
                         this + " was closed:\n" + getStackTraceString(getStackTraceForMethod("close")));
            }
         }
      }

      protected void finalize() throws Throwable {
         super.finalize();
         if (session.isOpen()) {
            LOG.debug("     ############# Session.finalize() -> " + this + " was not closed ###############\n" +
                      "Session was allocated from:\n" + getStackTraceString(creationStack));
         }
      }
   }

   private static class Log4JDebugLoggerWriter extends Writer {
      StringBuffer buf = new StringBuffer();

      public void close() { }

      public void flush() {
         LOG.debug(buf.toString());
         buf = new StringBuffer();
      }

      public void write(char[] cbuf, int off, int len) {
         buf.append(cbuf, off, len);
      }
   }
}