package co.unicauca.gestiontrabajogrado.infrastructure.http;

import java.net.http.HttpClient;
import java.time.Duration;

public class HttpClientFactory {
    private static HttpClient instance;

    public static HttpClient getInstance() {
        if (instance == null) {
            instance = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
        }
        return instance;
    }
}