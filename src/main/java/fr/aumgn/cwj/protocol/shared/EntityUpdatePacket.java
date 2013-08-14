package fr.aumgn.cwj.protocol.shared;

import io.netty.buffer.ByteBuf;
import fr.aumgn.cwj.protocol.Client;
import fr.aumgn.cwj.protocol.Packet.SharedPacket;
import fr.aumgn.cwj.protocol.PacketType;
import fr.aumgn.cwj.protocol.ProtocolHandler;

public class EntityUpdatePacket implements SharedPacket {

    private final byte[] bytes;

    public EntityUpdatePacket(ByteBuf buf) {
        int length = buf.readInt();
        this.bytes = new byte[length];
        buf.readBytes(bytes);
    }

    @Override
    public PacketType getType() {
        return PacketType.ENTITY_UPDATE;
    }

    @Override
    public void dispatchTo(ProtocolHandler dispatcher, Client client) {
        dispatcher.received(client, this);
    }

    @Override
    public void writeTo(ByteBuf buf) {
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }
}
