package net.codersky.mcutils.time;

import java.util.Objects;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.index.qual.NonNegative;

import net.codersky.mcutils.java.strings.replacers.Replacement;

/**
 * A class that keeps track of hours, minutes and seconds,
 * supporting {@link MCTimeUnit} and task creation. Implementing
 * the {@link Replacement} (With {@link #toString()}) and
 * {@link Cloneable} interfaces.
 * 
 * @since MCUtils 1.0.0
 *
 * @author xDec0de_
 * 
 * @see #Timer(int, int, int)
 * @see #toString(CharSequence, boolean)
 * @see #add(MCTimeUnit, int)
 * @see #tick()
 * @see #remove(MCTimeUnit, int)
 * @see #hasEnded()
 * @see #schedule(Plugin, Runnable)
 * @see #schedule(Plugin, Consumer, Object)
 */
public class Timer implements Replacement, Cloneable {

	/** Array of size 3: <p> 0: Hours <br> 1: Minutes <br> 2: Seconds */
	private final int[] time = {0, 0, 0};

	/**
	 * Creates a new {@link Timer} with a set time by using {@link #add(MCTimeUnit, int)}.
	 * 
	 * @param unit the {@link MCTimeUnit} to use.
	 * @param amount the amount of said <b>unit</b> to add.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @throws NullPointerException if <b>unit</b> is {@code null}.
	 */
	public Timer(@Nonnull MCTimeUnit unit, @NonNegative int amount) {
		add(unit, amount);
	}

	/**
	 * Creates a new {@link Timer} with a set amount of {@link #setHours(int) hours},
	 * {@link #addMinutes(int) minutes} and {@link #addSeconds(int) seconds}. If any
	 * unit except <b>hours</b> is higher than 59 time is still added as expected.
	 * 
	 * @param hours the amount of hours to add, this can in theory be as much as {@link Integer#MAX_VALUE}.
	 * @param minutes the amount of minutes to add. Limited to 59, excess is converted to hours.
	 * @param seconds the amount of seconds to add. Limited to 59, excess is converted to minutes or hours.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public Timer(int hours, int minutes, int seconds) {
		setHours(hours).addMinutes(minutes).addSeconds(seconds);
	}

	/**
	 * Creates a new {@link Timer} with a set amount of {@link #addMinutes(int) minutes} and
	 * {@link #addSeconds(int) seconds}. If any unit is higher than 59 time is still added as expected.
	 * 
	 * @param minutes the amount of minutes to add. Limited to 59, excess is converted to hours.
	 * @param seconds the amount of seconds to add. Limited to 59, excess is converted to minutes or hours.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public Timer(int minutes, int seconds) {
		this(0, minutes, seconds);
	}

	/**
	 * Creates a new {@link Timer} with a set amount of {@link #addSeconds(int) seconds}.
	 * 
	 * @param seconds the amount of seconds to add. Limited to 59, excess is converted to minutes or hours.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public Timer(int seconds) {
		this(0, 0, seconds);
	}

	/*
	 * Add time
	 */

