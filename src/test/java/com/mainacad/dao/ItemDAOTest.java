package com.mainacad.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mainacad.model.Item;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ItemDAOTest {

  private static List<Item> items = new ArrayList<>();
  private static boolean itemsCreated = false;

  @BeforeAll
  static void setUp() {
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
  }

  @AfterAll
  static void tearDown() {
    if (itemsCreated) {
      for (Item item :
          items) {
        ItemDAO.delete(item);
      }
    }
  }

  @Test
  void createUpdateDelete() {
    Item item = new Item("32165216465", "Some test item", 134665);
    assertNull(item.getId());
    ItemDAO.create(item);
    assertNotNull(item.getId());
    item.setPrice(100000);
    Item updatedItem = ItemDAO.update(item);
    assertEquals(100000, updatedItem.getPrice());
    ItemDAO.delete(updatedItem);
    assertNull(ItemDAO.findById(updatedItem.getId()));
  }

  @Test
  void findById() {
    assertNotNull(ItemDAO.findById(items.get(0).getId()));
  }

  @Test
  void findByItemCode() {
    List<Item> itemsInDB = ItemDAO.findByItemCode(items.get(0).getItemCode());
    for (Item item : itemsInDB) {
      assertEquals(items.get(0).getItemCode(), item.getItemCode());
    }
  }

  @Test
  void findByItemPriceBetween() {
    int priceFrom = 0;
    int priceTo = 100000;
    List<Item> itemsInDB = ItemDAO.findByItemPriceBetween(priceFrom, priceTo);
    for (Item item : itemsInDB) {
      assertTrue(item.getPrice() >= priceFrom & item.getPrice() <= priceTo);
    }
  }

}