package co.unicauca.gestiontrabajogrado.infrastructure.exceptions;

import java.util.Map;

public class ValidationException extends Exception {
    private Map<String, String> fieldErrors;

    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}