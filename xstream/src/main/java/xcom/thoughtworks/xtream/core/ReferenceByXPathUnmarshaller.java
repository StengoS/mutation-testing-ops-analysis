/*
 * Copyright (C) 2004, 2005, 2006 Joe Walnes.
 * Copyright (C) 2006, 2007, 2009 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 03. April 2004 by Joe Walnes
 */
package xcom.thoughtworks.xstream.core;

import xcom.thoughtworks.xstream.converters.ConverterLookup;
import xcom.thoughtworks.xstream.io.HierarchicalStreamReader;
import xcom.thoughtworks.xstream.io.path.Path;
import xcom.thoughtworks.xstream.io.path.PathTracker;
import xcom.thoughtworks.xstream.io.path.PathTrackingReader;
import xcom.thoughtworks.xstream.io.xml.XmlFriendlyReader;
import xcom.thoughtworks.xstream.mapper.Mapper;

public class ReferenceByXPathUnmarshaller extends AbstractReferenceUnmarshaller {

    private PathTracker pathTracker = new PathTracker();
    protected boolean isXmlFriendly;

    public ReferenceByXPathUnmarshaller(Object root, HierarchicalStreamReader reader,
                                        ConverterLookup converterLookup, Mapper mapper) {
        super(root, reader, converterLookup, mapper);
        this.reader = new PathTrackingReader(reader, pathTracker);
        isXmlFriendly = reader.underlyingReader() instanceof XmlFriendlyReader;
    }

    protected Object getReferenceKey(String reference) {
        final Path path = new Path(isXmlFriendly ? ((XmlFriendlyReader)reader.underlyingReader()).unescapeXmlName(reference) : reference);
        // We have absolute references, if path starts with '/'
        return reference.charAt(0) != '/' ? pathTracker.getPath().apply(path) : path;
    }

    protected Object getCurrentReferenceKey() {
        return pathTracker.getPath();
    }

}
