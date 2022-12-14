/*
 * Copyright (C) 2007, 2008, 2009 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 07. November 2007 by Joerg Schaible
 */
package xcom.thoughtworks.xstream.mapper;

import xcom.thoughtworks.xstream.InitializationException;
import xcom.thoughtworks.xstream.XStream;
import xcom.thoughtworks.xstream.annotations.XStreamAlias;
import xcom.thoughtworks.xstream.annotations.XStreamAsAttribute;
import xcom.thoughtworks.xstream.annotations.XStreamConverter;
import xcom.thoughtworks.xstream.annotations.XStreamConverters;
import xcom.thoughtworks.xstream.annotations.XStreamImplicit;
import xcom.thoughtworks.xstream.annotations.XStreamImplicitCollection;
import xcom.thoughtworks.xstream.annotations.XStreamInclude;
import xcom.thoughtworks.xstream.annotations.XStreamOmitField;
import xcom.thoughtworks.xstream.converters.Converter;
import xcom.thoughtworks.xstream.converters.ConverterMatcher;
import xcom.thoughtworks.xstream.converters.ConverterRegistry;
import xcom.thoughtworks.xstream.converters.SingleValueConverter;
import xcom.thoughtworks.xstream.converters.SingleValueConverterWrapper;
import xcom.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import xcom.thoughtworks.xstream.core.JVM;
import xcom.thoughtworks.xstream.core.util.DependencyInjectionFactory;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;


/**
 * A mapper that uses annotations to prepare the remaining mappers in the chain.
 * 
 * @author J&ouml;rg Schaible
 * @since 1.3
 */
public class AnnotationMapper extends MapperWrapper implements AnnotationConfiguration {

    private boolean locked;
    private final Object[] arguments;
    private final ConverterRegistry converterRegistry;
    private final ClassAliasingMapper classAliasingMapper;
    private final DefaultImplementationsMapper defaultImplementationsMapper;
    private final ImplicitCollectionMapper implicitCollectionMapper;
    private final FieldAliasingMapper fieldAliasingMapper;
    private final AttributeMapper attributeMapper;
    private final LocalConversionMapper localConversionMapper;
    private final Map<Class<?>, Map<Object, Converter>> converterCache = 
        new HashMap<Class<?>, Map<Object, Converter>>();
    private final Set<Class<?>> annotatedTypes = new WeakHashSet<Class<?>>();

    /**
     * Construct an AnnotationMapper.
     * 
     * @param wrapped the next {@link Mapper} in the chain
     * @since 1.3
     */
    public AnnotationMapper(
        final Mapper wrapped, final ConverterRegistry converterRegistry,
        final ClassLoader classLoader, final ReflectionProvider reflectionProvider,
        final JVM jvm) {
        super(wrapped);
        this.converterRegistry = converterRegistry;
        annotatedTypes.add(Object.class);
        classAliasingMapper = (ClassAliasingMapper)lookupMapperOfType(ClassAliasingMapper.class);
        defaultImplementationsMapper = (DefaultImplementationsMapper)lookupMapperOfType(DefaultImplementationsMapper.class);
        implicitCollectionMapper = (ImplicitCollectionMapper)lookupMapperOfType(ImplicitCollectionMapper.class);
        fieldAliasingMapper = (FieldAliasingMapper)lookupMapperOfType(FieldAliasingMapper.class);
        attributeMapper = (AttributeMapper)lookupMapperOfType(AttributeMapper.class);
        localConversionMapper = (LocalConversionMapper)lookupMapperOfType(LocalConversionMapper.class);
        locked = true;
        arguments = new Object[]{this, classLoader, reflectionProvider, jvm};
    }

    @Override
    public String realMember(final Class type, final String serialized) {
        if (!locked) {
            processAnnotations(type);
        }
        return super.realMember(type, serialized);
    }

    @Override
    public String serializedClass(final Class type) {
        if (!locked) {
            processAnnotations(type);
        }
        return super.serializedClass(type);
    }

    @Override
    public Class defaultImplementationOf(final Class type) {
        if (!locked) {
            processAnnotations(type);
        }
        final Class defaultImplementation = super.defaultImplementationOf(type);
        if (!locked) {
            processAnnotations(defaultImplementation);
        }
        return defaultImplementation;
    }

    @Override
    public Converter getLocalConverter(final Class definedIn, final String fieldName) {
        if (!locked) {
            processAnnotations(definedIn);
        }
        return super.getLocalConverter(definedIn, fieldName);
    }

    public void autodetectAnnotations(final boolean mode) {
        locked = !mode;
    }

    public void processAnnotations(final Class[] initialTypes) {
        if (initialTypes == null || initialTypes.length == 0) {
            return;
        }
        locked = true;
        synchronized (annotatedTypes) {
            final Set<Class<?>> types = new UnprocessedTypesSet();
            for (final Class initialType : initialTypes) {
                types.add(initialType);
            }
            processTypes(types);
        }
    }

