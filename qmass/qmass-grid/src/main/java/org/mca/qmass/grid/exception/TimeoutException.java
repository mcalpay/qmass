package org.mca.qmass.grid.exception;

/**
 * User: malpay
 * Date: 13.Haz.2011
 * Time: 15:59:53
 */
public class TimeoutException extends RuntimeException {
    public TimeoutException() {
    }

    public TimeoutException(String message) {
        super(message);
    }

    public TimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeoutException(Throwable cause) {
        super(cause);
    }
}
