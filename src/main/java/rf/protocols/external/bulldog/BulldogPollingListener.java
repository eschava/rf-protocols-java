package rf.protocols.external.bulldog;

import org.bulldog.core.Signal;
import org.bulldog.core.pinfeatures.DigitalInput;
import org.bulldog.linux.jni.NativeTools;
import rf.protocols.core.SignalLevelListener;

/**
* @author Eugene Schava <eschava@gmail.com>
*/
public class BulldogPollingListener implements Runnable {
    private final DigitalInput input;
    private final SignalLevelListener listener;
    private int pollingDelay;

    public BulldogPollingListener(DigitalInput input, SignalLevelListener listener, int pollingDelay) {
        this.input = input;
        this.listener = listener;
        this.pollingDelay = pollingDelay;
    }

    @Override
    public void run() {
        Signal oldValue = null;
        while (true) {
            long nextRead = System.nanoTime() + pollingDelay*1000;
            Signal value = input.read();
            if (value != oldValue) {
                listener.onSignal(value.getBooleanValue());
                oldValue = value;
            }
            long sleep = nextRead - System.nanoTime();
            if (sleep > 1000)
                NativeTools.sleepMicros((int) (sleep / 1000));
        }
    }
}
