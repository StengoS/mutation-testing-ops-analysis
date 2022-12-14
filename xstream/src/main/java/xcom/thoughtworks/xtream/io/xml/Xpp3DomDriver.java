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

import xcom.thoughtworks.xstream.io.HierarchicalStreamDriver;
import xcom.thoughtworks.xstream.io.naming.NameCoder;

import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;

/**
 * A {@link HierarchicalStreamDriver} for XPP DOM using the Xpp3 parser.
 * 
 * @author J&ouml;rg Schaible
 * @since upcoming
 */
public class Xpp3DomDriver extends AbstractXppDomDriver {

    /**
     * Construct an Xpp3DomDriver.
     * 
     * @since upcoming
     */
    public Xpp3DomDriver() {
        super(new XmlFriendlyNameCoder());
    }

    /**
     * Construct an Xpp3DomDriver.
     * 
     * @param nameCoder the replacer for XML friendly names
     * @since upcoming
     */
    public Xpp3DomDriver(NameCoder nameCoder) {
        super(nameCoder);
    }

    /**
     * {@inheritDoc}
     */
    protected XmlPullParser createParser() {
        return new MXParser();
    }
}
