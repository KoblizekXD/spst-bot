package dev.aa55h.util;

public final class Environment {
    private Environment() {}
    
    public static final String TOKEN = System.getenv("TOKEN");
    public static final String POSTGRES_DB = System.getenv("POSTGRES_DB");
    public static final String MEMCACHED_HOST = System.getenv("MEMCACHED_URL");
    public static final String LOG_CHANNEL_ID = System.getenv("LOG_CHANNEL_ID");
    public static final String VERIFIED_ROLE_ID = System.getenv("VERIFIED_ROLE_ID");
    public static final String UNVERIFIED_ROLE_ID = System.getenv("UNVERIFIED_ROLE_ID");
    public static final String GMAIL_OAUTH_TOKEN = System.getenv("GMAIL_OAUTH_TOKEN");
    public static final String CHANNEL_VERIFICATION_ID = System.getenv("CHANNEL_VERIFICATION_ID");
}
