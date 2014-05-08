package com.technoetic.xplanner.db;

import com.technoetic.xplanner.domain.*;
import com.technoetic.xplanner.file.File;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.NullableType;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * User: Mateusz Prokopowicz
 * Date: Dec 16, 2004
 * Time: 2:10:17 PM
 */
public class NoteHelper {
    //FIXME: Externalize all these query in the Note.xml mapping file
    //DEBT: Turn this into a NoteRepository that is spring loaded. No more static entry point. A session is passed in at construction time.
    static final String SELECT_NOTES_FILE_IS_ATTACHED_TO = "select note from Note note where note.file.id= ?";
    static final String FILE_DELETE_QUERY = "from file in " +
                                            File.class +
                                            " where file.id= ?";
    static final String NOTE_DELETE_QUERY = "from note in " +
                                            Note.class +
                                            " where note.id = ?";
    static final String SELECT_NOTE_ATTACHED_TO = "select note from Note note where note.attachedToId = ?";
    static final String SELECT_ATTACHED_TO_ID = "SELECT t.id, s.id, i.id, p.id FROM " +
                                                Project.class.getName() +
                                                " as p left join p.iterations as i left join i.userStories as s left join s.tasks as t" +
                                                " WHERE  p.id = ? or i.id = ? or s.id = ? or t.id = ? order by i.id, s.id, t.id";
    static final String NOTE_DELETION_QUERY = "from note in " +
                                              Note.class +
                                              " where note.attachedToId = ?";

    static final Class[] DELETE_ORDER = {Task.class,
                                         UserStory.class,
                                         Iteration.class,
                                         Project.class};


    public static int deleteNote(Note note, Session session)
        throws HibernateException {
        if (note.getFile() != null) {
            List noteList = session.find(SELECT_NOTES_FILE_IS_ATTACHED_TO,
                                         new Integer(note.getFile().getId()),
                                         Hibernate.INTEGER);
            if (noteList != null && noteList.size() == 1) {
                session.delete(FILE_DELETE_QUERY,
                               new Integer(((Note) noteList.get(0)).getFile().getId()),
                               Hibernate.INTEGER);
            }
        }
        return session.delete(NOTE_DELETE_QUERY,
                              new Integer(note.getId()),
                              Hibernate.INTEGER);
    }

    public static int deleteNotesFor(NoteAttachable obj, Session session)
        throws HibernateException {
        return deleteNotesFor(obj.getClass(), obj.getId(), session);
    }

    public static int deleteNotesFor(Class clazz, int objectId, Session session)
        throws HibernateException {
        Integer[] ids = new Integer[4];
        Arrays.fill(ids, new Integer(objectId));
        NullableType[] types = new NullableType[4];
        Arrays.fill(types, Hibernate.INTEGER);
        Iterator objIt = session.iterate(SELECT_ATTACHED_TO_ID,
                                         ids,
                                         types);
        Object[] oldRow = new Object[4];
        Arrays.fill(oldRow, new Integer(-1));
        int retVal = 0;
        while (objIt.hasNext()) {
            Object[] row = (Object[]) objIt.next();
            for (int i = 0;
                 i <= Arrays.asList(DELETE_ORDER).indexOf(clazz);
                 i++) {
                if (row[i] != null && !row[i].equals(oldRow[i])) {
                    List noteList = session.find(SELECT_NOTE_ATTACHED_TO,
                                                 row[i],
                                                 Hibernate.INTEGER);
                    for (Iterator noteIt = noteList.iterator(); noteIt.hasNext();) {
                        deleteNote((Note) noteIt.next(), session);
                    }
                }
            }
            oldRow = row;
        }
        return retVal;
    }

}
