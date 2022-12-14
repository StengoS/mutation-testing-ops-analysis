/*
 * Copyright (C) 2004 Joe Walnes.
 * Copyright (C) 2006, 2007 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 03. March 2004 by Joe Walnes
 */
package xcom.thoughtworks.xstream.converters.extended;

import xcom.thoughtworks.xstream.converters.Converter;
import xcom.thoughtworks.xstream.converters.MarshallingContext;
import xcom.thoughtworks.xstream.converters.UnmarshallingContext;
import xcom.thoughtworks.xstream.converters.basic.ByteConverter;
import xcom.thoughtworks.xstream.core.util.Base64Encoder;
import xcom.thoughtworks.xstream.io.HierarchicalStreamReader;
import xcom.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Converts a byte array to a single Base64 encoding string.
 *
 * @author Joe Walnes
 */
public class EncodedByteArrayConverter implements Converter {

    private static final Base64Encoder base64 = new Base64Encoder();
    private static final ByteConverter byteConverter = new ByteConverter();

    public boolean canConvert(Class type) {
        return type.isArray() && type.getComponentType().equals(byte.class);
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        writer.setValue(base64.encode((byte[]) source));
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        String data = reader.getValue(); // needs to be called before hasMoreChildren.
        if (!reader.hasMoreChildren()) {
            return base64.decode(data);
        } else {
            // backwards compatability ... try to unmarshal byte arrays that haven't been encoded
            return unmarshalIndividualByteElements(reader, context);
        }
    }

    private Object unmarshalIndividualByteElements(HierarchicalStreamReader reader, UnmarshallingContext context) {
        List bytes = new ArrayList(); // have to create a temporary list because don't know the size of the array
        boolean firstIteration = true;
        while (firstIteration || reader.hasMoreChildren()) { // hangover from previous hasMoreChildren
            reader.moveDown();
            //bytes.add(byteConverter.unmarshal(reader, context));
            bytes.add(byteConverter.fromString(reader.getValue()));
            reader.moveUp();
            firstIteration = false;
        }
        // copy into real array
        byte[] result = new byte[bytes.size()];
        int i = 0;
        for (Iterator iterator = bytes.iterator(); iterator.hasNext();) {
            Byte b = (Byte) iterator.next();
            result[i] = b.byteValue();
            i++;
        }
        return result;
    }

}
