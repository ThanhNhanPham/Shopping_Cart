package com.ecom.Shopping_Cart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class UserDtls {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String mobileNumber;

    private String email;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private String password;

    private String profileImage;

    private String role;

    private Boolean isEnable;

    private Boolean accountNonLocked;

    private int failedAttempt;

    private Date lockTime;

    private String resetToken;

    @Column(name="reset_token_expiry")
    private Instant resetTokenExpiry;
}
