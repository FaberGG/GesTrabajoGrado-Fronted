package co.unicauca.gestiontrabajogrado.domain.dto.review;

/**
 * DTO para representar un evaluador del departamento
 * Respuesta del endpoint GET /api/identity/users/evaluadores
 */
public class EvaluadorDTO {
    private Long id;
    private String nombres;
    private String apellidos;
    private String email;
    private String departamento;
    private Boolean disponible;

    public EvaluadorDTO() {
    }

    public EvaluadorDTO(Long id, String nombres, String apellidos, String email,
                        String departamento, Boolean disponible) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.departamento = departamento;
        this.disponible = disponible;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    /**
     * Devuelve el nombre completo del evaluador
     */
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " (" + email + ")";
    }
}

