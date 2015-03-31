package rf.protocols.device.ambient.ft005th;

import rf.protocols.core.Interval;
import rf.protocols.core.Properties;
import rf.protocols.core.impl.AbstractProperties;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class AmbientFT005THSignalListenerProperties extends AbstractProperties {
    // TODO: move to configuration
    public Interval zeroLength = new Interval(330, 650);
    public Interval oneLength = new Interval(900, 1100);
    public Interval pauseLength = new Interval(330, 650);
    public int packetSize = 48;

    @Override
    public Properties clone() {
        AmbientFT005THSignalListenerProperties clone = (AmbientFT005THSignalListenerProperties) super.clone();
        clone.zeroLength = zeroLength.clone();
        clone.oneLength = oneLength.clone();
        clone.pauseLength = pauseLength.clone();
        return clone;
    }
}
