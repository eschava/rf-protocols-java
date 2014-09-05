package rf.protocols.debug;

import org.bulldog.core.gpio.DigitalOutput;
import org.bulldog.core.platform.Board;
import org.bulldog.core.platform.Platform;
import rf.protocols.bulldog.BulldogSignalSender;
import rf.protocols.core.SignalLengthSender;
import rf.protocols.registry.StringMessageSenderRegistry;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class SendStringMessage {

    public static void main(String[] args) {
        String sender = System.getProperty("protocol");
        String message = System.getProperty("message");

        Board board = Platform.createBoard();
        // TODO: pins should be moved to configuration
        DigitalOutput output = board.getPin("PG2").as(DigitalOutput.class);
        SignalLengthSender signalSender = new BulldogSignalSender(output);

        StringMessageSenderRegistry.getInstance().sendMessage(sender, message, signalSender);
    }
}
