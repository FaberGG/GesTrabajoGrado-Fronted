package co.unicauca.gestiontrabajogrado.domain.dto.identity;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    private String nombres;
    private String apellidos;

    @SerializedName("celular")
    private Long celularNumerico;  // Cambiado a Long para que se serialice como número

    private String programa;
    private String rol;
    private String email;
    private String password;

    // Constructor, getters, setters
    public RegisterRequest() {}

    public RegisterRequest(String nombres, String apellidos,
                           String celular, String programa,
                           String rol, String email, String password) {
        this.nombres = nombres;
        this.apellidos = apellidos;

        // Convertir celular String a Long (puede ser null si está vacío)
        if (celular != null && !celular.trim().isEmpty()) {
            try {
                this.celularNumerico = Long.parseLong(celular.trim());
            } catch (NumberFormatException e) {
                this.celularNumerico = null;
            }
        } else {
            this.celularNumerico = null;
        }

        this.programa = programa;
        this.rol = rol;
        this.email = email;
        this.password = password;
    }

    // Getters y setters...
    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Long getCelularNumerico() {
        return celularNumerico;
    }

    public void setCelularNumerico(Long celularNumerico) {
        this.celularNumerico = celularNumerico;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}