/*
 * (c) 2022 Bodo Zunker. All rights reserved.
 * created 08.02.2022
 */
package bzr.demo.concurrency.examples.state;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import bzr.demo.concurrency.examples.Base;
import bzr.demo.concurrency.examples.Logger;

/**
 * @author bodo
 *
 */
@TestMethodOrder( OrderAnnotation.class )
class StableStateTest extends Base
{

	private StableState complexStateInstance;

	@BeforeAll
	static void setup()
	{
		initPool( StableState.POOL_SIZE );
	}

	@BeforeEach
	void initEveryTest()
	{
		complexStateInstance = new StableState();
		Logger.info( "" );
		initTest();
	}

	@AfterEach
	void afterEveryTest()
	{
		afterTest();
	}

	@AfterAll
	static void shutdown()
	{
		pool.shutdown();
	}

	@Test
	@Order( 1 )
	void unsafeArrayListUse()
	{
		Logger.info( "unsafeArrayListUse" );
		for( int i = 0; i < StableState.TEST_SIZE; i++ )
		{
			futures.add( pool.submit( complexStateInstance::unsafeArrayListUse ) );
		}
		Logger.info( "inbetween: " + complexStateInstance.atomicCounter.get() );

		waitForFuture();

		Logger.info( "result: " + complexStateInstance.atomicCounter.get() );
		assertEquals( StableState.TEST_SIZE * StableState.REPEAT_COUNT,
				complexStateInstance.atomicCounter.get() );
	}

	@Test
	@Order( 2 )
	void unsafeArrayListUseWithIfCheck()
	{
		Logger.info( "unsafeArrayListUseWithIfCheck" );
		for( int i = 0; i < StableState.TEST_SIZE; i++ )
		{
			futures.add( pool.submit( complexStateInstance::unsafeArrayListUseWithIfCheck ) );
		}
		Logger.info( "inbetween: " + complexStateInstance.atomicCounter.get() );

		waitForFuture();

		Logger.info( "result: " + complexStateInstance.atomicCounter.get() );
		assertEquals( StableState.TEST_SIZE * StableState.REPEAT_COUNT,
				complexStateInstance.atomicCounter.get() );
	}

	@Test
	@Order( 3 )
	void hasToWorkFinally()
	{
		Logger.info( "hasToWorkFinally" );
		for( int i = 0; i < StableState.TEST_SIZE; i++ )
		{
			futures.add( pool.submit( complexStateInstance::hasToWorkFinally ) );
		}
		Logger.info( "inbetween: " + complexStateInstance.atomicCounter.get() );

		waitForFuture();

		Logger.info( "result: " + complexStateInstance.atomicCounter.get() );
		assertEquals( StableState.TEST_SIZE * StableState.REPEAT_COUNT,
				complexStateInstance.atomicCounter.get() );
	}

	@Test
	@Order( 4 )
	void useLocal()
	{
		Logger.info( "useLocal" );
		for( int i = 0; i < StableState.TEST_SIZE; i++ )
		{
			futures.add( pool.submit( complexStateInstance::useLocal ) );
		}
		Logger.info( "inbetween: " + complexStateInstance.atomicCounter.get() );

		waitForFuture();

		Logger.info( "result: " + complexStateInstance.atomicCounter.get() );
		assertEquals( StableState.TEST_SIZE * StableState.REPEAT_COUNT,
				complexStateInstance.atomicCounter.get() );
	}

}
