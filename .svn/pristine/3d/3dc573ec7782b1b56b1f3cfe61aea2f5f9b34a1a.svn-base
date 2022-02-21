/*
 * (c) 2022 Bodo Zunker. All rights reserved.
 * created 21.02.2022
 */
package bzr.demo.concurrency.examples.state;

import java.util.Collections;
import java.util.List;

/**
 * 
 * stateless class for business logic
 *
 */
public class SeparatedState
{

	/**
	 * class containing unmodifiable state
	 */
	static class FinalState
	{

		final boolean isinited;
		final boolean isOpen;
		final boolean isConnected;
		final boolean isDisposed;

		final List<String> listData;

		public FinalState( final boolean isinited, final boolean isOpen, final boolean isConnected,
				final boolean isDisposed, final List<String> listData )
		{
			super();
			this.isinited = isinited;
			this.isOpen = isOpen;
			this.isConnected = isConnected;
			this.isDisposed = isDisposed;
			this.listData = Collections.unmodifiableList( listData );
		}

	}

	volatile FinalState state = null;

}
