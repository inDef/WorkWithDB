package com.mainacad.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

class ConnectionToDB {

  private static final String DB_URL = "jdbc:postgresql://localhost:5432/shop_db_ma";
  private static final String DB_USER = "postgres";
  private static final String DB_PASS = "azsxdc123";
  private static Logger logger = Logger.getLogger(ConnectionToDB.class.getName());

  protected static Connection getConnection() {
    Connection connection = null;

    try {
      connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    } catch (SQLException e) {
      logger.severe("Unable to get connection to Data Base!");
    }

    return connection;
  }
}
