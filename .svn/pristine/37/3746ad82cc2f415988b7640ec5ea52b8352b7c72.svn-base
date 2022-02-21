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
class PerformanceTest extends Base
{

	private Performance performanceInstance;

	@BeforeAll
	static void setup()
	{
		initPool( Performance.POOL_SIZE );
		showDurationDelta = true;
	}

	@BeforeEach
	void initEveryTest()
	{
		performanceInstance = new Performance();
		Logger.info( "" );
		initTest();
		initMemory();
	}

	@AfterEach
	void afterEveryTest()
	{
		afterTest();
		afterTestMemory();
	}

	@AfterAll
	static void shutdown()
	{
		pool.shutdown();
	}

	@Test
	@Order( 1 )
	void stringAdditionVs()
	{
		Logger.info( "stringAdditionVs" );
		for( int i = 0; i < Performance.TEST_SIZE; i++ )
		{
			futures.add( pool.submit( performanceInstance::stringAdditionVs ) );
		}

		waitForFuture();

		assertEquals( Performance.TEST_SIZE * Performance.REPEAT_COUNT, performanceInstance.atomicCounter.get() );
	}

	@Test
	@Order( 2 )
	void stringAppend()
	{
		Logger.info( "stringAppend" );
		for( int i = 0; i < Performance.TEST_SIZE; i++ )
		{
			futures.add( pool.submit( performanceInstance::stringAppend ) );
		}

		waitForFuture();

		assertEquals( Performance.TEST_SIZE * Performance.REPEAT_COUNT, performanceInstance.atomicCounter.get() );
	}

	@Test
	@Order( 4 )
	void streamFindFirstVs()
	{
		Logger.info( "streamFindFirstVs" );
		for( int i = 0; i < Performance.TEST_SIZE; i++ )
		{
			futures.add( pool.submit( performanceInstance::streamFindFirstVs ) );
		}

		waitForFuture();

	}

	@Test
	@Order( 5 )
	void parallelStreamFindFirstVs()
	{
		Logger.info( "parallelStreamFindFirstVs" );
		for( int i = 0; i < Performance.TEST_SIZE; i++ )
		{
			futures.add( pool.submit( performanceInstance::parallelStreamFindFirstVs ) );
		}

		waitForFuture();

	}

	@Test
	@Order( 3 )
	void normalForEachFindFirst()
	{
		lastDurationNs = 0L;
		Logger.info( "normalForEachFindFirst" );
		for( int i = 0; i < Performance.TEST_SIZE; i++ )
		{
			futures.add( pool.submit( performanceInstance::normalForEachFindFirst ) );
		}

		waitForFuture();

	}

	@Test
	@Order( 6 )
	void createNewVs()
	{
		lastDurationNs = 0L;
		Logger.info( "createNewVs" );
		for( int i = 0; i < Performance.TEST_SIZE; i++ )
		{
			futures.add( pool.submit( performanceInstance::createNewVs ) );
		}

		waitForFuture();

	}

	@Test
	@Order( 7 )
	void objectReuse()
	{
		Logger.info( "objectReuse" );
		for( int i = 0; i < Performance.TEST_SIZE; i++ )
		{
			futures.add( pool.submit( performanceInstance::objectReuse ) );
		}

		waitForFuture();

	}

}
