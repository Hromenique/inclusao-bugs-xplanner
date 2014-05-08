package com.technoetic.xplanner.domain;

import java.io.Serializable;

public class RoleAssociation implements Serializable {
    private int projectId;
    private int personId;
    private int roleId;

    RoleAssociation() {
        // Hibernate
    }

    public RoleAssociation(int projectId, int personId, int roleId) {
        this.projectId = projectId;
        this.personId = personId;
        this.roleId = roleId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleAssociation)) return false;

        final RoleAssociation roleAssociation = (RoleAssociation)o;

        if (personId != roleAssociation.personId) return false;
        if (projectId != roleAssociation.projectId) return false;
        if (roleId != roleAssociation.roleId) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = projectId;
        result = 29 * result + personId;
        result = 29 * result + roleId;
        return result;
    }

    public String toString(){
        return "RoleAssociation("+
               "personId="+this.getPersonId()+
               ", projectId="+this.getProjectId()+
               ", roleId="+this.getRoleId()+")";
    }
}
