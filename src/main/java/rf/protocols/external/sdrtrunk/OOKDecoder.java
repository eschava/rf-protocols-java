package rf.protocols.external.sdrtrunk;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OOKDecoder {
    private final float threshold;

    private long index = 0;
    private boolean active = false;
    private float prevSample;
    private boolean goodPeriod = false;
    private long periodStart = 1;
    private long delayedResult = 0;

    public OOKDecoder(float threshold) {
        this.threshold = threshold;
    }

    public long process(float sample) {
        long result = 0;
        index++;

        boolean edge = (prevSample < 0 && sample >= 0) || (prevSample > 0 && sample <= 0);

        if (active) {
            if (edge) {
                if (goodPeriod) {
                    periodStart = index;
                } else {
                    active = false;
                    result = -periodStart;
                }
                goodPeriod = false;
            } else if (!goodPeriod) {
                goodPeriod = Math.abs(sample) >= threshold;
            }
        }
        if (!active) {
            if (edge)
                periodStart = index;

            if (Math.abs(sample) >= threshold) {
                active = true;
                goodPeriod = true;
                if (result != 0)
                    delayedResult = result;
                result = periodStart;
            }
        }

        prevSample = sample;

        if (delayedResult != 0) {
            long t = result;
            result = delayedResult;
            delayedResult = t;
        }
        return result;
    }
}
