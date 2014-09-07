package rf.protocols.external.pi4j;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import rf.protocols.core.SignalLengthSender;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class PI4JSignalSender implements SignalLengthSender {
    private GpioPinDigitalOutput output;

    public PI4JSignalSender(GpioPinDigitalOutput output) {
        this.output = output;
    }

    @Override
    public void send(boolean high, long lengthInMicros) {
        long endTime = lengthInMicros * 1000 + System.nanoTime();

        output.setState(high);

        try {
            // wait millis left
            long leftNanos = endTime - System.nanoTime();
            if (leftNanos >= 1000000) {
                Thread.sleep(leftNanos / 1000000);
            }

            // wait nanos left
            long now;
            do {
                now = System.nanoTime();
            } while(now < endTime);
        } catch (InterruptedException ignored) {
        }
    }
}
