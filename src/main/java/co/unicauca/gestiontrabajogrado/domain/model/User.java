package co.unicauca.gestiontrabajogrado.domain.model;

/**
 * Modelo de dominio para representar un usuario del sistema
 */
public class User {
    private Integer id;
    private String nombres;
    private String apellidos;
    private String email;
    private String celular;
    private enumProgram programa;
    private enumRol rol;

    public User() {
    }

    public User(Integer id, String nombres, String apellidos, String email, String celular, enumProgram programa, enumRol rol) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.celular = celular;
        this.programa = programa;
        this.rol = rol;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public enumProgram getPrograma() {
        return programa;
    }

    public void setPrograma(enumProgram programa) {
        this.programa = programa;
    }

    public enumRol getRol() {
        return rol;
    }

    public void setRol(enumRol rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", email='" + email + '\'' +
                ", programa=" + programa +
                ", rol=" + rol +
                '}';
    }
}
