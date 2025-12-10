package co.unicauca.gestiontrabajogrado.domain.dto.submission;

import java.util.List;

/**
 * DTO para crear Formato A (parte data del multipart)
 * Alineado con el backend FormatoAController
 *
 * IMPORTANTE: El directorId NO se envía en el request.
 * El backend lo toma automáticamente del usuario autenticado (token JWT).
 * El docente que crea el FormatoA se convierte automáticamente en el director.
 */
public class FormatoAData {

    private String titulo;
    private Modalidad modalidad;
    private String objetivoGeneral;
    private List<String> objetivosEspecificos;
    // NO incluir directorId - el backend lo toma del usuario autenticado
    private Long codirectorId;
    private Long estudiante1Id;
    private Long estudiante2Id;

    public FormatoAData() {
    }

    public FormatoAData(String titulo, Modalidad modalidad, String objetivoGeneral,
                        List<String> objetivosEspecificos,
                        Long codirectorId, Long estudiante1Id, Long estudiante2Id) {
        this.titulo = titulo;
        this.modalidad = modalidad;
        this.objetivoGeneral = objetivoGeneral;
        this.objetivosEspecificos = objetivosEspecificos;
        this.codirectorId = codirectorId;
        this.estudiante1Id = estudiante1Id;
        this.estudiante2Id = estudiante2Id;
    }

    // Getters y Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(Modalidad modalidad) {
        this.modalidad = modalidad;
    }

    public String getObjetivoGeneral() {
        return objetivoGeneral;
    }

    public void setObjetivoGeneral(String objetivoGeneral) {
        this.objetivoGeneral = objetivoGeneral;
    }

    public List<String> getObjetivosEspecificos() {
        return objetivosEspecificos;
    }

    public void setObjetivosEspecificos(List<String> objetivosEspecificos) {
        this.objetivosEspecificos = objetivosEspecificos;
    }


    public Long getCodirectorId() {
        return codirectorId;
    }

    public void setCodirectorId(Long codirectorId) {
        this.codirectorId = codirectorId;
    }

    public Long getEstudiante1Id() {
        return estudiante1Id;
    }

    public void setEstudiante1Id(Long estudiante1Id) {
        this.estudiante1Id = estudiante1Id;
    }

    public Long getEstudiante2Id() {
        return estudiante2Id;
    }

    public void setEstudiante2Id(Long estudiante2Id) {
        this.estudiante2Id = estudiante2Id;
    }

    /**
     * Enum para modalidad del proyecto
     */
    public enum Modalidad {
        INVESTIGACION,
        PRACTICA_PROFESIONAL
    }
}

