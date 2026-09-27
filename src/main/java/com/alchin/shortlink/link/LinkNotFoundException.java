package com.alchin.shortlink.link;

public class LinkNotFoundException extends RuntimeException {
    public LinkNotFoundException(String message) { super(message); }
}
