/*
 * Copyright (C) 2003 Joe Walnes.
 * Copyright (C) 2006, 2007, 2009 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 26. September 2003 by Joe Walnes
 */
package xcom.thoughtworks.xstream.mapper;

import xcom.thoughtworks.xstream.XStreamException;

/**
 * Exception thrown if a mapper cannot locate the appropriate class for an element.
 * 
 * @author Joe Walnes
 * @author J&ouml;rg Schaible
 * @since 1.2
 */
public class CannotResolveClassException extends XStreamException {
    public CannotResolveClassException(String className) {
        super(className);
    }
}
