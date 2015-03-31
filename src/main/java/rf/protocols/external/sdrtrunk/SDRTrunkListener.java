package rf.protocols.external.sdrtrunk;

import rf.protocols.core.SignalLengthListener;
import sample.Listener;
import sample.complex.ComplexBuffer;
import source.tuner.FrequencyChangeEvent;
import source.tuner.FrequencyChangeListener;
import source.tuner.Tuner;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class SDRTrunkListener implements Listener<ComplexBuffer>, FrequencyChangeListener {
    private final OOKDecoder ookDecoder;
    private final SignalLengthListener listener;

    private double sampleLength;
    private boolean lastLevel;
    private long lastChangeIndex;

    public SDRTrunkListener(Tuner tuner, SignalLengthListener listener, float threshold) {
        ookDecoder = new OOKDecoder(threshold);
        this.listener = listener;

        setSampleRate(tuner.getSampleRate());
        lastLevel = false;
    }

    @Override
    public void receive(ComplexBuffer complexBuffer) {
        boolean skip = true;

        for (float sample : complexBuffer.getSamples()) {
            if (skip = !skip) continue;

            long ind = ookDecoder.process(sample);

            boolean level = lastLevel;
            if (ind > 0) {
                level = true;
            } else if (ind < 0) {
                level = false;
                ind = -ind;
            }

            if (level != lastLevel) {
                lastLevel = level;

                long lengthInMicros = (long) (sampleLength * (ind - lastChangeIndex));
                lastChangeIndex = ind;

                listener.onSignal(level, lengthInMicros);
            }
        }
    }

    @Override
    public void frequencyChanged(FrequencyChangeEvent event) {
        if (event.getAttribute() == FrequencyChangeEvent.Attribute.SAMPLE_RATE)
            setSampleRate((int) event.getValue());
    }

    private void setSampleRate(int sampleRate) {
        sampleLength = 1000000d / sampleRate;  // in microseconds
    }
}
