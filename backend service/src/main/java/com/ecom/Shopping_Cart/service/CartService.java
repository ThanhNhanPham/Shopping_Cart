package com.ecom.Shopping_Cart.service;

import com.ecom.Shopping_Cart.model.Cart;

import java.util.List;

public interface CartService {

    Cart saveCart(Integer productId, Integer userId);

    List<Cart> getCartByUser(int userId);

    Integer getCountCart(Integer userId);

    void updateQuantity(String sy, int cid);

}
