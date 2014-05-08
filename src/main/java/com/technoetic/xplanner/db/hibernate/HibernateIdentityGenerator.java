package com.technoetic.xplanner.db.hibernate;

import java.util.Properties;

import net.sf.hibernate.dialect.Dialect;
import net.sf.hibernate.id.TableHiLoGenerator;
import net.sf.hibernate.type.Type;

public class HibernateIdentityGenerator extends TableHiLoGenerator {
   public static final String TABLE_NAME = "identifier";
   public static final String NEXT_ID_COL = "nextId";
   public static final String SET_NEXT_ID_QUERY = "update " + TABLE_NAME + " set " + NEXT_ID_COL + " = ?";
   public static final String SET_NEXT_ID_ATOMIC_QUERY = SET_NEXT_ID_QUERY + " where " + NEXT_ID_COL + " = ?";
   public static final String GET_NEXT_ID_QUERY = "select " + NEXT_ID_COL + " from " + TABLE_NAME;

   public void configure(Type type, Properties params, Dialect d) {
      if (!params.containsKey(TABLE)) {
         params.setProperty(TABLE, TABLE_NAME);
      }
      if (!params.containsKey(COLUMN)) {
         params.setProperty(COLUMN, NEXT_ID_COL);
      }
      if (!params.containsKey(MAX_LO)) {
         params.setProperty(MAX_LO, "10");
      }
      super.configure(type, params, d);
   }

   public Object generatorKey() {

      return super.generatorKey();
   }
}
