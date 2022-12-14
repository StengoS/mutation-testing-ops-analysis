/*
 * Copyright (C) 2009 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 03. May 2009 by Joerg Schaible
 */
package xcom.thoughtworks.xstream.io.xml;

import xcom.thoughtworks.xstream.core.util.XmlHeaderAwareReader;
import xcom.thoughtworks.xstream.io.HierarchicalStreamReader;
import xcom.thoughtworks.xstream.io.HierarchicalStreamWriter;
import xcom.thoughtworks.xstream.io.StreamException;
import xcom.thoughtworks.xstream.io.naming.NameCoder;
import xcom.thoughtworks.xstream.io.xml.xppdom.XppDom;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * An abstract base class for a driver using an XPP DOM implementation. 
 * 
 * @author Joe Walnes
 * @author J&ouml;rg Schaible
 * @since upcoming
 */
public abstract class AbstractXppDomDriver extends AbstractXmlDriver {

    /**
     * Construct an AbstractXppDomDriver.
     * 
     * @param nameCoder the replacer for XML friendly names
     * @since upcoming
     */
    public AbstractXppDomDriver(NameCoder nameCoder) {
        super(nameCoder);
    }

    /**
     * {@inheritDoc}
     */
    public HierarchicalStreamReader createReader(Reader in) {
        try {
            XmlPullParser parser = createParser();
            parser.setInput(in);
            return new XppDomReader(XppDom.build(parser), getNameCoder());
        } catch (XmlPullParserException e) {
            throw new StreamException(e);
        } catch (IOException e) {
            throw new StreamException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public HierarchicalStreamReader createReader(InputStream in) {
        try {
            return createReader(new XmlHeaderAwareReader(in));
        } catch (UnsupportedEncodingException e) {
            throw new StreamException(e);
        } catch (IOException e) {
            throw new StreamException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public HierarchicalStreamWriter createWriter(Writer out) {
        return new PrettyPrintWriter(out, getNameCoder());
    }

    /**
     * {@inheritDoc}
     */
    public HierarchicalStreamWriter createWriter(OutputStream out) {
        return createWriter(new OutputStreamWriter(out));
    }

    /**
     * Create the parser of the XPP implementation.

     * @throws XmlPullParserException if the parser cannot be created
     * @since upcoming
     */
    protected abstract XmlPullParser createParser() throws XmlPullParserException;
}
