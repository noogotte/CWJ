package fr.aumgn.cwj.protocol;

import fr.aumgn.cwj.protocol.Packet.ServerPacket;

public interface Client {

    long getEntityId();

    void sendPacket(ServerPacket... packets);
}
