/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 8, 2006
 * Time: 2:53:15 AM
 */
package com.technoetic.xplanner.upgrade.schema;

/** @noinspection RefusedBequest*/
public class MySqlDBDialect extends DBDialect {
  public String getVendor() {return "MySQL";}

  public String getCreateTableQuery(String table, String fieldSQL) {
    return super.getCreateTableQuery(table, fieldSQL) + " type=InnoDB";
  }

  public String getAddForeignKeyConstraintQuery(String table,
                                                String constraintName,
                                                String foreignKey,
                                                String referencedTable,
                                                String referencedKey) {
    return "alter table " + table +
           " add constraint " + constraintName +
           " foreign key " + constraintName + " (" + foreignKey + ")" +
           " references " + referencedTable + " (" + referencedKey + ")";
  }

  public String getDropForeignKeyConstraintQuery(String table, String constraintName) {
    return "alter table " + table + " drop foreign key " + constraintName;
  }

  public String getAddUniqueConstraintQuery(String table, String constraintName, String column) {
    return "alter table " + table +
           " add constraint " + constraintName +
           " unique " + constraintName + " ( " + column + " )";
  }

  public String getDropUniqueConstraintQuery(String table, String constraintName) {
    return "alter table " + table + " drop index " + constraintName;
  }

}