	/**
	 * Adds time to this {@link Timer} with the corresponding multiplier
	 * of the specified <b>unit</b>.
	 * 
	 * @param unit the {@link MCTimeUnit} to use.
	 * @param amount the amount of <b>unit</b>s to add.
	 * 
	 * @return This {@link Timer}.
	 * 
	 * @throws NullPointerException if <b>unit</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public Timer add(@Nonnull MCTimeUnit unit, @NonNegative int amount) {
		return switch(unit) {
		case TICKS -> addTicks(amount);
		case SECONDS -> addSeconds(amount);
		case MINUTES -> addMinutes(amount);
		case HOURS -> addHours(amount);
		};
	}

	/**
	 * Adds the specified <b>amount</b> of {@link MCTimeUnit#TICKS ticks} to
	 * this {@link Timer}. Note that {@link Timer timers} only allow precision
	 * up to {@link MCTimeUnit#SECONDS seconds}, so this method converts
	 * <b>amount</b> to {@link MCTimeUnit#SECONDS seconds} and then calls
	 * {@link #addSeconds(int)}, losing precision when converting.
	 * 
	 * @param amount The amount of ticks to add.
	 * 
	 * @return This {@link Timer}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public Timer addTicks(@NonNegative int amount) {
		return addSeconds(amount / 20);
	}

	/**
	 * Adds the specified <b>amount</b> of {@link MCTimeUnit#SECONDS seconds} to
	 * this {@link Timer}. Amounts lower or equal to 0 will be ignored while
	 * amounts higher than 59 will be added without issues.
	 * 
	 * @param amount the amount of seconds to add.
	 * 
	 * @return This {@link Timer}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public Timer addSeconds(@NonNegative int amount) {
		if (amount <= 0)
			return this;
		final int totalSecs = amount + time[2];
		if (totalSecs < 60) {
			time[2] = totalSecs;
			return this;
		}
		final int minutes = totalSecs / 60;
		time[2] = totalSecs - (minutes * 60);
		return addMinutes(minutes);
	}

	/**
	 * Adds the specified <b>amount</b> of {@link MCTimeUnit#MINUTES minutes} to
	 * this {@link Timer}. Amounts lower or equal to 0 will be ignored while
	 * amounts higher than 59 will be added without issues.
	 * 
	 * @param amount the amount of minutes to add.
	 * 
	 * @return This {@link Timer}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public Timer addMinutes(@NonNegative int amount) {
		if (amount <= 0)
			return this;
		final int totalMins = amount + time[1];
		if (totalMins < 60) {
			time[1] = totalMins;
			return this;
		}
		final int hours = totalMins / 60;
		time[0] += hours;
		time[1] = totalMins - (hours * 60);
		return this;
	}

	/**
	 * Adds the specified <b>amount</b> of {@link MCTimeUnit#HOURS hours} to
	 * this {@link Timer}. Amounts lower or equal to 0 will be ignored while
	 * amounts higher than 59 will be added without issues.
	 * 
	 * @param amount the amount of hours to add.
	 * 
	 * @return This {@link Timer}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public Timer addHours(@NonNegative int amount) {
		if (amount > 0)
			time[0] += amount;
		return this;
	}

	/*
	 * Time removal
	 */

	/**
	 * Removes one second from this {@link Timer} in a much more
	 * optimized way than calling {@link #removeSeconds(int)} with
	 * 1 second as no multiplications nor divisions are used here.
	 * 
	 * @return This {@link Timer}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public Timer tick() {
		if (time[2] > 0) {
			time[2]--;
			return this;
		} else if (time[1] > 0) {
			time[2] = 59;
			time[1]--;
		} else if (time[0] > 0) {
			time[2] = 59;
			time[1] = 59;
			time[0]--;
		}
		return this;
	}

	/**
	 * Removes time from this {@link Timer} with the corresponding multiplier
	 * of the specified <b>unit</b>. Note that removing more time that the
	 * amount that this timer has will result on it being set to zero.
	 * See the individual methods of each {@link MCTimeUnit unit} for more
	 * details.
	 * 
	 * @param unit the {@link MCTimeUnit} to use.
	 * @param amount the amount of <b>unit</b>s to remove.
	 * 
	 * @return This {@link Timer}.
	 * 
	 * @throws NullPointerException if <b>unit</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #removeTicks(int)
	 * @see #removeSeconds(int)
	 * @see #removeMinutes(int)
	 * @see #removeHours(int)
	 */
	@Nonnull
	public Timer remove(@Nonnull MCTimeUnit unit, @NonNegative int amount) {
		return switch(unit) {
		case TICKS -> removeTicks(amount); 
		case SECONDS -> removeSeconds(amount); 
		case MINUTES -> removeMinutes(amount); 
		case HOURS -> removeHours(amount); 
		};
	}

