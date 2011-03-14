package sk.myshop.app.server.cache;

import java.io.Serializable;
import java.util.Collections;

import sk.myshop.app.server.cache.util.HashCodeCacheKey;
import sk.myshop.app.server.cache.util.HashCodeCalculator;
import sk.myshop.app.server.cache.util.Objects;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheManager;

public aspect CachingAspect {

    declare soft: CacheException: execution(net.sf.jsr107cache..* *.*(..));

    private pointcut cachedMethodExecution(Cached cached):
        execution(* *.*(..)) && @annotation(cached);

    private pointcut flushCacheMethodExecution(FlushCache cached):
        execution(* *.*(..)) && @annotation(cached);

    private pointcut flushCacheEntryMethodExecution(FlushCacheEntry cached):
        execution(* *.*(..)) && @annotation(cached);

    Object around(Cached cached): cachedMethodExecution(cached) {
        Cache cache = getCache(cached.value());
        Serializable cacheKey = getCacheKey(thisJoinPoint.getArgs());
        Object cachedValue = cache.get(cacheKey);

        if (cachedValue == null) {
            cachedValue = proceed(cached);
            cache.put(cacheKey, cachedValue);
        }

        return cachedValue;
    }

    after(FlushCache cached): flushCacheMethodExecution(cached) {
        Cache cache = getCache(cached.value());
        cache.clear();
    }

    after(FlushCacheEntry cached): flushCacheEntryMethodExecution(cached) {
        Cache cache = getCache(cached.value());
        Serializable cacheKey = getCacheKey(thisJoinPoint.getArgs());
        cache.remove(cacheKey);
    }

    protected Cache getCache(String cacheName) {
        CacheManager cacheManager = CacheManager.getInstance();
        Cache cache = cacheManager.getCache(cacheName);

        if (cache == null) {
            cache = cacheManager.getCacheFactory().createCache(Collections.emptyMap());
            cacheManager.registerCache(cacheName, cache);
        }

        return cache;
    }

    protected Serializable getCacheKey(Object[] args) {
        Serializable cacheKey;

        // Optimization: if there is only one Serializable argument, use it as
        // the key
        if (args.length == 1 && args[0] instanceof Serializable) {
            cacheKey = (Serializable) args[0];
        }

        // Generic method: calculate hashCode and checkSum for all arguments
        else {
            HashCodeCalculator calculator = new HashCodeCalculator();
            for (Object object : args) {
                int hash = Objects.nullSafeHashCode(object);
                calculator.append(hash);
            }

            cacheKey = new HashCodeCacheKey(calculator.getCheckSum(), calculator.getHashCode());
        }

        return cacheKey;
    }

}
