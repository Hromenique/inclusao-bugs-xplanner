package com.technoetic.xplanner.upgrade;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import net.sf.hibernate.HibernateException;
import org.apache.log4j.Logger;

import com.technoetic.xplanner.util.LogUtil;

public class CleanUpAttachments extends JdbcMigrationTaskSupport {
   public static final Logger LOG = LogUtil.getLogger();
  //DEBT(EXTERNALIZE QUERIES) move these query out to Note.xml
   public static final String UPDATE_NOTE_SQL = "update note set attachment_Id = null where id = ?";
   public static final String DELETE_ATTACHMENT_SQL = "delete from xfile where id = ?";
   public static final String NOTE_IDS_WITH_FAKE_ATTACHMENTS_QUERY =
         "select note.id from note left join xfile on note.attachment_id = xfile.id " +
         "where xfile.id is null and note.attachment_id is not null";
   public static final String ORPHAN_ATTACHMENTS_QUERY = "select xfile.id from note right join xfile " +
                                                         "on note.attachment_id = xfile.id " +
                                                         "where note.id is null";
  public CleanUpAttachments() {
    super("cleanup_attachments",8);
   }

   public void migrate() throws HibernateException, SQLException {
     cleanUpNotes();
     cleanUpOrphanAttachments();
   }

   /**
    * Nulls attachmentId if there is no file with given id
    *
    * @throws HibernateException
    */
   public void cleanUpNotes() throws HibernateException, SQLException {
      Collection notesToCleanUp = getNoteIdsWithFakeAttachment();
      LOG.debug("Updating " + notesToCleanUp.size() + " notes.");
      for (Iterator iterator = notesToCleanUp.iterator(); iterator.hasNext();) {
         Integer noteId = (Integer) iterator.next();
         LOG.debug("Assigning null to attachmentId for noteId: " + noteId);
         template.update(UPDATE_NOTE_SQL, new Object[] {noteId});
      }
   }

  /**
   * Deletes attachment which are not assigned to any note
    *
    * @throws HibernateException
    */
   public void cleanUpOrphanAttachments() throws HibernateException, SQLException {
      Collection attachmentsToDelete = getOrphanAttachments();
      LOG.debug("Deleting " + attachmentsToDelete.size() + " attachments.");
      for (Iterator iterator = attachmentsToDelete.iterator(); iterator.hasNext();) {
         Integer fileId = (Integer) iterator.next();
        template.update(DELETE_ATTACHMENT_SQL, new Object[]{fileId});
         LOG.debug("Deleting file id: " + fileId);
      }
   }

   private Collection getNoteIdsWithFakeAttachment()  {
      return template.queryForList(NOTE_IDS_WITH_FAKE_ATTACHMENTS_QUERY, Integer.class);
   }

   private Collection getOrphanAttachments() {
      return template.queryForList(ORPHAN_ATTACHMENTS_QUERY, Integer.class);

   }
  public static void main(String[] args) throws Exception {
     new CleanUpAttachments().run();
  }
}