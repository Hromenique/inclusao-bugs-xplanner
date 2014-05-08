package com.technoetic.xplanner.domain;

import java.io.Serializable;

/**
 * @hibernate.class
 * table="Person"
 */
public class Person extends DomainObject implements Serializable {
    private String name;
    private String email;
    private String phone;
    private String initials;
    private String userId;
    private boolean hidden;
    private String password;

    /**
     * Default constructor
     */
    public Person() {
    }

    /**
     * Public constructor
     * @param userId user identity
     */
    public Person(String userId) {
        this.userId = userId;
    }

    /**
     * @hibernate.property
     * column="name"
     * @param name Person first name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * @hibernate.property
     * column="email"
     * @param email Person primary mail
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    /**
     * @hibernate.property
     * column="phone"
     * @param phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    /**
     * @hibernate.property
     * column="initials"
     * @param initials for example: AZW, SYS, etc.
     */
    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getInitials() {
        return initials;
    }

    /**
     * @hibernate.property
     * column="userId"
     * @param userId (login) for example: sysadmin, sbate
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    /**
     * @hibernate.property
     * column="is_hidden"
     * @param hidden
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isHidden() {
        return hidden;
    }

    /**
     * @hibernate.property
     * column="password"
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

   public String getDescription() {return "";}

    public String toString(){
        return "Person(id="+this.getId()+
               ", name="+this.getName()+
               ", userId="+this.getUserId()+
               ", initials="+this.getInitials()+
               ", email="+this.getEmail()+
               ", phone="+this.getPhone()+")";
    }

   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      if (!super.equals(o)) return false;

      final Person person = (Person) o;

      if (userId != null ? !userId.equals(person.userId) : person.userId != null) return false;

      return true;
   }

   public int hashCode() {
      int result = super.hashCode();
      result = 29 * result + (userId != null ? userId.hashCode() : 0);
      return result;
   }
}