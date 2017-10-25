package br.com.events.security;

public class SecurityConstants {
    public static final String SECRET = "$1$FZQY4nDk$i8c5Ls7Lgid/fGspQA0Jt/";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "eve";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/sign-up";
}