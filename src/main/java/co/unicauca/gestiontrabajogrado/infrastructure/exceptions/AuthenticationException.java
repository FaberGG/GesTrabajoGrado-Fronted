package co.unicauca.gestiontrabajogrado.infrastructure.exceptions;

public class AuthenticationException extends Exception {
    public AuthenticationException(String message) {
        super(message);
    }
}