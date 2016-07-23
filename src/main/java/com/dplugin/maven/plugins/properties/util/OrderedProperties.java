package com.dplugin.maven.plugins.properties.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 有序properties 2016-07-23 22:48:18
 * @author nayuan
 */
public class OrderedProperties extends Properties {

    private LinkedHashSet<Object> keys = new LinkedHashSet();

    public Enumeration<Object> keys() {
        return Collections.enumeration(keys);
    }

    public Set<Object> keySet() {
        return keys;
    }

    public Object put(Object key, Object value) {
        if(!keys.contains(key)) {
            keys.add(key);
        }
        return super.put(key, value);
    }

    public synchronized void putAll(Map<?, ?> t) {
        for(Object key : t.keySet()) {
            if(!keys.contains(key)) {
                keys.add(key);
            }
        }
        super.putAll(t);
    }
}
