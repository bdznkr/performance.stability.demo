/*
 * (c) 2022 Bodo Zunker. All rights reserved.
 * created 12.02.2022
 */
package bzr.demo.concurrency.examples.state;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import bzr.demo.concurrency.examples.BaseClass;

/**
 * @author bodo
 *
 */
class StableState extends BaseClass
{

	static final int POOL_SIZE = 20;
	static final int TEST_SIZE = 100;
	static final int REPEAT_COUNT = 10000;
	static final int LATCH_SIZE = Math.min( POOL_SIZE, TEST_SIZE );

	public StableState()
	{
		super( LATCH_SIZE );
	}

	private List<String> list = null;

	final AtomicInteger atomicCounter = new AtomicInteger();

	void initArray()
	{
		if( list == null )
		{
			list = new ArrayList<>();
		}
	}

	void unsafeArrayListUse()
	{
		startAtSameTime();

		initArray();
		for( int j = 0; j < REPEAT_COUNT; j++ )
		{
			list.add( "test" + j );
		}

		atomicCounter.addAndGet( list.size() );
		list = null;
	}

	void unsafeArrayListUseWithIfCheck()
	{
		startAtSameTime();

		initArray();

		for( int j = 0; j < REPEAT_COUNT; j++ )
		{
			if( list != null )
			{
				list.add( "test" + j );
			}
		}

		if( list != null )
		{
			atomicCounter.addAndGet( list.size() );
		}
		list = null;
	}

	void hasToWorkFinally()
	{
		startAtSameTime();

		for( int j = 0; j < REPEAT_COUNT; j++ )
		{
			if( list == null )
			{
				list = new LinkedList<>();
			}
			if( list != null )
			{
				list.add( "test" + j );
				atomicCounter.incrementAndGet();
			}
		}

		list = null;
	}

	void useLocal()
	{
		startAtSameTime();

		for( int j = 0; j < REPEAT_COUNT; j++ )
		{
			List<String> listLocal = list;
			while( listLocal == null )
			{
				list = new ArrayList<>();
				listLocal = list;
			}
			// synchronized( listLocal )
			{
				listLocal.add( "test" + j );
			}
			atomicCounter.incrementAndGet();
		}

		list = null;
	}
}
