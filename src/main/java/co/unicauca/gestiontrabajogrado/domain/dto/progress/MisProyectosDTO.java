package co.unicauca.gestiontrabajogrado.domain.dto.progress;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para la respuesta del endpoint /api/progress/proyectos/mis-proyectos
 * Lista los proyectos donde el usuario autenticado es director o codirector
 */
public class MisProyectosDTO {
    private List<ProyectoResumenDTO> proyectos;
    private Integer total;

    // Getters y Setters
    public List<ProyectoResumenDTO> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<ProyectoResumenDTO> proyectos) {
        this.proyectos = proyectos;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * DTO para cada proyecto en la lista
     */
    public static class ProyectoResumenDTO {
        private Long proyectoId;
        private String titulo;
        private String estadoActual;
        private String estadoLegible;
        private String fase;
        private String modalidad;
        private String programa;
        private LocalDateTime ultimaActualizacion;
        private String rol; // "DIRECTOR" o "CODIRECTOR"
        private ParticipanteDTO director;
        private ParticipanteDTO codirector;
        private EstudiantesResumenDTO estudiantes;

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

        public LocalDateTime getUltimaActualizacion() {
            return ultimaActualizacion;
        }

        public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
            this.ultimaActualizacion = ultimaActualizacion;
        }

        public String getRol() {
            return rol;
        }

        public void setRol(String rol) {
            this.rol = rol;
        }

        public ParticipanteDTO getDirector() {
            return director;
        }

        public void setDirector(ParticipanteDTO director) {
            this.director = director;
        }

        public ParticipanteDTO getCodirector() {
            return codirector;
        }

        public void setCodirector(ParticipanteDTO codirector) {
            this.codirector = codirector;
        }

        public EstudiantesResumenDTO getEstudiantes() {
            return estudiantes;
        }

        public void setEstudiantes(EstudiantesResumenDTO estudiantes) {
            this.estudiantes = estudiantes;
        }

        /**
         * Verifica si el Formato A está rechazado pero no definitivamente
         */
        public boolean puedeReenviarFormatoA() {
            return estadoActual != null &&
                   (estadoActual.equals("FORMATO_A_RECHAZADO_1") ||
                    estadoActual.equals("FORMATO_A_RECHAZADO_2"));
        }

        /**
         * Verifica si el Formato A está aprobado
         */
        public boolean formatoAAprobado() {
            return estadoActual != null && estadoActual.equals("FORMATO_A_APROBADO");
        }

        /**
         * Verifica si puede subir anteproyecto
         */
        public boolean puedeSubirAnteproyecto() {
            return formatoAAprobado() && "FORMATO_A".equals(fase);
        }
    }

    /**
     * DTO para participante (director/codirector)
     */
    public static class ParticipanteDTO {
        private Long id;
        private String nombre;

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

    /**
     * DTO para estudiantes
     */
    public static class EstudiantesResumenDTO {
        private EstudianteDTO estudiante1;
        private EstudianteDTO estudiante2;

        public EstudianteDTO getEstudiante1() {
            return estudiante1;
        }

        public void setEstudiante1(EstudianteDTO estudiante1) {
            this.estudiante1 = estudiante1;
        }

        public EstudianteDTO getEstudiante2() {
            return estudiante2;
        }

        public void setEstudiante2(EstudianteDTO estudiante2) {
            this.estudiante2 = estudiante2;
        }
    }

    /**
     * DTO para estudiante
     */
    public static class EstudianteDTO {
        private Long id;
        private String nombre;
        private String email;

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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}

