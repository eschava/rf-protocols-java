package rf.protocols.external.bulldog;

import org.bulldog.core.pinfeatures.DigitalInput;
import org.bulldog.core.pinfeatures.DigitalOutput;
import org.bulldog.core.pinfeatures.Pin;
import org.bulldog.core.platform.Board;
import org.bulldog.core.platform.Platform;
import org.bulldog.cubieboard.Cubieboard;
import rf.protocols.core.SignalLengthListener;
import rf.protocols.core.SignalLengthSender;
import rf.protocols.core.SignalLevelListener;
import rf.protocols.external.ognl.PropertiesConfigurer;
import rf.protocols.core.impl.SignalLengthAdapterLevelListener;
import rf.protocols.external.Adapter;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class BulldogAdapter implements Adapter {
    private final Board board;
    private final BulldogAdapterProperties properties = new BulldogAdapterProperties();
    private final PropertiesConfigurer propertiesConfigurer = new PropertiesConfigurer(properties);

    public BulldogAdapter() {
        board = Platform.createBoard();
    }

    @Override
    public String getName() {
        return "bulldog";
    }

    @Override
    public void setProperty(String name, String value) {
        propertiesConfigurer.setProperty(name, value);
    }

    @Override
    public void addListener(SignalLengthListener listener) {
        addListener(new SignalLengthAdapterLevelListener(listener));
    }

    @Override
    public void addListener(final SignalLevelListener listener) {
        Pin pin = getPin(properties.inputPin);
        final DigitalInput input = pin.as(DigitalInput.class);

        if (properties.useInterrupts) {
            input.enableInterrupts();
            input.addInterruptListener(new BulldogInterruptListener(listener));
        } else {
            Thread readingThread = new Thread(new BulldogPollingListener(input, listener, properties.pollingDelay));
            readingThread.setDaemon(true);
            readingThread.start();
        }
    }

    @Override
    public SignalLengthSender getSignalSender() {
        DigitalOutput output = getPin(properties.outputPin).as(DigitalOutput.class);
        return new BulldogSignalSender(output);
    }

    private Pin getPin(String pinName) {
        Pin pin = board.getPin(pinName);
        // TODO: extra pins should be moved to Bulldog configuration
        if (pin == null && properties.cubieboard) {
            if (pinName.equals(properties.pinToAdd1.name))
                pin = addCubieboardPin(properties.pinToAdd1);
            else if (pinName.equals(properties.pinToAdd2.name))
                pin = addCubieboardPin(properties.pinToAdd2);
        }
        return pin;
    }

    private Pin addCubieboardPin(BulldogAdapterProperties.Pin pinConfig) {
        Pin pin = ((Cubieboard) board).createDigitalIOPin(
                pinConfig.name,
                pinConfig.address,
                pinConfig.bank,
                pinConfig.index,
                pinConfig.address + "_" + pinConfig.name.toLowerCase(),
                properties.useInterrupts);
        board.getPins().add(pin);
        return pin;
    }
}
