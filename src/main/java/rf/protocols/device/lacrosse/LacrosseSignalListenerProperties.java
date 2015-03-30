package rf.protocols.device.lacrosse;

import rf.protocols.core.Interval;
import rf.protocols.core.Properties;
import rf.protocols.core.impl.AbstractProperties;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class LacrosseSignalListenerProperties extends AbstractProperties {
    // TODO: move to configuration
    public Interval pauseLength = new Interval(900, 1200);
    public Interval zeroLength = new Interval(1200, 1500);
    public Interval oneLength = new Interval(300, 600);
    public int packetSize = 44;

    @Override
    public Properties clone() {
        LacrosseSignalListenerProperties clone = (LacrosseSignalListenerProperties) super.clone();
        clone.pauseLength = pauseLength.clone();
        clone.zeroLength = zeroLength.clone();
        clone.oneLength = oneLength.clone();
        return clone;
    }
}
