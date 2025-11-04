package co.unicauca.gestiontrabajogrado;

import co.unicauca.gestiontrabajogrado.application.controllers.LoginController;
import co.unicauca.gestiontrabajogrado.presentation.auth.LoginView;

import javax.swing.*;

/**
 * Punto de entrada de la aplicación
 */
public class Main {

    public static void main(String[] args) {
        // Configurar Look and Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo configurar el Look and Feel: " + e.getMessage());
        }

        // Configurar variables de entorno si es necesario
        configureEnvironment();

        // Iniciar la aplicación en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Crear la vista de login
                LoginView loginView = new LoginView();

                // Crear el controlador
                LoginController loginController = new LoginController(loginView);

                // Conectar vista y controlador
                loginView.setController(loginController);

                // Mostrar la ventana
                loginView.setVisible(true);

                System.out.println("✅ Aplicación iniciada correctamente");

            } catch (Exception e) {
                System.err.println("❌ Error al iniciar la aplicación: " + e.getMessage());
                e.printStackTrace();

                JOptionPane.showMessageDialog(
                        null,
                        "Error al iniciar la aplicación:\n" + e.getMessage(),
                        "Error Crítico",
                        JOptionPane.ERROR_MESSAGE
                );

                System.exit(1);
            }
        });
    }

    /**
     * Configura variables de entorno
     */
    private static void configureEnvironment() {
        // Configurar la URL del API Gateway si no está definida
        if (System.getProperty("api.gateway.url") == null) {
            System.setProperty("api.gateway.url", "http://localhost:8080");
        }

        System.out.println("📡 API Gateway URL: " + System.getProperty("api.gateway.url"));
    }
}