package co.unicauca.gestiontrabajogrado.application.session;

import co.unicauca.gestiontrabajogrado.domain.dto.identity.UserProfile;
import lombok.Getter;

/**
 * Gestor de sesión del usuario actual
 * Singleton que almacena el perfil del usuario y el token JWT
 */
public class SessionManager {

    private static SessionManager instance;

    /**
     * -- GETTER --
     *  Obtiene el usuario actual
     */
    @Getter
    private UserProfile currentUser;
    private String jwtToken;

    private SessionManager() {
        // Constructor privado para Singleton
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }

    /**
     * Inicia sesión guardando el usuario y el token
     */
    public void login(UserProfile user, String token) {
        this.currentUser = user;
        this.jwtToken = token;
    }

    /**
     * Obtiene el usuario actual
     */
    public UserProfile getCurrentUser() {
        return currentUser;
    }

    /**
     * Cierra la sesión limpiando los datos
     */
    public void logout() {
        this.currentUser = null;
        this.jwtToken = null;
    }

    /**
     * Verifica si hay una sesión activa
     */
    public boolean isAuthenticated() {
        return currentUser != null && jwtToken != null;
    }

    /**
     * Obtiene el token JWT
     */
    public String getToken() {
        return jwtToken;
    }

    /**
     * Obtiene el ID del usuario actual
     */
    public Long getUserId() {
        return currentUser != null ? currentUser.getId() : null;
    }

    /**
     * Obtiene el rol del usuario actual
     */
    public String getUserRole() {
        return currentUser != null ? currentUser.getRol() : null;
    }

    /**
     * Obtiene el email del usuario actual
     */
    public String getUserEmail() {
        return currentUser != null ? currentUser.getEmail() : null;
    }
}