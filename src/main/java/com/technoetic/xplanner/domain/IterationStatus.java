package com.technoetic.xplanner.domain;


//TODO refactor to use Enum
public class IterationStatus{
    protected final int code;

    public static final IterationStatus ACTIVE = new IterationStatusPersistent(0);
    public static final IterationStatus INACTIVE = new IterationStatusPersistent(1);

    public static final String ACTIVE_KEY = "active";
    public static final String INACTIVE_KEY = "inactive";

    public static final String[] KEYS = {ACTIVE_KEY, INACTIVE_KEY};

    protected IterationStatus (int code){
        this.code = code;
    }

    public String getKey (){
        return KEYS[code];
    }

    public int toInt() {
       return code;
    }


    public static IterationStatus fromKey(String key) {
       if (key == null) {
          return null;
       } else if (ACTIVE_KEY.equals(key)) {
          return ACTIVE;
       } else if (INACTIVE_KEY.equals(key)) {
          return INACTIVE;
       } else {
          throw new RuntimeException("Unknown iteration status key");
       }

    }

   public String toString() {
      return getKey();
   }

   public boolean equals(Object obj) {
      if (obj == this) return true;
      if (!(obj instanceof IterationStatus)) return false;
      return ((IterationStatus)obj).toInt() == toInt();
   }
}
