package com.ecom.Shopping_Cart.repository;

import com.ecom.Shopping_Cart.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Boolean existsByName(String name);

    List<Category> findByIsActiveTrue();

    @Query("SELECT COUNT(c) FROM Category c")
    Integer totalCategory();

}
