package co.unicauca.gestiontrabajogrado.domain.enums;

public enum Rol {
    ESTUDIANTE,
    DOCENTE,
    COORDINADOR,
    JEFE_DEPARTAMENTO,
    ADMIN;

    @Override
    public String toString() {
        return name();
    }
}