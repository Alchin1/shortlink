package com.alchin.shortlink.link;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
public class ShortLinkController {
    private final ShortLinkService service;

    public ShortLinkController(ShortLinkService service) {
        this.service = service;
    }

    @PostMapping("/api/links")
    public ResponseEntity<CreateLinkResponse> create(@RequestBody CreateLinkRequest request, HttpServletRequest servletRequest) {
        ShortLink link = service.create(request.url(), request.expiresAt());
        String base = servletRequest.getRequestURL().toString().replace(servletRequest.getRequestURI(), "");
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CreateLinkResponse(link.getCode(), base + "/" + link.getCode(), link.getOriginalUrl(), link.getExpiresAt())
        );
    }

    @GetMapping("/{code:[a-zA-Z0-9]+}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        ShortLink link = service.resolve(code);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, URI.create(link.getOriginalUrl()).toString())
                .build();
    }

    @GetMapping("/api/links/{code}")
    public LinkStatsResponse stats(@PathVariable String code) {
        ShortLink link = service.stats(code);
        return new LinkStatsResponse(link.getCode(), link.getOriginalUrl(), link.getCreatedAt(), link.getExpiresAt(), link.getClickCount());
    }

    public record CreateLinkRequest(String url, LocalDateTime expiresAt) {}
    public record CreateLinkResponse(String code, String shortUrl, String originalUrl, LocalDateTime expiresAt) {}
    public record LinkStatsResponse(String code, String originalUrl, LocalDateTime createdAt, LocalDateTime expiresAt, long clicks) {}
}
