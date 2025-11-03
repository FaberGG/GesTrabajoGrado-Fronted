package co.unicauca.gestiontrabajogrado.security;

import co.unicauca.gestiontrabajogrado.dto.identity.UserProfile;

/**
 * Gestiona la sesión del usuario autenticado (JWT + perfil)
 * Singleton para acceso global en la aplicación
 */
public class JwtSession {

    private static JwtSession instance;

    private String token;
    private UserProfile profile;

    private JwtSession() {
    }

    public static synchronized JwtSession getInstance() {
        if (instance == null) {
            instance = new JwtSession();
        }
        return instance;
    }

    /**
     * Establece el token y perfil tras un login exitoso
     */
    public void login(String token, UserProfile profile) {
        this.token = token;
        this.profile = profile;
    }

    /**
     * Limpia la sesión (logout)
     */
    public void logout() {
        this.token = null;
        this.profile = null;
    }

    /**
     * Verifica si hay un usuario autenticado
     */
    public boolean isLoggedIn() {
        return token != null && profile != null;
    }

    /**
     * Obtiene el token JWT
     */
    public String getToken() {
        return token;
    }

    /**
     * Obtiene el perfil del usuario
     */
    public UserProfile getProfile() {
        return profile;
    }

    /**
     * Obtiene el rol del usuario actual
     */
    public String getRol() {
        return profile != null ? profile.getRol() : null;
    }

    /**
     * Verifica si el usuario tiene un rol específico
     */
    public boolean hasRole(String rol) {
        return profile != null && rol.equals(profile.getRol());
    }

    /**
     * Obtiene el ID del usuario actual
     */
    public Long getUserId() {
        return profile != null ? profile.getId() : null;
    }
}

