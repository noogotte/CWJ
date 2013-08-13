package fr.aumgn.cwj.protocol.exception;

public class UnknownPacketException extends RuntimeException {

    private static final long serialVersionUID = 6424409962440755424L;

    private final int         id;

    public UnknownPacketException(int id) {
        super("Unknown packet : " + id);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
