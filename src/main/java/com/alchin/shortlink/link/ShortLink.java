package com.alchin.shortlink.link;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "short_links")
public class ShortLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String originalUrl;

    @Column(nullable = false, unique = true, length = 12)
    private String code;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private long clickCount;

    protected ShortLink() {}

    public ShortLink(String originalUrl, String code, LocalDateTime expiresAt) {
        this.originalUrl = originalUrl;
        this.code = code;
        this.expiresAt = expiresAt;
        this.createdAt = LocalDateTime.now();
        this.clickCount = 0;
    }

    public Long getId() { return id; }
    public String getOriginalUrl() { return originalUrl; }
    public String getCode() { return code; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public long getClickCount() { return clickCount; }
    public void recordClick() { clickCount++; }
    public boolean isExpired() { return expiresAt != null && LocalDateTime.now().isAfter(expiresAt); }
}
