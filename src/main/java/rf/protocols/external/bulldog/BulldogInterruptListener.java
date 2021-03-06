package rf.protocols.external.bulldog;

import org.bulldog.core.Edge;
import org.bulldog.core.pinfeatures.event.InterruptEventArgs;
import org.bulldog.core.pinfeatures.event.InterruptListener;
import rf.protocols.core.SignalLevelListener;

/**
 * Adapts {@link SignalLevelListener} to Bulldog {@link InterruptListener}
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public class BulldogInterruptListener implements InterruptListener {
    private SignalLevelListener signalLevelListener;

    public BulldogInterruptListener(SignalLevelListener signalLevelListener) {
        this.signalLevelListener = signalLevelListener;
    }

    @Override
    public void interruptRequest(InterruptEventArgs args) {
        signalLevelListener.onSignal(args.getEdge() == Edge.Rising);
    }
}
