/*
 * Copyright (C) 2004, 2005, 2006 Joe Walnes.
 * Copyright (C) 2006, 2007, 2009 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 29. September 2004 by James Strachan
 */
package xcom.thoughtworks.xstream.io.xml;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import xcom.thoughtworks.xstream.io.HierarchicalStreamReader;
import xcom.thoughtworks.xstream.io.HierarchicalStreamWriter;
import xcom.thoughtworks.xstream.io.StreamException;
import xcom.thoughtworks.xstream.io.naming.NameCoder;

/**
 * A driver using the StAX API to create XML reader and writer.
 *
 * @author James Strachan
 * @author J&ouml;rg Schaible
 * @version $Revision: 1705 $
 */
public class StaxDriver extends AbstractXmlDriver {

    private QNameMap qnameMap;
    private XMLInputFactory inputFactory;
    private XMLOutputFactory outputFactory;

    public StaxDriver() {
        this(new QNameMap());
    }

    public StaxDriver(QNameMap qnameMap) {
        this(qnameMap, new XmlFriendlyNameCoder());
    }

    /**
     * @since upcoming
     */
    public StaxDriver(QNameMap qnameMap, NameCoder nameCoder) {
        super(nameCoder);
        this.qnameMap = qnameMap;
    }
    
    /**
     * @since upcoming
     */
    public StaxDriver(NameCoder nameCoder) {
        this(new QNameMap(), nameCoder);
    }

    /**
     * @since 1.2
     * @deprecated As of upcoming, use {@link StaxDriver#StaxDriver(QNameMap, NameCoder)} instead.
     */
    public StaxDriver(QNameMap qnameMap, XmlFriendlyReplacer replacer) {
        this(qnameMap, (NameCoder)replacer);
    }
    
    /**
     * @since 1.2
     * @deprecated As of upcoming, use {@link StaxDriver#StaxDriver(NameCoder)} instead.
     */
    public StaxDriver(XmlFriendlyReplacer replacer) {
        this(new QNameMap(), (NameCoder)replacer);
    }

    public HierarchicalStreamReader createReader(Reader xml) {
        try {
            return createStaxReader(createParser(xml));
        }
        catch (XMLStreamException e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamReader createReader(InputStream in) {
        try {
            return createStaxReader(createParser(in));
        }
        catch (XMLStreamException e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamWriter createWriter(Writer out) {
        try {
            return createStaxWriter(getOutputFactory().createXMLStreamWriter(out));
        }
        catch (XMLStreamException e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamWriter createWriter(OutputStream out) {
        try {
            return createStaxWriter(getOutputFactory().createXMLStreamWriter(out));
        }
        catch (XMLStreamException e) {
            throw new StreamException(e);
        }
    }

    public AbstractPullReader createStaxReader(XMLStreamReader in) {
        return new StaxReader(qnameMap, in, getNameCoder());
    }

    public StaxWriter createStaxWriter(XMLStreamWriter out, boolean writeStartEndDocument) throws XMLStreamException {
        return new StaxWriter(qnameMap, out, writeStartEndDocument, isRepairingNamespace(), getNameCoder());
    }

    public StaxWriter createStaxWriter(XMLStreamWriter out) throws XMLStreamException {
        return createStaxWriter(out, true);
    }


    // Properties
    //-------------------------------------------------------------------------
    public QNameMap getQnameMap() {
        return qnameMap;
    }

    public void setQnameMap(QNameMap qnameMap) {
        this.qnameMap = qnameMap;
    }

    public XMLInputFactory getInputFactory() {
        if (inputFactory == null) {
            inputFactory = createInputFactory();
        }
        return inputFactory;
    }

    public XMLOutputFactory getOutputFactory() {
        if (outputFactory == null) {
            outputFactory = createOutputFactory();
        }
        return outputFactory;
    }

    public boolean isRepairingNamespace() {
        return Boolean.TRUE.equals(getOutputFactory().getProperty(
                XMLOutputFactory.IS_REPAIRING_NAMESPACES));
    }

    /**
     * @since 1.2
     */
    public void setRepairingNamespace(boolean repairing) {
        getOutputFactory().setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES,
                repairing ? Boolean.TRUE : Boolean.FALSE);
    }


    // Implementation methods
    //-------------------------------------------------------------------------
    protected XMLStreamReader createParser(Reader xml) throws XMLStreamException {
        return getInputFactory().createXMLStreamReader(xml);
    }

    protected XMLStreamReader createParser(InputStream xml) throws XMLStreamException {
        return getInputFactory().createXMLStreamReader(xml);
    }

    /**
     * @since upcoming
     */
    protected XMLInputFactory createInputFactory() {
        return XMLInputFactory.newInstance();
    }

    /**
     * @since upcoming
     */
    protected XMLOutputFactory createOutputFactory() {
        return XMLOutputFactory.newInstance();
    }
}
