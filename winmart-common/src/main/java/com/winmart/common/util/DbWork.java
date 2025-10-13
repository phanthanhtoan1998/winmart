package com.winmart.common.util;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.SQLException;

public class DbWork implements Work {
    Connection conn;

    @Override
    public void execute(Connection connection) throws SQLException {
        this.conn = connection;
    }

    public Connection getConnection() {
        return conn;
    }
}
