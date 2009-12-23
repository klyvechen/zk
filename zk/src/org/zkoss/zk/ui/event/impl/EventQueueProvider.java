/* EventQueueProvider.java

	Purpose:
		
	Description:
		
	History:
		Thu Jul 23 19:33:00     2009, Created by tomyeh

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

*/
package org.zkoss.zk.ui.event.impl;

import org.zkoss.zk.ui.event.*;

/**
 * Used to instantiate an event queue.
 * @author tomyeh
 * @since 5.0.0
 */
public interface EventQueueProvider {
	/** Returns the event queue with the specified name in the
	 * specified scope.
	 *
	 * <p>Note:
	 * <ul>
	 * <li>This method can be called only in an activated execution,
	 * i.e., {@link org.zkoss.zk.ui.Executions#getCurrent} not null.</li>
	 * <li>By default, {@link EventQueueProviderImpl} is used.
	 * To customize it, refer to {@link EventQueues}.
	 * </ul>
	 *
	 * @param name the name of the event queue.
	 * @param scope the scope of the event queue.
	 * It must support {@link EventQueues#DESKTOP} and {@link EventQueues#APPLICATION}.
	 * Developers might extend it to support other types of event queues.
	 * @param autoCreate whether to create the event queue if not found.
	 * @return the event queue with the associated name, or null if
	 * not found and autoCreate is false
	 * @exception IllegalStateException if not in an activated execution
	 * @exception UnsupportedOperationException if the scope is not supported
	 */
	public EventQueue lookup(String name, String scope, boolean autoCreate);
	/** Removes the event qeueue.
	 * @param name the name of the event queue.
	 * @param scope the scope of the event queue.
	 * It must support {@link EventQueues#DESKTOP} and {@link EventQueues#APPLICATION}.
	 * Developers might extend it to support other types of event queues.
	 * @return true if it is removed successfully
	 */
	public boolean remove(String name, String scope);
}
