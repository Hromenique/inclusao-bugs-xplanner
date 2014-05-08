package com.technoetic.xplanner.domain;

import java.io.Serializable;

public class Attribute implements Serializable {
    private int targetId;
    private String name;
    private String value;

    public Attribute() {
    }

    public Attribute(int targetId, String name, String value) {
        this.targetId = targetId;
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attribute)) return false;

        final Attribute attribute = (Attribute)o;

        if (targetId != attribute.targetId) return false;
        if (name != null ? !name.equals(attribute.name) : attribute.name != null) return false;
        if (value != null ? !value.equals(attribute.value) : attribute.value != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = targetId;
        result = 29 * result + (name != null ? name.hashCode() : 0);
        result = 29 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
