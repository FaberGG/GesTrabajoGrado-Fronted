package co.unicauca.gestiontrabajogrado.domain.dto.progress;

public class ParticipanteDTO {
    private Long id;
    private String nombre;

    public ParticipanteDTO() {}

    public ParticipanteDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return "ParticipanteDTO{id=" + id + ", nombre='" + nombre + "'}";
    }
}