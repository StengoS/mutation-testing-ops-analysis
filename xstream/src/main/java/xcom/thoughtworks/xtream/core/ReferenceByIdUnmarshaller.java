/*
 * Copyright (C) 2004, 2005, 2006 Joe Walnes.
 * Copyright (C) 2006, 2007, 2008, 2009 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 15. March 2004 by Joe Walnes
 */
package xcom.thoughtworks.xstream.core;

import xcom.thoughtworks.xstream.converters.ConverterLookup;
import xcom.thoughtworks.xstream.io.HierarchicalStreamReader;
import xcom.thoughtworks.xstream.mapper.Mapper;

public class ReferenceByIdUnmarshaller extends AbstractReferenceUnmarshaller {

    public ReferenceByIdUnmarshaller(Object root, HierarchicalStreamReader reader,
                                     ConverterLookup converterLookup, Mapper mapper) {
        super(root, reader, converterLookup, mapper);
    }

    protected Object getReferenceKey(String reference) {
        return reference;
    }

    protected Object getCurrentReferenceKey() {
        String attributeName = getMapper().aliasForSystemAttribute("id");
        return attributeName == null ? null : reader.getAttribute(attributeName);
    }
}
