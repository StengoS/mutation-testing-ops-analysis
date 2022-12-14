/*
 * Copyright (C) 2005 Joe Walnes.
 * Copyright (C) 2006, 2007, 2008, 2009 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 12. April 2005 by Joe Walnes
 */
package xcom.thoughtworks.xstream.converters.javabean;

import xcom.thoughtworks.xstream.converters.ConversionException;
import xcom.thoughtworks.xstream.converters.Converter;
import xcom.thoughtworks.xstream.converters.MarshallingContext;
import xcom.thoughtworks.xstream.converters.UnmarshallingContext;
import xcom.thoughtworks.xstream.io.HierarchicalStreamReader;
import xcom.thoughtworks.xstream.io.HierarchicalStreamWriter;
import xcom.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import xcom.thoughtworks.xstream.mapper.Mapper;

/**
 * Can convert any bean with a public default constructor. The {@link BeanProvider} used as
 * default is based on {@link java.beans.BeanInfo}. Indexed properties are currently not supported.
 */
public class JavaBeanConverter implements Converter {

    /*
     * TODO:
     *  - support indexed properties
     */
    private Mapper mapper;
    private BeanProvider beanProvider;
    /**
     * @deprecated As of 1.3, no necessity for field anymore.
     */
    private String classAttributeIdentifier;

    public JavaBeanConverter(Mapper mapper) {
        this(mapper, new BeanProvider());
    }

    public JavaBeanConverter(Mapper mapper, BeanProvider beanProvider) {
        this.mapper = mapper;
        this.beanProvider = beanProvider;
    }

    /**
     * @deprecated As of 1.3, use {@link #JavaBeanConverter(Mapper)} and {@link xcom.thoughtworks.xstream.XStream#aliasAttribute(String, String)}
     */
    public JavaBeanConverter(Mapper mapper, String classAttributeIdentifier) {
        this(mapper, new BeanProvider());
        this.classAttributeIdentifier = classAttributeIdentifier;
    }

    /**
     * Only checks for the availability of a public default constructor.
     * If you need stricter checks, subclass JavaBeanConverter
     */
    public boolean canConvert(Class type) {
        return beanProvider.canInstantiate(type);
    }

    public void marshal(final Object source, final HierarchicalStreamWriter writer, final MarshallingContext context) {
        final String classAttributeName = classAttributeIdentifier != null ? classAttributeIdentifier : mapper.aliasForSystemAttribute("class");
        beanProvider.visitSerializableProperties(source, new BeanProvider.Visitor() {
            public boolean shouldVisit(String name, Class definedIn) {
                return mapper.shouldSerializeMember(definedIn, name);
            }
            
            public void visit(String propertyName, Class fieldType, Class definedIn, Object newObj) {
                if (newObj != null) {
                    writeField(propertyName, fieldType, newObj, definedIn);
                }
            }

            private void writeField(String propertyName, Class fieldType, Object newObj, Class definedIn) {
                String serializedMember = mapper.serializedMember(source.getClass(), propertyName);
				ExtendedHierarchicalStreamWriterHelper.startNode(writer, serializedMember, fieldType);
                Class actualType = newObj.getClass();
                Class defaultType = mapper.defaultImplementationOf(fieldType);
                if (!actualType.equals(defaultType) && classAttributeName != null) {
                    writer.addAttribute(classAttributeName, mapper.serializedClass(actualType));
                }
                context.convertAnother(newObj);

                writer.endNode();
            }
        });
    }

    public Object unmarshal(final HierarchicalStreamReader reader, final UnmarshallingContext context) {
        final Object result = instantiateNewInstance(context);

        while (reader.hasMoreChildren()) {
            reader.moveDown();

            String propertyName = mapper.realMember(result.getClass(), reader.getNodeName());

            boolean propertyExistsInClass = beanProvider.propertyDefinedInClass(propertyName, result.getClass());

            if (propertyExistsInClass) {
                Class type = determineType(reader, result, propertyName);
                Object value = context.convertAnother(result, type);
                beanProvider.writeProperty(result, propertyName, value);
            } else if (mapper.shouldSerializeMember(result.getClass(), propertyName)) {
                throw new ConversionException("Property '" + propertyName + "' not defined in class " + result.getClass().getName());
            }

            reader.moveUp();
        }

        return result;
    }

    private Object instantiateNewInstance(UnmarshallingContext context) {
        Object result = context.currentObject();
        if (result == null) {
            result = beanProvider.newInstance(context.getRequiredType());
        }
        return result;
    }

    private Class determineType(HierarchicalStreamReader reader, Object result, String fieldName) {
        final String classAttributeName = classAttributeIdentifier != null ? classAttributeIdentifier : mapper.aliasForSystemAttribute("class");
        String classAttribute = classAttributeName == null ? null : reader.getAttribute(classAttributeName);
        if (classAttribute != null) {
            return mapper.realClass(classAttribute);
        } else {
            return mapper.defaultImplementationOf(beanProvider.getPropertyType(result, fieldName));
        }
    }

    /**
     * @deprecated As of 1.3
     */
    public static class DuplicateFieldException extends ConversionException {
        public DuplicateFieldException(String msg) {
            super(msg);
        }
    }
}
