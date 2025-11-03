package co.unicauca.gestiontrabajogrado.controller;

import co.unicauca.gestiontrabajogrado.domain.model.User;
import co.unicauca.gestiontrabajogrado.domain.model.enumRol;
import co.unicauca.gestiontrabajogrado.dto.identity.UserProfile;
import co.unicauca.gestiontrabajogrado.presentation.auth.LoginView;
import co.unicauca.gestiontrabajogrado.security.JwtSession;
import co.unicauca.gestiontrabajogrado.services.AuthService;

/**
 * Controlador para el manejo de login
 * Ahora integrado con AuthService y JwtSession
 */
public class LoginController {

    private static User currentUser;
    private final AuthService authService;
    private LoginView loginView;

    public LoginController() {
        this.authService = new AuthService();
    }

    public LoginController(LoginView loginView) {
        this();
        this.loginView = loginView;
    }

    /**
     * Intenta autenticar al usuario con el microservicio Identity
     *
     * @param email Email del usuario
     * @param password Contraseña
     * @param rememberMe (No implementado aún)
     * @return true si el login fue exitoso
     */
    public boolean login(String email, String password, boolean rememberMe) {
        try {
            boolean success = authService.login(email, password);

            if (success) {
                // Obtener el perfil de la sesión y crear User local
                UserProfile profile = JwtSession.getInstance().getProfile();
                currentUser = convertirUserProfileAUser(profile);
                System.out.println("✓ Login exitoso como " + profile.getRol());
                return true;
            } else {
                System.out.println("✗ Credenciales inválidas");
                return false;
            }
        } catch (Exception e) {
            System.err.println("✗ Error durante login: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Convierte UserProfile (del microservicio) a User (del dominio local)
     */
    private User convertirUserProfileAUser(UserProfile profile) {
        enumRol rol = enumRol.valueOf(profile.getRol());
        return new User(
            profile.getId(),
            profile.getNombres(),
            profile.getApellidos(),
            profile.getEmail(),
            rol
        );
    }

    /**
     * Maneja el proceso de login desde la vista
     */
    public void handleLogin(String email, String password, boolean rememberMe) {
        if (login(email, password, rememberMe)) {
            System.out.println("Login exitoso para: " + email);

            // Cerrar la vista de login
            if (loginView != null) {
                loginView.dispose();
            }

            // Abrir la vista correspondiente según el rol del usuario
            abrirVistaPorRol();
        } else {
            System.out.println("Login fallido para: " + email);
            javax.swing.JOptionPane.showMessageDialog(null,
                "Credenciales inválidas. Por favor verifique su email y contraseña.",
                "Error de Login",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Abre la vista correspondiente según el rol del usuario
     */
    private void abrirVistaPorRol() {
        if (currentUser == null) {
            System.err.println("Error: Usuario actual es null");
            return;
        }

        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                switch (currentUser.getRol()) {
                    case DOCENTE:
                        abrirVistaDocente();
                        break;
                    case ADMIN:
                        javax.swing.JOptionPane.showMessageDialog(null,
                            "Vista de Administrador en desarrollo",
                            "Información",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case ESTUDIANTE:
                        javax.swing.JOptionPane.showMessageDialog(null,
                            "Vista de Estudiante en desarrollo",
                            "Información",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        break;
                    default:
                        javax.swing.JOptionPane.showMessageDialog(null,
                            "Rol no reconocido",
                            "Error",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                System.err.println("Error al abrir vista: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Abre la vista del docente
     */
    private void abrirVistaDocente() {
        try {
            // Crear la vista del docente pasando el usuario actual
            // DocenteView ya es un JFrame, no necesita ser agregado a otro JFrame
            co.unicauca.gestiontrabajogrado.presentation.dashboard.docenteview.DocenteView docenteView =
                new co.unicauca.gestiontrabajogrado.presentation.dashboard.docenteview.DocenteView(currentUser);

            // DocenteView ya configura su propio título, tamaño y posición
            // Solo necesitamos hacerlo visible
            docenteView.setVisible(true);

            System.out.println("✓ Vista de Docente abierta exitosamente");
        } catch (Exception e) {
            System.err.println("✗ Error al abrir DocenteView: " + e.getMessage());
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null,
                "Error al abrir la vista del docente:\n" + e.getMessage(),
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Abre la vista de registro
     */
    public void handleRegistrarse() {
        System.out.println("Abriendo vista de registro...");
        try {
            co.unicauca.gestiontrabajogrado.presentation.auth.RegisterView registerView =
                new co.unicauca.gestiontrabajogrado.presentation.auth.RegisterView();
            registerView.setVisible(true);
        } catch (Exception e) {
            System.err.println("Error al abrir RegisterView: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el email guardado (si existe)
     */
    public String getRememberedEmail() {
        // TODO: Implementar carga desde preferencias
        return "";
    }

    /**
     * Cierra la sesión del usuario
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Obtiene el usuario actual autenticado
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Guarda las credenciales para recordar
     */
    public void saveCredentials(String email, String password) {
        // TODO: Implementar almacenamiento seguro de credenciales
    }

    /**
     * Carga las credenciales guardadas
     */
    public String[] loadCredentials() {
        // TODO: Implementar carga de credenciales
        return new String[]{"", ""};
    }
}
