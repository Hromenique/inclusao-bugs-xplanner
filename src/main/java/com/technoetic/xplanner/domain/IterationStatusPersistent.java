package com.technoetic.xplanner.domain;

import net.sf.hibernate.PersistentEnum;

public class IterationStatusPersistent extends IterationStatus implements PersistentEnum {
    protected IterationStatusPersistent(int code) {
        super(code);
    }

    public static IterationStatus fromInt(int i) {
        switch (i) {
            case 0:
                return ACTIVE;
            case 1:
                return INACTIVE;
            default:
                throw new RuntimeException("Unknown iteration status code");
        }
    }

}
