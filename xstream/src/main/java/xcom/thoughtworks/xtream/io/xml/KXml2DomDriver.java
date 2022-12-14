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

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;

/**
 * A {@link HierarchicalStreamDriver} for XPP DOM using the Xpp3 parser.
 * 
 * @author J&ouml;rg Schaible
 * @since upcoming
 */
public class KXml2DomDriver extends AbstractXppDomDriver {

    /**
     * Construct an KXml2DomDriver.
     * 
     * @since upcoming
     */
    public KXml2DomDriver() {
        super(new XmlFriendlyNameCoder());
    }

    /**
     * Construct a KXml2DomDriver.
     * 
     * @param nameCoder the replacer for XML friendly names
     * @since upcoming
     */
    public KXml2DomDriver(NameCoder nameCoder) {
        super(nameCoder);
    }

    /**
     * {@inheritDoc}
     */
    protected XmlPullParser createParser() {
        return new KXmlParser();
    }
}
