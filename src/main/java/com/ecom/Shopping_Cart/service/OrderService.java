package com.ecom.Shopping_Cart.service;

import com.ecom.Shopping_Cart.model.OrderRequest;
import com.ecom.Shopping_Cart.model.ProductOrder;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    void saveOrder(int userId, OrderRequest orderRequest) throws Exception;

    List<ProductOrder> getOrderByUser(int userId);

    ProductOrder UpdateOrderStatus(int id, String status);

//    List<ProductOrder> getAllOrders();

    ProductOrder getOrderByOrderId(String orderId);

    Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize);

    Integer countOrder();

    Double totalOrderRevenue();

}
