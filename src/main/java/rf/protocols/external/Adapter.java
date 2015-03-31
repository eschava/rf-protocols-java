package rf.protocols.external;

import rf.protocols.core.SignalLengthListener;
import rf.protocols.core.SignalLengthSender;
import rf.protocols.core.SignalLevelListener;

/**
 * Adapter to external pin library
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public interface Adapter {
    String getName();
    void setProperty(String name, String value);
    void addListener(SignalLengthListener listener);
    void addListener(SignalLevelListener listener);
    SignalLengthSender getSignalSender();
}
