package rf.protocols.registry.interfaces;

import rf.protocols.core.Properties;
import rf.protocols.external.ognl.PropertiesConfigurer;

/**
 * Helper implementation of {@link SignalListenerFactory}
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public abstract class AbstractSignalListenerFactory<Factory extends SignalListenerFactory, Props extends Properties>
        implements SignalListenerFactory<Factory>, Cloneable {

    private String protocol;
    private Props properties;
    private PropertiesConfigurer propertiesConfigurer;

    public AbstractSignalListenerFactory(String protocol, Props properties) {
        this.protocol = protocol;
        this.properties = properties;
        propertiesConfigurer = new PropertiesConfigurer(properties);
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    public Props getProperties() {
        return properties;
    }

    @Override
    public void setProperty(String property, String value) {
        propertiesConfigurer.setProperty(property, value);
    }

    @Override
    public Factory clone(String newProtocolName) {
        try {
            AbstractSignalListenerFactory clone = (AbstractSignalListenerFactory) super.clone();
            clone.protocol = newProtocolName;
            clone.properties = properties.clone();
            clone.propertiesConfigurer = new PropertiesConfigurer(clone.properties);
            return (Factory) clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
