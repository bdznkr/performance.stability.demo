/*
 * (c) 2022 Bodo Zunker. All rights reserved.
 * created 14.02.2022
 */
package bzr.demo.concurrency.examples;

import java.util.concurrent.CountDownLatch;

/**
 * @author bodo
 *
 */
public class BaseClass
{

	protected final CountDownLatch triggerLatch;

	public BaseClass( final int latchSize )
	{
		super();
		triggerLatch = new CountDownLatch( latchSize );
	}

	protected void startAtSameTime()
	{
		triggerLatch.countDown();
		try
		{
			triggerLatch.await();
		}catch( InterruptedException e )
		{
			Thread.currentThread().interrupt();
		}
	}

}
