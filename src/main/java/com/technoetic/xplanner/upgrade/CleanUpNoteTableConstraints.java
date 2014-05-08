/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: Apr 27, 2006
 * Time: 3:12:28 AM
 */
package com.technoetic.xplanner.upgrade;

import com.tacitknowledge.util.migration.MigrationException;

/**
 * Make sure we cannot delete files that are still referenced by notes
 * If constaints on the note table has a different name, access mysql manually though mysql client: mysql -u xplanner -p xplanner
 * Run 'show create table note'
 * Change from patch0007_iteration45_47.sql:
 * Removed following statement from patch0007_iteration45_47.sql:
 *    alter table note add constraint noteAttachment foreign key (attachment_id) references xfile (id);
 * table person
 * Foreign Key	FK68AF8F596607D1C Reference	id <- story.customer_id
 * Foreign Key	FK0015856662D8A5 Reference	id <- story.customer_id
 * Foreign Key	FK90D2EE1032DA9A45 Reference	id <- notification_receivers.person_id
 *
 */
public class CleanUpNoteTableConstraints extends JdbcMigrationTaskSupport {

  public CleanUpNoteTableConstraints() {
    super("cleanup_note_constraints", 13);
  }

  public void migrate() throws MigrationException {
      migrater.withNoException().dropForeignKeyConstraint("note", "FK33AFF2A62F61B7");
      migrater.withNoException().dropForeignKeyConstraint("note", "false");
      migrater.withNoException().dropForeignKeyConstraint("note", "noteAttachment");
      migrater.withNoException().dropForeignKeyConstraint("note", "noteAttachments");

      migrater.addForeignKeyConstraint("note", "noteAttachments", "attachment_id", "xfile", "id");
    }

  public static void main(String[] args) throws Exception {
     new CleanUpAttachments().run();
  }

}