	/**
	 * Removes a specific <b>amount</b> of {@link MCTimeUnit#TICKS ticks}
	 * from this {@link Timer}. Note that {@link Timer timers} don't store
	 * {@link MCTimeUnit#TICKS ticks}, so this method will actually use
	 * {@link #removeSeconds(int)} with <b>amount</b> / 20, so attempting
	 * to remove 30 {@link MCTimeUnit#TICKS ticks} will actually remove 20
	 * {@link MCTimeUnit#TICKS ticks} (1 {@link MCTimeUnit#SECOND second}).
	 * 
	 * @param amount the amount of {@link MCTimeUnit#TICKS ticks} to remove.
	 * 
	 * @return This {@link Timer}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public Timer removeTicks(@NonNegative int amount) {
		return removeSeconds(amount / 20);
	}

	/**
	 * Removes a specific <b>amount</b> of {@link MCTimeUnit#SECONDS seconds}
	 * from this {@link Timer}. If the <b>amount</b> to remove is higher
	 * than {@link #getTotalSeconds()}, the resulting amount will be 0. This method
	 * will also remove {@link MCTimeUnit#HOURS hours} and {@link MCTimeUnit#MINUTES minutes}
	 * from the {@link Timer} if
	 * necessary.
	 * 
	 * @param amount the amount of {@link MCTimeUnit#SECONDS seconds} to remove.
	 * 
	 * @return This {@link Timer}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public Timer removeSeconds(@NonNegative int amount) {
		if (amount <= 0)
			return this;
		final int totalSecs = time[2] - amount;
		if (totalSecs >= 0) {
			time[2] = totalSecs;
			return this;
		}
		time[2] = 0;
		return removeMinutes(+totalSecs / 60);
	}

	/**
	 * Removes a specific <b>amount</b> of {@link MCTimeUnit#MINUTES minutes}
	 * from this {@link Timer}. If the <b>amount</b> to remove is higher
	 * than {@link #getTotalMinutes()}, the resulting amount will be 0. This method
	 * will also remove {@link MCTimeUnit#HOURS hours} from the {@link Timer} if
	 * necessary.
	 * 
	 * @param amount the amount of {@link MCTimeUnit#MINUTES minutes} to remove.
	 * 
	 * @return This {@link Timer}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public Timer removeMinutes(@NonNegative int amount) {
		if (amount <= 0)
			return this;
		final int totalMins = time[1] - amount;
		if (totalMins >= 0) {
			time[1] = totalMins;
			return this;
		}
		time[1] = 0;
		return removeHours(+totalMins / 60);
	}

	/**
	 * Removes a specific <b>amount</b> of {@link MCTimeUnit#HOURS hours}
	 * from this {@link Timer}. If the <b>amount</b> to remove is higher
	 * than {@link #getHours()}, the resulting amount will be 0.
	 * 
	 * @param amount the amount of {@link MCTimeUnit#HOURS hours} to remove.
	 * 
	 * @return This {@link Timer}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public Timer removeHours(@NonNegative int amount) {
		final int totalHours = time[1] - amount;
		time[2] = totalHours < 0 ? 0 : totalHours;
		return this;
	}

	/*
	 * Time setters
	 */

	/**
	 * Sets the <b>amount</b> of {@link MCTimeUnit#SECONDS seconds} stored in
	 * this {@link Timer}. {@link MCTimeUnit#SECONDS seconds} can only
	 * range from 0 to 59, other amounts will be ignored.
	 * 
	 * @param amount the amount of {@link MCTimeUnit#SECONDS seconds} to set.
	 * 
	 * @return This {@link Timer}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public Timer setSeconds(@NonNegative int amount) {
		if (amount >= 0 && amount < 60)
			time[2] = amount;
		return this;
	}

	/**
	 * Sets the <b>amount</b> of {@link MCTimeUnit#MINUTES minutes} stored in
	 * this {@link Timer}. {@link MCTimeUnit#MINUTES Minutes} can only
	 * range from 0 to 59, other amounts will be ignored.
	 * 
	 * @param amount the amount of {@link MCTimeUnit#MINUTES minutes} to set.
	 * 
	 * @return This {@link Timer}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public Timer setMinutes(@NonNegative int amount) {
		if (amount >= 0 && amount < 60)
			time[1] = amount;
		return this;
	}

	/**
	 * Sets the <b>amount</b> of {@link MCTimeUnit#HOURS hours} stored in
	 * this {@link Timer}. {@link MCTimeUnit#HOURS Hours} can in
	 * theory be as much as {@link Integer#MAX_VALUE}. Amounts lower
	 * than 0 will be ignored.
	 * 
	 * @param amount the amount of {@link MCTimeUnit#HOURS hours} to set.
	 * 
	 * @return This {@link Timer}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public Timer setHours(@NonNegative int amount) {
		if (amount >= 0 && amount < 60)
			time[0] = amount;
		return this;
	}

	/*
	 * Time getters
	 */

