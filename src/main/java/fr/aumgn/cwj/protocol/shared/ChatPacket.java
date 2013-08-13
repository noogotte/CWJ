package fr.aumgn.cwj.protocol.shared;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

import fr.aumgn.cwj.protocol.Client;
import fr.aumgn.cwj.protocol.Packet;
import fr.aumgn.cwj.protocol.ProtocolHandler;
import fr.aumgn.cwj.protocol.PacketType;

public class ChatPacket implements Packet.SharedPacket {

    private static final Charset MESSAGE_CHARSET = CharsetUtil.UTF_16LE;

    private final String         message;

    public ChatPacket(ByteBuf buf) {
        int length = buf.readInt();
        byte[] bytes = new byte[length * 2];
        buf.readBytes(bytes);
        message = new String(bytes, MESSAGE_CHARSET);
    }

    public ChatPacket(String message) {
        this.message = message;
    }

    @Override
    public PacketType getType() {
        return PacketType.CHAT;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void dispatchTo(ProtocolHandler dispatcher, Client client) {
    }

    @Override
    public void writeTo(ByteBuf buf) {
        buf.writeLong(0l);
        byte[] bytes = message.getBytes(MESSAGE_CHARSET);
        buf.writeInt(bytes.length / 2);
        buf.writeBytes(bytes);
    }
}
