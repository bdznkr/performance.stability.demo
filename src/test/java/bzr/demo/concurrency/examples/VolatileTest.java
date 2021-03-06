/*
 * (c) 2022 Bodo Zunker. All rights reserved.
 * created 08.02.2022
 */
package bzr.demo.concurrency.examples;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @author bodo
 *
 */
@TestMethodOrder( OrderAnnotation.class )
class VolatileTest extends Base
{

	private VolatileClass volatileInstance;

	@BeforeAll
	static void setup()
	{
		initPool( VolatileClass.POOL_SIZE );
	}

	@BeforeEach
	void initEveryTest()
	{
		volatileInstance = new VolatileClass();
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
	void countUnprotected()
	{
		Logger.info( "countUnprotected" );
		for( int i = 0; i < VolatileClass.TEST_SIZE; i++ )
		{
			futures.add( pool.submit( volatileInstance::countUpUnprotected ) );

		}
		Logger.info( "inbetween: " + volatileInstance.unprotectedCounter );

		waitForFuture();

		Logger.info( "result: " + volatileInstance.unprotectedCounter );
		assertEquals( VolatileClass.TEST_SIZE * VolatileClass.REPEAT_COUNT, volatileInstance.unprotectedCounter );
	}

	@Test
	@Order( 2 )
	void countUnprotected2()
	{
		Logger.info( "countUnprotected2" );
		for( int i = 0; i < VolatileClass.TEST_SIZE; i++ )
		{
			futures.add( pool.submit( volatileInstance::countUpUnprotected ) );

		}
		Logger.info( "inbetween: " + volatileInstance.unprotectedCounter );

		waitForFuture();

		Logger.info( "result: " + volatileInstance.unprotectedCounter );
		assertEquals( VolatileClass.TEST_SIZE * VolatileClass.REPEAT_COUNT, volatileInstance.unprotectedCounter );
	}

	@Test
	@Order( 3 )
	void countSynchronized()
	{
		Logger.info( "countUpSynchronized" );
		for( int i = 0; i < VolatileClass.TEST_SIZE; i++ )
		{
			futures.add( pool.submit( volatileInstance::countUpSynchronized ) );

		}
		Logger.info( "inbetween: " + volatileInstance.unprotectedCounter );

		waitForFuture();

		Logger.info( "result: " + volatileInstance.unprotectedCounter );
		assertEquals( VolatileClass.TEST_SIZE * VolatileClass.REPEAT_COUNT, volatileInstance.unprotectedCounter );
	}

	@Test
	@Order( 4 )
	void countVolatile()
	{
		Logger.info( "countVolatile" );
		for( int i = 0; i < VolatileClass.TEST_SIZE; i++ )
		{
			futures.add( pool.submit( volatileInstance::countUpVolatile ) );

		}
		Logger.info( "inbetween: " + volatileInstance.volatileCounter );
		waitForFuture();
		Logger.info( "result: " + volatileInstance.volatileCounter );
		assertEquals( VolatileClass.TEST_SIZE * VolatileClass.REPEAT_COUNT, volatileInstance.volatileCounter );
	}

	@Test
	@Order( 5 )
	void countAtomic()
	{
		Logger.info( "countAtomic" );
		for( int i = 0; i < VolatileClass.TEST_SIZE; i++ )
		{
			futures.add( pool.submit( volatileInstance::countUpAtomic ) );

		}
		Logger.info( "inbetween: " + volatileInstance.atomicCounter.get() );
		waitForFuture();
		Logger.info( "result: " + volatileInstance.atomicCounter.get() );
		assertEquals( VolatileClass.TEST_SIZE * VolatileClass.REPEAT_COUNT,
				volatileInstance.atomicCounter.get() );
	}

	@Test
	@Order( 6 )
	void countUnprotectedLatched()
	{
		Logger.info( "countUnprotectedLatched" );
		for( int i = 0; i < VolatileClass.TEST_SIZE; i++ )
		{
			futures
					.add( pool.submit( () -> volatileInstance.countLatched( volatileInstance::countUpUnprotected ) ) );

		}
		Logger.info( "inbetween: " + volatileInstance.unprotectedCounter );
		waitForFuture();
		Logger.info( "result: " + volatileInstance.unprotectedCounter );
		assertEquals( VolatileClass.TEST_SIZE * VolatileClass.REPEAT_COUNT, volatileInstance.unprotectedCounter );
	}

	@Test
	@Order( 7 )
	void countVolatileLatched()
	{
		Logger.info( "countVolatileLatched" );
		for( int i = 0; i < VolatileClass.TEST_SIZE; i++ )
		{
			futures.add( pool.submit( () -> volatileInstance.countLatched( volatileInstance::countUpVolatile ) ) );

		}
		Logger.info( "inbetween: " + volatileInstance.volatileCounter );
		waitForFuture();
		Logger.info( "result: " + volatileInstance.volatileCounter );
		assertEquals( VolatileClass.TEST_SIZE * VolatileClass.REPEAT_COUNT, volatileInstance.volatileCounter );
	}

	@Test
	@Order( 8 )
	void countAtomicLatched()
	{
		Logger.info( "countAtomicLatched" );
		for( int i = 0; i < VolatileClass.TEST_SIZE; i++ )
		{
			futures.add( pool.submit( () -> volatileInstance.countLatched( volatileInstance::countUpAtomic ) ) );

		}
		Logger.info( "inbetween: " + volatileInstance.atomicCounter.get() );
		waitForFuture();
		Logger.info( "result: " + volatileInstance.atomicCounter.get() );
		assertEquals( VolatileClass.TEST_SIZE * VolatileClass.REPEAT_COUNT,
				volatileInstance.atomicCounter.get() );
	}

}
