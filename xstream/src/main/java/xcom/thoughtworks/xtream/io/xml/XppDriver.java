/*
 * Copyright (C) 2004, 2005, 2006 Joe Walnes.
 * Copyright (C) 2006, 2007, 2008, 2009 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 08. March 2004 by Joe Walnes
 */
package xcom.thoughtworks.xstream.io.xml;


import xcom.thoughtworks.xstream.io.HierarchicalStreamDriver;
import xcom.thoughtworks.xstream.io.naming.NameCoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


/**
 * A {@link HierarchicalStreamDriver} using the XmlPullParserFactory to locate an XPP parser.
 *
 * @author Joe Walnes
 * @author J&ouml;rg Schaible
 */
public class XppDriver extends AbstractXppDriver {

    private static XmlPullParserFactory factory;

    public XppDriver() {
        super(new XmlFriendlyNameCoder());
    }

    /**
     * @since upcoming
     */
    public XppDriver(NameCoder nameCoder) {
        super(nameCoder);
    }

    /**
     * @since 1.2
     * @deprecated As of upcoming, use {@link XppDriver#XppDriver(NameCoder)} instead.
     */
    public XppDriver(XmlFriendlyReplacer replacer) {
        this((NameCoder)replacer);
    }

    /**
     * {@inheritDoc}
     */
    protected synchronized XmlPullParser createParser() throws XmlPullParserException {
        if (factory == null) {
            factory = XmlPullParserFactory.newInstance(null, XppDriver.class);
        }
        return factory.newPullParser();
    }
}
