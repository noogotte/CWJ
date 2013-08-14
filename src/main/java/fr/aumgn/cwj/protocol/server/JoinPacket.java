package fr.aumgn.cwj.protocol.server;

import static fr.aumgn.cwj.protocol.PacketDataLength.ENTITY_DATA;
import io.netty.buffer.ByteBuf;
import fr.aumgn.cwj.protocol.Client;
import fr.aumgn.cwj.protocol.Packet.ServerPacket;
import fr.aumgn.cwj.protocol.PacketType;

public class JoinPacket implements ServerPacket {

    private final long entityId;

    public JoinPacket(Client client) {
        this.entityId = client.getEntityId();
    }

    @Override
    public PacketType getType() {
        return PacketType.JOIN;
    }

    public long getEntityId() {
        return entityId;
    }

    @Override
    public void writeTo(ByteBuf buf) {
        buf.writeInt(0);
        buf.writeLong(entityId);
        buf.writeZero(ENTITY_DATA);
    }
}
