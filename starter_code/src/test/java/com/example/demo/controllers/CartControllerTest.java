package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {


    private CartController cartController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);

    private static final String MOCK_USERNAME = "duyduy";
    private static final String MOCK_INVALID_USERNAME = "duy fake";
    private static final long MOCK_ITEM_ID = 1L;
    private static final int MOCK_QUANTITY = 3;
    private static final String MOCK_PRICE = "12.34";


    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObject(cartController,"userRepository",userRepo);
        TestUtils.injectObject(cartController,"cartRepository",cartRepo);
        TestUtils.injectObject(cartController,"itemRepository",itemRepo);

        when(userRepo.findByUsername(MOCK_USERNAME)).thenReturn(getUser());
        when(itemRepo.findById(MOCK_ITEM_ID)).thenReturn(getItem());
    }

    @Test
    public void testAddItemToCart() {
        int expectedQuantity = MOCK_QUANTITY + 1;
        BigDecimal expectedTotal = new BigDecimal(MOCK_PRICE).multiply(BigDecimal.valueOf(expectedQuantity));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(MOCK_USERNAME);
        modifyCartRequest.setItemId(MOCK_ITEM_ID);
        modifyCartRequest.setQuantity(MOCK_QUANTITY);

        ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);
        Cart cart = response.getBody();

        assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        assertNotNull(cart);
        assertEquals(MOCK_USERNAME, cart.getUser().getUsername() );
        assertEquals(expectedQuantity, cart.getItems().size());
        assertEquals(expectedTotal, cart.getTotal());
    }

    @Test
    public void testInvalidUsername() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(MOCK_INVALID_USERNAME);
        modifyCartRequest.setItemId(MOCK_ITEM_ID);
        modifyCartRequest.setQuantity(MOCK_QUANTITY);

        ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);
        assertEquals(HttpStatus.NOT_FOUND.value(),response.getStatusCodeValue());
    }

    @Test
    public void testInvalidItem() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(MOCK_USERNAME);
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setQuantity(MOCK_QUANTITY);

        ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void testRemoveItemFromCat() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(MOCK_USERNAME);
        modifyCartRequest.setItemId(MOCK_ITEM_ID);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);
        Cart cart = response.getBody();

        assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        assertNotNull(cart);
        assertEquals(MOCK_USERNAME,cart.getUser().getUsername());
        assertEquals(0,cart.getItems().size());
        assertEquals(0,cart.getTotal().intValue());
    }

    @Test
    public void testRemoveItemFromCatWithInvalidUsername() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(MOCK_INVALID_USERNAME);
        modifyCartRequest.setItemId(MOCK_ITEM_ID);
        modifyCartRequest.setQuantity(MOCK_QUANTITY);

        ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }




    private static User getUser() {
        User user = new User();
        user.setUsername(MOCK_USERNAME);
        user.setCart(getCart(user));
        return user;
    }

    private static Cart getCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.addItem(getItem().orElse(null));
        return cart;
    }

    private static Optional<Item> getItem() {
        Item item = new Item();
        item.setId(MOCK_ITEM_ID);
        item.setPrice(new BigDecimal(MOCK_PRICE));
        return Optional.of(item);
    }


}
