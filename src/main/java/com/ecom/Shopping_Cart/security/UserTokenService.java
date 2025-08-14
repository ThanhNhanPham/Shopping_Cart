package com.ecom.Shopping_Cart.security;

import com.ecom.Shopping_Cart.model.UserDtls;
import com.ecom.Shopping_Cart.model.UserToken;
import com.ecom.Shopping_Cart.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;

import static java.time.Instant.now;

@Service
@RequiredArgsConstructor
public class UserTokenService {
    private final UserTokenRepository userTokenRepository;

    public static String sha256(String raw) {
        try {
            var md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(raw.getBytes()));
        } catch (Exception e) { throw new RuntimeException(e); }
    }
    @Transactional
    public void store(UserDtls user, TokenType type, String jti, String tokenRaw,
                      String ua, String ip, Instant expiresAt) {
        ZoneId VN = ZoneId.of("Asia/Ho_Chi_Minh");
        var t = new UserToken();
        t.setUser(user);
        t.setType(type);
        t.setJti(jti);
        t.setTokenHash(sha256(tokenRaw));// múi giờ của server chạy code
        t.setCreatedAt(LocalDateTime.now(VN)); // nếu muốn cố định VN
        t.setExpiresAt(LocalDateTime.ofInstant(expiresAt, VN));
        t.setUserAgent(ua);
        t.setIp(ip);
        userTokenRepository.save(t);
    }
    public boolean validateRefresh(String jti, String rawToken) {
        return userTokenRepository.findByJtiAndType(jti, TokenType.REFRESH)
                .filter(t -> !t.isRevoked() && t.getExpiresAt().isAfter(ChronoLocalDateTime.from(now())))
                .filter(t -> t.getTokenHash().equals(sha256(rawToken)))
                .isPresent();
    }
    @Transactional
    public void rotate(String oldJti, String newJti, UserDtls user, String newRaw, Instant newExp,
                       String ua, String ip) {
        userTokenRepository.findByJtiAndType(oldJti, TokenType.REFRESH).ifPresent(t -> {
            t.setRevoked(true);
            t.setReplacedBy(newJti);
        });
        store(user, TokenType.REFRESH, newJti, newRaw, ua, ip, newExp);
    }
    @Transactional
    public void revoke(String jti) {
        userTokenRepository.findByJtiAndType(jti, TokenType.REFRESH).ifPresent(t -> t.setRevoked(true));
    }

    @Transactional
    public int revokeAllByUser(int userId) {
        return userTokenRepository.revokeAllByUser(userId);
    }
}
