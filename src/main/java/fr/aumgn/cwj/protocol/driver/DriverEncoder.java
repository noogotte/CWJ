package fr.aumgn.cwj.protocol.driver;

import fr.aumgn.cwj.protocol.Packet.ServerPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.ByteOrder;

public class DriverEncoder extends MessageToByteEncoder<ServerPacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ServerPacket packet, ByteBuf out) throws Exception {
        out = out.order(ByteOrder.LITTLE_ENDIAN);
        out.writeInt(packet.getType().getId());
        packet.writeTo(out);
    }
}
