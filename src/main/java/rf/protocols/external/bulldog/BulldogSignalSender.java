package rf.protocols.external.bulldog;

import org.bulldog.core.Signal;
import org.bulldog.core.pinfeatures.DigitalOutput;
import org.bulldog.linux.jni.NativeTools;
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

        int microsLeft = (int) ((endTime - System.nanoTime()) / 1000);
        if (microsLeft > 0)
            NativeTools.sleepMicros(microsLeft);
    }
}
