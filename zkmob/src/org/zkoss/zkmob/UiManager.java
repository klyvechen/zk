/* UiManager.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Tue May 5 10:12:21     2007, Created by henrichen
}}IS_NOTE

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zkmob;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Screen;
import javax.microedition.lcdui.Ticker;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.zkoss.zkmob.impl.AlertFactory;
import org.zkoss.zkmob.impl.ChoiceGroupFactory;
import org.zkoss.zkmob.impl.CommandFactory;
import org.zkoss.zkmob.impl.DateFieldFactory;
import org.zkoss.zkmob.impl.FormFactory;
import org.zkoss.zkmob.impl.GaugeFactory;
import org.zkoss.zkmob.impl.ImageItemFactory;
import org.zkoss.zkmob.impl.ListFactory;
import org.zkoss.zkmob.impl.ListItemFactory;
import org.zkoss.zkmob.impl.SpacerFactory;
import org.zkoss.zkmob.impl.StringItemFactory;
import org.zkoss.zkmob.impl.TextBoxFactory;
import org.zkoss.zkmob.impl.TextFieldFactory;
import org.zkoss.zkmob.impl.TickerFactory;
import org.zkoss.zkmob.impl.Zk;
import org.zkoss.zkmob.impl.ZkFactory;

/** 
 * The manager to handle the life cycle of zmobi UI components. 
 */
public class UiManager {
	private static Hashtable _uiFactoryMap = new Hashtable(16);
	//predefined a SAXParserFactory
	private static SAXParserFactory _factory = SAXParserFactory.newInstance();
	
	//predefined Ui component factory
	static {
		//virtual components
		new ZkFactory("zk");
		new TickerFactory("tc");
		new ListItemFactory("li");
		new CommandFactory("cm");
		
		//top level window
		new FormFactory("fr");
		new AlertFactory("al");
		new ListFactory("ls");
		new TextBoxFactory("tb");
		
		//items (MIDP 1.0)
		new DateFieldFactory("df");
		new GaugeFactory("gg");
		new ImageItemFactory("ii");
		new StringItemFactory("si");
		new TextFieldFactory("tf");
		new ChoiceGroupFactory("cg");
		
		//items (MIDP 2.0)
		new SpacerFactory("sp");
	}
	
	//The current ZK desktop
	private Zk _zk;
	
	/** register an UiFactory to a Ui tag name. 
	 * @param name the Ui tag name
	 * @param uiFactory an UiFactory used to create an Ui component
	 */
	public static void registerUiFactory(String name, UiFactory uiFactory) {
		_uiFactoryMap.put(name, uiFactory);
	}

	public static SAXParser getSAXParser() {
		try {
			return _factory.newSAXParser();
		} catch (ParserConfigurationException e) {
			throw new IllegalStateException(e.toString()); 
		} catch (SAXException e) {
			throw new IllegalStateException(e.toString()); 
		}
	}
	
	/**
	 * given Ui tag name and create an associated Ui component.
	 * @param parent the parent component
	 * @param tag the Ui tag name
	 * @param attrs the Ui tag attributes 
	 */
	public Object create(Object parent, String tag, Attributes attrs, Context ctx) {
		UiFactory uiFactory = (UiFactory) _uiFactoryMap.get(tag);

		if (uiFactory != null) {
			ZkComponent comp = (ZkComponent) uiFactory.create(parent, tag, attrs, ctx);
			if (comp instanceof Zk) {
				_zk = (Zk) comp;
			}
			_zk.registerUi(comp.getId(), comp);

			if (comp instanceof Displayable) {
				//remember current displayable
				final String current = attrs.getValue("cr"); //current
				if (_zk.getCurrent() == null && "t".equalsIgnoreCase(current)) {
					_zk.setCurrent((Displayable) comp);
				}
				
				//associate Ticker with Screen component
				//Ticker must be created before Component
				if (comp instanceof Screen) {
					String tickerid = attrs.getValue("tc");
					if (tickerid != null) {
						Object ticker = _zk.lookupUi(tickerid);
						if (ticker instanceof Ticker) {
							((Screen)comp).setTicker((Ticker) ticker);
						}
					}
				}
			}
			return comp;
		}
		return null;
	}

	/**
	 * Called after PageHandler finishing creating a component.
	 * @param parent The parent component
	 * @param tag The tag name
	 * @param comp The component
	 * @param ctx The page handling context
	 */
	public void afterCreate(Object parent, String tag, Object comp, Context ctx) {
		final UiFactory uiFactory = (UiFactory) _uiFactoryMap.get(tag);

		if (uiFactory != null) {
			uiFactory.afterCreate(parent, tag, comp, ctx);
		}
	}

	public void loadPageOnThread(Display disp, String url) {
		new Thread(new PageRequest(disp, url, this)).start();
	}
	
	/* package */ void loadPage(Display disp, String url) {
		try {
			myLoadPage(disp, url);
		} catch (IOException e) {
			final Alert alert = new Alert("Exception:", e.toString(), null, AlertType.ERROR);
			alert.setTimeout(Alert.FOREVER);
			disp.setCurrent(alert);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void myLoadPage(Display disp, String url) throws IOException, SAXException {
		HttpConnection conn = null;
		InputStream is = null;
		
		try {
		    conn = (HttpConnection)Connector.open(url);
		    is = request(conn, null);
		    // Load the responsed page, the current Displayable is put in _current
		    getSAXParser().parse(is, new PageHandler(this, new Context(conn, disp)));
		} finally {
			if (is != null)	is.close();
			if (conn != null) conn.close();
		}
	}
	
	/** utility to load Image on a separate thread.
	 * 
	 * @param item Imageable item to be setup the loaded image
	 * @param url the Image url
	 */
	public static void loadImageOnThread(Imageable item, String url) {
		new Thread(new ImageRequest(item, url)).start();
	}
	
	/** utility to be called by {@link ImageRequest} only.
	 * 
	 * @param url The image url
	 * @return the Image object
	 */
	/*package*/ static Image loadImage(String url) {
		try {
			return myLoadImage(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private static Image myLoadImage(String url) throws IOException {
		HttpConnection conn = null;
		InputStream is = null;
		
		try {
		    conn = (HttpConnection)Connector.open(url);
		    is = request(conn, null);
		
		    // Load the response
		    return Image.createImage(is);
		} finally {
			if (is != null)	is.close();
			if (conn != null) conn.close();
		}
	}		

	public static InputStream request(HttpConnection conn, String request) throws IOException {
		OutputStream os = null;
		int rc;
		
		try {
		    // Set the request method and headers
		    conn.setRequestMethod(HttpConnection.POST);
		    conn.setRequestProperty("Content-Type",
            	"application/x-www-form-urlencoded");
		    conn.setRequestProperty("User-Agent",
		        "Profile/MIDP-2.0 Configuration/CLDC-1.0 MIL");
		    conn.setRequestProperty("Content-Language", "en-US");
		
		    // Getting the output stream may flush the headers
		    os = conn.openOutputStream();
		    if (request != null) {
		    	os.write(request.getBytes());
		    }
		    // Getting the response code will open the connection,
		    // send the request, and read the HTTP response headers.
		    // The headers are stored until requested.
		    rc = conn.getResponseCode();
		    if (rc != HttpConnection.HTTP_OK) {
		        throw new IOException("HTTP response code: " + rc);
		    }
		
		    return conn.openInputStream();
		} catch (ClassCastException e) {
		    throw new IllegalArgumentException("Not an HTTP URL");
		} finally {
			if (os != null) os.close();
		}
	}
}
