package co.unicauca.gestiontrabajogrado.dto.submission;

/**
 * DTO genérico para respuestas que solo contienen un ID
 */
public class IdResponse {

    private Long id;

    public IdResponse() {
    }

    public IdResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "IdResponse{id=" + id + '}';
    }
}

