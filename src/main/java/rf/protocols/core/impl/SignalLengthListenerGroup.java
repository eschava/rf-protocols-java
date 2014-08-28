package rf.protocols.core.impl;

import rf.protocols.core.SignalLengthListener;

import java.util.Collection;

/**
 * Group of {@link rf.protocols.core.SignalLengthListener}
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public class SignalLengthListenerGroup implements SignalLengthListener {
    private Collection<SignalLengthListener> listeners;

    public SignalLengthListenerGroup(Collection<SignalLengthListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void onSignal(boolean high, long lengthInMicros) {
        for (SignalLengthListener listener : listeners)
            listener.onSignal(high, lengthInMicros);
    }
}
