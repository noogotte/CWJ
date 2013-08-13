package fr.aumgn.cwj.protocol.server;

import io.netty.buffer.ByteBuf;
import fr.aumgn.cwj.protocol.Packet.ServerPacket;
import fr.aumgn.cwj.protocol.PacketType;

public class ServerFullPacket implements ServerPacket {

    @Override
    public PacketType getType() {
        return PacketType.SERVER_FULL;
    }

    @Override
    public void writeTo(ByteBuf buf) {
    }
}
