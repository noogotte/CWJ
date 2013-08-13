package fr.aumgn.cwj.protocol;

public interface Client {

    long getEntityId();

    void sendPacket(Packet.ServerPacket... packets);
}
