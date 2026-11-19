package software.ulpgc.katas.infrastructure.adapters.randomuser;

import com.google.gson.JsonParseException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

public class UserFetcher {
    private final String baseUrl;
    private final int count;
    private final UserDeserializer deserializer = new UserDeserializer();

    public UserFetcher(String baseUrl, int count) {
        this.baseUrl = baseUrl;
        this.count = count;
    }

    public List<UserDto> fetchUsers() {
        try (HttpClient client = HttpClient.newHttpClient()) {
            URI uri = createUri();
            HttpRequest request = createRequest(uri);
            HttpResponse<String> response = sendRequest(client, request);
            return deserializeResponse(response);
        }
    }

    private URI createUri() {
        String separator = baseUrl.endsWith("/") ? "" : "/";
        return URI.create(baseUrl + separator + "?results=" + count);
    }

    private static HttpRequest createRequest(URI uri) {
        return HttpRequest.newBuilder(uri)
                .GET()
                .timeout(Duration.ofSeconds(15))
                .build();
    }

    private static HttpResponse<String> sendRequest(HttpClient client, HttpRequest request) {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (HttpTimeoutException e) {
            throw new IllegalStateException("Tiempo de espera agotado", e);
        } catch (ConnectException e) {
            throw new IllegalStateException("Error de conexión", e);
        } catch (IOException e) {
            throw new IllegalStateException("Error de comunicación", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Petición interrumpida", e);
        }
    }

    private List<UserDto> deserializeResponse(HttpResponse<String> response) {
        checkStatus(response.statusCode());

        try {
            return deserializer.deserialize(response.body());
        } catch (JsonParseException e) {
            throw new IllegalStateException("Respuesta JSON inválida", e);
        }
    }

    private void checkStatus(int statusCode) {
        if (statusCode != 200) {
            throw new IllegalStateException("Error HTTP: " + statusCode);
        }
    }
}
