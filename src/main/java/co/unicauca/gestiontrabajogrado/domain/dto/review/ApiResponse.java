package co.unicauca.gestiontrabajogrado.domain.dto.review;

import java.util.List;

/**
 * Wrapper genérico para respuestas del Review Service
 * Todas las respuestas del microservicio vienen envueltas en este formato
 */
public class ApiResponse<T> {
    private Boolean success;
    private String message;
    private T data;
    private List<String> errors;

    public ApiResponse() {
    }

    public ApiResponse(Boolean success, String message, T data, List<String> errors) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}

