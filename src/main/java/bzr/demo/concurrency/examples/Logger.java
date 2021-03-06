/*
 * (c) 2022 Bodo Zunker. All rights reserved.
 * created 14.02.2022
 */
package bzr.demo.concurrency.examples;

/**
 * @author bodo
 *
 */
final public class Logger
{

	private Logger()
	{
		throw new IllegalStateException( "Utility class" );
	}

	/**
	 * 
	 */
	private static final String SOME_SPACE = "\t\t";

	/**
	 * @param string
	 */
	public static void info( final String info )
	{
		System.out.println( info );
	}

	/**
	 * @param string
	 */
	public static void infoThreadName( final String info )
	{
		info( info + SOME_SPACE + Thread.currentThread().getName() );

	}

}
