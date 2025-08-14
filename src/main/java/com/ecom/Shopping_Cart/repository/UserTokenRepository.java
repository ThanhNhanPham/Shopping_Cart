package com.ecom.Shopping_Cart.repository;

import com.ecom.Shopping_Cart.model.UserToken;
import com.ecom.Shopping_Cart.security.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken,Long> {
    Optional<UserToken> findByJtiAndType(String jti, TokenType type);
    @Modifying
    @Query("update UserToken t set t.revoked = true where t.user.id = :userId and t.revoked = false")
    int revokeAllByUser(@Param("userId") int userId);

}
