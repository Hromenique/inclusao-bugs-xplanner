package com.technoetic.xplanner.forms;

import com.technoetic.xplanner.actions.UpdateTimeNotificationReceivers;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import java.io.Serializable;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

public class ProjectEditorForm extends AbstractEditorForm {

   private String name;
    private String description;
    private boolean hidden;
    private boolean sendemail;
    private boolean optEscapeBrackets;
    private String wikiUrl;
    private String personToDelete;
    private String personToAddId;
    private ArrayList people = new ArrayList();


    public String getWikiUrl() {
        return this.wikiUrl;
    }

    public void setWikiUrl(String wikiUrl) {
        this.wikiUrl = wikiUrl;
    }

    public boolean isSendingMissingTimeEntryReminderToAcceptor() {
        return sendemail;
    }

    public void setSendemail(boolean sendemail) {
        this.sendemail = sendemail;
    }

    public boolean isOptEscapeBrackets() {
        return optEscapeBrackets;
    }

    public void setOptEscapeBrackets(boolean optEscapeBrackets) {
        this.optEscapeBrackets = optEscapeBrackets;
    }

    public String getContainerId() {
        return null;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (isSubmitted() &&
            !getAction().equals(UpdateTimeNotificationReceivers.ADD) &&
            !getAction().equals(UpdateTimeNotificationReceivers.DELETE))
        {
            require(errors, name, "project.editor.missing_name");
        }
        return errors;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        name = null;
        description = null;
        wikiUrl = null;
        people.clear();
        personToDelete = null;

        hidden = false;
        sendemail = false;
        optEscapeBrackets = false;
    }

    public void reset() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getPersonToDelete() {
        return personToDelete;
    }

    public void setPersonToDelete(String personToDelete) {
        this.personToDelete = personToDelete;
    }

    public String getPersonToAddId() {
        return personToAddId;
    }

    public void setPersonToAddId(String personToAddId) {
        this.personToAddId = personToAddId;
    }

    public ArrayList getPeople() {
        return people;
    }

    public void addPersonInfo(String id, String userId, String initials, String name) {
        PersonInfo personInfo = this.new PersonInfo();
        personInfo.setId(id);
        personInfo.setUserId(userId);
        personInfo.setName(name);
        personInfo.setInitials(initials);
        people.add(personInfo);
    }

    public void removePersonInfo(int rowNbr) {
        people.remove(rowNbr);
    }

    public class PersonInfo implements Serializable {
        String id;
        String userId;
        String initials;
        String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getInitials() {
            return initials;
        }

        public void setInitials(String initials) {
            this.initials = initials;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof PersonInfo)) {
                return false;
            }
            return this.id.equals(((PersonInfo) obj).getId());
        }
    }

}