package com.technoetic.xplanner.actions;

import com.technoetic.xplanner.domain.repository.ObjectRepository;
import com.technoetic.xplanner.security.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.domain.Iteration;
import com.technoetic.xplanner.domain.Person;
import com.technoetic.xplanner.domain.Task;
import com.technoetic.xplanner.domain.UserStory;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.forms.TaskEditorForm;
import com.technoetic.xplanner.mail.EmailFormatterImpl;
import com.technoetic.xplanner.mail.EmailNotificationSupport;
import com.technoetic.xplanner.security.SecurityHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

public class EditTaskAction extends EditObjectAction {
	private EmailNotificationSupport emailNotificationSupport;


	@Override
	protected ActionForward doExecute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse reply) throws Exception {
		TaskEditorForm form = (TaskEditorForm) actionForm;
		String action = form.getAction();
		Task oldTask = null;
		if(StringUtils.hasText(form.getOid())){
			oldTask = new Task();
			copyProperties(oldTask,(Task)getRepository(Task.class).load(Integer.parseInt(form.getOid())));
		}
		ActionForward forward = super.doExecute(actionMapping, actionForm, request, reply);
		sendNotification(forward, oldTask, request, action);
		setTaskDisposition(form, request);
		return forward;
	}

	private void sendNotification(ActionForward forward, Task oldTask, HttpServletRequest request, String action) throws NumberFormatException, AuthenticationException {
		if (forward.getRedirect()) {
			Task task = (Task) request.getAttribute(TARGET_OBJECT);
			int remoteUserId = SecurityHelper.getRemoteUserId(request);
			ObjectRepository repository = getRepository(Person.class);
			if(remoteUserId==0 || task ==null){
				return;
			}
			try{

				Person acceptor = null;
				Person oldAcceptor = null;

				if(task.getAcceptorId() !=0){
					acceptor = (Person) repository.load(task.getAcceptorId());
					if((oldTask ==null) || (task.getAcceptorId() == oldTask.getAcceptorId())){
						oldAcceptor = acceptor;
					}
				}
				if(oldAcceptor == null && task.getAcceptorId() != 0){
					oldAcceptor = (Person) repository.load(task.getAcceptorId());
				}

				Person editor = (Person) repository.load(remoteUserId);
				int storyId = NumberUtils.toInt(request.getParameter("fkey"),0);
				if(storyId ==0){
					if(task.getStory() != null){
						storyId = task.getStory().getId();
					}else{
						return;
					}
				}
				UserStory story = (UserStory) getRepository(UserStory.class).load(storyId);
				Map<Integer, List<Object>> notificationEmails = new HashMap<Integer, List<Object>>();
				emailNotificationSupport.compileEmail(notificationEmails, remoteUserId, acceptor, task, story);
				if(acceptor != null){
					emailNotificationSupport.compileEmail(notificationEmails, acceptor.getId(), editor, task, story);
				}
				Map<String, Object> params = new HashMap<String, Object>();
				if(action.equals(CREATE_ACTION)){
					params.put(EmailFormatterImpl.TEMPLATE, "com/technoetic/xplanner/mail/velocity/email_notification_task_created.vm");
					params.put(EmailFormatterImpl.SUBJECT, "task.created.subject");
					params.put(EmailFormatterImpl.TITLE, "task.created.title");
					params.put(EmailFormatterImpl.HEADER, "task.created.header");
					params.put(EmailFormatterImpl.FOOTER, "task.created.footer");
				}else{
					if(oldAcceptor != null &&((acceptor == null) || oldAcceptor.equals(acceptor))){
						emailNotificationSupport.compileEmail(notificationEmails, oldAcceptor.getId(), editor, task, story);
					}
					params.put(EmailFormatterImpl.TEMPLATE, "com/technoetic/xplanner/mail/velocity/email_notification_task_updated.vm");
					params.put(EmailFormatterImpl.SUBJECT, "task.updated.subject");
					params.put(EmailFormatterImpl.TITLE, "task.updated.title");
					params.put(EmailFormatterImpl.HEADER, "task.updated.header");
					params.put(EmailFormatterImpl.FOOTER, "task.updated.footer");
					params.put("oldTask", oldTask);
				}
				if(acceptor != null){
					params.put("asignee", acceptor.getName());
				}else{
					params.put("asignee", "Unassigned");
				}
				if(oldAcceptor != null){
					params.put("oldAsignee", oldAcceptor.getName());
				}else{
					params.put("oldAsignee", "Unassigned");
				}

				params.put("creator", editor.getName());
				params.put("task", task);
				emailNotificationSupport.sendNotifications(notificationEmails, params);
				System.out.println("Email was sent to:");
				System.out.println(acceptor);
				System.out.println(oldAcceptor);
				System.out.println(editor);
				
			}catch(RepositoryException ex){
				ex.printStackTrace();
			}
		}
	}

	private void setTaskDisposition(TaskEditorForm form, HttpServletRequest request) throws RepositoryException {
		if (!form.isSubmitted()) {
			String oid = form.getOid();
			if (oid == null) {
				int storyId = Integer.parseInt(request.getParameter("fkey"));
				UserStory story = (UserStory) getRepository(UserStory.class).load(storyId);
				form.setDispositionName(getTaskDisposition(story));
			}
		}
	}

	private String getTaskDisposition(UserStory story) throws RepositoryException {
		Iteration iteration = (Iteration) getRepository(Iteration.class).load(story.getIterationId());
		return iteration.getNewTaskDispositionName(story);
	}

	public void setEmailNotificationSupport(EmailNotificationSupport emailNotificationSupport) {
		this.emailNotificationSupport = emailNotificationSupport;
	}
}
