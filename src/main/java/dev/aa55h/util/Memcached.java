package dev.aa55h.util;

import net.spy.memcached.MemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public final class Memcached {

    private static final Logger log = LoggerFactory.getLogger(Memcached.class);
    private static Memcached instance;
    
    private MemcachedClient client;
    
    private Memcached() {
        try {
            client = new MemcachedClient(new InetSocketAddress(Environment.MEMCACHED_HOST.split(":")[0],
                    Integer.parseInt(Environment.MEMCACHED_HOST.split(":")[1])));
            log.info("Memcached instance created.");
        } catch (IOException e) {
            log.error("Failed to connect to Memcached server", e);
        }
    }
    
    public static Memcached getInstance() {
        if (instance == null) {
            instance = new Memcached();
        }
        return instance;
    }
    
    public String createCode(String email, long id) {
        Object existing = client.get(String.valueOf(id));
        if (existing != null) {
            return existing.toString().split("\\|")[0].toString();
        }
        
        String random = UniqueCodeGenerator.random();
        client.set(String.valueOf(id), 60 * 60, random + "|" + email + "|" + id);
        return random;
    }
    
    public String invalidateCode(String id) {
        Object data = client.get(id);
        if (data != null) {
            client.delete(id);
            return data.toString();
        }
        return null;
    }
    
    public String getCodeData(String id) {
        Object data = client.get(id);
        return data != null ? data.toString() : null;
    }
    
    public void shutdown() {
        if (client != null) {
            client.shutdown();
            log.info("Memcached client shutdown successfully.");
        }
    }
}
