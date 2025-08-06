package com.ecom.Shopping_Cart.repository;

import com.ecom.Shopping_Cart.model.ProductOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {
    List<ProductOrder> findByUserId(int userId);

    ProductOrder findByOrderId(String orderId);

    Page<ProductOrder> findAll(Pageable pageable);

    @Query("SELECT COUNT(o) FROM ProductOrder o WHERE o.status='Order Received' OR o.status='Delivered'")
    Integer countOrder();

    @Query("SELECT SUM(o.price*o.quantity) FROM ProductOrder o WHERE o.status='Delivered'")
    Double totalOrderRevenue();

}
