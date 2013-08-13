package fr.aumgn.cwj.protocol.exception;

public class MismatchedClientVersionException extends ProtocolHandlerException {

    private static final long serialVersionUID = 4264570758428410626L;
    private final int         server;
    private final int         client;

    public MismatchedClientVersionException(int server, int client) {
        super("Got client version " + client + " expected " + server);
        this.server = server;
        this.client = client;
    }

    public int getServerVersion() {
        return server;
    }

    public int getClientVersion() {
        return client;
    }
}
