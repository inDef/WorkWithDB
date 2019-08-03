package com.mainacad.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mainacad.model.Cart;
import com.mainacad.model.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CartDAOTest {

  private static List<Cart> carts = new ArrayList<>();
  private static User user = new User("test", "test", "test", "test");

  @BeforeAll
  static void setUp() {
    user = UserDAO.findByLogin(user.getLogin());
    if (user.getId() == null) {
      user = UserDAO.create(user);
    }

    int cartsCount = 5;
    for (int i = 1; i <= cartsCount; i++) {
      Long creationTime = new Date().getTime();
      Cart cart = new Cart(creationTime, true, user.getId());
      if (i == cartsCount) {
        cart.setClosed(false);
      }
      cart = CartDAO.create(cart);
      carts.add(cart);
    }
  }

  @Test
  void createUpdate() {
    Cart cart = new Cart(new Date().getTime(), false, user.getId());
    assertNull(cart.getId());
    Cart cartInDB = CartDAO.create(cart);
    assertNotNull(cartInDB.getId());
    cart.setClosed(true);
    cartInDB = CartDAO.update(cart);
    assertTrue(cartInDB.getClosed());
  }

  @Test
  void findByUser() {
    List<Cart> usersCartsInDB = CartDAO.findByUser(user);
    assertNotNull(usersCartsInDB);
    for (Cart cart :
        usersCartsInDB) {
      assertEquals(user.getId(), cart.getUserId());
    }
  }

  @Test
  void findOpenCartByUser() {
    Cart cart = CartDAO.findOpenCartByUser(user.getId());
    assertNotNull(cart);
    assertFalse(cart.getClosed());
  }

}