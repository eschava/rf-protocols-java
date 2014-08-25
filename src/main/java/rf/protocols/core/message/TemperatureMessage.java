package rf.protocols.core.message;

import rf.protocols.core.Message;

/**
 * Message providing temperature
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public interface TemperatureMessage extends Message {
    double getTemperature();
}
