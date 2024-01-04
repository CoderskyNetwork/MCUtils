package net.codersky.mcutils.time;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.index.qual.Positive;

/**
 * A similar {@code enum} to {@link TimeUnit}, except it
 * handles time units that can be converted to in-game
 * ticks only. Note that this {@code enum} is not intended
 * to be used to calculate dates but rather to create
 * {@link BukkitTask tasks} or {@link Timer timers}.
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 */
public enum MCTimeUnit {

	/** Represents in-game ticks, which are 20 per second. */
	TICKS,
	/** Represents real time seconds (20 ticks per unit). */
	SECONDS,
	/** Represents real time minutes (1200 ticks per unit). */
	MINUTES,
	/** Represents real time hours (72000 ticks per unit). */
	HOURS;

	/**
	 * Converts any <b>amount</b> of this {@link MCTimeUnit} to
	 * {@link MCTimeUnit#TICKS ticks}.
	 * 
	 * @param amount The amount of this {@link MCTimeUnit} to convert
	 * to {@link MCTimeUnit#TICKS ticks}.
	 * 
	 * @return The specified <b>amount</b> of this {@link MCTimeUnit} converted
	 * to {@link MCTimeUnit#TICKS ticks}. 0 if <b>amount</b> is minor or
	 * equal to 0.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public long toTicks(@Positive long amount) {
		if (amount <= 0)
			return 0;
		return switch (this) {
		case TICKS -> amount;
		case SECONDS -> amount * 20;
		case MINUTES -> amount * 1200;
		case HOURS -> amount * 7200;
		};
	}

	/**
	 * Converts any <b>amount</b> of this {@link MCTimeUnit} to
	 * {@link MCTimeUnit#SECONDS seconds}.
	 * 
	 * @param amount the amount of this {@link MCTimeUnit} to convert
	 * to {@link MCTimeUnit#SECONDS seconds}.
	 * 
	 * @return The specified <b>amount</b> of this {@link MCTimeUnit} converted
	 * to {@link MCTimeUnit#SECONDS seconds}. 0 if <b>amount</b> is minor or
	 * equal to 0.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public long toSeconds(@Positive long amount) {
		if (amount <= 0)
			return 0;
		return switch (this) {
		case TICKS -> amount / 20;
		case SECONDS -> amount;
		case MINUTES -> amount * 60;
		case HOURS -> amount * 60 * 60;
		};
	}

	/**
	 * Converts any <b>amount</b> of this {@link MCTimeUnit} to
	 * {@link MCTimeUnit#MINUTES minutes}.
	 * 
	 * @param amount the amount of this {@link MCTimeUnit} to convert
	 * to {@link MCTimeUnit#MINUTES minutes}.
	 * 
	 * @return The specified <b>amount</b> of this {@link MCTimeUnit} converted
	 * to {@link MCTimeUnit#MINUTES minutes}. 0 if <b>amount</b> is minor or
	 * equal to 0.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public long toMinutes(@Positive long amount) {
		if (amount <= 0)
			return 0;
		return switch (this) {
		case TICKS -> amount / 60 / 20;
		case SECONDS -> amount / 60;
		case MINUTES -> amount;
		case HOURS -> amount * 60;
		};
	}

	/**
	 * Converts any <b>amount</b> of this {@link MCTimeUnit} to
	 * {@link MCTimeUnit#HOURS hours}.
	 * 
	 * @param amount the amount of this {@link MCTimeUnit} to convert
	 * to {@link MCTimeUnit#HOURS hours}.
	 * 
	 * @return The specified <b>amount</b> of this {@link MCTimeUnit} converted
	 * to {@link MCTimeUnit#HOURS hours}. 0 if <b>amount</b> is minor or
	 * equal to 0.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public long toHours(@Positive long amount) {
		if (amount <= 0)
			return 0;
		return switch (this) {
		case TICKS -> amount / 60 / 60 / 20;
		case SECONDS -> amount / 60 / 60;
		case MINUTES -> amount / 60;
		case HOURS -> amount;
		};
	}

	/**
	 * Converts any <b>amount</b> of this {@link MCTimeUnit} to
	 * other <b>unit</b> by using the corresponding method.
	 * 
	 * @param amount the amount of this {@link MCTimeUnit} to convert
	 * to another <b>unit</b>.
	 * @param unit the {@link MCTimeUnit} to convert to.
	 * 
	 * @return The specified <b>amount</b> of this {@link MCTimeUnit} converted
	 * to the specified <b>unit</b>. 0 if <b>amount</b> is minor or
	 * equal to 0.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public long toUnit(@Positive long amount, @Nonnull MCTimeUnit unit) {
		if (amount <= 0)
			return 0;
		return switch (unit) {
		case TICKS -> toTicks(amount);
		case SECONDS -> toSeconds(amount);
		case MINUTES -> toMinutes(amount);
		case HOURS -> toHours(amount);
		};
	}
}
