package com.technoetic.xplanner.db.hibernate;

import java.sql.Types;

import net.sf.hibernate.dialect.OracleDialect;

public class XPlannerOracleDialect extends OracleDialect {
    public XPlannerOracleDialect() {
        super();
        registerColumnType(Types.VARBINARY, "LONG RAW");
    }
}

