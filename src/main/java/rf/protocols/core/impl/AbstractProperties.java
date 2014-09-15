package rf.protocols.core.impl;

import rf.protocols.core.Properties;

/**
 * Helper implementation of {@link rf.protocols.core.Properties}
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public class AbstractProperties implements Properties, Cloneable {

    @Override
    public Properties clone() {
        try {
            return (Properties) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
