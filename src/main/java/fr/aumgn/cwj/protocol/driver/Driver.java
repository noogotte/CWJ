package fr.aumgn.cwj.protocol.driver;

import fr.aumgn.cwj.protocol.ProtocolHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class Driver extends ChannelInitializer<SocketChannel> {

    private final ProtocolHandler protocolHandler;

    public Driver(ProtocolHandler protocolHandler) {
        this.protocolHandler = protocolHandler;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        channel.pipeline().addLast(new DriverEncoder(), new DriverDecoder(), new DriverHandler(protocolHandler));
    }
}
