/*
 * (c) 2022 Bodo Zunker. All rights reserved.
 * created 20.02.2022
 */
package bzr.demo.concurrency.examples.state;

import java.util.List;

/**
 * @author bodo
 *
 */
public class ComplexState
{

	boolean isinited = false;
	boolean isOpen = false;
	boolean isConnected = false;
	boolean isDisposed = false;

	List<String> listData = null;

}
