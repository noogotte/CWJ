package fr.aumgn.cwj.protocol.exception;

public abstract class ProtocolHandlerException extends RuntimeException {

    private static final long serialVersionUID = 584408316950822423L;

    public ProtocolHandlerException() {
    }

    public ProtocolHandlerException(String message) {
        super(message);
    }

    public ProtocolHandlerException(Throwable throwable) {
        super(throwable);
    }

    public ProtocolHandlerException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
