package rf.protocols.core.impl;

import rf.protocols.core.SignalListenerFactory;
import rf.protocols.core.SignalListenerProperties;

/**
 * Helper implementation of {@link rf.protocols.core.SignalListenerFactory}
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public abstract class AbstractSignalListenerFactory<Factory extends SignalListenerFactory, Props extends SignalListenerProperties>
        implements SignalListenerFactory<Factory>, Cloneable {

    private String name;
    private Props properties;

    public AbstractSignalListenerFactory(String name, Props properties) {
        this.name = name;
        this.properties = properties;
    }

    @Override
    public String getName() {
        return name;
    }

    public Props getProperties() {
        return properties;
    }

    @Override
    public void setProperty(String property, String value) {
        properties.setProperty(property, value);
    }

    @Override
    public Factory clone(String newName) {
        try {
            AbstractSignalListenerFactory clone = (AbstractSignalListenerFactory) super.clone();
            clone.name = newName;
            clone.properties = properties.clone();
            return (Factory) clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
