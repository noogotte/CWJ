package fr.aumgn.cwj.utils;

public class UnexpectedException extends RuntimeException {

    private static final long serialVersionUID = 1247520383991079478L;

    public UnexpectedException() {
        super();
    }

    public UnexpectedException(String message) {
        super(message);
    }

    public UnexpectedException(Throwable throwable) {
        super(throwable);
    }

    public UnexpectedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
