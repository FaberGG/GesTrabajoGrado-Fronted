package co.unicauca.gestiontrabajogrado.domain.model;

public enum enumModalidad {
    INVESTIGACION(2),
    PRACTICA_PROFESIONAL(1);

    private final int maxEstudiantes;

    enumModalidad(int max) {
        this.maxEstudiantes = max;
    }

    public int getMaxEstudiantes() {
        return maxEstudiantes;
    }
}

