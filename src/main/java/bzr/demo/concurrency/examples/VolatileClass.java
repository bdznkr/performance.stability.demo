/*
 * (c) 2022 Bodo Zunker. All rights reserved.
 * created 12.02.2022
 */
package bzr.demo.concurrency.examples;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author bodo
 *
 */
class VolatileClass extends BaseClass
{
	static final int POOL_SIZE = MasterClass.POOL_SIZE;
	static final int TEST_SIZE = 10;
	static final int REPEAT_COUNT = 100;
	static final int LATCH_SIZE = Math.min( POOL_SIZE, TEST_SIZE );

	public VolatileClass()
	{
		super( LATCH_SIZE );
	}

	int unprotectedCounter = 0;
	volatile int volatileCounter = 0;
	final AtomicInteger atomicCounter = new AtomicInteger();

	/**
	 * @return
	 */
	void countUpUnprotected()
	{
		for( int i = 0; i < REPEAT_COUNT; i++ )
		{
			unprotectedCounter = unprotectedCounter + 1;
		}
		Logger.infoThreadName( String.valueOf( unprotectedCounter ) );
	}

	/**
	 * @return
	 */
	void countUpSynchronized()
	{
		for( int i = 0; i < REPEAT_COUNT; i++ )
		{
			synchronized( this )
			{
				unprotectedCounter = unprotectedCounter + 1;
			}
		}
		Logger.infoThreadName( String.valueOf( unprotectedCounter ) );
	}

	/**
	 * @return
	 */
	void countUpVolatile()
	{
		for( int i = 0; i < REPEAT_COUNT; i++ )
		{
			volatileCounter = volatileCounter + 1;
		}
		Logger.infoThreadName( String.valueOf( volatileCounter ) );
	}

	void countUpAtomic()
	{
		int last = 0;
		for( int i = 0; i < REPEAT_COUNT; i++ )
		{
			last = atomicCounter.incrementAndGet();
		}
		Logger.infoThreadName( String.valueOf( last ) );
	}

	void countLatched( final Runnable counter )
	{
		triggerLatch.countDown();

		try
		{
			triggerLatch.await();

			counter.run();

		}catch( InterruptedException e )
		{
			Thread.currentThread().interrupt();
		}
	}

}