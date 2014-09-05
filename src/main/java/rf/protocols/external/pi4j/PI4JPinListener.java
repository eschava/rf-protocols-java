package rf.protocols.external.pi4j;

import com.pi4j.io.gpio.event.*;
import rf.protocols.core.SignalLevelListener;

/**
 * Adapts {@link SignalLevelListener} to PI4J {@link PinListener} and {@link GpioPinListenerDigital} listeners
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PI4JPinListener implements PinListener, GpioPinListenerDigital {
    private SignalLevelListener signalLevelListener;

    public PI4JPinListener(SignalLevelListener signalLevelListener) {
        this.signalLevelListener = signalLevelListener;
    }

    @Override
    public void handlePinEvent(PinEvent pinEvent) {
        signalLevelListener.onSignal(((PinDigitalStateChangeEvent)pinEvent).getState().isHigh());
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        signalLevelListener.onSignal(event.getState().isHigh());
    }
}
