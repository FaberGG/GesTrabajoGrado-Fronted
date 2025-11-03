package co.unicauca.gestiontrabajogrado.controller;

import co.unicauca.gestiontrabajogrado.dto.FormatoADetalleDTO;
import co.unicauca.gestiontrabajogrado.dto.ProyectoGradoResponseDTO;

import java.util.List;

public class CoordinadorController {

    public CoordinadorController() {
    }

    public java.util.List<co.unicauca.gestiontrabajogrado.presentation.dashboard.coordinadorview.PropuestaRow> obtenerPropuestas() {
        // TODO: Implementar llamada al servicio
        return new java.util.ArrayList<>();
    }

    public co.unicauca.gestiontrabajogrado.domain.model.enumEstadoFormato aprobarFormato(Integer formatoId, String comentarios) {
        // TODO: Implementar aprobación de formato
        System.out.println("Aprobando formato " + formatoId + " con comentarios: " + comentarios);
        return co.unicauca.gestiontrabajogrado.domain.model.enumEstadoFormato.APROBADO;
    }

    public co.unicauca.gestiontrabajogrado.domain.model.enumEstadoFormato rechazarFormato(Integer formatoId, String comentarios) {
        // TODO: Implementar rechazo de formato
        System.out.println("Rechazando formato " + formatoId + " con comentarios: " + comentarios);
        return co.unicauca.gestiontrabajogrado.domain.model.enumEstadoFormato.RECHAZADO;
    }

    public void setView(Object view) {
        // TODO: Guardar referencia a la vista si es necesario
        System.out.println("Vista establecida: " + view.getClass().getSimpleName());
    }

    public java.util.List<co.unicauca.gestiontrabajogrado.presentation.dashboard.coordinadorview.PropuestaRow> obtenerPropuestas(boolean soloPendientes) {
        // TODO: Implementar obtención de propuestas
        return obtenerPropuestas();
    }

    public co.unicauca.gestiontrabajogrado.dto.DetallePropuestaDTO obtenerDetallePropuesta(Integer id) {
        // TODO: Implementar obtención de detalle
        return new co.unicauca.gestiontrabajogrado.dto.DetallePropuestaDTO();
    }


    public int contarPorEstado(co.unicauca.gestiontrabajogrado.domain.model.enumEstadoFormato estado) {
        // TODO: Implementar conteo por estado
        return 0;
    }
}
