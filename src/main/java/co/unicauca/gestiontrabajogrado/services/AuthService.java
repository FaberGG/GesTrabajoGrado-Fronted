package co.unicauca.gestiontrabajogrado.services;

import co.unicauca.gestiontrabajogrado.config.AppConfig;
import co.unicauca.gestiontrabajogrado.dto.identity.LoginRequest;
import co.unicauca.gestiontrabajogrado.dto.identity.LoginResponse;
import co.unicauca.gestiontrabajogrado.dto.identity.UserProfile;
import co.unicauca.gestiontrabajogrado.net.GatewayHttpClient;
import co.unicauca.gestiontrabajogrado.security.JwtSession;

import java.io.IOException;

/**
 * Servicio de autenticación con Identity microservice
 */
public class AuthService {

    private final GatewayHttpClient httpClient;

    public AuthService() {
        this.httpClient = new GatewayHttpClient(AppConfig.BASE_URL);
    }

    /**
     * Realiza login con email y password
     * Si es exitoso, guarda el token y obtiene el perfil
     *
     * @param email Email del usuario
     * @param password Contraseña
     * @return true si el login fue exitoso, false en caso contrario
     */
    public boolean login(String email, String password) throws IOException, InterruptedException {
        try {
            // 1. Llamar al endpoint de login
            LoginRequest loginRequest = new LoginRequest(email, password);
            LoginResponse loginResponse = httpClient.postJson(
                AppConfig.IDENTITY_LOGIN_PATH,
                loginRequest,
                LoginResponse.class,
                null // Sin token aún
            );

            if (loginResponse == null || loginResponse.getToken() == null) {
                return false;
            }

            String token = loginResponse.getToken();

            // 2. Obtener el perfil del usuario con el token
            UserProfile profile = httpClient.getJson(
                AppConfig.IDENTITY_PROFILE_PATH,
                UserProfile.class,
                token
            );

            if (profile == null) {
                return false;
            }

            // 3. Guardar en sesión
            JwtSession.getInstance().login(token, profile);

            System.out.println("✓ Login exitoso: " + profile.getNombreCompleto() + " (" + profile.getRol() + ")");
            return true;

        } catch (IOException e) {
            System.err.println("✗ Error en login: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Verifica si el token actual es válido
     */
    public boolean verifyToken() throws IOException, InterruptedException {
        JwtSession session = JwtSession.getInstance();
        if (!session.isLoggedIn()) {
            return false;
        }

        try {
            // Intenta obtener el perfil con el token actual
            UserProfile profile = httpClient.getJson(
                AppConfig.IDENTITY_PROFILE_PATH,
                UserProfile.class,
                session.getToken()
            );
            return profile != null;
        } catch (IOException e) {
            // Si falla, el token probablemente expiró
            session.logout();
            return false;
        }
    }

    /**
     * Cierra la sesión actual
     */
    public void logout() {
        JwtSession.getInstance().logout();
        System.out.println("✓ Sesión cerrada");
    }
}
