package com.technoetic.xplanner.db.hibernate;

import net.sf.hibernate.SessionFactory;

public class GlobalSessionFactory {
    private static SessionFactory factory;

    public static SessionFactory get() {
        return factory;
    }

    public static void set(SessionFactory factory) {
        GlobalSessionFactory.factory = factory;
    }
}