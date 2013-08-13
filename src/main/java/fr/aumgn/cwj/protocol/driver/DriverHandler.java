package fr.aumgn.cwj.protocol.driver;

import fr.aumgn.cwj.CWJ;
import fr.aumgn.cwj.protocol.Client;
import fr.aumgn.cwj.protocol.Packet;
import fr.aumgn.cwj.protocol.Packet.ClientPacket;
import fr.aumgn.cwj.protocol.ProtocolHandler;
import fr.aumgn.cwj.protocol.exception.AlreadyInitializedConnectionException;
import fr.aumgn.cwj.protocol.exception.ProtocolHandlerException;
import fr.aumgn.cwj.protocol.exception.UninitializedConnectionException;
import fr.aumgn.cwj.protocol.shared.VersionPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.io.IOException;
import java.util.logging.Level;

public class DriverHandler extends SimpleChannelInboundHandler<Packet.ClientPacket> {

    private static final AttributeKey<Client> CLIENT_KEY = new AttributeKey<>("CLIENT");

    private final ProtocolHandler             protocolHandler;

    public DriverHandler(ProtocolHandler protocolHandler) {
        this.protocolHandler = protocolHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, Packet.ClientPacket packet) throws Exception {
        if (packet instanceof VersionPacket) {
            handleVersionPacket(context, (VersionPacket) packet);
        }
        else {
            handlePacket(context, packet);
        }
    }

    private void handleVersionPacket(ChannelHandlerContext context, VersionPacket versionPacket) {
        Attribute<Client> clientAttribute = context.attr(CLIENT_KEY);
        Client client = clientAttribute.get();
        if (client != null) {
            throw new AlreadyInitializedConnectionException();
        }

        client = versionPacket.initConnection(protocolHandler, context);
        clientAttribute.set(client);
    }

    private Client client(ChannelHandlerContext context) {
        return context.attr(CLIENT_KEY).get();
    }

    private void handlePacket(ChannelHandlerContext context, ClientPacket packet) {
        Client client = client(context);
        if (client == null) {
            throw new UninitializedConnectionException();
        }

        packet.dispatchTo(protocolHandler, client);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        Client client = client(context);
        if (cause instanceof ProtocolHandlerException) {
            String clientName = clientName(context, client);
            CWJ.getLogger().log(Level.FINEST, "Exception while handling packet for client " + clientName, cause);
        }
        else if (cause instanceof IOException) {
            String clientName = clientName(context, client);
            CWJ.getLogger().info("Connection with client " + clientName + " lost.");
        }
        else {
            CWJ.getLogger().log(Level.SEVERE, "Error", cause);
        }

        if (client != null) {
            protocolHandler.disconnect(client);
        }
        context.close();
    }

    private String clientName(ChannelHandlerContext context, Client client) {
        if (client == null) {
            return context.channel().remoteAddress().toString();
        }

        return String.valueOf(client.getEntityId());
    }
}
