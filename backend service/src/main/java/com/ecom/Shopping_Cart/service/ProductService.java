package com.ecom.Shopping_Cart.service;

import com.ecom.Shopping_Cart.model.Product;
import org.springframework.data.domain.Page;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    Product saveProduct(Product product);

//    List<Product> getAllProducts();

    Boolean deleteProduct(int id);

    Product getProductById(int id);

    Product updateProduct(Product product, MultipartFile image);

    List<Product> getAllIsActiveProduct(String category);

    List<Product> searchProduct(String ch);

    Page<Product> getAllActiveProductPagination(Integer pageNo, Integer pageSize,String category);

    Page<Product> searchProductPagination(Integer pageNo, Integer pageSize,String ch);

    Page<Product> getAllProductsPagination(Integer pageNo, Integer pageSize);

    Page<Product> searchActiveProductPagination(Integer pageNo, Integer pageSize, String category,String ch);

    Integer countProduct();

    Integer totalProductStock();

}
