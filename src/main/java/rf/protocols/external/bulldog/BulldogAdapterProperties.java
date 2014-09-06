package rf.protocols.external.bulldog;

import rf.protocols.core.impl.AbstractProperties;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class BulldogAdapterProperties extends AbstractProperties {
    public boolean useInterrupts = true;
    public int pollingDelay = 20;

    // TODO: extra pins should be moved to Bulldog configuration
    public boolean cubieboard = false;
    public Pin pinToAdd1 = new Pin();
    public Pin pinToAdd2 = new Pin();

    public static class Pin {

        public String name;
        public int address;
        public String bank;
        public int index;
    }
}
