package fr.aumgn.cwj;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.aumgn.cwj.protocol.driver.Driver;
import fr.aumgn.cwj.utils.OneLinerFormatter;

public class CWJ {

    private static final Logger logger = Logger.getLogger(CWJ.class.getName());
    private static final Server server = new Server();

    static {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new OneLinerFormatter());
        logger.addHandler(consoleHandler);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
    }

    public static void main(String[] args) throws InterruptedException {
        logger.info("Starting CWJ");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new Driver(server));
            bootstrap.option(ChannelOption.SO_BACKLOG, 128);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

            logger.info("Base folder : \"" + server.getFolder().toAbsolutePath() + "\"");
            server.getPluginManager().load();
            server.getPluginManager().enableAll();

            logger.info("Starting server on port " + server.getPort());
            ChannelFuture channelFuture = bootstrap.bind(server.getPort()).sync();
            channelFuture.channel().closeFuture().sync();
        }
        finally {
            shutdown(workerGroup, bossGroup);
        }
    }

    private static void shutdown(EventLoopGroup workerGroup, EventLoopGroup bossGroup) {
        server.getPluginManager().disableAll();
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    public static Logger getLogger() {
        return logger;
    }

    public static Server getServer() {
        return server;
    }
}
