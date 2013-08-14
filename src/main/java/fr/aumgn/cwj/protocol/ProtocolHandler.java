package fr.aumgn.cwj.protocol;

import fr.aumgn.cwj.protocol.shared.EntityUpdatePacket;
import fr.aumgn.cwj.protocol.shared.VersionPacket;
import io.netty.channel.ChannelHandlerContext;

public interface ProtocolHandler {

    Client initConnection(ChannelHandlerContext context, VersionPacket versionPacket);

    void disconnect(Client client);

    void received(Client client, EntityUpdatePacket entityUpdatePacket);
}
