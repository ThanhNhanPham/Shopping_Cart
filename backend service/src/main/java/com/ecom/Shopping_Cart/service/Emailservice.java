package com.ecom.Shopping_Cart.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class Emailservice {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.from}")
    private String from;
    public boolean sendPasswordReset(String to, String resetLink) {
        try {
            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper h = new MimeMessageHelper(mime, "UTF-8");
            h.setFrom(from);
            h.setTo(to);
            h.setSubject("Đặt lại mật khẩu");
            h.setText(
                    """
                    <p>Chào bạn,</p>
                    <p>Nhấn vào liên kết dưới đây để đặt lại mật khẩu của bạn:</p>
                    <p><a href="%s">%s</a></p>
                    <p>Nếu bạn không yêu cầu, hãy bỏ qua email này.</p>
                    """.formatted(resetLink, resetLink),
                    true // HTML
            );
            mailSender.send(mime);
            return true;
        } catch (Exception e) {
            log.error("Send mail failed to {}: {}", to, e.getMessage(), e);
            return false;
        }
    }
}
