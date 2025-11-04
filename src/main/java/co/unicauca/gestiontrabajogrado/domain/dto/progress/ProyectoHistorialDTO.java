package co.unicauca.gestiontrabajogrado.domain.dto.progress;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO que refleja la respuesta del endpoint:
 * GET /api/progress/proyectos/{id}/historial
 */
public class ProyectoHistorialDTO {
    private Long proyectoId;
    private List<EventoDTO> historial;
    private Integer paginaActual;
    private Integer tamanoPagina;
    private Long totalEventos;
    private Integer totalPaginas;

    public ProyectoHistorialDTO() {}

    // Getters y Setters
    public Long getProyectoId() { return proyectoId; }
    public void setProyectoId(Long proyectoId) { this.proyectoId = proyectoId; }

    public List<EventoDTO> getHistorial() { return historial; }
    public void setHistorial(List<EventoDTO> historial) { this.historial = historial; }

    public Integer getPaginaActual() { return paginaActual; }
    public void setPaginaActual(Integer paginaActual) { this.paginaActual = paginaActual; }

    public Integer getTamanoPagina() { return tamanoPagina; }
    public void setTamanoPagina(Integer tamanoPagina) { this.tamanoPagina = tamanoPagina; }

    public Long getTotalEventos() { return totalEventos; }
    public void setTotalEventos(Long totalEventos) { this.totalEventos = totalEventos; }

    public Integer getTotalPaginas() { return totalPaginas; }
    public void setTotalPaginas(Integer totalPaginas) { this.totalPaginas = totalPaginas; }

    /**
     * DTO para un evento individual en el historial
     */
    public static class EventoDTO {
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
    }

    /**
     * DTO para información del responsable de un evento
     */
    public static class ResponsableDTO {
        private Long id;
        private String nombre;
        private String rol;

        public ResponsableDTO() {}

        // Getters y Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }
    }
}