package com.technoetic.xplanner.domain;

import net.sf.hibernate.PersistentEnum;

//TODO delete this class and replace it with Disposition
public class UserStoryDispositionType implements PersistentEnum{
    private final int code;

    public static final String PLANNED_TEXT = "Planned";
    public static final String CARRIED_OVER_TEXT = "Carried Over";
    public static final String ADDED_TEXT = "Added";

    public static final String PLANNED_KEY = "planned";
    public static final String CARRIED_OVER_KEY = "carriedOver";
    public static final String ADDED_KEY = "added";

    public static final UserStoryDispositionType PLANNED = new UserStoryDispositionType(0);
    public static final UserStoryDispositionType CARRIED_OVER = new UserStoryDispositionType(1);
    public static final UserStoryDispositionType ADDED = new UserStoryDispositionType(2);

    public static final String[] KEYS = {PLANNED_KEY, CARRIED_OVER_KEY, ADDED_KEY};

    private UserStoryDispositionType (int code){
        this.code = code;
    }

    public int toInt() {
        return code;
    }

    public String getKey (){
        return KEYS[code];
    }

    public static UserStoryDispositionType fromKey(String key) {
        //TODO should be handled by iterating through the keys
        if (key == null)
            return null;
        else if (PLANNED_KEY.equals(key))
            return PLANNED;
        else if (CARRIED_OVER_KEY.equals(key))
            return CARRIED_OVER;
        else if (ADDED_KEY.equals(key))
            return ADDED;
        else
            throw new RuntimeException ("Unknown disposition key");
    }

    public static UserStoryDispositionType fromInt(int i){
       switch (i) {
           case 0: return PLANNED;
           case 1: return CARRIED_OVER;
           case 2: return ADDED;
           default: throw new RuntimeException ("Unknown disposition code");
       }
    }

    public String toString() {
        return getKey();
    }

    public String getName() {
        return  getKey();
    }
}
