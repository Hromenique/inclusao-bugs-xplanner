package com.technoetic.xplanner.export;

import net.sf.hibernate.Session;

import javax.servlet.http.HttpServletResponse;

public interface Exporter {
    byte[] export(Session session, Object object) throws ExportException;

    void initializeHeaders(HttpServletResponse response);
}
