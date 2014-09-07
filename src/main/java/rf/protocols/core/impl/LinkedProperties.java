package rf.protocols.core.impl;

import java.util.*;

/**
 * Same as JDK {@link java.util.Properties} but saves original order of properties
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public class LinkedProperties extends Properties {
    private Map<Object, Object> linkedMap = new LinkedHashMap<Object, Object>();

    @Override
    public synchronized Object put(Object key, Object value) {
        linkedMap.put(key, value);
        return super.put(key, value);
    }

    @Override
    public Enumeration<?> propertyNames() {
        return new IteratorEnumeration(values().iterator());
    }

    @Override
    public Set<String> stringPropertyNames() {
        Set<String> propertyNames = new LinkedHashSet<String>();
        for (Map.Entry<Object,Object> entry : linkedMap.entrySet()) {
            if (entry.getKey() instanceof String && entry.getValue() instanceof String)
                propertyNames.add((String) entry.getKey());
        }
        return propertyNames;
    }

    @Override
    public synchronized Enumeration<Object> keys() {
        return new IteratorEnumeration(keySet().iterator());
    }

    @Override
    public Collection<Object> values() {
        return linkedMap.values();
    }

    @Override
    public Set<Map.Entry<Object, Object>> entrySet() {
        return linkedMap.entrySet();
    }

    private class IteratorEnumeration implements Enumeration<Object> {
        private final Iterator<Object> iterator;

        public IteratorEnumeration(Iterator<Object> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasMoreElements() {
            return iterator.hasNext();
        }

        @Override
        public Object nextElement() {
            return iterator.next();
        }
    }
}
