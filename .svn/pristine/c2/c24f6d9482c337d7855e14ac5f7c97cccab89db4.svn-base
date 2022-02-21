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
class MurphysLawTest extends Base
{

	private MurphysLaw murphysLawInstance;

	@BeforeAll
	static void setup()
	{
		initPool( MurphysLaw.POOL_SIZE );
	}

	@BeforeEach
	void initEveryTest()
	{
		murphysLawInstance = new MurphysLaw();
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
	void expectedInstanceValue()
	{
		Logger.info( "expectedInstanceValue" );
		for( int i = 0; i < MurphysLaw.TEST_SIZE; i++ )
		{
			futures.add( pool.submit( murphysLawInstance::expectedInstanceValue ) );
		}

		waitForFuture();

		Logger.info( "result: " + murphysLawInstance.atomicCounter.get() );
		assertEquals( MurphysLaw.TEST_SIZE * MurphysLaw.REPEAT_COUNT, murphysLawInstance.atomicCounter.get() );
	}

	@Test
	@Order( 2 )
	void unexpectedInstanceValue()
	{
		Logger.info( "unexpectedInstanceValue" );
		for( int i = 0; i < MurphysLaw.TEST_SIZE; i++ )
		{
			futures.add( pool.submit( murphysLawInstance::unexpectedInstanceValue ) );
		}

		waitForFuture();

		Logger.info( "result: " + murphysLawInstance.atomicCounter.get() );
		assertEquals( MurphysLaw.TEST_SIZE * MurphysLaw.REPEAT_COUNT, murphysLawInstance.atomicCounter.get() );
	}

	@Test
	@Order( 3 )
	void localCheck()
	{
		Logger.info( "localCheck" );
		for( int i = 0; i < MurphysLaw.TEST_SIZE; i++ )
		{
			futures.add( pool.submit( murphysLawInstance::localCheck ) );
		}

		waitForFuture();

		Logger.info( "result: " + murphysLawInstance.atomicCounter.get() );
		assertEquals( MurphysLaw.TEST_SIZE * MurphysLaw.REPEAT_COUNT, murphysLawInstance.atomicCounter.get() );
	}

	@Test
	@Order( 4 )
	void silentFail()
	{
		Logger.info( "silentFail" );
		for( int i = 0; i < MurphysLaw.TEST_SIZE; i++ )
		{
			futures.add( pool.submit( murphysLawInstance::silentFail ) );
		}

		waitForFuture();

		Logger.info( "result: " + murphysLawInstance.lazyInstance.workCounter.get() );
		assertEquals( MurphysLaw.TEST_SIZE * MurphysLaw.REPEAT_COUNT,
				murphysLawInstance.lazyInstance.workCounter.get() );
	}

}
