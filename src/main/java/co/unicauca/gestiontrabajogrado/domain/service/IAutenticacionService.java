package co.unicauca.gestiontrabajogrado.domain.service;

import co.unicauca.gestiontrabajogrado.domain.model.User;

public interface IAutenticacionService {
    boolean autenticar(String email, String password);
    void cerrarSesion();
    User register(User user, String plainPassword);
    User login(String email, String plainPassword);
}

