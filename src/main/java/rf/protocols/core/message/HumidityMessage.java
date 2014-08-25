package rf.protocols.core.message;

import rf.protocols.core.Message;

/**
 * Message providing humidity
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public interface HumidityMessage extends Message {
    double getHumidity();
}