	/**
	 * Gets the total amount of {@link MCTimeUnit#SECONDS seconds} stored on this
	 * {@link Timer} and multiplies that by 20 to convert to {@link MCTimeUnit#TICKS ticks}.
	 * That means this method will also take into account {@link MCTimeUnit#HOURS hours}
	 * and {@link MCTimeUnit#MINUTES minutes} stored on this {@link Timer}.
	 * <p>
	 * In other words, this is {@link #getTotalSeconds()} * 20.
	 * 
	 * @return The amount of {@link MCTimeUnit#HOURS hours} stored on this
	 * {@link Timer}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public int getTotalTicks() {
		return getTotalSeconds() * 20;
	}

	/**
	 * Gets the amount of {@link MCTimeUnit#SECONDS seconds} stored on this
	 * {@link Timer}. This amount will always be from 0 to 59. To get the total
	 * amount of {@link MCTimeUnit#SECONDS} use {@link #getTotalSeconds()}.
	 * 
	 * @return The amount of {@link MCTimeUnit#SECONDS seconds} stored on this
	 * {@link Timer}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public int getSeconds() {
		return time[2];
	}

	/**
	 * Gets the total amount of {@link MCTimeUnit#SECONDS seconds} stored on this
	 * {@link Timer}. That means this method will also take into account
	 * {@link MCTimeUnit#HOURS hours} and {@link MCTimeUnit#MINUTES minutes}
	 * stored on this {@link Timer}.
	 * 
	 * @return The amount of {@link MCTimeUnit#HOURS hours} stored on this
	 * {@link Timer}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public int getTotalSeconds() {
		return getTotalMinutes() * 60 + time[2];
	}

	/**
	 * Gets the amount of {@link MCTimeUnit#MINUTES minutes} stored on this
	 * {@link Timer}. This amount will always be from 0 to 59. To get the total
	 * amount of {@link MCTimeUnit#MINUTES minutes} use {@link #getTotalMinutes()}.
	 * 
	 * @return The amount of {@link MCTimeUnit#MINUTES minutes} stored on this
	 * {@link Timer}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public int getMinutes() {
		return time[1];
	}

	/**
	 * Gets the total amount of {@link MCTimeUnit#MINUTES minutes} stored on this
	 * {@link Timer}. That means this method will also take into account
	 * {@link MCTimeUnit#HOURS hours} stored on this {@link Timer}.
	 * 
	 * @return The amount of {@link MCTimeUnit#HOURS hours} stored on this
	 * {@link Timer}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public int getTotalMinutes() {
		return (time[0] * 60) + time[1];
	}

	/**
	 * Gets the amount of {@link MCTimeUnit#HOURS hours} stored on this
	 * {@link Timer}.
	 * 
	 * @return The amount of {@link MCTimeUnit#HOURS hours} stored on this
	 * {@link Timer}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public int getHours() {
		return time[0];
	}

	/*
	 * Utility
	 */

	/**
	 * Checks if this {@link Timer} has ended. A {@link Timer}
	 * is considered to be ended when {@link #getHours()}, {@link #getMinutes()}
	 * and {@link #getSeconds()} all return zero.
	 * 
	 * @return {@code true} if this {@link Timer} has ended, {@code false} otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public boolean hasEnded() {
		return time[2] == 0 && time[1] == 0 && time[0] == 0;
	}

	/*
	 * String conversion
	 */

	@Nonnull
	private void appendTime(StringBuilder builder, int time, boolean fill) {
		if (fill && time <= 9)
			builder.append('0');
		builder.append(time);
	}

	/**
	 * Converts this {@link Timer} to a {@link String} with the specified
	 * separator.
	 * 
	 * @param separator The separator to use between time units, a separator
	 * of ":" will return, for example 1:30:45.
	 * @param fill filling set to true means that if the unit is for example
	 * 9, it will be "filled" to 09 so the length of the string is more consistent.
	 * 
	 * @return This {@link Timer} converted to {@link String}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public String toString(@Nullable CharSequence separator, boolean fill) {
		final StringBuilder builder = new StringBuilder();
		for (int unit : time) {
			if (!builder.isEmpty())
				appendTime(builder.append(separator), unit, fill);
			else if (unit != 0)
				appendTime(builder, unit, fill);
		}
		return builder.toString();
	}

	/**
	 * Converts this {@link Timer} to a {@link String} with the specified
	 * separator. Filling is set to {@code true} ({@link #toString(CharSequence, boolean)})
	 * 
	 * @param separator The separator to use between time units, a separator
	 * of ":" will return, for example 1:30:45.
	 * 
	 * @return This {@link Timer} converted to {@link String}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public String toString(@Nullable CharSequence separator) {
		return toString(separator, true);
	}

	/**
	 * Converts this {@link Timer} to a {@link String} with the specified
	 * separator. The separator used is ":" ({@link #toString(CharSequence, boolean)}).
	 * 
	 * @param fill filling set to true means that if the unit is for example
	 * 9, it will be "filled" to 09 so the length of the string is more consistent.
	 * 
	 * @return This {@link Timer} converted to {@link String}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public String toString(boolean fill) {
		return toString(":", fill);
	}

	/**
	 * Converts this {@link Timer} to a {@link String} with the specified
	 * separator. The separator used is ":" and filling is set to {@code true}
	 * ({@link #toString(CharSequence, boolean)}).
	 * 
	 * @return This {@link Timer} converted to {@link String}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	@Override
	public String toString() {
		return toString(":", true);
	}

	/*
	 * Tasks
	 */

