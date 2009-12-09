/*
 * KXML2Parser.java
 * 14/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package impl.javame.com.twitterapime.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.twitterapime.parser.Handler;
import com.twitterapime.parser.Parser;
import com.twitterapime.parser.ParserException;
import com.twitterapime.parser.XMLHandler;

/**
 * <p>
 * This class implements a parser based on KXML2 library.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.1
 */
public final class KXML2Parser extends Parser {
	/**
	 * @see com.twitterapime.parser.Parser#parse(java.io.InputStream, com.twitterapime.parser.Handler)
	 */
	public void parse(InputStream stream, Handler handler) throws IOException,
		ParserException {
		if (stream == null) {
			throw new NullPointerException("stream cannot be null.");
		}
		if (handler == null) {
			throw new NullPointerException("handler cannot be null.");
		} else if (!(handler instanceof XMLHandler)) {
			throw new ClassCastException(
				"handler must an instace of XMLHandler.");
		}
		//
		KXmlParser parser = new KXmlParser();
		XMLHandler xmlHandler = (XMLHandler)handler;
		KXML2Attributes attrs = new KXML2Attributes();
		//
		try {
			parser.setInput(new InputStreamReader(stream));
			int etype;
			//
			while (true) {
				etype = parser.next();
				//
				if (etype == XmlPullParser.START_DOCUMENT) {
					xmlHandler.startDocument();
				} else if (etype == XmlPullParser.START_TAG) {
					attrs.loadAttributes(parser);
					//
					xmlHandler.startElement(
						parser.getNamespace(),
						null,
						parser.getName(),
						attrs);
				} else if (etype == XmlPullParser.END_TAG) {
					xmlHandler.endElement(
						parser.getNamespace(),
						null,
						parser.getName());
				} else if (etype == XmlPullParser.TEXT) {
					xmlHandler.text(parser.getText());
				} else if (etype == XmlPullParser.END_DOCUMENT) {
					xmlHandler.endDocument();
					break;
				}
			}
		} catch (XmlPullParserException e) {
			throw new ParserException(e.getMessage());
		}
	}
}