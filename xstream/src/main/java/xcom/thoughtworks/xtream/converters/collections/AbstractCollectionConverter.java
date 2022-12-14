/*
 * Copyright (C) 2003, 2004, 2005 Joe Walnes.
 * Copyright (C) 2006, 2007, 2008, 2009 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 26. September 2003 by Joe Walnes
 */
package xcom.thoughtworks.xstream.converters.collections;

import xcom.thoughtworks.xstream.converters.ConversionException;
import xcom.thoughtworks.xstream.converters.Converter;
import xcom.thoughtworks.xstream.converters.MarshallingContext;
import xcom.thoughtworks.xstream.converters.UnmarshallingContext;
import xcom.thoughtworks.xstream.core.util.HierarchicalStreams;
import xcom.thoughtworks.xstream.io.HierarchicalStreamReader;
import xcom.thoughtworks.xstream.io.HierarchicalStreamWriter;
import xcom.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import xcom.thoughtworks.xstream.mapper.Mapper;

/**
 * Base helper class for converters that need to handle
 * collections of items (arrays, Lists, Maps, etc).
 * <p/>
 * <p>Typically, subclasses of this will converter the outer
 * structure of the collection, loop through the contents and
 * call readItem() or writeItem() for each item.</p>
 *
 * @author Joe Walnes
 */
public abstract class AbstractCollectionConverter implements Converter {

    private final Mapper mapper;

    public abstract boolean canConvert(Class type);

    public AbstractCollectionConverter(Mapper mapper) {
        this.mapper = mapper;
    }

    protected Mapper mapper() {
        return mapper;
    }

    public abstract void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context);

    public abstract Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context);



    protected void writeItem(Object item, MarshallingContext context, HierarchicalStreamWriter writer) {
        // PUBLISHED API METHOD! If changing signature, ensure backwards compatibility.
        if (item == null) {
            // todo: this is duplicated in TreeMarshaller.start()
            String name = mapper().serializedClass(null);
            ExtendedHierarchicalStreamWriterHelper.startNode(writer, name, Mapper.Null.class);
            writer.endNode();
        } else {
            String name = mapper().serializedClass(item.getClass());
            ExtendedHierarchicalStreamWriterHelper.startNode(writer, name, item.getClass());
            context.convertAnother(item);
            writer.endNode();
        }
    }

    protected Object readItem(HierarchicalStreamReader reader, UnmarshallingContext context, Object current) {
        Class type = HierarchicalStreams.readClassType(reader, mapper());
        return context.convertAnother(current, type);
    }

    protected Object createCollection(Class type) {
        Class defaultType = mapper().defaultImplementationOf(type);
        try {
            return defaultType.newInstance();
        } catch (InstantiationException e) {
            throw new ConversionException("Cannot instantiate " + defaultType.getName(), e);
        } catch (IllegalAccessException e) {
            throw new ConversionException("Cannot instantiate " + defaultType.getName(), e);
        }
    }
}
