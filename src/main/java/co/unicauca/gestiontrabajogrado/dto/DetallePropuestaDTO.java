package co.unicauca.gestiontrabajogrado.dto;

/**
 * DTO stub para detalles de una propuesta
 */
public class DetallePropuestaDTO {
    public Integer id;
    public String titulo;
    public String modalidad;
    public String estado;
    
    public String formatearParaVista() {
        // Stub implementation
        return "Detalle: " + titulo;
    }
}
