package net.codersky.mcutils;

/**
 * Enum containing all platforms supported by MCUtils
 *
 * @since MCUtils 1.0.0
 *
 * @author xDec0de_
 */
public enum MCPlatform {

	/** The Spigot server platform, <b>only</b> for Spigot servers that aren't Paper or a Paper fork. */
	SPIGOT,
	/** The Velocity proxy platform, for Velocity and forks of it. */
	VELOCITY;

	/**
	 * Checks if this {@link MCPlatform} is a server platform.
	 *
	 * @return {@code true} if this {@link MCPlatform} is a server platform,
	 * {@code false} otherwise (See {@link #isProxy()}).
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #isProxy()
	 */
	public boolean isServer() {
		return !isProxy();
	}

	/**
	 * Checks if this {@link MCPlatform} is a proxy platform.
	 *
	 * @return {@code true} if this {@link MCPlatform} is a proxy platform,
	 * {@code false} otherwise (See {@link #isServer()}).
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #isServer()
	 */
	public boolean isProxy() {
		return this == VELOCITY;
	}
}
