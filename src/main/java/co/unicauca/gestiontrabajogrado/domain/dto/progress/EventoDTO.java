package co.unicauca.gestiontrabajogrado.domain.dto.progress;

import java.time.LocalDateTime;

/**
 * DTO para un evento individual en el historial
 */
public class EventoDTO {
    private Long eventoId;
    private Long proyectoId;
    private String tipoEvento;
    private LocalDateTime fecha;
    private String descripcion;
    private Integer version;
    private String resultado;
    private String observaciones;
    private ResponsableDTO responsable;

    public EventoDTO() {}

    // Getters y Setters
    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }

    public Long getProyectoId() { return proyectoId; }
    public void setProyectoId(Long proyectoId) { this.proyectoId = proyectoId; }

    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public ResponsableDTO getResponsable() { return responsable; }
    public void setResponsable(ResponsableDTO responsable) { this.responsable = responsable; }

    // Clase interna para responsable
    public static class ResponsableDTO {
        private Long id;
        private String nombre;
        private String rol;

        public ResponsableDTO() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }
    }
}