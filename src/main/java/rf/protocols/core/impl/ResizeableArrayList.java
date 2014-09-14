package rf.protocols.core.impl;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class ResizeableArrayList<E> extends ArrayList<E> {
    private Factory<E> factory = Factory.NULL_FACTORY;

    public ResizeableArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    public ResizeableArrayList() {
    }

    public ResizeableArrayList(Collection<? extends E> c) {
        super(c);
    }

    public ResizeableArrayList(int initialCapacity, Factory<E> factory) {
        super(initialCapacity);
        this.factory = factory;
    }

    public ResizeableArrayList(Factory<E> factory) {
        this.factory = factory;
    }

    public ResizeableArrayList(Collection<? extends E> c, Factory<E> factory) {
        super(c);
        this.factory = factory;
    }

    public void setFactory(Factory<E> factory) {
        this.factory = factory;
    }

    @Override
    public E get(int index) {
        while (index >= size())
            add(factory.create(size()));
        return super.get(index);
    }

    @Override
    public E set(int index, E element) {
        while (index >= size())
            add(factory.create(size()));
        return super.set(index, element);
    }

    public static interface Factory<E> {
        public static Factory NULL_FACTORY = new Factory() {
            @Override
            public Object create(int index) {
                return null;
            }
        };

        E create(int index);
    }
}
