package net.codersky.mcutils.java.annotations;

import java.lang.annotation.Documented;

import org.bukkit.event.EventHandler;

/**
 * The annotated method, constructor or field is considered internal and should
 * not be used. This annotation may be added to accessible methods, constructors
 * or fields that are required to be accessible but should not
 * be treated as such. For example, an {@link EventHandler}
 * method is required <i>(Not really right now)</i> to be {@code public}
 * so it can be called internally, however, calling it directly should
 * be generally avoided unless this is explicitly allowed by the method.
 * This annotation exits to avoid confusion on this kind of scenarios where
 * the method is intended to be internal (And therefore not visible) but this may
 * not be clear at first glance without reading the whole documentation of it.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 */
@Documented
public @interface Internal {
}
