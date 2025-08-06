package com.ecom.Shopping_Cart.repository;

import com.ecom.Shopping_Cart.model.UserDtls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserDtls, Integer> {

    UserDtls findByEmail(String email);

    List<UserDtls> findByRole(String role);

    Boolean existsByEmail(String email);

    @Query("SELECT COUNT(u) FROM UserDtls u")
    Integer totalUsers();

}


