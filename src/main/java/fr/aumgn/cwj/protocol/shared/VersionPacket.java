package fr.aumgn.cwj.protocol.shared;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import fr.aumgn.cwj.protocol.Client;
import fr.aumgn.cwj.protocol.Packet.SharedPacket;
import fr.aumgn.cwj.protocol.PacketType;
import fr.aumgn.cwj.protocol.ProtocolHandler;

public class VersionPacket implements SharedPacket {

    private final int version;

    public VersionPacket(ByteBuf buf) {
        this.version = buf.readInt();
    }

    public VersionPacket(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public PacketType getType() {
        return PacketType.VERSION;
    }

    public Client initConnection(ProtocolHandler dispatcher, ChannelHandlerContext context) {
        return dispatcher.initConnection(context, this);
    }

    /**
     * Never called, replaced by
     * {@link #initConnection(ProtocolHandler, ChannelHandlerContext)}
     */
    @Override
    public void dispatchTo(ProtocolHandler dispatcher, Client client) {
    }

    @Override
    public void writeTo(ByteBuf buf) {
        buf.writeInt(version);
    }
}