    private void processAnnotations(final Class initialType) {
        if (initialType == null) {
            return;
        }
        synchronized (annotatedTypes) {
            final Set<Class<?>> types = new UnprocessedTypesSet();
            types.add(initialType);
            processTypes(types);
        }
    }

    private void processTypes(final Set<Class<?>> types) {
        while (!types.isEmpty()) {
            final Iterator<Class<?>> iter = types.iterator();
            final Class<?> type = iter.next();
            iter.remove();

            if (annotatedTypes.add(type)) {
                if (type.isPrimitive()) {
                    continue;
                }

                addParametrizedTypes(type, types);

                processConverterAnnotations(type);
                processAliasAnnotation(type, types);

                if (type.isInterface()) {
                    continue;
                }

                processImplicitCollectionAnnotation(type);

                final Field[] fields = type.getDeclaredFields();
                for (int i = 0; i < fields.length; i++ ) {
                    final Field field = fields[i];
                    if (field.isEnumConstant()
                        || (field.getModifiers() & (Modifier.STATIC | Modifier.TRANSIENT)) > 0) {
                        continue;
                    }

                    addParametrizedTypes(field.getGenericType(), types);

                    if (field.isSynthetic()) {
                        continue;
                    }

                    processFieldAliasAnnotation(field);
                    processAsAttributeAnnotation(field);
                    processImplicitAnnotation(field);
                    processOmitFieldAnnotation(field);
                    processLocalConverterAnnotation(field);
                }
            }
        }
    }

    private void addParametrizedTypes(Type type, final Set<Class<?>> types) {
        final Set<Type> processedTypes = new HashSet<Type>();
        final Set<Type> localTypes = new LinkedHashSet<Type>() {

            @Override
            public boolean add(final Type o) {
                if (o instanceof Class) {
                    return types.add((Class<?>)o);
                }
                return o == null || processedTypes.contains(o) ? false : super.add(o);
            }

        };
        while (type != null) {
            processedTypes.add(type);
            if (type instanceof Class) {
                final Class<?> clazz = (Class<?>)type;
                types.add(clazz);
                if (!clazz.isPrimitive()) {
                    final TypeVariable<?>[] typeParameters = clazz.getTypeParameters();
                    for (final TypeVariable<?> typeVariable : typeParameters) {
                        localTypes.add(typeVariable);
                    }
                    localTypes.add(clazz.getGenericSuperclass());
                    for (final Type iface : clazz.getGenericInterfaces()) {
                        localTypes.add(iface);
                    }
                }
            } else if (type instanceof TypeVariable) {
                final TypeVariable<?> typeVariable = (TypeVariable<?>)type;
                final Type[] bounds = typeVariable.getBounds();
                for (final Type bound : bounds) {
                    localTypes.add(bound);
                }
            } else if (type instanceof ParameterizedType) {
                final ParameterizedType parametrizedType = (ParameterizedType)type;
                localTypes.add(parametrizedType.getRawType());
                final Type[] actualArguments = parametrizedType.getActualTypeArguments();
                for (final Type actualArgument : actualArguments) {
                    localTypes.add(actualArgument);
                }
            } else if (type instanceof GenericArrayType) {
                final GenericArrayType arrayType = (GenericArrayType)type;
                localTypes.add(arrayType.getGenericComponentType());
            }

            if (!localTypes.isEmpty()) {
                final Iterator<Type> iter = localTypes.iterator();
                type = iter.next();
                iter.remove();
            } else {
                type = null;
            }
        }
    }

    private void processConverterAnnotations(final Class<?> type) {
        if (converterRegistry != null) {
            final XStreamConverters convertersAnnotation = type
                .getAnnotation(XStreamConverters.class);
            final XStreamConverter converterAnnotation = type
                .getAnnotation(XStreamConverter.class);
            final List<XStreamConverter> annotations = convertersAnnotation != null
                ? new ArrayList<XStreamConverter>(Arrays.asList(convertersAnnotation.value()))
                : new ArrayList<XStreamConverter>();
            if (converterAnnotation != null) {
                annotations.add(converterAnnotation);
            }
            for (final XStreamConverter annotation : annotations) {
                final Class<? extends ConverterMatcher> converterType = annotation.value();
                final Converter converter = cacheConverter(
                    converterType, converterAnnotation != null ? type : null);
                if (converter != null) {
                    if (converterAnnotation != null || converter.canConvert(type)) {
                        converterRegistry.registerConverter(converter, XStream.PRIORITY_NORMAL);
                    } else {
                        throw new InitializationException("Converter "
                            + converterType.getName()
                            + " cannot handle annotated class "
                            + type.getName());
                    }
                }
            }
        }
    }

