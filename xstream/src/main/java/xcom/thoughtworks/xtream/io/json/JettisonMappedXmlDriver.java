/*
 * Copyright (c) 2007, 2008, 2009 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 30. March 2007 by Joerg Schaible
 */
package xcom.thoughtworks.xstream.io.json;

import xcom.thoughtworks.xstream.io.HierarchicalStreamDriver;
import xcom.thoughtworks.xstream.io.HierarchicalStreamReader;
import xcom.thoughtworks.xstream.io.HierarchicalStreamWriter;
import xcom.thoughtworks.xstream.io.StreamException;
import xcom.thoughtworks.xstream.io.xml.QNameMap;
import xcom.thoughtworks.xstream.io.xml.StaxReader;

import org.codehaus.jettison.mapped.Configuration;
import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.codehaus.jettison.mapped.MappedXMLInputFactory;
import org.codehaus.jettison.mapped.MappedXMLOutputFactory;

import javax.xml.stream.XMLStreamException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;


/**
 * Simple XStream driver wrapping Jettison's Mapped reader and writer. Serializes object from
 * and to JSON.
 * 
 * @author Dejan Bosanac
 */
public class JettisonMappedXmlDriver implements HierarchicalStreamDriver {

    private final MappedXMLOutputFactory mof;
    private final MappedXMLInputFactory mif;
    private final MappedNamespaceConvention convention;

    public JettisonMappedXmlDriver() {
        this(new Configuration());
    }

    public JettisonMappedXmlDriver(final Configuration config) {
        mof = new MappedXMLOutputFactory(config);
        mif = new MappedXMLInputFactory(config);
        convention = new MappedNamespaceConvention(config);
    }
    
    public HierarchicalStreamReader createReader(final Reader reader) {
        try {
            return new StaxReader(new QNameMap(), mif.createXMLStreamReader(reader));
        } catch (final XMLStreamException e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamReader createReader(final InputStream input) {
        try {
            return new StaxReader(new QNameMap(), mif.createXMLStreamReader(input));
        } catch (final XMLStreamException e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamWriter createWriter(final Writer writer) {
        try {
            return new JettisonStaxWriter(new QNameMap(), mof.createXMLStreamWriter(writer), convention);
        } catch (final XMLStreamException e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamWriter createWriter(final OutputStream output) {
        try {
            return new JettisonStaxWriter(new QNameMap(), mof.createXMLStreamWriter(output), convention);
        } catch (final XMLStreamException e) {
            throw new StreamException(e);
        }
    }

}
