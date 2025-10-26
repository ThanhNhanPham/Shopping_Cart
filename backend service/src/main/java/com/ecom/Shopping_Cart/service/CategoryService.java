package com.ecom.Shopping_Cart.service;

import com.ecom.Shopping_Cart.model.Category;

import java.util.List;

public interface CategoryService {


    Category saveCategory(Category category);

    Boolean existCategory(String name);

    List<Category> getAllCategory();

    Boolean deleteCategory(int id);

    Category getCategoryById(int id);

    List<Category> getAllActiveCategory();
    Integer countCategory();
}
