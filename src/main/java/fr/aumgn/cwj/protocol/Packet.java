package fr.aumgn.cwj.protocol;

import io.netty.buffer.ByteBuf;

public interface Packet {

    PacketType getType();

    interface ServerPacket extends Packet {

        void writeTo(ByteBuf buf);
    }

    interface ClientPacket extends Packet {

        void dispatchTo(ProtocolHandler dispatcher, Client client);
    }

    interface SharedPacket extends ServerPacket, ClientPacket {
    }
}
