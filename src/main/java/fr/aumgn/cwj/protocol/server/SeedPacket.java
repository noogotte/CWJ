package fr.aumgn.cwj.protocol.server;

import io.netty.buffer.ByteBuf;
import fr.aumgn.cwj.protocol.Packet;
import fr.aumgn.cwj.protocol.PacketType;

public class SeedPacket implements Packet.ServerPacket {

    private final int seed;

    public SeedPacket(int seed) {
        this.seed = seed;
    }

    @Override
    public PacketType getType() {
        return PacketType.SEED_DATA;
    }

    @Override
    public void writeTo(ByteBuf buf) {
        buf.writeInt(seed);
    }
}
