package co.unicauca.gestiontrabajogrado.domain.service;

import co.unicauca.gestiontrabajogrado.infrastructure.repository.IFormatoARepository;
import co.unicauca.gestiontrabajogrado.infrastructure.repository.IProyectoGradoRepository;
import co.unicauca.gestiontrabajogrado.infrastructure.repository.IUserRepository;

/**
 * Servicio stub para manejo de proyectos de grado
 */
public class ProyectoGradoService implements IProyectoGradoService {
    
    public ProyectoGradoService(IProyectoGradoRepository proyectoRepository,
                               IFormatoARepository formatoRepository,
                               ArchivoService archivoService,
                               IUserRepository userRepository) {
    }
}