	/**
	 * Schedules a new {@link TimerTask} to execute a {@link Runnable} that will
	 * use a {@link #clone() clone} of this {@link Timer} as a reference. Meaning
	 * that if this {@link Timer} has 5 {@link MCTimeUnit#MINUTES minutes} stored
	 * at the time of calling this method, <b>runnable</b> will run 5 {@link MCTimeUnit#MINUTES minutes}
	 * from when this method was called. Modifying this {@link Timer} will have no
	 * effect on the {@link TimerTask} as the task uses a {@link #clone()} as previously mentioned.
	 * In order to modify the {@link TimerTask} {@link Timer}, you need to use {@link TimerTask#getTimer()}.
	 * 
	 * @param plugin the {@link Plugin} that will schedule this task on the
	 * {@link Bukkit#getScheduler() Bukkit scheduler}.
	 * @param runnable the {@link Runnable} that will run once the {@link #clone() clone} of
	 * this {@link Timer} {@link #hasEnded() ends}.
	 * 
	 * @return The new {@link TimerTask} that has been scheduled.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #schedule(Plugin, Consumer, Object)
	 */
	@Nonnull
	public TimerTask schedule(@Nonnull Plugin plugin, @Nonnull Runnable runnable) {
		return new TimerTask(clone()).schedule(plugin, runnable);
	}

	/**
	 * Schedules a new {@link TimerTask} to execute a {@link Consumer} that will
	 * use a {@link #clone() clone} of this {@link Timer} as a reference. Meaning
	 * that if this {@link Timer} has 5 {@link MCTimeUnit#MINUTES minutes} stored
	 * at the time of calling this method, <b>runnable</b> will run 5 {@link MCTimeUnit#MINUTES minutes}
	 * from when this method was called. Modifying this {@link Timer} will have no
	 * effect on the {@link TimerTask} as the task uses a {@link #clone()} as previously mentioned.
	 * In order to modify the {@link TimerTask} {@link Timer}, you need to use {@link TimerTask#getTimer()}.
	 * 
	 * @param <T> the type of the input to the operation
	 * @param plugin the {@link Plugin} that will schedule this task on the
	 * {@link Bukkit#getScheduler() Bukkit scheduler}.
	 * @param consumer the {@link Consumer} that will run once the {@link #clone() clone} of
	 * this {@link Timer} {@link #hasEnded() ends}.
	 * @param obj the object that will be used by the {@link Consumer}.
	 * 
	 * @return The new {@link TimerTask} that has been scheduled.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #schedule(Plugin, Runnable)
	 */
	@Nonnull
	public <T> TimerTask schedule(@Nonnull Plugin plugin, @Nonnull Consumer<T> consumer, @Nullable T obj) {
		return new TimerTask(clone()).schedule(plugin, consumer, obj);
	}

	/*
	 * Utility
	 */

	/**
	 * Clones this {@link Timer} by calling the {@link #Timer(int, int, int)}
	 * constructor.
	 * 
	 * @return A clone of this {@link Timer}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public Timer clone() {
		return new Timer(getHours(), getMinutes(), getSeconds());
	}

	/*
	 * Replacement
	 */

	@Nonnull
	@Override
	public String asReplacement() {
		return toString();
	}

	/*
	 * Java
	 */

	@Override
	public boolean equals(@Nullable Object obj) {
		if (obj == null || !(obj instanceof Timer))
			return false;
		final Timer other = (Timer) obj;
		return	other.getHours() == this.getHours() &&
				other.getMinutes() == this.getMinutes() &&
				other.getSeconds() == this.getSeconds();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(time);
	}
}
