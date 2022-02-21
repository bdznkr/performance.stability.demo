/*
 * (c) 2022 Bodo Zunker. All rights reserved.
 * created 14.02.2022
 */
package bzr.demo.concurrency.examples;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author bodo
 *
 */
public class Base
{

	protected ArrayList<Future<?>> futures;

	protected static ExecutorService pool;

	protected long startTimeNs;

	protected static long lastDurationNs;
	protected static boolean showDurationDelta = false;

	private long startTotalMemory;

	private long startFreeMemory;

	/**
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * 
	 */
	protected void waitForFuture()
	{
		Throwable lastEx = null;
		for( Future<?> future : futures )
		{
			try
			{
				future.get();
			}catch( InterruptedException e )
			{
				Thread.currentThread().interrupt();
			}catch( ExecutionException e )
			{
				Throwable cause = e.getCause();
				Logger.info( cause.toString() );
				lastEx = cause;
			}
		}
		if( lastEx != null )
			throw new RuntimeException( lastEx );
	}

	protected static void initPool( final int poolSize )
	{
		pool = Executors.newFixedThreadPool( poolSize );
		for( int i = 0; i < poolSize; i++ )
		{
			pool.submit( () -> {
				try
				{
					Thread.sleep( 10 );
				}catch( InterruptedException e )
				{
					Thread.currentThread().interrupt();
				}
			} );

		}
	}

	protected void initTest()
	{
		futures = new ArrayList<>();
		startTimeNs = System.nanoTime();
	}

	void initMemory()
	{
		System.gc();
		Runtime runtime = Runtime.getRuntime();
		startTotalMemory = runtime.totalMemory();
		startFreeMemory = runtime.freeMemory();
	}

	protected void afterTest()
	{
		long durationNs = System.nanoTime() - startTimeNs;
		Logger.info( "Dauer: " + durationNs / 1000L + " μs" );
		if( showDurationDelta && lastDurationNs > 0L )
		{
			Logger.info( "Delta to prior: " + (durationNs - lastDurationNs) / 1000L + " μs factor: "
					+ (durationNs > lastDurationNs ? (double) durationNs / lastDurationNs + " slower"
							: (double) lastDurationNs / durationNs + " faster") );
		}
		Logger.info( "" );
		lastDurationNs = durationNs;
	}

	void afterTestMemory()
	{
		Runtime runtime = Runtime.getRuntime();
		long usedMemoryDelta = (runtime.totalMemory() - runtime.freeMemory())
				- (startTotalMemory - startFreeMemory);
		Logger.info( "Memory: usedDelta=" + usedMemoryDelta / 1000L + " KB" );
		Logger.info( "" );
	}

}
