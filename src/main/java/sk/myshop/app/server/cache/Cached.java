package sk.myshop.app.server.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotated method will have its results cached in a named cache.
 * <p>
 * Method arguments should implement hashCode in a reliable way (result doesn't
 * change during the lifespan of the object). Method return value should be
 * serializable.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cached {

    /**
     * @return Cache name.
     */
    String value();

}
