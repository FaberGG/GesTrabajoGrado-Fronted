package co.unicauca.gestiontrabajogrado.dto;

import java.time.LocalDateTime;

/**
 * DTO para representar el estado actual de un proyecto
 * Mapea la respuesta del endpoint GET /api/progress/proyectos/{id}/estado
 */
public class ProyectoEstadoDTO {
    private Long proyectoId;
    private String titulo;
    private String modalidad;
    private String programa;
    private String estadoActual;
    private String estadoLegible;
    private String fase;
    private LocalDateTime ultimaActualizacion;
    private String siguientePaso;
    private FormatoAEstadoDTO formatoA;
    private AnteproyectoEstadoDTO anteproyecto;
    private ParticipantesDTO participantes;

    // Constructores
    public ProyectoEstadoDTO() {}

    // Getters y Setters
    public Long getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Long proyectoId) {
        this.proyectoId = proyectoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(String estadoActual) {
        this.estadoActual = estadoActual;
    }

    public String getEstadoLegible() {
        return estadoLegible;
    }

    public void setEstadoLegible(String estadoLegible) {
        this.estadoLegible = estadoLegible;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public String getSiguientePaso() {
        return siguientePaso;
    }

    public void setSiguientePaso(String siguientePaso) {
        this.siguientePaso = siguientePaso;
    }

    public FormatoAEstadoDTO getFormatoA() {
        return formatoA;
    }

    public void setFormatoA(FormatoAEstadoDTO formatoA) {
        this.formatoA = formatoA;
    }

    public AnteproyectoEstadoDTO getAnteproyecto() {
        return anteproyecto;
    }

    public void setAnteproyecto(AnteproyectoEstadoDTO anteproyecto) {
        this.anteproyecto = anteproyecto;
    }

    public ParticipantesDTO getParticipantes() {
        return participantes;
    }

    public void setParticipantes(ParticipantesDTO participantes) {
        this.participantes = participantes;
    }

    // Clases internas para subobjetos
    public static class FormatoAEstadoDTO {
        private String estado;
        private Integer versionActual;
        private Integer intentoActual;
        private Integer maxIntentos;
        private LocalDateTime fechaUltimoEnvio;
        private LocalDateTime fechaUltimaEvaluacion;

        // Getters y Setters
        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

        public Integer getVersionActual() {
            return versionActual;
        }

        public void setVersionActual(Integer versionActual) {
            this.versionActual = versionActual;
        }

        public Integer getIntentoActual() {
            return intentoActual;
        }

        public void setIntentoActual(Integer intentoActual) {
            this.intentoActual = intentoActual;
        }

        public Integer getMaxIntentos() {
            return maxIntentos;
        }

        public void setMaxIntentos(Integer maxIntentos) {
            this.maxIntentos = maxIntentos;
        }

        public LocalDateTime getFechaUltimoEnvio() {
            return fechaUltimoEnvio;
        }

        public void setFechaUltimoEnvio(LocalDateTime fechaUltimoEnvio) {
            this.fechaUltimoEnvio = fechaUltimoEnvio;
        }

        public LocalDateTime getFechaUltimaEvaluacion() {
            return fechaUltimaEvaluacion;
        }

        public void setFechaUltimaEvaluacion(LocalDateTime fechaUltimaEvaluacion) {
            this.fechaUltimaEvaluacion = fechaUltimaEvaluacion;
        }
    }

    public static class AnteproyectoEstadoDTO {
        private String estado;
        private LocalDateTime fechaEnvio;
        private Boolean evaluadoresAsignados;

        // Getters y Setters
        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

        public LocalDateTime getFechaEnvio() {
            return fechaEnvio;
        }

        public void setFechaEnvio(LocalDateTime fechaEnvio) {
            this.fechaEnvio = fechaEnvio;
        }

        public Boolean getEvaluadoresAsignados() {
            return evaluadoresAsignados;
        }

        public void setEvaluadoresAsignados(Boolean evaluadoresAsignados) {
            this.evaluadoresAsignados = evaluadoresAsignados;
        }
    }

    public static class ParticipantesDTO {
        private UsuarioBasicoDTO director;
        private UsuarioBasicoDTO codirector;

        // Getters y Setters
        public UsuarioBasicoDTO getDirector() {
            return director;
        }

        public void setDirector(UsuarioBasicoDTO director) {
            this.director = director;
        }

        public UsuarioBasicoDTO getCodirector() {
            return codirector;
        }

        public void setCodirector(UsuarioBasicoDTO codirector) {
            this.codirector = codirector;
        }
    }

    public static class UsuarioBasicoDTO {
        private Long id;
        private String nombre;

        // Getters y Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
    }
}