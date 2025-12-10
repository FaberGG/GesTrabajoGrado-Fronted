package co.unicauca.gestiontrabajogrado.domain.dto.progress;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO que refleja la respuesta del endpoint:
 * GET /api/progress/estudiantes/{estudianteId}/historial
 *
 * Este endpoint devuelve información completa del proyecto del estudiante
 * incluyendo estado actual, participantes e historial de eventos
 */
public class EstudianteProyectoDTO {
    private Long proyectoId;
    private Long estudianteId;
    private String tituloProyecto;
    private String estadoActual;
    private String estadoLegible;
    private String fase;
    private EstudiantesDTO estudiantes;
    private List<EventoHistorialDTO> historial;
    private Integer paginaActual;
    private Integer tamanoPagina;
    private Long totalEventos;
    private Integer totalPaginas;

    public EstudianteProyectoDTO() {}

    // Getters y Setters
    public Long getProyectoId() { return proyectoId; }
    public void setProyectoId(Long proyectoId) { this.proyectoId = proyectoId; }

    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }

    public String getTituloProyecto() { return tituloProyecto; }
    public void setTituloProyecto(String tituloProyecto) { this.tituloProyecto = tituloProyecto; }

    public String getEstadoActual() { return estadoActual; }
    public void setEstadoActual(String estadoActual) { this.estadoActual = estadoActual; }

    public String getEstadoLegible() { return estadoLegible; }
    public void setEstadoLegible(String estadoLegible) { this.estadoLegible = estadoLegible; }

    public String getFase() { return fase; }
    public void setFase(String fase) { this.fase = fase; }

    public EstudiantesDTO getEstudiantes() { return estudiantes; }
    public void setEstudiantes(EstudiantesDTO estudiantes) { this.estudiantes = estudiantes; }

    public List<EventoHistorialDTO> getHistorial() { return historial; }
    public void setHistorial(List<EventoHistorialDTO> historial) { this.historial = historial; }

    public Integer getPaginaActual() { return paginaActual; }
    public void setPaginaActual(Integer paginaActual) { this.paginaActual = paginaActual; }

    public Integer getTamanoPagina() { return tamanoPagina; }
    public void setTamanoPagina(Integer tamanoPagina) { this.tamanoPagina = tamanoPagina; }

    public Long getTotalEventos() { return totalEventos; }
    public void setTotalEventos(Long totalEventos) { this.totalEventos = totalEventos; }

    public Integer getTotalPaginas() { return totalPaginas; }
    public void setTotalPaginas(Integer totalPaginas) { this.totalPaginas = totalPaginas; }

    /**
     * DTO para información de los estudiantes del proyecto
     */
    public static class EstudiantesDTO {
        private EstudianteInfoDTO estudiante1;
        private EstudianteInfoDTO estudiante2;

        public EstudiantesDTO() {}

        public EstudianteInfoDTO getEstudiante1() { return estudiante1; }
        public void setEstudiante1(EstudianteInfoDTO estudiante1) { this.estudiante1 = estudiante1; }

        public EstudianteInfoDTO getEstudiante2() { return estudiante2; }
        public void setEstudiante2(EstudianteInfoDTO estudiante2) { this.estudiante2 = estudiante2; }
    }

    /**
     * DTO para información de un estudiante individual
     */
    public static class EstudianteInfoDTO {
        private Long id;
        private String nombre;
        private String email;

        public EstudianteInfoDTO() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    /**
     * DTO para un evento individual en el historial
     */
    public static class EventoHistorialDTO {
        private Long eventoId;
        private Long proyectoId;
        private String tipoEvento;
        private LocalDateTime fecha;
        private String descripcion;
        private Integer version;
        private String resultado;
        private String observaciones;

        public EventoHistorialDTO() {}

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
    }
}

