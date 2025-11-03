package co.unicauca.gestiontrabajogrado.controller;

import co.unicauca.gestiontrabajogrado.domain.model.User;
import co.unicauca.gestiontrabajogrado.dto.FormatoADetalleDTO;
import co.unicauca.gestiontrabajogrado.dto.ProyectoGradoResponseDTO;

public class EstudianteController {
    private User currentUser;
    private ProyectoGradoResponseDTO proyectoActual;

    public EstudianteController() {
    }

    public EstudianteController(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public ProyectoGradoResponseDTO cargarDatosTrabajoGrado() {
        // TODO: Implementar llamada al servicio
        return proyectoActual;
    }

    public boolean tieneProyecto() {
        // TODO: Implementar verificación con el servicio
        return proyectoActual != null;
    }

    public ProyectoGradoResponseDTO getProyectoActual() {
        return proyectoActual;
    }

    public void setProyectoActual(ProyectoGradoResponseDTO proyecto) {
        this.proyectoActual = proyecto;
    }

    public String obtenerNombreDirector() {
        // TODO: Implementar consulta al servicio de usuarios
        return "Director Ejemplo";
    }

    public String obtenerNombreCodirector() {
        // TODO: Implementar consulta al servicio de usuarios
        return "Codirector Ejemplo";
    }

    public String obtenerNombreEstudiante2() {
        // TODO: Implementar consulta al servicio de usuarios
        return "Estudiante 2 Ejemplo";
    }

    public String obtenerEstadoActualTexto() {
        if (proyectoActual != null && proyectoActual.getEstado() != null) {
            return proyectoActual.getEstado().toString();
        }
        return "Sin proyecto";
    }

    public FormatoADetalleDTO obtenerUltimoFormatoA() {
        if (proyectoActual != null && proyectoActual.getFormatosA() != null && !proyectoActual.getFormatosA().isEmpty()) {
            return proyectoActual.getFormatosA().get(proyectoActual.getFormatosA().size() - 1);
        }
        return null;
    }

    public void volverAlDashboard() {
        // TODO: Implementar navegación al dashboard
        System.out.println("Volviendo al dashboard...");
    }
}
