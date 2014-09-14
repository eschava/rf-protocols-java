package rf.protocols.device.remoteswitch;

import rf.protocols.core.PacketFactory;
import rf.protocols.core.message.StringMessage;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class RemoteSwitchPacketFactory implements PacketFactory<RemoteSwitchPacket, StringMessage> {
    @Override
    public RemoteSwitchPacket createPacket(StringMessage message) {
        return new RemoteSwitchPacket(message.getValue());
    }

    @Override
    public void setProperty(String property, String value) {
    }

    @Override
    public PacketFactory<RemoteSwitchPacket, StringMessage> clone(String newName) {
        return this; // no state
    }
}
