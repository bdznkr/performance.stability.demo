/*
 * (c) 2022 Bodo Zunker. All rights reserved.
 * created 12.02.2022
 */
package bzr.demo.concurrency.examples;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author bodo
 *
 */
class MurphysLaw extends BaseClass
{

	static final int POOL_SIZE = 2;
	static final int TEST_SIZE = 20;
	static final int REPEAT_COUNT = 100;
	static final int LATCH_SIZE = Math.min( POOL_SIZE, TEST_SIZE );

	public MurphysLaw()
	{
		super( LATCH_SIZE );
	}

	static class SomeComponent
	{
		final AtomicInteger workCounter = new AtomicInteger();

		private boolean someState = true;

		/**
		 * @param someState
		 *          the someState to set
		 */
		public final void setSomeState( final boolean someState )
		{
			this.someState = someState;
		}

		/**
		 * @return the someState
		 */
		public final boolean isSomeState()
		{
			return someState;
		}

		int doWork()
		{
			return workCounter.incrementAndGet();
		}

		void doWorkInState()
		{
			if( !someState )
			{
				Logger.infoThreadName( "not in state" );
				// throw new IllegalStateException( "not in state" );
			}else
			{
				workCounter.incrementAndGet();
			}
		}

		void doWorkNotInState()
		{
			if( someState )
			{
				Logger.infoThreadName( "in state" );
				// throw new IllegalStateException( "not in state" );
			}else
			{
				workCounter.incrementAndGet();
			}
		}
	}

	SomeComponent lazyInstance = null;

	final AtomicInteger atomicCounter = new AtomicInteger();

	private final CountDownLatch triggerLatch = new CountDownLatch( LATCH_SIZE );

	/**
	 * Item 71: Use lazy initialization judiciously
	 * https://stackoverflow.com/a/3580658
	 *
	 * @return
	 */
	SomeComponent initInstance()
	{
		SomeComponent result = lazyInstance;
		if( result != null ) // First check (no locking)
			return result;
		synchronized( this )
		{
			// Second check (with locking)
			if( lazyInstance == null )
			{
				lazyInstance = computeInstance();// extra method to ensure constructor
																					// is executed fully first!
			}
			return lazyInstance;
		}
	}

	/**
	 * @return
	 */
	private SomeComponent computeInstance()
	{
		return new SomeComponent();
	}

	synchronized void closeInstance()
	{
		if( lazyInstance != null )
		{
			lazyInstance = null;
		}
	}

	void expectedInstanceValue()
	{
		startAtSameTime();

		initInstance();

		for( int j = 0; j < REPEAT_COUNT; j++ )
		{
			lazyInstance.doWork();
		}

		atomicCounter.addAndGet( lazyInstance.workCounter.get() );

		closeInstance();
	}

	void unexpectedInstanceValue()
	{
		startAtSameTime();

		initInstance();

		for( int j = 0; j < REPEAT_COUNT; j++ )
		{
			if( lazyInstance != null )
			{
				lazyInstance.doWork();
			}
		}

		if( lazyInstance != null )
		{
			atomicCounter.addAndGet( lazyInstance.workCounter.get() );
		}
		closeInstance();
	}

	void localCheck()
	{
		startAtSameTime();

		initInstance();
		for( int j = 0; j < REPEAT_COUNT; j++ )
		{
			SomeComponent localInstance = lazyInstance;

			if( localInstance != null )
			{
				localInstance.doWork();
				atomicCounter.incrementAndGet();
			}

		}

		closeInstance();
	}

	void silentFail()
	{
		startAtSameTime();

		initInstance();

		for( int j = 0; j < REPEAT_COUNT; j++ )
		{

			if( lazyInstance.isSomeState() )
			{

				lazyInstance.doWorkInState();

				lazyInstance.setSomeState( false );

			}else
			{
				lazyInstance.doWorkNotInState();

				lazyInstance.setSomeState( true );

			}

		}

	}

}
