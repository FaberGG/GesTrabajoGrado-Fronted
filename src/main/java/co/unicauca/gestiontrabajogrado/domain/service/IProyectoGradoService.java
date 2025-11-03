package co.unicauca.gestiontrabajogrado.domain.service;

import co.unicauca.gestiontrabajogrado.dto.ProyectoGradoRequestDTO;
import co.unicauca.gestiontrabajogrado.dto.ProyectoGradoResponseDTO;
import java.util.List;

public interface IProyectoGradoService {
    ProyectoGradoResponseDTO crearProyecto(ProyectoGradoRequestDTO request);
    ProyectoGradoResponseDTO obtenerProyectoPorId(Long id);
    List<ProyectoGradoResponseDTO> listarProyectosPorDocente(Long docenteId);
    List<ProyectoGradoResponseDTO> listarTodosProyectos();
    ProyectoGradoResponseDTO actualizarProyecto(Long id, ProyectoGradoRequestDTO request);
    void eliminarProyecto(Long id);
}

