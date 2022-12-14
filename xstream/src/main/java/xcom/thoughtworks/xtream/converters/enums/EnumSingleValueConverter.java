/*
 * Copyright (C) 2008, 2009 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 *
 * Created on 12. February 2008 by Joerg Schaible
 */
package xcom.thoughtworks.xstream.converters.enums;

import xcom.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;


/**
 * A single value converter for arbitrary enums. Converter is internally automatically
 * instantiated for enum types.
 * 
 * @author J&ouml;rg Schaible
 * @since 1.3
 */
public class EnumSingleValueConverter extends AbstractSingleValueConverter {

    private final Class enumType;

    public EnumSingleValueConverter(Class type) {
        if (!Enum.class.isAssignableFrom(type) && type != Enum.class) {
            throw new IllegalArgumentException("Converter can only handle defined enums");
        }
        enumType = type;
    }

    @SuppressWarnings("unchecked")
    public boolean canConvert(Class type) {
        return enumType.isAssignableFrom(type);
    }

    @SuppressWarnings("unchecked")
    public Object fromString(String str) {
        return Enum.valueOf(enumType, str);
    }
}
