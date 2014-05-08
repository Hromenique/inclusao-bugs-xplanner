package com.technoetic.xplanner.file;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import java.io.InputStream;


public interface FileSystem {
    Directory getRootDirectory() throws HibernateException;

    Directory getDirectory(Session session, int directoryId) throws HibernateException;

    Directory getDirectory(String path) throws HibernateException;

    File createFile(Directory directory, String name, String contentType,
            long size, InputStream data) throws HibernateException;

    File createFile(Session session, int directoryId, String name, String contentType,
            long size, InputStream data) throws HibernateException;

    File getFile(Session session, int fileId) throws HibernateException;

    void deleteFile(Session session, int fileId) throws HibernateException;

    Directory createDirectory(Session session, int parentDirectoryId, String name) throws HibernateException;

    Directory createDirectory(Session session, Directory parent, String name) throws HibernateException;

    void deleteDirectory(Session session, int directoryId) throws HibernateException;

}
