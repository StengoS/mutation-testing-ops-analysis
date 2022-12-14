/*
 * Copyright (C) 2006, 2007 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 03. April 2006 by Joerg Schaible
 */
package xcom.thoughtworks.xstream.converters.reflection;

import xcom.thoughtworks.xstream.converters.ConversionException;
import xcom.thoughtworks.xstream.converters.Converter;
import xcom.thoughtworks.xstream.converters.MarshallingContext;
import xcom.thoughtworks.xstream.converters.UnmarshallingContext;
import xcom.thoughtworks.xstream.io.HierarchicalStreamReader;
import xcom.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * A special converter that prevents self-serialization. The serializing XStream instance
 * adds a converter of this type to prevent self-serialization and will throw an
 * exception instead.
 * 
 * @author J&ouml;rg Schaible
 * @since 1.2
 */
public class SelfStreamingInstanceChecker implements Converter {

    private final Object self;
    private Converter defaultConverter;

    public SelfStreamingInstanceChecker(Converter defaultConverter, Object xstream) {
        this.defaultConverter = defaultConverter;
        this.self = xstream;
    }

    public boolean canConvert(Class type) {
        return type == self.getClass();
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        if (source == self) {
            throw new ConversionException("Cannot marshal the XStream instance in action");
        }
        defaultConverter.marshal(source, writer, context);
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return defaultConverter.unmarshal(reader, context);
    }

}
