package com.alchin.shortlink.link;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class ShortLinkService {
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final SecureRandom random = new SecureRandom();
    private final ShortLinkRepository repository;

    public ShortLinkService(ShortLinkRepository repository) {
        this.repository = repository;
    }

    public ShortLink create(String originalUrl, LocalDateTime expiresAt) {
        if (originalUrl == null || !(originalUrl.startsWith("http://") || originalUrl.startsWith("https://"))) {
            throw new IllegalArgumentException("URL must start with http:// or https://");
        }
        if (expiresAt != null && !expiresAt.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Expiration must be in the future");
        }
        return repository.save(new ShortLink(originalUrl, nextCode(), expiresAt));
    }

    @Transactional
    public ShortLink resolve(String code) {
        ShortLink link = repository.findByCode(code)
                .orElseThrow(() -> new LinkNotFoundException("Short link not found"));
        if (link.isExpired()) {
            throw new LinkExpiredException("Short link has expired");
        }
        link.recordClick();
        return link;
    }

    public ShortLink stats(String code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new LinkNotFoundException("Short link not found"));
    }

    private String nextCode() {
        String code;
        do {
            StringBuilder builder = new StringBuilder(7);
            for (int i = 0; i < 7; i++) builder.append(CHARS.charAt(random.nextInt(CHARS.length())));
            code = builder.toString();
        } while (repository.existsByCode(code));
        return code;
    }
}
