package sk.myshop.app.server.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Execution of annotated method will cause the given cache to be flushed (all
 * cache entries will be removed).
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FlushCache {

    /**
     * @return Cache name.
     */
    String value();

}
