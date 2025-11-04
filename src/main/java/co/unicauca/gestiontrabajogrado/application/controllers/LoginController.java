package co.unicauca.gestiontrabajogrado.application.controllers;

import co.unicauca.gestiontrabajogrado.application.session.SessionManager;
import co.unicauca.gestiontrabajogrado.domain.dto.identity.LoginResponse;
import co.unicauca.gestiontrabajogrado.infrastructure.exceptions.*;
import co.unicauca.gestiontrabajogrado.infrastructure.services.IdentityService;
import co.unicauca.gestiontrabajogrado.presentation.auth.LoginView;

import javax.swing.SwingUtilities;

/**
 * Controlador para el login
 * Coordina entre LoginView e IdentityService
 */
public class LoginController {

    private final LoginView view;
    private final IdentityService identityService;
    private final SessionManager sessionManager;

    public LoginController(LoginView view) {
        this.view = view;
        this.identityService = new IdentityService();
        this.sessionManager = SessionManager.getInstance();
    }

    /**
     * Maneja el intento de login
     */
    public void handleLogin(String email, String password) {
        // Validaciones básicas en el controllers
        if (email == null || email.trim().isEmpty()) {
            view.showError("El email es obligatorio");
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            view.showError("La contraseña es obligatoria");
            return;
        }

        // Mostrar loading
        view.setLoginEnabled(false);
        view.showLoading("Autenticando...");

        // Ejecutar en hilo separado para no bloquear la UI
        new Thread(() -> {
            try {
                // Llamar al servicio de identidad
                LoginResponse response = identityService.login(email, password);

                // Guardar la sesión
                sessionManager.login(response.getUser(), response.getToken());

                // Actualizar UI en el hilo de Swing
                SwingUtilities.invokeLater(() -> {
                    view.hideLoading();
                    view.showSuccess("Login exitoso");

                    // Navegar al dashboard según el rol
                    navigateToDashboard();
                });

            } catch (AuthenticationException e) {
                // Error de autenticación (credenciales inválidas)
                SwingUtilities.invokeLater(() -> {
                    view.hideLoading();
                    view.setLoginEnabled(true);
                    view.showError("Credenciales inválidas");
                });

            } catch (NetworkException e) {
                // Error de red
                SwingUtilities.invokeLater(() -> {
                    view.hideLoading();
                    view.setLoginEnabled(true);
                    view.showError("Error de conexión: " + e.getMessage());
                });

            } catch (Exception e) {
                // Error inesperado
                SwingUtilities.invokeLater(() -> {
                    view.hideLoading();
                    view.setLoginEnabled(true);
                    view.showError("Error inesperado: " + e.getMessage());
                });
            }
        }).start();
    }

    /**
     * Navega al dashboard según el rol del usuario
     */
    private void navigateToDashboard() {
        String rol = sessionManager.getUserRole();

        view.dispose(); // Cerrar la vista de login

        // NOTA: Estos dashboards se implementarán en la Fase 2
        switch (rol) {
            case "ESTUDIANTE":
                openEstudianteDashboard();
                break;
            case "DOCENTE":
                openDocenteDashboard();
                break;
            case "COORDINADOR":
                openCoordinadorDashboard();
                break;
            case "JEFE_DEPARTAMENTO":
                openJefeDepartamentoDashboard();
                break;
            default:
                view.showError("Rol no reconocido: " + rol);
        }
    }

    // Métodos para abrir dashboards según rol

    private void openEstudianteDashboard() {
        SwingUtilities.invokeLater(() -> {
            try {
                co.unicauca.gestiontrabajogrado.presentation.dashboard.estudiante.EstudianteView estudianteView =
                        new co.unicauca.gestiontrabajogrado.presentation.dashboard.estudiante.EstudianteView();

                co.unicauca.gestiontrabajogrado.application.controllers.EstudianteController estudianteController =
                        new co.unicauca.gestiontrabajogrado.application.controllers.EstudianteController(estudianteView);

                estudianteView.setController(estudianteController);
                estudianteView.setVisible(true);

                System.out.println("✅ Dashboard de Estudiante abierto");

            } catch (Exception e) {
                System.err.println("❌ Error al abrir EstudianteView: " + e.getMessage());
                e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(null,
                        "Error al abrir dashboard de estudiante:\n" + e.getMessage(),
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void openDocenteDashboard() {
        SwingUtilities.invokeLater(() -> {
            try {
                co.unicauca.gestiontrabajogrado.presentation.dashboard.docenteview.DocenteView docenteView =
                        new co.unicauca.gestiontrabajogrado.presentation.dashboard.docenteview.DocenteView();

                docenteView.setVisible(true);

                System.out.println("✅ Dashboard de Docente abierto");

            } catch (Exception e) {
                System.err.println("❌ Error al abrir DocenteView: " + e.getMessage());
                e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(null,
                        "Error al abrir dashboard de docente:\n" + e.getMessage(),
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void openCoordinadorDashboard() {
        SwingUtilities.invokeLater(() -> {
            try {
                co.unicauca.gestiontrabajogrado.presentation.dashboard.coordinadorview.CoordinadorView coordinadorView =
                        new co.unicauca.gestiontrabajogrado.presentation.dashboard.coordinadorview.CoordinadorView();

                coordinadorView.setVisible(true);

                System.out.println("✅ Dashboard de Coordinador abierto");

            } catch (Exception e) {
                System.err.println("❌ Error al abrir CoordinadorView: " + e.getMessage());
                e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(null,
                        "Error al abrir dashboard de coordinador:\n" + e.getMessage(),
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void openJefeDepartamentoDashboard() {
        SwingUtilities.invokeLater(() -> {
            try {
                co.unicauca.gestiontrabajogrado.presentation.dashboard.jefedepartamentoview.JefeDepartamentoView jefeView =
                        new co.unicauca.gestiontrabajogrado.presentation.dashboard.jefedepartamentoview.JefeDepartamentoView();

                jefeView.setVisible(true);

                System.out.println("✅ Dashboard de Jefe de Departamento abierto");

            } catch (Exception e) {
                System.err.println("❌ Error al abrir JefeDepartamentoView: " + e.getMessage());
                e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(null,
                        "Error al abrir dashboard de jefe de departamento:\n" + e.getMessage(),
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Navega a la vista de registro
     */
    public void handleNavigateToRegister() {
        view.dispose();

        SwingUtilities.invokeLater(() -> {
            co.unicauca.gestiontrabajogrado.presentation.auth.RegisterView registerView =
                    new co.unicauca.gestiontrabajogrado.presentation.auth.RegisterView();

            co.unicauca.gestiontrabajogrado.application.controllers.RegisterController registerController =
                    new co.unicauca.gestiontrabajogrado.application.controllers.RegisterController(registerView);

            registerView.setController(registerController);
            registerView.setVisible(true);
        });
    }
}