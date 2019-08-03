package com.mainacad.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mainacad.model.Cart;
import com.mainacad.model.Item;
import com.mainacad.model.Order;
import com.mainacad.model.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class OrderDAOTest {

  private static User user = new User("test", "test", "test", "test");
  private static boolean userCreated = false;
  private static List<Cart> carts = new ArrayList<>();
  private static List<Order> orders = new ArrayList<>();
  private static List<Item> items = new ArrayList<>();
  private static boolean itemsCreated = false;

  @BeforeAll
  static void setUp() {
    user = UserDAO.findByLogin(user.getLogin());
    if (user.getId() == null) {
      user = UserDAO.create(user);
      userCreated = true;
    }

    carts = CartDAO.findByUser(user);
    if (carts.size() == 0) {
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

    items = ItemDAO.findAll();
    if (items.size() == 0) {
      int itemsCount = 5;
      for (int i = 1; i <= itemsCount; i++) {
        int randomPrice = (int) (Math.random() * 1000);
        int randomCode = (int) (Math.random() * 100000);
        Item item = ItemDAO
            .create(new Item(String.valueOf(randomCode), "test item " + randomCode, randomPrice));
        items.add(item);
      }
      itemsCreated = true;
    }

    for (Cart cart : carts) {
      for (Item item : items) {
        int randomAmount = (int) (Math.random() * 100);
        Order order = OrderDAO.create(new Order(item.getId(), randomAmount, cart.getId()));
        orders.add(order);
      }
    }

  }

  @AfterAll
  static void tearDown() {
    for (Order order :
        orders) {
      OrderDAO.delete(order.getId());
    }

    if (userCreated) {
      UserDAO.delete(user.getId());
    }

    if (itemsCreated) {
      for (Item item :
          items) {
        ItemDAO.delete(item);
      }
    }

  }

  @Test
  void createUpdateDelete() {
    Order order = new Order(items.get(1).getId(), 32, carts.get(3).getId());
    Order orderIdDB = OrderDAO.create(order);
    assertNotNull(orderIdDB.getId());
    orderIdDB.setAmount(3);
    Order updatedOrder = OrderDAO.update(orderIdDB);
    assertEquals(3, updatedOrder.getAmount());
    OrderDAO.delete(updatedOrder.getId());
    assertNull(OrderDAO.findById(updatedOrder.getId()));
  }

  @Test
  void findById() {
    assertNotNull(OrderDAO.findById(orders.get(3).getId()));
  }

  @Test
  void findByCart() {
    List<Order> ordersByCart = OrderDAO.findByCart(carts.get(3).getId());
    for (Order order :
        ordersByCart) {
      assertNotNull(order);
      assertEquals(carts.get(3).getId(), order.getCartId());
    }
  }


  @Test
  void findClosedOrdersByUserAndPeriod() {
    Long from = 1564809361118L;
    Long to = 1564809361205L;
    List<Order> ordersInDB = OrderDAO.findClosedOrdersByUserAndPeriod(user.getId(), from, to);
    for (Order order :
        ordersInDB) {
      assertTrue(CartDAO.findById(order.getCartId()).getClosed());
      Cart relatedCart = CartDAO.findById(order.getCartId());
      assertTrue(relatedCart.getCreationTime() >= from & relatedCart.getCreationTime() <= to);

    }

  }
}