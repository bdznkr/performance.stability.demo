/*
 * (c) 2022 Bodo Zunker. All rights reserved.
 * created 12.02.2022
 */
package bzr.demo.concurrency.examples;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author bodo
 *
 */
class Performance extends BaseClass
{
	/**
	 * 
	 */
	private static final String TEST_STRING = "test";
	static final int POOL_SIZE = 5;
	static final int TEST_SIZE = 10;
	static final int REPEAT_COUNT = 1000;
	static final int LATCH_SIZE = Math.min( POOL_SIZE, TEST_SIZE );

	private final List<String> list;

	final AtomicInteger atomicCounter = new AtomicInteger();

	/**
	 * 
	 */
	public Performance()
	{
		super( LATCH_SIZE );
		list = initSampleList();
	}

	void stringAdditionVs()
	{
		startAtSameTime();

		String result = "";

		for( int i = 0; i < REPEAT_COUNT; i++ )
		{
			result = result + " test " + i + " ";

			atomicCounter.incrementAndGet();
		}

	}

	void stringAppend()
	{
		startAtSameTime();

		StringBuilder builder = new StringBuilder();

		for( int i = 0; i < REPEAT_COUNT; i++ )
		{

			builder.append( " test " + i + " " );
			builder.append( i );
			builder.append( " " );

			atomicCounter.incrementAndGet();
		}

	}

	void streamFindFirstVs()
	{
		startAtSameTime();

		List<String> source = list;

		for( int i = 0; i < REPEAT_COUNT; i++ )
		{
			final String find = getFind( i );
			streamFindFirst( source, find );

		}

	}

	Optional<String> streamFindFirst( final List<String> source, final String find )
	{
		return source.stream().filter( s -> s.equals( find ) ).findFirst();
	}

	String getFind( final int i )
	{
		return TEST_STRING + i;
	}

	void parallelStreamFindFirstVs()
	{
		startAtSameTime();

		List<String> source = list;

		for( int i = 0; i < REPEAT_COUNT; i++ )
		{
			final String find = getFind( i );
			parallelStreamFindFirst( source, find );

		}

	}

	Optional<String> parallelStreamFindFirst( final List<String> source, final String find )
	{
		return source.parallelStream().filter( s -> s.equals( find ) ).findFirst();
	}

	void normalForEachFindFirst()
	{
		startAtSameTime();

		List<String> source = list;

		for( int i = 0; i < REPEAT_COUNT; i++ )
		{
			String find = getFind( i );
			findFirst( source, find );

		}

	}

	String findFirst( final List<String> source, final String find )
	{
		for( String string : source )
		{
			if( string.equals( find ) )
				return string;
		}
		return null;
	}

	void streamNestedForEachVs()
	{

	}

	void parallelStreamNestedFoerEachVs()
	{

	}

	void normalForEach()
	{

	}

	static class DataClass
	{
		String string1;
		String string2;
		int i1;
		Date d;
		Format dateFormat1 = new SimpleDateFormat( "hh:mm:ss" );
		Format dateFormat2 = new SimpleDateFormat( "hh:mm:ss" );
		Format dateFormat3 = new SimpleDateFormat( "hh:mm:ss" );
		Format dateFormat4 = new SimpleDateFormat( "hh:mm:ss" );

		public DataClass( final String string1, final String string2, final int i1, final Date d )

		{
			super();
			init( string1, string2, i1, d );
		}

		public void doWork()
		{
			i1++;
			string2 = dateFormat1.format( d );
		}

		void clear()
		{
			string1 = null;
			string2 = null;
		}

		void init( final String string1, final String string2, final int i1, final Date d )
		{
			this.string1 = string1;
			this.string2 = string2;
			this.i1 = i1;
			this.d = d;
		}
	}

	void createNewVs()
	{
		for( int i = 0; i < REPEAT_COUNT; i++ )
		{
			DataClass data = new DataClass( list.get( i ), TEST_STRING, i, new Date() );
			data.doWork();
		}
	}

	final static Queue<DataClass> reuseQueue = new ConcurrentLinkedQueue<>();

	void objectReuse()
	{
		for( int i = 0; i < REPEAT_COUNT; i++ )
		{
			DataClass data = reuseQueue.poll();
			if( data == null )
			{
				data = new DataClass( list.get( i ), TEST_STRING, i, new Date() );
			}else
			{
				data.init( list.get( i ), TEST_STRING, i, new Date() );
			}

			data.doWork();

			data.clear();
			reuseQueue.add( data );
		}
	}

	private List<String> initSampleList()
	{
		CopyOnWriteArrayList<String> source = new CopyOnWriteArrayList<>();
		for( int i = 0; i < REPEAT_COUNT; i++ )
		{
			source.add( TEST_STRING + i );
		}
		return source;
	}
}
