package rf.protocols.device.remoteswitch;

import rf.protocols.core.PacketSender;
import rf.protocols.core.SignalLengthSender;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class RemoteSwitchPacketSender implements PacketSender<RemoteSwitchPacket>, Cloneable {
    private String name = "RemoteSwitch";
    private RemoteSwitchSignalListenerProperties properties = new RemoteSwitchSignalListenerProperties();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RemoteSwitchSignalListenerProperties getProperties() {
        return properties;
    }

    public void setProperties(RemoteSwitchSignalListenerProperties properties) {
        this.properties = properties;
    }

    @Override
    public void setProperty(String property, String value) {
        properties.setProperty(property, value);
    }

    public void send(RemoteSwitchPacket packet, SignalLengthSender signalSender) {
        String trits = packet.toString();

        sendSeparator(signalSender);

        for (int i = 0; i < properties.repeats; i++) {
            for (byte trit : trits.getBytes())
            {
                switch (trit) {
                    case '0':
                        send0Trit(signalSender);
                        break;
                    case '1':
                        send1Trit(signalSender);
                        break;
                    case '2':
                        send2Trit(signalSender);
                        break;
                }
            }

            sendSeparator(signalSender);
        }
    }

    private void send0Trit(SignalLengthSender signalSender) {
        signalSender.send(true,  (long) (properties.separatorLength * properties.shortSignalFraction));
        signalSender.send(false, (long) (properties.separatorLength * properties.longSignalFraction));
        signalSender.send(true,  (long) (properties.separatorLength * properties.shortSignalFraction));
        signalSender.send(false, (long) (properties.separatorLength * properties.longSignalFraction));
    }

    private void send1Trit(SignalLengthSender signalSender) {
        signalSender.send(true,  (long) (properties.separatorLength * properties.longSignalFraction));
        signalSender.send(false, (long) (properties.separatorLength * properties.shortSignalFraction));
        signalSender.send(true,  (long) (properties.separatorLength * properties.longSignalFraction));
        signalSender.send(false, (long) (properties.separatorLength * properties.shortSignalFraction));
    }

    private void send2Trit(SignalLengthSender signalSender) {
        signalSender.send(true,  (long) (properties.separatorLength * properties.shortSignalFraction));
        signalSender.send(false, (long) (properties.separatorLength * properties.longSignalFraction));
        signalSender.send(true,  (long) (properties.separatorLength * properties.longSignalFraction));
        signalSender.send(false, (long) (properties.separatorLength * properties.shortSignalFraction));
    }

    private void sendSeparator(SignalLengthSender signalSender) {
        signalSender.send(true, (long) (properties.separatorLength * properties.shortSignalFraction));
        signalSender.send(false, properties.separatorLength);
    }

    @Override
    public RemoteSwitchPacketSender clone(String newName) {
        try {
            RemoteSwitchPacketSender clone = (RemoteSwitchPacketSender) super.clone();
            clone.properties = (RemoteSwitchSignalListenerProperties) properties.clone();
            clone.setName(newName);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
