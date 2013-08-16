package fr.aumgn.cwj;

import io.netty.channel.ChannelHandlerContext;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.SortedSet;

import com.google.common.collect.Sets;

import fr.aumgn.cwj.plugin.PluginManager;
import fr.aumgn.cwj.protocol.Client;
import fr.aumgn.cwj.protocol.ProtocolHandler;
import fr.aumgn.cwj.protocol.exception.MismatchedClientVersionException;
import fr.aumgn.cwj.protocol.exception.ServerFullException;
import fr.aumgn.cwj.protocol.server.JoinPacket;
import fr.aumgn.cwj.protocol.server.SeedPacket;
import fr.aumgn.cwj.protocol.server.ServerFullPacket;
import fr.aumgn.cwj.protocol.shared.ChatPacket;
import fr.aumgn.cwj.protocol.shared.EntityUpdatePacket;
import fr.aumgn.cwj.protocol.shared.VersionPacket;

public final class Server implements ProtocolHandler {

    private static final int      VERSION = 3;

    private final int             port;
    private final int             seed;
    private final PluginManager   pluginManager;
    private final SortedSet<Long> playerIds;

    /**
     * Package-private so that it can only be instantiated by {@link CWJ}
     */
    Server() {
        this.port = 12345;
        this.seed = 17;
        this.pluginManager = new PluginManager(this.getFolder());
        this.playerIds = Sets.newTreeSet();
        for (long id = 1; id < 10; id++) {
            playerIds.add(id);
        }
    }

    private long popPlayerId() {
        long entityId = playerIds.first();
        playerIds.remove(entityId);
        return entityId;
    }

    private void pushPlayerId(long entityId) {
        playerIds.add(entityId);
    }

    public int getPort() {
        return port;
    }

    public int getSeed() {
        return seed;
    }

    void loadPlugins() {
        pluginManager.load();
    }

    @Override
    public void disconnect(Client client) {
        pushPlayerId(client.getEntityId());
    }

    @Override
    public Client initConnection(ChannelHandlerContext context, VersionPacket versionPacket) {
        if (versionPacket.getVersion() != VERSION) {
            CWJ.getLogger().info("Client try to connect with version " + versionPacket.getVersion());
            context.write(new VersionPacket(VERSION));
            context.flush();
            throw new MismatchedClientVersionException(VERSION, versionPacket.getVersion());
        }

        if (playerIds.isEmpty()) {
            CWJ.getLogger().info(
                    "Client Version " + versionPacket.getVersion() + " tried to connect but server was full");
            context.write(new ServerFullPacket());
            context.flush();
            throw new ServerFullException();
        }

        long playerId = popPlayerId();
        Player client = new Player(context, playerId);
        CWJ.getLogger().info(
                "Client Version " + versionPacket.getVersion() + " connected with entity id " + client.getEntityId()
                        + " and ip " + client.getIpAddress());
        client.sendPacket(new JoinPacket(client), new SeedPacket(seed), new ChatPacket("Welcome !"));
        return client;
    }

    @Override
    public void received(Client client, EntityUpdatePacket entityUpdatePacket) {
    }

    public Path getFolder() {
        return FileSystems.getDefault().getPath("");
    }
}
