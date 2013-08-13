package fr.aumgn.cwj.protocol;

import static fr.aumgn.cwj.protocol.PacketDataLength.FLOAT;
import static fr.aumgn.cwj.protocol.PacketDataLength.INT16;
import static fr.aumgn.cwj.protocol.PacketDataLength.INT32;
import static fr.aumgn.cwj.protocol.PacketDataLength.INT64;
import static fr.aumgn.cwj.protocol.PacketDataLength.INT64_VEC3;
import static fr.aumgn.cwj.protocol.PacketDataLength.INT8;
import static fr.aumgn.cwj.protocol.PacketDataLength.ITEM_DATA;
import static fr.aumgn.cwj.protocol.PacketDataLength.VEC3;
import fr.aumgn.cwj.protocol.exception.NotImplementedPacketException;
import fr.aumgn.cwj.protocol.exception.ServerOnlyPacketException;
import fr.aumgn.cwj.protocol.shared.ChatPacket;
import fr.aumgn.cwj.protocol.shared.EntityUpdatePacket;
import fr.aumgn.cwj.protocol.shared.VersionPacket;
import io.netty.buffer.ByteBuf;

public enum PacketType {

    ENTITY_UPDATE {

        @Override
        public Packet.ClientPacket read(ByteBuf buf) {
            return new EntityUpdatePacket(buf);
        }
    },

    MULTIPLE_ENTITY_UPDATE {

        @Override
        public Packet.ClientPacket read(ByteBuf buf) {
            return serverOnlyPacket();
        }
    },

    UPDATE_FINISHED {

        @Override
        public Packet.ClientPacket read(ByteBuf buf) {
            return serverOnlyPacket();
        }
    },

    UNKNOWN3 {

        @Override
        public Packet.ClientPacket read(ByteBuf buf) {
            return serverOnlyPacket();
        }
    },

    SERVER_UPDATE {

        @Override
        public Packet.ClientPacket read(ByteBuf buf) {
            return serverOnlyPacket();
        }
    },

    CURRRENT_TIME {

        @Override
        public Packet.ClientPacket read(ByteBuf buf) {
            return serverOnlyPacket();
        }
    },

    INTERACT {

        private static final int LENGTH = ITEM_DATA + 4 * INT32 + INT8 + INT16;

        @Override
        public Packet.ClientPacket read(ByteBuf buf) {
            return skipPacket(LENGTH, buf);
        }
    },

    HIT {

        private static final int LENGTH = 2 * INT64 + FLOAT + INT8 + 3 + 2 * INT32 + INT64_VEC3 + VEC3 + 3 * INT8 + 1;

        @Override
        public Packet.ClientPacket read(ByteBuf buf) {
            return skipPacket(LENGTH, buf);
        }
    },

    UNKNOWN8 {

        private static final int LENGTH = 40;

        @Override
        public Packet.ClientPacket read(ByteBuf buf) {
            return skipPacket(LENGTH, buf);
        }
    },

    SHOOT {

        private static final int LENGTH = INT64 + 3 * INT32 + 4 + INT64_VEC3 + 3 * INT32 + VEC3 + 4 * FLOAT + INT32
                                                + INT8 + 2 * INT32;

        @Override
        public Packet.ClientPacket read(ByteBuf buf) {
            return skipPacket(LENGTH, buf);
        }
    },

    CHAT {

        @Override
        public Packet.ClientPacket read(ByteBuf buf) {
            return new ChatPacket(buf);
        }
    },

    CHUNK_DISCOVERED {

        private static final int LENGTH = 2 * INT32;

        @Override
        public Packet.ClientPacket read(ByteBuf buf) {
            return skipPacket(LENGTH, buf);
        }
    },

    SECTOR_DISCOVERED {

        private static final int LENGTH = 2 * INT32;

        @Override
        public Packet.ClientPacket read(ByteBuf buf) {
            return skipPacket(LENGTH, buf);
        }
    },

    UNUSED13 {

        @Override
        public Packet.ClientPacket read(ByteBuf buf) {
            return notImplementedPacket();
        }
    },

    UNUSED14 {

        @Override
        public Packet.ClientPacket read(ByteBuf buf) {
            return notImplementedPacket();
        }
    },

    SEED_DATA {

        @Override
        public Packet.ClientPacket read(ByteBuf buf) {
            return serverOnlyPacket();
        }
    },

    JOIN {

        @Override
        public Packet.ClientPacket read(ByteBuf buf) {
            return serverOnlyPacket();
        }
    },

    VERSION {

        @Override
        public VersionPacket read(ByteBuf buf) {
            return new VersionPacket(buf);
        }
    },

    SERVER_FULL {

        @Override
        public Packet.ClientPacket read(ByteBuf buf) {
            return serverOnlyPacket();
        }
    },

    UNKNOWN {

        /**
         * Unknown packets raise an exception directly in the decoder. This
         * method must never be called.
         */
        @Override
        public Packet.ClientPacket read(ByteBuf buf) {
            return notImplementedPacket();
        }
    };

    private PacketType() {
    }

    public int getId() {
        return ordinal();
    }

    public abstract Packet.ClientPacket read(ByteBuf buf);

    protected Packet.ClientPacket skipPacket(int length, ByteBuf buf) {
        buf.skipBytes(length);
        return new NoopClientPacket(this);
    }

    protected Packet.ClientPacket notImplementedPacket() {
        throw new NotImplementedPacketException();
    }

    protected Packet.ClientPacket serverOnlyPacket() {
        throw new ServerOnlyPacketException();
    }

    public static PacketType valueForId(int id) {
        PacketType[] types = values();
        if (id < 0 || id >= types.length) {
            return UNKNOWN;
        }

        return types[id];
    }

    private static class NoopClientPacket implements Packet.ClientPacket {

        private final PacketType type;

        public NoopClientPacket(PacketType type) {
            this.type = type;
        }

        @Override
        public PacketType getType() {
            return type;
        }

        @Override
        public void dispatchTo(ProtocolHandler dispatcher, Client client) {
        }
    }
}
