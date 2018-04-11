/* F85_ZK_3913VM.java

        Purpose:
                
        Description:
                
        History:
                Wed Apr 11 3:55 PM:29 CST 2018, Created by klyve

Copyright (C) 2018 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zktest.test2;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkmax.zul.Barcode;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;

public class F85_ZK_3913Composer extends SelectorComposer {
	
	Barcode bc = new Barcode();
	
	@Wire
	private Button qrcodeDecode;
	
	@Wire
	private Button code128Decode;
	
	@Wire
	private Button decodeNo3;
	
	@Listen("onTest=#qrcodeDecode")
	public void qrcodeDecode(Event evt) {
		String barcodeValue = bc.decodeBarcodeImage(evt.getData().toString());
		Messagebox.show(barcodeValue);
	}
	
	@Listen("onTest=#code128Decode")
	public void code128Decode(Event evt) {
		String barcodeValue = bc.decodeBarcodeImage(evt.getData().toString());
		Messagebox.show(barcodeValue);
	}
	
	@Listen("onTest=#decodeNo3")
	public void no3Decode(Event evt) {
		String barcodeValue = bc.decodeBarcodeImage(evt.getData().toString());
		Messagebox.show(barcodeValue);
	}
}
