package me.xdec0de.mcutils.general;

import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

import org.checkerframework.checker.index.qual.Positive;

/**
 * Class used to do fast performance as of right now,
 * this class uses {@link ThreadMXBean#getCurrentThreadCpuTime()}
 * to get the actual execution time of the methods, as well as creating
 * a new {@link Thread} every time a test runs, sleeping before every execution
 * to try and have the most accurate results possible.
 * <p>
 * <b>Note:</b> Even with all of the mentioned features mentioned above, times
 * might not be 100% accurate and may be subject to CPU load, so please make sure
 * to run tests with the minimum CPU load possible. As also mentioned, this method
 * exists for <b><i>fast testing</i></b>, not <b><i>precise results</i></b>,
 * if you really want precision, using an actual profiler is much better than this.
 * 
 * @author xDec0de_
 * 
 * @since MCUtils 1.0.0
 * 
 * @see #addTest(String, Runnable)
 * @see #run(PrintStream)
 * @see #run(PrintStream, int)
 */
public class PerformanceTest {

	private final long amount;
	private final long sleep;

	private final LinkedHashMap<String, Runnable> tests = new LinkedHashMap<>();
	private final LinkedHashMap<String, List<Long>> results = new LinkedHashMap<>();

	/**
	 * Creates a new {@link PerformanceTest} with the specified <b>amount</b>
	 * of times to run and the time to <b>sleep</b> per execution.
	 * 
	 * @param amount the amount of times to call all tasks per execution, if <= 0, 1 will be used.
	 * @param sleep the time (In milliseconds) to sleep per execution, if <= 0, 1 will be used.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #addTest(String, Runnable)
	 * @see #run(PrintStream)
	 * @see #run(PrintStream, int)
	 */
	public PerformanceTest(@Positive long amount, @Positive long sleep) {
		this.amount = amount < 1 ? 1 : amount;
		this.sleep = sleep < 1 ? 1 : sleep;
	}

	/**
	 * Adds a <b>task</b> with the specified <b>id</b> to this {@link PerformanceTest},
	 * if a task with said <b>id</b> already exists, it will be replaced, if either
	 * <b>id</b> or <b>task</b> are null, this method won't do anything.
	 * 
	 * @param id the id of the task to add, will be the name displayed on the test results.
	 * @param task the task to run, usually just the method you want to test, note that
	 * defining the task outside this method does affect performance, so keep that in mind.
	 * 
	 * @return This {@link PerformanceTest}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public PerformanceTest addTest(@Nonnull String id, @Nonnull Runnable task) {
		if (id != null && task != null) {
			tests.put(id, task);
			results.put(id, new ArrayList<Long>());
		}
		return this;
	}

	/**
	 * Runs all tasks stored on this {@link PerformanceTest} the specified
	 * amount of <b>times</b>, printing test details as well as minimum, average
	 * and maximum results on <b>stream</b>.
	 * 
	 * @param stream the stream to print all information, normally {@link System#out}
	 * @param times the amount of times to repeat this {@link PerformanceTest}, test
	 * execution will run on a separate thread but not multiple threads, must be higher or
	 * equal to 1.
	 * 
	 * @return This {@link PerformanceTest}
	 * 
	 * @since MCUtils 1.0.0
	 */
	public PerformanceTest run(@Nonnull PrintStream stream, @Positive int times) {
		final int checkedTimes = times < 1 ? 1 : times;
		System.out.println("Starting performance test (Repeat: "+checkedTimes+" | Amount: "+amount+" | Sleep: "+sleep+")");
		System.out.println(" ");
		new PerformanceTestThread(stream, checkedTimes).start();
		return this;
	}

	class PerformanceTestThread extends Thread {

		private final PrintStream stream;
		private final long times;

		PerformanceTestThread(PrintStream stream, long repeat) {
			this.stream = stream;
			this.times = repeat;
		}

		@Override
		public void run() {
			final ThreadMXBean threadMx = ManagementFactory.getThreadMXBean();
			for (long i = 0; i < times; i++) {
				tests.forEach((id, runnable) -> {
					try {
						runnable.run(); // Method "warm-up"
						Thread.sleep(sleep); // Sleeping to reduce load.
						final long start = threadMx.getCurrentThreadCpuTime(); // Getting actual CPU time used by this thread
						for (int j = 0; j < amount; j++)
							runnable.run(); // Calling the method itself
						final long total = Math.round((threadMx.getCurrentThreadCpuTime() - start) / 1000000);
						results.get(id).add(total);
					} catch (InterruptedException | IllegalArgumentException e) {
						e.printStackTrace();
					}
				});
			}
			printResults();
		}

		private void printResults() {
			results.forEach((id, resultList) -> {
				long min = Long.MAX_VALUE;
				long max = Long.MIN_VALUE;
				long sum = 0;
				for (long time : resultList) {
					if (time < min)
						min = time;
					if (time > max)
						max = time;
					sum += time;
				}
				stream.println(id+" - Min: " + min + "ms | Average: "+ (sum / resultList.size()) +"ms | Max: " + max+"ms");
			});
		}
	}
}
