package co.unicauca.gestiontrabajogrado.presentation.dashboard.docenteview;

import co.unicauca.gestiontrabajogrado.domain.dto.progress.MisProyectosDTO;

import java.time.LocalDateTime;

/**
 * Representa un proyecto en la lista del docente
 * Incluye métodos de ayuda para determinar acciones disponibles
 */
public class ProyectoItem {
    private final Long proyectoId;
    private final String titulo;
    private final String estadoActual;
    private final String estadoLegible;
    private final String fase;
    private final String modalidad;
    private final String programa;
    private final LocalDateTime ultimaActualizacion;
    private final String rol;
    private final String estudiantesNombres;

    public ProyectoItem(MisProyectosDTO.ProyectoResumenDTO proyecto) {
        this.proyectoId = proyecto.getProyectoId();
        this.titulo = proyecto.getTitulo();
        this.estadoActual = proyecto.getEstadoActual();
        this.estadoLegible = proyecto.getEstadoLegible();
        this.fase = proyecto.getFase();
        this.modalidad = proyecto.getModalidad();
        this.programa = proyecto.getPrograma();
        this.ultimaActualizacion = proyecto.getUltimaActualizacion();
        this.rol = proyecto.getRol();

        // Construir nombres de estudiantes
        StringBuilder nombres = new StringBuilder();
        if (proyecto.getEstudiantes() != null) {
            if (proyecto.getEstudiantes().getEstudiante1() != null) {
                nombres.append(proyecto.getEstudiantes().getEstudiante1().getNombre());
            }
            if (proyecto.getEstudiantes().getEstudiante2() != null) {
                if (nombres.length() > 0) nombres.append(", ");
                nombres.append(proyecto.getEstudiantes().getEstudiante2().getNombre());
            }
        }
        this.estudiantesNombres = nombres.length() > 0 ? nombres.toString() : "Sin estudiantes";
    }

    // Getters
    public Long getProyectoId() {
        return proyectoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getEstadoActual() {
        return estadoActual;
    }

    public String getEstadoLegible() {
        return estadoLegible;
    }

    public String getFase() {
        return fase;
    }

    public String getModalidad() {
        return modalidad;
    }

    public String getPrograma() {
        return programa;
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public String getRol() {
        return rol;
    }

    public String getEstudiantesNombres() {
        return estudiantesNombres;
    }

    // Métodos de lógica de negocio

    /**
     * Verifica si el Formato A está rechazado pero puede reenviarse
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
     * (Formato A aprobado y aún en fase FORMATO_A)
     */
    public boolean puedeSubirAnteproyecto() {
        return formatoAAprobado() && "FORMATO_A".equals(fase);
    }

    /**
     * Verifica si el Formato A está rechazado definitivamente
     */
    public boolean formatoARechazadoDefinitivo() {
        return estadoActual != null && estadoActual.equals("FORMATO_A_RECHAZADO_DEFINITIVO");
    }

    /**
     * Verifica si el formato A está en evaluación
     */
    public boolean formatoAEnEvaluacion() {
        return estadoActual != null &&
                (estadoActual.equals("EN_PRIMERA_EVALUACION_FORMATO_A") ||
                 estadoActual.equals("EN_SEGUNDA_EVALUACION_FORMATO_A") ||
                 estadoActual.equals("EN_TERCERA_EVALUACION_FORMATO_A"));
    }

    /**
     * Verifica si el anteproyecto está en evaluación
     */
    public boolean anteproyectoEnEvaluacion() {
        return estadoActual != null && estadoActual.equals("ANTEPROYECTO_EN_EVALUACION");
    }

    /**
     * Obtiene el color del badge según el estado
     */
    public java.awt.Color getColorEstado() {
        if (formatoAAprobado()) {
            return new java.awt.Color(212, 237, 218); // Verde claro
        } else if (puedeReenviarFormatoA()) {
            return new java.awt.Color(255, 243, 205); // Amarillo
        } else if (formatoARechazadoDefinitivo()) {
            return new java.awt.Color(248, 215, 218); // Rojo claro
        } else if (formatoAEnEvaluacion()) {
            return new java.awt.Color(227, 242, 253); // Azul claro
        } else if (anteproyectoEnEvaluacion()) {
            return new java.awt.Color(237, 231, 246); // Púrpura claro
        }
        return new java.awt.Color(248, 249, 250); // Gris por defecto
    }

    /**
     * Obtiene el color del texto del badge según el estado
     */
    public java.awt.Color getColorTextoEstado() {
        if (formatoAAprobado()) {
            return new java.awt.Color(21, 87, 36); // Verde oscuro
        } else if (puedeReenviarFormatoA()) {
            return new java.awt.Color(133, 100, 4); // Amarillo oscuro
        } else if (formatoARechazadoDefinitivo()) {
            return new java.awt.Color(114, 28, 36); // Rojo oscuro
        } else if (formatoAEnEvaluacion()) {
            return new java.awt.Color(21, 101, 192); // Azul oscuro
        } else if (anteproyectoEnEvaluacion()) {
            return new java.awt.Color(74, 20, 140); // Púrpura oscuro
        }
        return new java.awt.Color(66, 66, 66); // Gris oscuro por defecto
    }

    /**
     * Obtiene el icono según el estado
     */
    public String getIconoEstado() {
        if (formatoAAprobado()) {
            return "✅";
        } else if (puedeReenviarFormatoA()) {
            return "⚠️";
        } else if (formatoARechazadoDefinitivo()) {
            return "❌";
        } else if (formatoAEnEvaluacion()) {
            return "⏳";
        } else if (anteproyectoEnEvaluacion()) {
            return "📝";
        }
        return "📄";
    }

    /**
     * Obtiene el texto formateado de la fecha
     */
    public String getFechaFormateada() {
        if (ultimaActualizacion == null) return "—";
        try {
            java.time.format.DateTimeFormatter formatter =
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return ultimaActualizacion.format(formatter);
        } catch (Exception e) {
            return ultimaActualizacion.toString();
        }
    }
}

