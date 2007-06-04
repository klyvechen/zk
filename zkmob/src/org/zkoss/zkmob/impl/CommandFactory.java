/* CommandFactory.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Jun 1, 2007 12:06:30 PM, Created by henrichen
}}IS_NOTE

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zkmob.impl;

import javax.microedition.lcdui.Command;

import org.xml.sax.Attributes;
import org.zkoss.zkmob.AbstractUiFactory;
import org.zkoss.zkmob.Context;
import org.zkoss.zkmob.Listable;
import org.zkoss.zkmob.UiManager;
import org.zkoss.zkmob.ZkComponent;
import org.zkoss.zkmob.ZkComponents;

/**
 * An UiFactory that create a Command Ui component.
 * @author henrichen
 *
 */
public class CommandFactory extends AbstractUiFactory {

	public CommandFactory(String name) {
		super(name);
	}

	public Object create(Object parent, String tag, Attributes attrs, Context ctx) {
		final String id = attrs.getValue("id"); //id
		final String label = attrs.getValue("lb"); //label
		final String longLabel = attrs.getValue("ll"); //longLabel
		final String typeStr = attrs.getValue("tp"); //type
		final int type = Integer.parseInt(typeStr);
		final String priorityStr = attrs.getValue("pr"); //priority
		final int priority = Integer.parseInt(priorityStr); 
		final ZkComponent owner = (ZkComponent) parent;
		final Command cmd = new ZkCommand(owner, owner.getZk(), id, label, longLabel, type, priority);
		
		ZkComponents.registerCommand(owner, cmd, owner.getZk());

		return cmd;
	}
}
