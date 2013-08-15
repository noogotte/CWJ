package fr.aumgn.cwj;

import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

import fr.aumgn.cwj.protocol.Client;
import fr.aumgn.cwj.protocol.Packet;
import fr.aumgn.cwj.protocol.Packet.ServerPacket;

public class Player implements Client {

    private final ChannelHandlerContext context;
    private final long                  entityId;

    public Player(ChannelHandlerContext context, long entityId) {
        this.context = context;
        this.entityId = entityId;
    }

    @Override
    public long getEntityId() {
        return entityId;
    }

    @Override
    public void sendPacket(ServerPacket... packets) {
        for (Packet.ServerPacket packet : packets) {
            CWJ.getLogger().finer("Sending packet " + packet.getType());
            context.write(packet);
            context.flush();
        }
    }

    @Override
    public InetSocketAddress getAdress() {
        if (context.channel().remoteAddress() instanceof InetSocketAddress) {
            return (InetSocketAddress) context.channel().remoteAddress();
        }
        return null;
    }
}
