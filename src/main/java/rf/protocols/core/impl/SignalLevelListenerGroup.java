package rf.protocols.core.impl;

import rf.protocols.core.SignalLevelListener;

import java.util.Collection;

/**
 * Group of {@link rf.protocols.core.SignalLevelListener}
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public class SignalLevelListenerGroup implements SignalLevelListener {
    private Collection<SignalLevelListener> listeners;

    public SignalLevelListenerGroup(Collection<SignalLevelListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void onSignal(boolean high) {
        for (SignalLevelListener listener : listeners)
            listener.onSignal(high);
    }
}
