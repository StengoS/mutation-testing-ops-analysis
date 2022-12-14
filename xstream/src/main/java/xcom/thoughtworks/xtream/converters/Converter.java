/*
 * Copyright (C) 2003, 2004 Joe Walnes.
 * Copyright (C) 2006, 2007 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 26. September 2003 by Joe Walnes
 */
package xcom.thoughtworks.xstream.converters;

import xcom.thoughtworks.xstream.io.HierarchicalStreamReader;
import xcom.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Converter implementations are responsible marshalling Java objects
 * to/from textual data.
 * <p/>
 * <p>If an exception occurs during processing, a {@link ConversionException}
 * should be thrown.</p>
 * <p/>
 * <p>If working with the high level {@link xcom.thoughtworks.xstream.XStream} facade,
 * you can register new converters using the XStream.registerConverter() method.</p>
 * <p/>
 * <p>If working with the lower level API, the
 * {@link xcom.thoughtworks.xstream.converters.ConverterLookup} implementation is
 * responsible for looking up the appropriate converter.</p>
 * <p/>
 * <p>Converters for object that can store all information in a single value
 * should implement {@link xcom.thoughtworks.xstream.converters.SingleValueConverter}.
 * <p>{@link xcom.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter}
 * provides a starting point.</p>
 * <p/>
 * <p>{@link xcom.thoughtworks.xstream.converters.collections.AbstractCollectionConverter}
 * provides a starting point for objects that hold a collection of other objects
 * (such as Lists and Maps).</p>
 *
 * @author Joe Walnes
 * @see xcom.thoughtworks.xstream.XStream
 * @see xcom.thoughtworks.xstream.converters.ConverterLookup
 * @see xcom.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter
 * @see xcom.thoughtworks.xstream.converters.collections.AbstractCollectionConverter
 */
public interface Converter extends ConverterMatcher {

    /**
     * Convert an object to textual data.
     *
     * @param source  The object to be marshalled.
     * @param writer  A stream to write to.
     * @param context A context that allows nested objects to be processed by XStream.
     */
    void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context);

    /**
     * Convert textual data back into an object.
     *
     * @param reader  The stream to read the text from.
     * @param context
     * @return The resulting object.
     */
    Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context);

}
