package co.unicauca.gestiontrabajogrado;

import co.unicauca.gestiontrabajogrado.presentation.auth.LoginView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Punto de entrada de la aplicación - Versión de PRODUCCIÓN
 * Sistema de Gestión de Trabajos de Grado - Universidad del Cauca
 *
 * @version 2.0.0
 * @date Noviembre 2025
 */
public class MainProduction {

    private static final String APP_NAME = "Sistema de Gestión de Trabajos de Grado";
    private static final String APP_VERSION = "2.0.0";
    private static final String UNIVERSITY = "Universidad del Cauca";

    public static void main(String[] args) {
        // Imprimir banner de inicio
        printBanner();

        // Configurar propiedades del sistema
        configureSystemProperties();

        // Configurar Look and Feel
        configureLookAndFeel();

        // Cargar configuración
        loadConfiguration();

        // Verificar conectividad con el API Gateway
        if (!verifyGatewayConnection()) {
            showConnectionErrorAndExit();
            return;
        }

        // Iniciar la aplicación en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Mostrar splash screen (opcional)
                showSplashScreen();
                // Crear la vista de login
                LoginView loginView = new LoginView();

                // Crear el controlador
                co.unicauca.gestiontrabajogrado.application.controllers.LoginController loginController =
                        new co.unicauca.gestiontrabajogrado.application.controllers.LoginController(loginView);

                // Conectar vista y controlador
                loginView.setController(loginController);

                // Mostrar la ventana
                loginView.setVisible(true);

                System.out.println("Aplicación iniciada correctamente");
                System.out.println("Modo: PRODUCCIÓN");
                System.out.println("API Gateway: " + System.getProperty("api.gateway.url"));
                System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

            } catch (Exception e) {
                System.err.println("Error crítico al iniciar la aplicación");
                e.printStackTrace();

                JOptionPane.showMessageDialog(
                        null,
                        "Error al iniciar la aplicación:\n\n" + e.getMessage() +
                                "\n\nPor favor contacte al administrador del sistema.",
                        "Error Crítico - " + APP_NAME,
                        JOptionPane.ERROR_MESSAGE
                );

                System.exit(1);
            }
        });
    }

    /**
     * Imprime el banner de inicio en consola
     */
    private static void printBanner() {
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("  " + APP_NAME);
        System.out.println("  " + UNIVERSITY);
        System.out.println("  Versión: " + APP_VERSION);
        System.out.println("  Arquitectura: Microservicios");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println();
    }

    /**
     * Configura propiedades del sistema
     */
    private static void configureSystemProperties() {
        // Configurar encoding por defecto
        System.setProperty("file.encoding", "UTF-8");

        // Habilitar anti-aliasing para mejor renderizado de fuentes
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Configurar locale a español
        System.setProperty("user.language", "es");
        System.setProperty("user.country", "CO");

        // Configurar tamaño de buffer para HTTP
        System.setProperty("http.keepAlive", "true");
        System.setProperty("http.maxConnections", "5");
    }

    /**
     * Configura el Look and Feel de la aplicación
     */
    private static void configureLookAndFeel() {
        try {
            // Intentar usar FlatLaf si está disponible (moderno)
            try {
                Class.forName("com.formdev.flatlaf.FlatLightLaf");
                UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
                System.out.println("✅ Look and Feel: FlatLaf (Moderno)");
                return;
            } catch (ClassNotFoundException e) {
                // FlatLaf no disponible, usar alternativa
            }

            // Usar Nimbus si está disponible
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    System.out.println("✅ Look and Feel: Nimbus");
                    return;
                }
            }

            // Fallback: usar el del sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.out.println("✅ Look and Feel: Sistema");

        } catch (Exception e) {
            System.err.println("⚠️ No se pudo configurar Look and Feel, usando por defecto");
        }
    }

    /**
     * Carga la configuración desde application.properties
     */
    private static void loadConfiguration() {
        Properties props = new Properties();

        try (InputStream input = MainProduction.class.getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input != null) {
                props.load(input);

                // Cargar URL del API Gateway
                String gatewayUrl = props.getProperty("api.gateway.url");
                if (gatewayUrl != null && !gatewayUrl.trim().isEmpty()) {
                    System.setProperty("api.gateway.url", gatewayUrl);
                    System.out.println("✅ Configuración cargada desde application.properties");
                    return;
                }
            }

        } catch (IOException e) {
            System.err.println("⚠️ No se pudo cargar application.properties: " + e.getMessage());
        }

        // Configuración por defecto si no se pudo cargar
        if (System.getProperty("api.gateway.url") == null) {
            // Producción: usar URL del servidor
            String defaultUrl = "http://localhost:8080";
            System.setProperty("api.gateway.url", defaultUrl);
            System.out.println("⚠️ Usando configuración por defecto: " + defaultUrl);
        }
    }

    /**
     * Verifica la conectividad con el API Gateway
     */
    private static boolean verifyGatewayConnection() {
        String gatewayUrl = System.getProperty("api.gateway.url");

        System.out.println("🔍 Verificando conexión con API Gateway...");
        System.out.println("   URL: " + gatewayUrl);

        try {
            // Intentar hacer un ping simple (timeout de 5 segundos)
            java.net.URL url = new java.net.URL(gatewayUrl + "/actuator/health");
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();

            if (responseCode == 200 || responseCode == 404) {
                // 200 = OK, 404 = Gateway existe pero endpoint no (aceptable)
                System.out.println("✅ Conexión exitosa con API Gateway");
                return true;
            } else {
                System.err.println("⚠️ API Gateway respondió con código: " + responseCode);
                return askUserToContinue();
            }

        } catch (java.net.UnknownHostException e) {
            System.err.println("❌ No se pudo resolver el host del API Gateway");
            return askUserToContinue();

        } catch (java.net.ConnectException e) {
            System.err.println("❌ No se pudo conectar al API Gateway (¿está corriendo?)");
            return askUserToContinue();

        } catch (java.net.SocketTimeoutException e) {
            System.err.println("❌ Timeout al conectar con API Gateway");
            return askUserToContinue();

        } catch (Exception e) {
            System.err.println("⚠️ Error al verificar conexión: " + e.getMessage());
            return askUserToContinue();
        }
    }

    /**
     * Pregunta al usuario si desea continuar sin conexión al Gateway
     */
    private static boolean askUserToContinue() {
        int result = JOptionPane.showConfirmDialog(
                null,
                "No se pudo conectar al API Gateway.\n\n" +
                        "URL configurada: " + System.getProperty("api.gateway.url") + "\n\n" +
                        "Esto puede deberse a:\n" +
                        "• El servidor no está corriendo\n" +
                        "• La URL es incorrecta\n" +
                        "• Problemas de red\n\n" +
                        "¿Desea continuar de todas formas?",
                "Advertencia de Conexión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        return result == JOptionPane.YES_OPTION;
    }

    /**
     * Muestra un error de conexión y cierra la aplicación
     */
    private static void showConnectionErrorAndExit() {
        JOptionPane.showMessageDialog(
                null,
                "No se pudo establecer conexión con el servidor.\n\n" +
                        "Por favor verifique:\n" +
                        "1. El API Gateway está corriendo\n" +
                        "2. La URL está correctamente configurada\n" +
                        "3. No hay problemas de red\n\n" +
                        "Contacte al administrador del sistema si el problema persiste.",
                "Error de Conexión - " + APP_NAME,
                JOptionPane.ERROR_MESSAGE
        );

        System.exit(1);
    }

    /**
     * Muestra un splash screen mientras carga la aplicación
     */
    private static void showSplashScreen() {
        // Crear splash screen simple
        JWindow splash = new JWindow();
        splash.setLayout(new BorderLayout());

        // Panel con información
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 255, 255));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185), 2),
                BorderFactory.createEmptyBorder(30, 50, 30, 50)
        ));

        // Logo/Título
        JLabel lblTitle = new JLabel(APP_NAME);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(41, 128, 185));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUniversity = new JLabel(UNIVERSITY);
        lblUniversity.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUniversity.setForeground(new Color(52, 73, 94));
        lblUniversity.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblVersion = new JLabel("Versión " + APP_VERSION);
        lblVersion.setFont(new Font("Arial", Font.PLAIN, 12));
        lblVersion.setForeground(new Color(127, 140, 141));
        lblVersion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblLoading = new JLabel("Cargando...");
        lblLoading.setFont(new Font("Arial", Font.ITALIC, 12));
        lblLoading.setForeground(new Color(149, 165, 166));
        lblLoading.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblUniversity);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblVersion);
        panel.add(Box.createVerticalStrut(20));
        panel.add(lblLoading);

        splash.add(panel, BorderLayout.CENTER);
        splash.pack();
        splash.setLocationRelativeTo(null);
        splash.setVisible(true);

        // Cerrar splash después de 2 segundos
        Timer timer = new Timer(2000, e -> splash.dispose());
        timer.setRepeats(false);
        timer.start();
    }
}

