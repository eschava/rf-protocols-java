package rf.protocols.core.impl;

import rf.protocols.core.SignalLengthListener;
import rf.protocols.core.SignalLevelListener;

/**
 * Adapts {@link rf.protocols.core.SignalLengthListener} to {@link rf.protocols.core.SignalLevelListener}
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public class SignalLengthAdapterLevelListener implements SignalLevelListener {
    private long lastSignalTime_ = 0;

    private final SignalLengthListener listener;

    public SignalLengthAdapterLevelListener(SignalLengthListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSignal(boolean high) {
        long currentTime = System.nanoTime();
        if (lastSignalTime_ != 0) {
            long lengthInMicros = (currentTime - lastSignalTime_) / 1000;
            listener.onSignal(!high, lengthInMicros);
        }
        lastSignalTime_ = currentTime;
    }
}
