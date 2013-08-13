package fr.aumgn.cwj.protocol.driver;

import fr.aumgn.cwj.protocol.Packet;
import fr.aumgn.cwj.protocol.PacketType;
import fr.aumgn.cwj.protocol.exception.UnknownPacketException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.ByteOrder;
import java.util.List;

public class DriverDecoder extends ReplayingDecoder<Object> {

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf buf, List<Object> packets) throws Exception {
        buf = buf.order(ByteOrder.LITTLE_ENDIAN);

        int id = buf.readInt();
        PacketType type = PacketType.valueForId(id);
        if (type == PacketType.UNKNOWN) {
            throw new UnknownPacketException(id);
        }

        Packet.ClientPacket packet = type.read(buf);
        packets.add(packet);
    }
}
