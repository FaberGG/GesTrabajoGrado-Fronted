package co.unicauca.gestiontrabajogrado.infrastructure.http;

import java.net.http.HttpClient;
import java.time.Duration;

public class HttpClientFactory {
    private static HttpClient instance;

    public static HttpClient getInstance() {
        if (instance == null) {
            instance = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1) // Forzar HTTP/1.1 (como curl)
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
        }
        return instance;
    }
}