/*
 * Copyright (C) 2005 Joe Walnes.
 * Copyright (C) 2006, 2007, 2009 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 18. March 2005 by Joe Walnes
 */

// ***** READ THIS *****
// This class will only compile with JDK 1.5.0 or above as it test Java enums.
// If you are using an earlier version of Java, just don't try to build this class. XStream should work fine without it.

package xcom.thoughtworks.xstream.converters.enums;

import xcom.thoughtworks.xstream.converters.Converter;
import xcom.thoughtworks.xstream.converters.MarshallingContext;
import xcom.thoughtworks.xstream.converters.UnmarshallingContext;
import xcom.thoughtworks.xstream.io.HierarchicalStreamWriter;
import xcom.thoughtworks.xstream.io.HierarchicalStreamReader;

/**
 * Converter for JDK 1.5 enums. Combined with EnumMapper this also deals with polymorphic enums.
 *
 * @author Eric Snell 
 * @author Bryan Coleman
 */
public class EnumConverter implements Converter {

    public boolean canConvert(Class type) {
        return type.isEnum() || Enum.class.isAssignableFrom(type);
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        writer.setValue(((Enum) source).name());
    }

    @SuppressWarnings("unchecked")
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Class type = context.getRequiredType();
        if (type.getSuperclass() != Enum.class) {
            type = type.getSuperclass(); // polymorphic enums
        }
        return Enum.valueOf(type, reader.getValue());
    }

}
