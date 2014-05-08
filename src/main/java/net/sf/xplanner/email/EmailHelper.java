package net.sf.xplanner.email;

import com.technoetic.xplanner.mail.EmailNotificationSupport;

import net.sf.xplanner.events.ObjectCreated;
import net.sf.xplanner.events.ObjectDeleted;
import net.sf.xplanner.events.ObjectUpdated;

public class EmailHelper {
	private EmailNotificationSupport emailNotificationSupport;
	
	public void setEmailNotificationSupport(
			EmailNotificationSupport emailNotificationSupport) {
		this.emailNotificationSupport = emailNotificationSupport;
	}

	public void sendEmail(ObjectDeleted event) {
		// TODO Auto-generated method stub
		
	}

	public void sendEmail(ObjectCreated event) {
		// TODO Auto-generated method stub
		
	}

	public void sendEmail(ObjectUpdated event) {
		// TODO Auto-generated method stub
		
	}

}
