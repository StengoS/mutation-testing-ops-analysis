/*
 * Copyright (C) 2003, 2004, 2005 Joe Walnes.
 * Copyright (C) 2006, 2007 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 01. October 2003 by Joe Walnes
 */
package xcom.thoughtworks.xstream.converters.extended;

import xcom.thoughtworks.xstream.converters.Converter;
import xcom.thoughtworks.xstream.converters.MarshallingContext;
import xcom.thoughtworks.xstream.converters.UnmarshallingContext;
import xcom.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import xcom.thoughtworks.xstream.io.HierarchicalStreamReader;
import xcom.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Converts a java.awt.Color to XML, using four nested elements:
 * red, green, blue, alpha.
 *
 * @author Joe Walnes
 */
public class ColorConverter implements Converter {

    public boolean canConvert(Class type) {
        // String comparison is used here because Color.class loads the class which in turns instantiates AWT,
        // which is nasty if you don't want it.
        return type.getName().equals("java.awt.Color");
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        Color color = (Color) source;
        write("red", color.getRed(), writer);
        write("green", color.getGreen(), writer);
        write("blue", color.getBlue(), writer);
        write("alpha", color.getAlpha(), writer);
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Map elements = new HashMap();
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            elements.put(reader.getNodeName(), Integer.valueOf(reader.getValue()));
            reader.moveUp();
        }
        return new Color(((Integer) elements.get("red")).intValue(),
                ((Integer) elements.get("green")).intValue(),
                ((Integer) elements.get("blue")).intValue(),
                ((Integer) elements.get("alpha")).intValue());
    }

    private void write(String fieldName, int value, HierarchicalStreamWriter writer) {
        ExtendedHierarchicalStreamWriterHelper.startNode(writer, fieldName, int.class);
        writer.setValue(String.valueOf(value));
        writer.endNode();
    }

}
