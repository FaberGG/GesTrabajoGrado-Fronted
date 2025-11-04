package co.unicauca.gestiontrabajogrado.application.controllers;

import co.unicauca.gestiontrabajogrado.domain.dto.identity.RegisterRequest;
import co.unicauca.gestiontrabajogrado.domain.dto.identity.UserProfile;
import co.unicauca.gestiontrabajogrado.infrastructure.exceptions.*;
import co.unicauca.gestiontrabajogrado.infrastructure.services.IdentityService;
import co.unicauca.gestiontrabajogrado.presentation.auth.RegisterView;

import javax.swing.SwingUtilities;

/**
 * Controlador para el registro de usuarios
 * Coordina entre RegisterView e IdentityService
 */
public class RegisterController {

    private final RegisterView view;
    private final IdentityService identityService;

    public RegisterController(RegisterView view) {
        this.view = view;
        this.identityService = new IdentityService();
    }

    /**
     * Maneja el intento de registro
     */
    public void handleRegister(String nombres, String apellidos,
                               String celular, String programa,
                               String rol, String email, String password) {

        // Validaciones básicas
        if (!validateFields(nombres, apellidos, programa, rol, email, password)) {
            return;
        }

        // Validar formato de email
        if (!email.endsWith("@unicauca.edu.co")) {
            view.showError("El email debe ser institucional (@unicauca.edu.co)");
            return;
        }

        // Validar contraseña
        if (!validatePassword(password)) {
            view.showError("La contraseña debe tener al menos 8 caracteres, " +
                    "una mayúscula, un número y un carácter especial");
            return;
        }

        // Mostrar loading
        view.setRegisterEnabled(false);
        view.showLoading("Registrando usuario...");

        // Ejecutar en hilo separado
        new Thread(() -> {
            try {
                // Crear el DTO de registro
                RegisterRequest request = new RegisterRequest(
                        nombres,
                        apellidos,
                        celular,
                        programa,
                        rol,
                        email,
                        password
                );

                // Llamar al servicio de identidad
                UserProfile registeredUser = identityService.register(request);

                // Actualizar UI en el hilo de Swing
                SwingUtilities.invokeLater(() -> {
                    view.hideLoading();
                    view.showSuccess(
                            "¡Registro exitoso!\n\n" +
                                    "Usuario: " + registeredUser.getNombreCompleto() + "\n" +
                                    "Email: " + registeredUser.getEmail() + "\n\n" +
                                    "Ya puede iniciar sesión."
                    );

                    // Navegar al login
                    navigateToLogin();
                });

            } catch (ValidationException e) {
                // Error de validación
                SwingUtilities.invokeLater(() -> {
                    view.hideLoading();
                    view.setRegisterEnabled(true);
                    view.showError("Datos inválidos: " + e.getMessage());
                });

            } catch (NetworkException e) {
                // Error de red
                SwingUtilities.invokeLater(() -> {
                    view.hideLoading();
                    view.setRegisterEnabled(true);
                    view.showError("Error de conexión: " + e.getMessage());
                });

            } catch (Exception e) {
                // Error inesperado
                SwingUtilities.invokeLater(() -> {
                    view.hideLoading();
                    view.setRegisterEnabled(true);
                    view.showError("Error inesperado: " + e.getMessage());
                });
            }
        }).start();
    }

    /**
     * Valida que los campos obligatorios no estén vacíos
     */
    private boolean validateFields(String nombres, String apellidos,
                                   String programa, String rol,
                                   String email, String password) {
        if (nombres == null || nombres.trim().isEmpty()) {
            view.showError("Los nombres son obligatorios");
            return false;
        }

        if (apellidos == null || apellidos.trim().isEmpty()) {
            view.showError("Los apellidos son obligatorios");
            return false;
        }

        if (programa == null || programa.equals("Programa *")) {
            view.showError("Debe seleccionar un programa");
            return false;
        }

        if (rol == null || rol.equals("Rol *")) {
            view.showError("Debe seleccionar un rol");
            return false;
        }

        if (email == null || email.trim().isEmpty()) {
            view.showError("El email es obligatorio");
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            view.showError("La contraseña es obligatoria");
            return false;
        }

        return true;
    }

    /**
     * Valida la contraseña según los requisitos del backend
     * - Mínimo 8 caracteres
     * - Al menos una mayúscula
     * - Al menos un número
     * - Al menos un carácter especial
     */
    private boolean validatePassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean hasUpperCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }

        return hasUpperCase && hasDigit && hasSpecialChar;
    }

    /**
     * Navega a la vista de login
     */
    public void handleNavigateToLogin() {
        navigateToLogin();
    }

    /**
     * Navega a la vista de login
     */
    private void navigateToLogin() {
        view.dispose();

        SwingUtilities.invokeLater(() -> {
            co.unicauca.gestiontrabajogrado.presentation.auth.LoginView loginView =
                    new co.unicauca.gestiontrabajogrado.presentation.auth.LoginView();

            co.unicauca.gestiontrabajogrado.application.controllers.LoginController loginController =
                    new co.unicauca.gestiontrabajogrado.application.controllers.LoginController(loginView);

            loginView.setController(loginController);
            loginView.setVisible(true);
        });
    }
}