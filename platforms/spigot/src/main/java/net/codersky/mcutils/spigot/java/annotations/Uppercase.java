package net.codersky.mcutils.spigot.java.annotations;

import java.lang.annotation.Documented;

/**
 * The annotated parameter or field must be UPPER case only and of course not
 * {@code null}. This can be applied to any text based parameter
 * required by a constructor or method and implies that any input that
 * isn't fully upper case will not work correctly, optionally throwing
 * an exception if this condition isn't fulfilled.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 */
@Documented
public @interface Uppercase {
}
