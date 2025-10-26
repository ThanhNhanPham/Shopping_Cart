package com.ecom.Shopping_Cart.security;

import com.ecom.Shopping_Cart.model.UserDtls;
import com.ecom.Shopping_Cart.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDtls u = userService.getUserByEmail(email);
        if (u == null) throw new UsernameNotFoundException("User not found");
        return new User(u.getEmail(), u.getPassword(),
                u.getIsEnable() != null ? u.getIsEnable() : true,
                true,
                true,
                u.getAccountNonLocked() != null ? u.getAccountNonLocked() : true,
                List.of(new SimpleGrantedAuthority(u.getRole() != null ? u.getRole() : "ROLE_USER")));
    }
}
