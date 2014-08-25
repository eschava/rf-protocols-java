package rf.protocols.core;

/**
 * Listener of transport-level messages
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public interface PacketListener<P extends Packet> {
    void onPacket(P packet);
}
