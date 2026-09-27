package com.alchin.shortlink.link;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShortLinkServiceTest {
    @Mock ShortLinkRepository repository;
    @InjectMocks ShortLinkService service;

    @Test
    void createsValidLink() {
        when(repository.existsByCode(any())).thenReturn(false);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        ShortLink link = service.create("https://example.com/page", null);
        assertEquals("https://example.com/page", link.getOriginalUrl());
        assertEquals(7, link.getCode().length());
    }

    @Test
    void rejectsInvalidUrl() {
        assertThrows(IllegalArgumentException.class, () -> service.create("example.com", null));
    }

    @Test
    void rejectsPastExpiration() {
        assertThrows(IllegalArgumentException.class,
                () -> service.create("https://example.com", LocalDateTime.now().minusMinutes(1)));
    }
}
