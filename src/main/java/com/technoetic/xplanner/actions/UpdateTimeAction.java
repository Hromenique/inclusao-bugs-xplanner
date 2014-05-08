package com.technoetic.xplanner.actions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.Task;
import com.technoetic.xplanner.domain.TimeEntry;
import com.technoetic.xplanner.domain.UserStory;
import com.technoetic.xplanner.domain.repository.ObjectRepository;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.format.DecimalFormat;
import com.technoetic.xplanner.forms.IterationStatusEditorForm;
import com.technoetic.xplanner.forms.TimeEditorForm;
import com.technoetic.xplanner.history.HistoricalEvent;
import com.technoetic.xplanner.history.HistorySupport;
import com.technoetic.xplanner.security.SecurityHelper;

public class UpdateTimeAction extends AbstractAction {
   private static final Logger log = Logger.getLogger("UpdateTimeAction");
   public static final String UPDATE_TIME_ACTION = "UPDATE_TIME";
   public static final String UPDATE_ESTIMATE_ACTION = "UPDATE_ESTIMATE";
   private static final String SAVED_FORM_KEY = "UpdateTimeAction.savedForm";

   protected ActionForward doExecute(ActionMapping actionMapping, ActionForm actionForm,
                                     HttpServletRequest request, HttpServletResponse reply)
         throws Exception {
      TimeEditorForm form = loadFormFromSessionIfExistsOrFromRequest(actionForm, request);
      try {
         Session session = getSession(request);
         try {
            ActionForward forward;
            if (!form.isSubmitted()) {
               populateForm(session, form, request);
               forward = new ActionForward(actionMapping.getInput());
            } else {
               if (!isIterationStarted(session, form, request)) {
                  saveFormInSession(request, form);
                  request.setAttribute("from_edit/time", "true");
                  return new ActionForward("/do/start/iteration", false);
               }
               forward = doAction(session, form, request, actionMapping);
            }
            return forward;
            // DEBT: Remove exception handling since it is completely handled in super class.
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

   private TimeEditorForm loadFormFromSessionIfExistsOrFromRequest(ActionForm actionForm, HttpServletRequest request) {
      TimeEditorForm form = (TimeEditorForm) actionForm;
      TimeEditorForm savedForm = loadFormFromSession(request);
      if (savedForm != null) {
         form = savedForm;
         deleteFormFromSession(request);
      }
      return form;
   }

   private void saveFormInSession(HttpServletRequest request, TimeEditorForm form) {
      request.getSession(true).setAttribute(SAVED_FORM_KEY, form);
   }

   private void deleteFormFromSession(HttpServletRequest request) {request.getSession(true).removeAttribute(SAVED_FORM_KEY);}

   private TimeEditorForm loadFormFromSession(HttpServletRequest request) {return (TimeEditorForm) request.getSession(true).getAttribute(SAVED_FORM_KEY);}

   private ActionForward doAction(Session session, TimeEditorForm form,
                                  HttpServletRequest request, ActionMapping actionMapping) throws Exception {
      if (form.getAction().equals(UPDATE_TIME_ACTION)) {
         return doUpdateTimeAction(session, form, request, actionMapping);
      } else if (form.getAction().equals(UPDATE_ESTIMATE_ACTION)) {
         return doUpdateEstimateAction(session, form, request, actionMapping);
      } else {
         throw new ServletException("Unknown action: " + form.getAction());
      }
   }

  private ActionForward doUpdateTimeAction(Session session, TimeEditorForm form,
                                           HttpServletRequest request, ActionMapping actionMapping) throws Exception {
    DecimalFormat decimalParser = new DecimalFormat(request);
    SimpleDateFormat dateTimeFormat = getDateTimeFormat(request);
    SimpleDateFormat dateFormat = getDateFormat(request);
    try {
      List timeEntries = session.find("from timeEntry in class com.technoetic.xplanner.domain.TimeEntry " +
                                      "where timeEntry.taskId = ? order by timeEntry.reportDate",
                                      form.getOid(), Hibernate.STRING);

      double hoursWorked = 0;
      boolean rowDeleted = false;
      for (int i = 0; i < form.getRowcount(); i++) {

        int id = 0;
        if (isPresent(form.getEntryId(i))) {
          id = Integer.parseInt(form.getEntryId(i));
        }

        if (form.getDeleted(i) != null && form.getDeleted(i).equals("true")) {
          TimeEntry entry = getEntry(timeEntries, id);
          session.delete(entry);
          rowDeleted = true;
          continue;
        }

        Date startTime = null;
        if (isPresent(form.getStartTime(i))) {
          startTime = dateTimeFormat.parse(form.getStartTime(i));
        }

        Date endTime = null;
        if (isPresent(form.getEndTime(i))) {
          endTime = dateTimeFormat.parse(form.getEndTime(i));
        }

        int person1Id = 0;
        if (isPresent(form.getPerson1Id(i))) {
          person1Id = Integer.parseInt(form.getPerson1Id(i));
        }

        int person2Id = 0;
        if (isPresent(form.getPerson2Id(i))) {
          person2Id = Integer.parseInt(form.getPerson2Id(i));
        }

        double duration = 0;
        if (isPresent(form.getDuration(i))) {
          duration = decimalParser.parse(form.getDuration(i));
        }

        Date reportDate = null;
        if (isPresent(form.getReportDate(i))) {
          reportDate = dateFormat.parse(form.getReportDate(i));
        }

        String description = form.getDescription(i);

        if (id == 0) {
          if (startTime != null || duration > 0) {
            TimeEntry entry = new TimeEntry();
            entry.setTaskId(Integer.parseInt(form.getOid()));
            editEntry(entry, startTime, endTime, duration, person1Id, person2Id, reportDate, description);
            hoursWorked += entry.getEffort();
            if (timeEntries == null || timeEntries.isEmpty())
            {
               startTask(session, Integer.parseInt(form.getOid()));
            }
            session.save(entry);
          }
        } else {
          TimeEntry entry = getEntry(timeEntries, id);
          editEntry(entry, startTime, endTime, duration, person1Id, person2Id, reportDate, description);
          hoursWorked += entry.getEffort();
        }
      }

      if (isPresent(form.getRemainingHours())) {
        Double remainingHours = new Double(form.getRemainingHours());
        if (remainingHours.doubleValue() >= 0.0) {
          Task task = (Task) session.load(Task.class, new Integer(form.getOid()));
          task.setEstimatedHours(hoursWorked + remainingHours.doubleValue());
          session.save(task);
        }
      }

      if (rowDeleted) {
        form.reset(actionMapping, request);
      }
      return new ActionForward(request.getParameter("returnto"), true);
    } finally { //DEBT(060107)
      session.flush();
      session.connection().commit();
    }
  }

   private void startTask(Session session, int taskId) throws HibernateException {
      Task task = (Task) session.load(Task.class, new Integer(taskId));
      task.start();
      session.save(task);
   }

   private void editEntry(TimeEntry entry,
                          Date startTime,
                          Date endTime,
                          double duration,
                          int person1Id,
                          int person2Id,
                          Date reportDate,
                          String description) {
     entry.setStartTime(startTime);
     entry.setEndTime(endTime);
     entry.setDuration(duration);
     entry.setPerson1Id(person1Id);
     entry.setPerson2Id(person2Id);
     entry.setReportDate(reportDate);
     entry.setDescription(description);
   }

  private boolean isIterationStarted(Session session, TimeEditorForm form ,
                                     HttpServletRequest request) throws RepositoryException, HibernateException {//Autostart iteration


     Task task = (Task) session.load(Task.class, new Integer(form.getOid()));
     UserStory story = task.getStory();
     ObjectRepository repository = getRepository(Iteration.class);
     Iteration iteration = (Iteration) repository.load(story.getIterationId());
     if (!iteration.isActive()) {
        request.setAttribute("edit/iteration", iteration);
        request.setAttribute(IterationStatusEditorForm.SAVE_TIME_ATTR, Boolean.TRUE);
        return false;
     }
     return true;
  }

   private ActionForward doUpdateEstimateAction(Session session, TimeEditorForm form,
                                                HttpServletRequest request, ActionMapping actionMapping)
         throws Exception {
      try {
         Task task = (Task) session.load(Task.class, new Integer(form.getOid()));
         String oldEstimation = DecimalFormat.format(request, task.getEstimatedHours());
         HistorySupport.saveEvent(session, task, HistoricalEvent.REESTIMATED, "Estimate changed from " +
                                                                              oldEstimation
                                                                              + " to " + request.getParameter(
                                                                                    "estimate"),
                                  SecurityHelper.getRemoteUserId(request), new Date());
         task.setEstimatedHours(new DecimalFormat(request).parse(request.getParameter("estimate")));
         if (request.getSession().getAttribute("edit/iteration") != null) {
            request.setAttribute("edit/iteration", request.getSession().getAttribute("edit/iteration"));
            request.getSession().removeAttribute("edit/iteration");
            return actionMapping.findForward("start/iteration");
         } else {
            return new ActionForward(request.getParameter("returnto"), true);
         }
      } finally {
         session.flush();
         session.connection().commit();
      }
   }

   private boolean isPresent(String value) {
      return value != null && !value.equals("") && !value.equals("null");
   }

   private void populateForm(Session session, TimeEditorForm form,
                             HttpServletRequest request) throws Exception {
      SimpleDateFormat dateTimeFormat = getDateTimeFormat(request);
      SimpleDateFormat dateFormat = getDateFormat(request);
      List entries = session.find("from timeEntry in class com.technoetic.xplanner.domain.TimeEntry " +
                                  "where timeEntry.taskId = ? order by timeEntry.reportDate",
                                  form.getOid(), Hibernate.STRING);
      Task task = null;
      if (entries.size() == 0) {
         int taskId = Integer.parseInt(form.getOid());
         task = (Task) session.load(Task.class, new Integer(taskId));
      }
      session.connection().rollback();

      int i = 0;
      TimeEntry entry = null;
      Iterator itr = entries.iterator();
      while (itr.hasNext()) {
         entry = (TimeEntry) itr.next();
         form.setEntryId(i, Integer.toString(entry.getId()));
         if (entry.getStartTime() != null) {
            form.setStartTime(i, dateTimeFormat.format(entry.getStartTime()));
         } else {
            form.setStartTime(i, "");
         }
         if (entry.getEndTime() != null) {
            form.setEndTime(i, dateTimeFormat.format(entry.getEndTime()));
         } else {
            form.setEndTime(i, "");
         }
         form.setDuration(i, DecimalFormat.format(request, entry.getDuration()));
         form.setPerson1Id(i, Integer.toString(entry.getPerson1Id()));
         form.setPerson2Id(i, Integer.toString(entry.getPerson2Id()));
         if (entry.getReportDate() != null) {
            form.setReportDate(i, dateFormat.format(entry.getReportDate()));
         } else {
            form.setReportDate(i, "");
         }
         form.setDescription(i, entry.getDescription());
         i++;
      }
      if (entry == null || entry.getEndTime() != null || entry.getDuration() > 0) {
// start new row
         form.setEntryId(i, "0");
         form.setDeleted(i, null);
         form.setStartTime(i, "");
         form.setEndTime(i, "");
         form.setReportDate(i, dateFormat.format(new Date()));
         if (i == 0) {
            int personId = task.getAcceptorId();
            form.setPerson1Id(i, personId == 0 ? null : Integer.toString(personId));
            form.setPerson2Id(i, null);
         } else {
            form.setPerson1Id(i, form.getPerson1Id(i - 1));
            form.setPerson2Id(i, form.getPerson2Id(i - 1));
         }
         i++;
      }
      form.setRowcount(i);
   }

   private TimeEntry getEntry(List entries, int entryId) {
      for (int i = 0; i < entries.size(); i++) {
         if (((TimeEntry) entries.get(i)).getId() == entryId) {
            return (TimeEntry) entries.get(i);
         }
      }
      return null;
   }

   private SimpleDateFormat getDateTimeFormat(HttpServletRequest request) {
      HttpSession session = request.getSession();
      Locale locale = (Locale) session.getAttribute(Globals.LOCALE_KEY);
      if (locale == null) {
         locale = Locale.getDefault();
      }
      return new SimpleDateFormat(getResources(request).getMessage(locale, "format.datetime"));
   }

   private SimpleDateFormat getDateFormat(HttpServletRequest request) {
      HttpSession session = request.getSession();
      Locale locale = (Locale) session.getAttribute(Globals.LOCALE_KEY);
      if (locale == null) {
         locale = Locale.getDefault();
      }
      return new SimpleDateFormat(getResources(request).getMessage(locale, "format.date"));
   }

   public static boolean isFromUpdateTime(HttpServletRequest request) {return request.getAttribute("from_edit/time") != null;}
}