    private void processAliasAnnotation(final Class<?> type, final Set<Class<?>> types) {
        final XStreamAlias aliasAnnotation = type.getAnnotation(XStreamAlias.class);
        if (aliasAnnotation != null) {
            if (classAliasingMapper == null) {
                throw new InitializationException("No "
                    + ClassAliasingMapper.class.getName()
                    + " available");
            }
            if (aliasAnnotation.impl() != Void.class) {
                // Alias for Interface/Class with an impl
                classAliasingMapper.addClassAlias(aliasAnnotation.value(), type);
                defaultImplementationsMapper.addDefaultImplementation(
                    aliasAnnotation.impl(), type);
                if (type.isInterface()) {
                    types.add(aliasAnnotation.impl()); // alias Interface's impl
                }
            } else {
                classAliasingMapper.addClassAlias(aliasAnnotation.value(), type);
            }
        }
    }

    @Deprecated
    private void processImplicitCollectionAnnotation(final Class<?> type) {
        final XStreamImplicitCollection implicitColAnnotation = type
            .getAnnotation(XStreamImplicitCollection.class);
        if (implicitColAnnotation != null) {
            if (implicitCollectionMapper == null) {
                throw new InitializationException("No "
                    + ImplicitCollectionMapper.class.getName()
                    + " available");
            }
            final String fieldName = implicitColAnnotation.value();
            final String itemFieldName = implicitColAnnotation.item();
            final Field field;
            try {
                field = type.getDeclaredField(fieldName);
            } catch (final NoSuchFieldException e) {
                throw new InitializationException(type.getName()
                    + " does not have a field named '"
                    + fieldName
                    + "' as required by "
                    + XStreamImplicitCollection.class.getName());
            }
            Class itemType = null;
            final Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                final Type typeArgument = ((ParameterizedType)genericType)
                    .getActualTypeArguments()[0];
                itemType = getClass(typeArgument);
            }
            if (itemType == null) {
                implicitCollectionMapper.add(type, fieldName, null, Object.class);
            } else {
                if (itemFieldName.equals("")) {
                    implicitCollectionMapper.add(type, fieldName, null, itemType);
                } else {
                    implicitCollectionMapper.add(type, fieldName, itemFieldName, itemType);
                }
            }
        }
    }

    private void processFieldAliasAnnotation(final Field field) {
        final XStreamAlias aliasAnnotation = field.getAnnotation(XStreamAlias.class);
        if (aliasAnnotation != null) {
            if (fieldAliasingMapper == null) {
                throw new InitializationException("No "
                    + FieldAliasingMapper.class.getName()
                    + " available");
            }
            fieldAliasingMapper.addFieldAlias(aliasAnnotation.value(), field
                .getDeclaringClass(), field.getName());
        }
    }

    private void processAsAttributeAnnotation(final Field field) {
        final XStreamAsAttribute asAttributeAnnotation = field
            .getAnnotation(XStreamAsAttribute.class);
        if (asAttributeAnnotation != null) {
            if (attributeMapper == null) {
                throw new InitializationException("No "
                    + AttributeMapper.class.getName()
                    + " available");
            }
            attributeMapper.addAttributeFor(field);
        }
    }

    private void processImplicitAnnotation(final Field field) {
        final XStreamImplicit implicitAnnotation = field.getAnnotation(XStreamImplicit.class);
        if (implicitAnnotation != null) {
            if (implicitCollectionMapper == null) {
                throw new InitializationException("No "
                    + ImplicitCollectionMapper.class.getName()
                    + " available");
            }
            final String fieldName = field.getName();
            final String itemFieldName = implicitAnnotation.itemFieldName();
            Class itemType = null;
            final Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                final Type typeArgument = ((ParameterizedType)genericType)
                    .getActualTypeArguments()[0];
                itemType = getClass(typeArgument);
            }
            if (itemFieldName != null && !"".equals(itemFieldName)) {
                implicitCollectionMapper.add(
                    field.getDeclaringClass(), fieldName, itemFieldName, itemType);
            } else {
                implicitCollectionMapper.add(field.getDeclaringClass(), fieldName, itemType);
            }
        }
    }

    private void processOmitFieldAnnotation(final Field field) {
        final XStreamOmitField omitFieldAnnotation = field
            .getAnnotation(XStreamOmitField.class);
        if (omitFieldAnnotation != null) {
            if (fieldAliasingMapper == null) {
                throw new InitializationException("No "
                    + FieldAliasingMapper.class.getName()
                    + " available");
            }
            fieldAliasingMapper.omitField(field.getDeclaringClass(), field.getName());
        }
    }

    private void processLocalConverterAnnotation(final Field field) {
        final XStreamConverter annotation = field.getAnnotation(XStreamConverter.class);
        if (annotation != null) {
            final Class<? extends ConverterMatcher> converterType = annotation.value();
            final Converter converter = cacheConverter(converterType, field.getType());
            if (converter != null) {
                if (localConversionMapper == null) {
                    throw new InitializationException("No "
                        + LocalConversionMapper.class.getName()
                        + " available");
                }
                localConversionMapper.registerLocalConverter(field.getDeclaringClass(), field
                    .getName(), converter);
            }
        }
    }

    private Converter cacheConverter(final Class<? extends ConverterMatcher> converterType,
        final Class targetType) {
        Converter result = null;
        final Object[] args;
        Map<Object, Converter> converterMapping = converterCache.get(converterType);
        if (converterMapping != null) {
            result = converterMapping.get(targetType.getName());
            if (result == null) {
                result = converterMapping.get(null);
            }
        }
        if (result == null) {
            if (targetType != null) {
                args = new Object[arguments.length + 1];
                System.arraycopy(arguments, 0, args, 1, arguments.length);
                args[0] = targetType;
            } else {
                args = arguments;
            }
            
            final BitSet usedArgs = new BitSet();
            final Converter converter;
            try {
                if (SingleValueConverter.class.isAssignableFrom(converterType)
                    && !Converter.class.isAssignableFrom(converterType)) {
                    final SingleValueConverter svc = (SingleValueConverter)DependencyInjectionFactory
                        .newInstance(converterType, args, usedArgs);
                    converter = new SingleValueConverterWrapper(svc);
                } else {
                    converter = (Converter)DependencyInjectionFactory.newInstance(
                        converterType, args, usedArgs);
                }
            } catch (final Exception e) {
                throw new InitializationException("Cannot instantiate converter "
                    + converterType.getName()
                    + (targetType != null ? " for type " + targetType.getName() : ""), e);
            }
            if (converterMapping == null) {
                converterMapping = new HashMap<Object, Converter>();
                converterCache.put(converterType, converterMapping);
            }
            if (targetType != null && usedArgs.get(0)) {
                converterMapping.put(targetType.getName(), converter);
            } else {
                converterMapping.put(null, converter);
            }
            result = converter;
        }
        return result;
    }

    private Class<?> getClass(final Type typeArgument) {
        Class<?> type = null;
        if (typeArgument instanceof ParameterizedType) {
            type = (Class<?>)((ParameterizedType)typeArgument).getRawType();
        } else if (typeArgument instanceof Class) {
            type = (Class<?>)typeArgument;
        }
        return type;
    }

    private final class UnprocessedTypesSet extends LinkedHashSet<Class<?>> {
        @Override
        public boolean add(Class<?> type) {
            if (type == null) {
                return false;
            }
            while (type.isArray()) {
                type = type.getComponentType();
            }
            final String name = type.getName();
            if (name.startsWith("java.") || name.startsWith("javax.")) {
                return false;
            }
            final boolean ret = annotatedTypes.contains(type) ? false : super.add(type);
            if (ret) {
                final XStreamInclude inc = type.getAnnotation(XStreamInclude.class);
                if (inc != null) {
                    final Class<?>[] incTypes = inc.value();
                    if (incTypes != null) {
                        for (final Class<?> incType : incTypes) {
                            add(incType);
                        }
                    }
                }
            }
            return ret;
        }
    }

    private static class WeakHashSet<K> implements Set<K> {

        private static Object NULL = new Object();
        private final WeakHashMap<K, Object> map = new WeakHashMap<K, Object>();

        public boolean add(final K o) {
            return map.put(o, NULL) == null;
        }

        public boolean addAll(final Collection<? extends K> c) {
            boolean ret = false;
            for (final K k : c) {
                ret = add(k) | false;
            }
            return ret;
        }

        public void clear() {
            map.clear();
        }

        public boolean contains(final Object o) {
            return map.containsKey(o);
        }

        public boolean containsAll(final Collection<?> c) {
            return map.keySet().containsAll(c);
        }

        public boolean isEmpty() {
            return map.isEmpty();
        }

        public Iterator<K> iterator() {
            return map.keySet().iterator();
        }

        public boolean remove(final Object o) {
            return map.remove(o) != null;
        }

        public boolean removeAll(final Collection<?> c) {
            boolean ret = false;
            for (final Object object : c) {
                ret = remove(object) | false;
            }
            return ret;
        }

        public boolean retainAll(final Collection<?> c) {
            boolean ret = false;
            for (final Iterator<K> iter = iterator(); iter.hasNext();) {
                final K element = iter.next();
                if (!c.contains(element)) {
                    iter.remove();
                    ret = true;
                }
            }
            return ret;
        }

        public int size() {
            return map.size();
        }

        public Object[] toArray() {
            return map.keySet().toArray();
        }

        public <T> T[] toArray(final T[] a) {
            return map.keySet().toArray(a);
        }
    }
}
