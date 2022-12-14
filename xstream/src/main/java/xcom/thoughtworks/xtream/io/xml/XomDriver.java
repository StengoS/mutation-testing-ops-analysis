/*
 * Copyright (C) 2006 Joe Walnes.
 * Copyright (C) 2006, 2007, 2009 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 12. April 2006 by Joerg Schaible
 */
package xcom.thoughtworks.xstream.io.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import xcom.thoughtworks.xstream.io.HierarchicalStreamReader;
import xcom.thoughtworks.xstream.io.HierarchicalStreamWriter;
import xcom.thoughtworks.xstream.io.StreamException;
import xcom.thoughtworks.xstream.io.naming.NameCoder;

public class XomDriver extends AbstractXmlDriver {

    private final Builder builder;

    public XomDriver() {
        this(new Builder());
    }

    public XomDriver(Builder builder) {
        this(builder, new XmlFriendlyNameCoder());
    }
    
    /**
     * @since upcoming
     */
    public XomDriver(NameCoder nameCoder) {
        this(new Builder(), nameCoder);    
    }
    
    /**
     * @since upcoming
     */
    public XomDriver(Builder builder, NameCoder nameCoder) {
        super(nameCoder);    
        this.builder = builder;
    }

    /**
     * @since 1.2
     * @deprecated As of upcoming, use {@link #XomDriver(Builder, NameCoder)} instead
     */
    public XomDriver(XmlFriendlyReplacer replacer) {
        this(new Builder(), replacer);        
    }
    
    /**
     * @since 1.2
     * @deprecated As of upcoming, use {@link #XomDriver(Builder, NameCoder)} instead
     */
    public XomDriver(Builder builder, XmlFriendlyReplacer replacer) {
        this((NameCoder)replacer);    
    }

    protected Builder getBuilder() {
        return this.builder;
    }

    public HierarchicalStreamReader createReader(Reader text) {
        try {
            Document document = builder.build(text);
            return new XomReader(document, getNameCoder());
        } catch (ValidityException e) {
            throw new StreamException(e);
        } catch (ParsingException e) {
            throw new StreamException(e);
        } catch (IOException e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamReader createReader(InputStream in) {
        try {
            Document document = builder.build(in);
            return new XomReader(document, getNameCoder());
        } catch (ValidityException e) {
            throw new StreamException(e);
        } catch (ParsingException e) {
            throw new StreamException(e);
        } catch (IOException e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamWriter createWriter(final Writer out) {
        return new PrettyPrintWriter(out, getNameCoder());
    }

    public HierarchicalStreamWriter createWriter(final OutputStream out) {
        return new PrettyPrintWriter(new OutputStreamWriter(out), getNameCoder());
    }
}
