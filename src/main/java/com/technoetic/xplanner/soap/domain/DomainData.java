package com.technoetic.xplanner.soap.domain;

import com.technoetic.xplanner.domain.Identifiable;

import java.io.Serializable;
import java.util.Calendar;

public abstract class DomainData implements Identifiable, Serializable {
    private Calendar lastUpdateTime;
    private int id;

    public Calendar getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Calendar lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
