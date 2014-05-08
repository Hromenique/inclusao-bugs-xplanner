package com.technoetic.xplanner.acceptance.upgrade;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript;
import com.technoetic.xplanner.domain.Note;
import com.technoetic.xplanner.file.File;
import com.technoetic.xplanner.upgrade.CleanUpAttachments;
import com.technoetic.xplanner.upgrade.schema.DBSchemaMigrater;


public class CleanUpAttachmentsTestScript extends AbstractDatabaseTestScript
{
  protected static final Logger LOG = CleanUpAttachments.LOG;

  private CleanUpAttachments cleanUpAttachments;
   private DBSchemaMigrater migrater;

  protected void setUp() throws Exception {
     super.setUp();
     migrater = createDBSchemaMigrater();
     migrater.withNoException().dropForeignKeyConstraint("note", "noteAttachments");
    cleanUpAttachments = new CleanUpAttachments();
    commitSession();
  }

  protected void tearDown() throws Exception {
    migrater.withNoException().addForeignKeyConstraint("note", "noteAttachments", "attachment_id", "xfile", "id");
    commitAndCloseSession();
  }

   public void testCleanUpOrphanAttachments() throws Exception
   {
      File attachementToDelete = new File();
      attachementToDelete.setName("orphan file");
      mom.save(attachementToDelete);
      commitCloseAndOpenSession();
      File actualFile = (File) getSession().get(File.class, new Integer(attachementToDelete.getId()));
      LOG.debug("file to be deleted = " + actualFile);
      assertNotNull(actualFile);
      cleanUpAttachments.run();
      commitCloseAndOpenSession();
      actualFile = (File) getSession().get(File.class, new Integer(attachementToDelete.getId()));
      assertNull(actualFile);
   }

   public void testCleanUpNotes() throws Exception
   {
      File file = new File();
      file.setName("orphan file");
      Note noteToUpdate = new Note();
      noteToUpdate.setSubject("Note to update");
      noteToUpdate.setBody("Note to update");
      noteToUpdate.setFile(file);
      mom.save(noteToUpdate);
      mom.save(file);
      commitAndCloseSession();
      cleanUpAttachments.run();
      commitCloseAndOpenSession();
      Note actualNote = (Note) getSession().get(Note.class, new Integer(noteToUpdate.getId()));
      assertNotNull(actualNote);
      assertNotNull(actualNote.getFile());
      mom.deleteObject(file);
      commitCloseAndOpenSession();
      cleanUpAttachments.run();
      actualNote = (Note) getSession().get(Note.class, new Integer(noteToUpdate.getId()));
      assertNotNull(actualNote);
      assertNull(actualNote.getFile());
   }

}
