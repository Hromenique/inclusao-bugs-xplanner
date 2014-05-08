/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 8, 2006
 * Time: 1:32:52 AM
 */
package com.technoetic.xplanner.upgrade.schema;

/** @noinspection RefusedBequest*/
public class HsqldbDBDialect extends DBDialect {
  public String getVendor() {return "HSQL Database Engine";}
  public String getAddForeignKeyConstraintQuery(String table,
                                                String constraintName,
                                                String foreignKey,
                                                String referencedTable,
                                                String referencedKey) {
    return "alter table " +
           table +
           " add constraint " +
           constraintName +
           " foreign key (" + foreignKey + ") references " +
           referencedTable +
           " (" +
           referencedKey +
           ")";
  }

  public String getDropForeignKeyConstraintQuery(String table, String constraintName) {
    return "alter table " + table + " drop constraint " + constraintName;
  }

  public String getAddUniqueConstraintQuery(String table, String constraintName, String column) {
    return "alter table " + table + " add constraint " + constraintName + " unique " + " ( " + column + " )";
  }

  public String getRenameColumnQuery(String table, String oldName, String newName, String type) {
    return "alter table " + table + " alter column " + oldName + " rename to " + newName;
  }

}