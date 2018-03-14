/* B85_ZK_3783Test.java

        Purpose:
                
        Description:
                
        History:
                Wed Mar 14 2:20 PM:53 CST 2018, Created by klyve

Copyright (C) 2018 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zktest.zats.test2;

import org.junit.Assert;
import org.junit.Test;
import org.zkoss.zktest.zats.WebDriverTestCase;

public class B85_ZK_3783Test extends WebDriverTestCase {
	@Test
	public void test() throws Exception {
		connect();
		for (int i = 0; i < jq(".z-comboitem").length(); i++){
			click(jq(".z-combobox-button"));
			waitResponse();
			click(jq(".z-comboitem").get(i));
			waitResponse();
			click(jq("body"));
			waitResponse();
			String css = getWebDriver().findElement(jq(".z-panel-close").toBy()).getCssValue("line-height");
			Assert.assertEquals(css, "24px");
		}
	}
}
