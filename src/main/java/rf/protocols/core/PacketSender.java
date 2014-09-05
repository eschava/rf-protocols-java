package rf.protocols.core;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public interface PacketSender<P extends Packet> {
    void send(P packet, SignalLengthSender signalSender);
}
