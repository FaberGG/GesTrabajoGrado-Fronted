package co.unicauca.gestiontrabajogrado.domain.model;

public class User {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private enumRol rol;

    public User() {
    }

    public User(Long id, String nombre, String apellido, String email, enumRol rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public enumRol getRol() {
        return rol;
    }

    public void setRol(enumRol rol) {
        this.rol = rol;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}

