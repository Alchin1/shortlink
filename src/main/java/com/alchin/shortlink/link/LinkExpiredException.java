package com.alchin.shortlink.link;

public class LinkExpiredException extends RuntimeException {
    public LinkExpiredException(String message) { super(message); }
}
