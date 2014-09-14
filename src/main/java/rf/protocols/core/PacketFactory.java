package rf.protocols.core;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public interface PacketFactory<P extends Packet, M extends Message> {
    void setProperty(String property, String value);

    P createPacket(M message);

    PacketFactory<P, M> clone(String newName);
}
