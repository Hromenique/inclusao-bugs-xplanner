/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import net.sf.hibernate.UserType;
import net.sf.hibernate.HibernateException;
import org.hsqldb.Types;

public abstract class CharacterEnumType implements UserType {
   private static final int[] SQL_TYPES = {Types.CHAR};

   public int[] sqlTypes() {
      return SQL_TYPES;
   }

   public abstract Class returnedClass();

   public boolean equals(Object x, Object y) throws HibernateException {
      return x == y;
   }

   public Object deepCopy(Object value) throws HibernateException {
      return value;
   }

   public boolean isMutable() {
      return false;
   }

   public Object nullSafeGet(ResultSet resultSet,
                             String[] names,
                             Object owner)
           throws HibernateException, SQLException {

     String name = resultSet.getString(names[0]);
     return resultSet.wasNull() ? null : getType(name);
   }

   protected abstract CharacterEnum getType(String code);

   public void nullSafeSet(PreparedStatement statement, Object value, int index)
         throws HibernateException, SQLException {
       if (value == null) {
           statement.setNull(index, Types.CHAR);
       } else {
           statement.setString(index, convert(value));
       }
   }

   private String convert(Object value)
   {
      CharacterEnum characterEnum = (CharacterEnum) value;

      return String.copyValueOf(new char[] {characterEnum.getCode()} );
   }

}
