package rf.protocols.core;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class Interval implements Cloneable {
    private long min, max;

    public Interval(long med) {
        min = max = med;
    }

    public Interval(long med, double tolerance) {
        this(med);
        setTolerance(tolerance);
    }

    public Interval(long min, long max) {
        this.min = min;
        this.max = max;
    }

    public long getMin() {
        return min;
    }

    public long getMed() {
        return min + (max - min) / 2;
    }

    public long getMax() {
        return max;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public void setMed(long med) {
        min = max = med;
    }

    public void setDelta(long delta) {
        long med = getMed();
        min = med - delta;
        max = med + delta;
    }

    public void setTolerance(double tolerance) {
        long med = getMed();
        min = (long) (med * (1 - tolerance));
        max = (long) (med * (1 + tolerance));
    }

    public boolean isInside(long v) {
        return v >= min && v <= max;
    }

    @Override
    public String toString() {
        return "Interval[" + min + "," + max + ']';
    }

    @Override
    public Interval clone() {
        try {
            return (Interval) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
