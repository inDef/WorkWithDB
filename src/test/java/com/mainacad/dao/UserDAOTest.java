package com.mainacad.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.mainacad.model.User;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UserDAOTest {

  private static List<User> users = new ArrayList<>();

  @BeforeAll
  static void setUp() {
    User user1 = new User("test_login1", "test_pass", "test_name", "test_surname");
    User user2 = new User("test_login2", "test_pass", "test_name", "test_surname");
    users.add(user1);
    users.add(user2);
    for (User user :
        users) {
      UserDAO.create(user);
    }
  }

  @AfterAll
  static void tearDown() {
    for (User user : users) {
      if (user.getId() != null) {
        UserDAO.delete(user.getId());
      }
    }
  }

  @Test
  void createAndFindAndDelete() {
    User user = new User("test_login3", "test_pass", "test_name", "test_surname");
    assertNull(user.getId());
    User userInDB = UserDAO.create(user);

    assertNotNull(userInDB);
    assertNotNull(userInDB.getId());

    User checkedUserInDB = UserDAO.findById(userInDB.getId());
    assertNotNull(checkedUserInDB);

    checkedUserInDB.setFirstName("name_test");
    User updatedUser = UserDAO.update(checkedUserInDB);
    assertEquals("name_test", updatedUser.getFirstName());

    UserDAO.delete(checkedUserInDB.getId());
    User deletedUser = UserDAO.findById(userInDB.getId());
    assertNull(deletedUser);
  }

  @Test
  void findAll() {
    List<User> usersInDB = UserDAO.findAll();
    for (User user : usersInDB) {
      assertNotNull(user.getId());
    }
  }
}