package com.ecom.Shopping_Cart.controller;

import com.ecom.Shopping_Cart.model.UserDtls;
import com.ecom.Shopping_Cart.security.JwtUtil;
import com.ecom.Shopping_Cart.security.TokenType;
import com.ecom.Shopping_Cart.security.UserTokenService;
import com.ecom.Shopping_Cart.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.time.Duration;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserTokenService tokenService;
    private final UserService userService;
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestParam("username") String email,
                                      @RequestParam String password,
                                      HttpServletRequest req,
                                      HttpServletResponse resp,
                                      RedirectAttributes ra) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
            UserDetails user = (UserDetails) auth.getPrincipal();
            UserDtls userEntity = userService.getUserByEmail(user.getUsername());

            String access = jwtUtil.generateAccessToken(user);
            String refresh = jwtUtil.generateRefreshToken(user);

            // Lưu refresh token (hash) vào DB
            tokenService.store(
                    userEntity,
                    TokenType.REFRESH,
                    jwtUtil.extractJti(refresh),
                    refresh,
                    req.getHeader("User-Agent"),
                    req.getRemoteAddr(),
                    jwtUtil.toInstant(jwtUtil.extractExpiration(refresh))
            );
            addCookie(resp, "accessToken", access, "/", Duration.ofSeconds(900));
            addCookie(resp, "refreshToken", refresh, "/auth", Duration.ofDays(30));

            // Đăng nhập thành công → redirect về trang chủ
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/"))
                    .build();

        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            ra.addFlashAttribute("sucMsg", "Sai username hoặc password");
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/signin"))
                    .build();
        } catch (Exception e) {
            // Trường hợp lỗi khác → redirect về signin kèm param generic error
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/signin?error=server"))
                    .build();
        }
    }
    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(@CookieValue(value = "refreshToken", required = false) String rt,
                                        HttpServletRequest req,
                                        HttpServletResponse resp) {
        if (rt == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        // Kiểm tra refresh ở DB (hash + expiry + revoked)
        String jtiOld = jwtUtil.extractJti(rt);
        String username = jwtUtil.extractUsername(rt);

        if (!tokenService.validateRefresh(jtiOld, rt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // Sinh cặp mới + rotate
        var user = (UserDetails) new org.springframework.security.core.userdetails.User(username, "", java.util.List.of());
        String accessNew = jwtUtil.generateAccessToken(user);
        String refreshNew = jwtUtil.generateRefreshToken(user);
        tokenService.rotate(
                jtiOld,
                jwtUtil.extractJti(refreshNew),
                userService.getUserByEmail(username),
                refreshNew,
                jwtUtil.toInstant(jwtUtil.extractExpiration(refreshNew)),
                req.getHeader("User-Agent"),
                req.getRemoteAddr()
        );

        addCookie(resp, "accessToken", accessNew, "/", Duration.ofSeconds(900));
        addCookie(resp, "refreshToken", refreshNew, "/auth", Duration.ofDays(30));
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(value = "refreshToken", required = false) String rt,
                                       HttpServletResponse resp) {
        if (rt != null) tokenService.revoke(jwtUtil.extractJti(rt));
        clearCookie(resp, "accessToken", "/");
        clearCookie(resp, "refreshToken", "/auth");
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(java.net.URI.create("/signin"))
                .build();
    }

    // addCookie để thêm cookie vào response
    private static void addCookie(HttpServletResponse resp, String name, String value, String path, Duration maxAge) {
        ResponseCookie c = ResponseCookie.from(name, value)
                .httpOnly(true).secure(false).sameSite("Strict").path(path).maxAge(maxAge).build();
        resp.addHeader(HttpHeaders.SET_COOKIE, c.toString());
    }
    private static void clearCookie(HttpServletResponse resp, String name, String path) {
        ResponseCookie c = ResponseCookie.from(name, "").httpOnly(true).secure(true).sameSite("Strict")
                .path(path).maxAge(Duration.ZERO).build();
        resp.addHeader(HttpHeaders.SET_COOKIE, c.toString());
    }

}
