/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 8, 2006
 * Time: 1:49:22 AM
 */
package com.technoetic.xplanner.upgrade.schema;

/** @noinspection MagicNumber,ClassNamingConvention,RefusedBequest*/
public class MySqlTo4_0DBDialect extends MySqlDBDialect {
  public double getToVersion() {return 4.0;}

  public String getAddForeignKeyConstraintQuery(String table,
                                                String constraintName,
                                                String foreignKey,
                                                String referencedTable,
                                                String referencedKey) {
    return getCreateIndexQuery(table, constraintName, foreignKey, false) + ";"+
           super.getAddForeignKeyConstraintQuery(table, constraintName, foreignKey, referencedTable, referencedKey) + ";";
  }
}