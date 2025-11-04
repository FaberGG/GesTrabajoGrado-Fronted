package co.unicauca.gestiontrabajogrado.domain.enums;


public enum Programa {
    INGENIERIA_DE_SISTEMAS("Ingeniería de Sistemas"),
    INGENIERIA_ELECTRONICA_Y_TELECOMUNICACIONES("Ingeniería Electrónica y Telecomunicaciones"),
    AUTOMATICA_INDUSTRIAL("Automática Industrial"),
    TECNOLOGIA_EN_TELEMATICA("Tecnología en Telemática");

    private final String displayName;

    Programa(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return name();
    }
}