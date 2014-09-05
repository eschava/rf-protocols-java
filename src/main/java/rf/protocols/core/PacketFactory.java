package rf.protocols.core;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public interface PacketFactory<P extends Packet, M extends Message> {
    P createPacket(M message);
}
