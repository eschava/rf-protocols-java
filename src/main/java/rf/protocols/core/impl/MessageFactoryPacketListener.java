package rf.protocols.core.impl;

import rf.protocols.core.*;

/**
 * Packet listener notifing message listener by message (valid only) created using message factory
 *
 * @author Eugene Schava <eschava@gmail.com>
 */
public class MessageFactoryPacketListener<P extends Packet, M extends Message> implements PacketListener<P> {
    private MessageFactory<P, M> messageFactory;
    private MessageListener<M> messageListener;

    public MessageFactoryPacketListener(MessageFactory<P, M> messageFactory, MessageListener<M> messageListener) {
        this.messageFactory = messageFactory;
        this.messageListener = messageListener;
    }

    @Override
    public void onPacket(P packet) {
        M message = messageFactory.createMessage(packet);
        if (message != null) {
            if (message.isValid())
                messageListener.onMessage(message);
            else
                System.out.println("WARN: Cannot create valid message from packet " + packet); // TODO: use some logger
        }
    }
}
