package sk.myshop.app.server.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Execution of annotated method will cause the given cache entry to be flushed
 * (cache key is based on method arguments).
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FlushCacheEntry {

    /**
     * @return Cache name.
     */
    String value();

}
