/* B85_ZK_3772Test.java

        Purpose:
                
        Description:
                
        History:
                Wed Mar 14 12:52 PM:05 CST 2018, Created by klyve

Copyright (C) 2018 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zktest.zats.test2;

import org.junit.Assert;
import org.junit.Test;
import org.zkoss.zktest.zats.WebDriverTestCase;

public class B85_ZK_3772Test extends WebDriverTestCase {
	@Test
	public void test() throws Exception {
		connect();
		String  cursorCss= getWebDriver().findElement(
				jq(".z-splitlayout-splitter-horizontal.z-splitlayout-splitter-draggable").toBy())
				.getCssValue("cursor");
		Assert.assertEquals("col-resize", cursorCss);
	}
}
