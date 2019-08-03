package com.mainacad.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

class ConnectionToDBTest {

  @Test
  void getConnection() {
    Connection connection = ConnectionToDB.getConnection();
    assertNotNull(connection);

    try {
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}