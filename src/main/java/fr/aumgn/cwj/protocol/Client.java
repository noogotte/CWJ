package fr.aumgn.cwj.protocol;

import java.net.InetSocketAddress;

import fr.aumgn.cwj.protocol.Packet.ServerPacket;

public interface Client {

    long getEntityId();

    InetSocketAddress getIpAddress();

    void sendPacket(ServerPacket... packets);
}
