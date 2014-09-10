package rf.protocols.core;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public interface PacketSender<P extends Packet> {
    void setProperty(String property, String value);

    void send(P packet, SignalLengthSender signalSender);

    PacketSender<P> clone(String newName);
}
