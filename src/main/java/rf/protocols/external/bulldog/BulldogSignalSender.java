package rf.protocols.external.bulldog;

import org.bulldog.core.Signal;
import org.bulldog.core.gpio.DigitalOutput;
import org.bulldog.core.util.BulldogUtil;
import rf.protocols.core.SignalLengthSender;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class BulldogSignalSender implements SignalLengthSender {
    private DigitalOutput output;

    public BulldogSignalSender(DigitalOutput output) {
        this.output = output;
    }

    @Override
    public void send(boolean high, long lengthInMicros) {
        long endTime = lengthInMicros * 1000 + System.nanoTime();

        output.write(Signal.fromBooleanValue(high));

        try {
            long leftNanos = endTime - System.nanoTime();
            if (leftNanos >= 1000000) {
                Thread.sleep(leftNanos / 1000000);
                leftNanos = endTime - System.nanoTime();
            }

            if (leftNanos > 0)
                BulldogUtil.sleepNs((int) leftNanos);
//                Thread.sleep(0, (int) leftNanos); // works incorrect
        } catch (InterruptedException ignored) {
        }
    }
}
