package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.List;
import java.util.Optional;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepo = mock(ItemRepository.class);


    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObject(itemController,"itemRepository",itemRepo);

        Item item1 = getItem(1L, "item 01");
        Item item2 = getItem(2L, "item 02 dup");
        Item item3 = getItem(3L, "item 02 dup");
        Item item4 = getItem(4L, "item 03");

        //find item by id -> return single item
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item1));
        //find item by name, some item have the same name -> return list
        when(itemRepo.findByName(item2.getName())).thenReturn(Lists.list(item2, item3));
        //return all item
        when(itemRepo.findAll()).thenReturn(Lists.list(item1, item2, item3, item4));
    }

    @Test
    public void findAllItem(){
        ResponseEntity<List<Item>> listResponseEntity =itemController.getItems();
        List<Item> listItem = listResponseEntity.getBody();

        assertEquals(HttpStatus.OK.value(), listResponseEntity.getStatusCodeValue());
        // 4 items
        assertEquals(4,listItem.size());
    }

    @Test
    public void findItemsByName(){
        ResponseEntity<List<Item>> listResponseEntity = itemController.getItemsByName("item 02 dup");
        List<Item> listItemByName = listResponseEntity.getBody();

        assertEquals(HttpStatus.OK.value(), listResponseEntity.getStatusCodeValue());
        // 2 items with name = "item 02 dup"
        assertEquals(2,listItemByName.size());
    }

    @Test
    public void findItemById(){
        Long mockID = 1L;
        ResponseEntity<Item> responseEntity = itemController.getItemById(mockID);
        Item item = responseEntity.getBody();

        assertNotNull(item);
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    public void findItemNotExistId(){
        Long mockID = 10L;
        ResponseEntity<Item> responseEntity = itemController.getItemById(mockID);
        Item item = responseEntity.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    public void findItemsNotExistName(){
        ResponseEntity<List<Item>> listResponseEntity = itemController.getItemsByName("item not exist");
        List<Item> listItemByName = listResponseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), listResponseEntity.getStatusCodeValue());
    }

    private static Item getItem(long itemId, String itemName) {
        Item item = new Item();
        item.setId(itemId);
        item.setName(itemName);
        return item;
    }
}
