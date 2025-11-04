package co.unicauca.gestiontrabajogrado.infrastructure.exceptions;

public class NetworkException extends Exception {
    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkException(String message) {
    }
}