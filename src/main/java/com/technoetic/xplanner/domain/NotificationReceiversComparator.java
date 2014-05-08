package com.technoetic.xplanner.domain;

import java.util.Comparator;

public class NotificationReceiversComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        return ((Person)o1).getName().compareTo(((Person)o2).getName());
    }
}
