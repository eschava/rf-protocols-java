package rf.protocols.device.generic.intervalsequence;

import org.junit.Test;
import rf.protocols.device.generic.intervals.IntervalsPacket;

import static org.junit.Assert.assertEquals;

public class IntervalSequenceMessageFactoryTest {

    @Test
    public void testCreateMessage() throws Exception {
        IntervalSequenceProtocolProperties props = new IntervalSequenceProtocolProperties();
        props.setProperty("prefix", "00");
        props.setProperty("postfix", "11");
        props.setProperty("sequence[0].name", "A");
        props.setProperty("sequence[0].sequence", "01");
        props.setProperty("sequence[1].name", "a");
        props.setProperty("sequence[1].sequence", "010");
        props.setProperty("sequence[2].name", "B");
        props.setProperty("sequence[2].sequence", "10");

        IntervalSequenceMessageFactory factory = new IntervalSequenceMessageFactory("test", props);
        assertEquals(null, factory.createMessage(new IntervalsPacket(""))); // empty
        assertEquals(null, factory.createMessage(new IntervalsPacket("1011"))); // no prefix
        assertEquals(null, factory.createMessage(new IntervalsPacket("0010"))); // no postfix
        assertEquals(null, factory.createMessage(new IntervalsPacket("00111"))); // short message
        assertEquals("B", factory.createMessage(new IntervalsPacket("001011")).getValue()); // simple OK message
        assertEquals("A", factory.createMessage(new IntervalsPacket("000111")).getValue()); // simple OK message
        assertEquals("a", factory.createMessage(new IntervalsPacket("0001011")).getValue()); // simple OK message
        assertEquals("aBA", factory.createMessage(new IntervalsPacket("00010100111")).getValue()); // OK message
        assertEquals(null, factory.createMessage(new IntervalsPacket("000101001111"))); // incomplete message
    }
}