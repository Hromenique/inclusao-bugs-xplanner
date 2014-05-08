package com.technoetic.xplanner.file;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import java.io.InputStream;

public class MockFileSystem implements FileSystem {
    public Directory createDirectory(Session session, Directory parent, String name) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    public Directory createDirectory(Session session, int parentDirectoryId, String name) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    public File createFileReturn;
    public File createFile(Directory directory, String name, String contentType,
            long size, InputStream data) throws HibernateException {
        return createFileReturn;
    }

    public File createFile(Session session, int directoryId, String name, String contentType,
            long size, InputStream data) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    public void deleteDirectory(Session session, int directoryId) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    public void deleteFile(Session session, int fileId) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    public Directory getDirectory(Session session, int directoryId) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    public Directory getDirectory2Return;
    public Directory getDirectory(String path) throws HibernateException {
        return getDirectory2Return;
    }

    public File getFile(Session session, int fileId) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    public Directory getRootDirectory() throws HibernateException {
        throw new UnsupportedOperationException();
    }
}
