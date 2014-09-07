package rf.protocols.external.pi4j;

import com.pi4j.io.gpio.*;
import rf.protocols.core.SignalLengthListener;
import rf.protocols.core.SignalLengthSender;
import rf.protocols.core.SignalLevelListener;
import rf.protocols.core.impl.SignalLengthAdapterLevelListener;
import rf.protocols.external.Adapter;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PI4JAdapter implements Adapter {
    private PI4JAdapterProperties properties = new PI4JAdapterProperties();

    @Override
    public String getName() {
        return "pi4j";
    }

    @Override
    public void setProperty(String name, String value) {
        properties.setProperty(name, value);
    }

    @Override
    public void addListener(String pinName, SignalLengthListener listener) {
        addListener(pinName, new SignalLengthAdapterLevelListener(listener));
    }

    @Override
    public void addListener(String pinName, final SignalLevelListener listener) {
        GpioController controller = GpioFactory.getInstance();
        Pin pin = getPinByName(pinName);
        GpioPinDigitalInput input = controller.provisionDigitalInputPin(pin);
        input.addListener(new PI4JPinListener(listener));
    }

    private Pin getPinByName(String pinName) {
        try {
            return (Pin) RaspiPin.class.getField(pinName).get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SignalLengthSender getSignalSender(String pinName) {
        GpioController controller = GpioFactory.getInstance();
        Pin pin = getPinByName(pinName);
        GpioPinDigitalOutput output = controller.provisionDigitalOutputPin(pin);
        return new PI4JSignalSender(output);
    }
}
