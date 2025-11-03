package co.unicauca.gestiontrabajogrado.presentation.common;

import co.unicauca.gestiontrabajogrado.domain.service.*;

/**
 * Gestor singleton para servicios de la aplicación
 * Permite acceso global a servicios importantes sin duplicar instancias
 *
 * NOTA: Los repositorios fueron eliminados. Este servicio solo mantiene
 * referencias a servicios de dominio que ya no dependen de persistencia local.
 */
public class ServiceManager {

    private static ServiceManager instance;

    // Servicios de dominio (mantener solo para compatibilidad temporal)
    private IAutenticacionService autenticacionService;
    private IProyectoGradoService proyectoGradoService;
    private IArchivoService archivoService;


    private ServiceManager() {
        // Constructor privado para patrón singleton
        inicializarServicios();
    }

    /**
     * Obtiene la instancia única del ServiceManager
     */
    public static ServiceManager getInstance() {
        if (instance == null) {
            synchronized (ServiceManager.class) {
                if (instance == null) {
                    instance = new ServiceManager();
                }
            }
        }
        return instance;
    }

    /**
     * Inicializa los servicios (sin repositorios - ya no se usan)
     */
    private void inicializarServicios() {
        // Los servicios ahora están en paquete co.unicauca.gestiontrabajogrado.services
        // y se instancian directamente donde se necesitan (AuthService, SubmissionService, etc.)
        // Este ServiceManager se mantiene por compatibilidad con código legacy
        System.out.println("ServiceManager: Los servicios HTTP se crean on-demand en los controladores");
    }

    /**
     * Crea una instancia de una clase usando reflexión (solo constructores sin parámetros)
     */
    @SuppressWarnings("unchecked")
    private <T> T crearInstancia(String nombreClase, Class<T> interfaz) {
        try {
            Class<?> clase = Class.forName(nombreClase);
            return (T) clase.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            System.err.println("No se pudo crear instancia de " + nombreClase + ": " + e.getMessage());
            return null;
        }
    }

    // ========== GETTERS (mantenidos por compatibilidad, retornan null) ==========

    public IAutenticacionService getAutenticacionService() {
        return autenticacionService;
    }

    public IProyectoGradoService getProyectoGradoService() {
        return proyectoGradoService;
    }

    public IProyectoGradoService getProyectoService() {
        return proyectoGradoService;
    }

    public IArchivoService getArchivoService() {
        return archivoService;
    }

    // ========== SETTERS (para configuración manual si es necesario) ==========

    public void setAutenticacionService(IAutenticacionService service) {
        this.autenticacionService = service;
    }

    public void setProyectoGradoService(IProyectoGradoService service) {
        this.proyectoGradoService = service;
    }
}