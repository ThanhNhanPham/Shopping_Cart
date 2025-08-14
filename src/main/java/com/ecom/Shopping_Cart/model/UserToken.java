package com.ecom.Shopping_Cart.model;

import com.ecom.Shopping_Cart.security.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_token",
        indexes = {
                @Index(columnList = "jti", unique = true),
                @Index(columnList = "expiresAt"),
                @Index(columnList = "type"),
                @Index(columnList = "user_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private UserDtls user;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 10)
    private TokenType type;

    @Column(nullable = false, unique = true, length = 64)
    private String jti;
    @Column(nullable = false, length = 128)

    private String tokenHash; // hash của token

    @Column(name = "created_at", columnDefinition = "datetime(6) not null")
    private LocalDateTime createdAt;
    @Column(name = "expires_at",columnDefinition = "datetime(6) not null")
    private LocalDateTime expiresAt;
    @Column(nullable = false)  private boolean revoked=false;

    @Column(length = 64)
    private String replacedBy;

    @Column(length = 255)
    private String userAgent;

    @Column(length = 45)
    private String ip;
}
