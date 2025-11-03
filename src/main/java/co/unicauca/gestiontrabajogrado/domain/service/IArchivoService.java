package co.unicauca.gestiontrabajogrado.domain.service;

public interface IArchivoService {
    byte[] descargarArchivo(String ruta);
    String guardarArchivo(byte[] contenido, String nombre);
}

