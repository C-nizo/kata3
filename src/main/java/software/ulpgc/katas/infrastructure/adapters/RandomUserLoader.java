package software.ulpgc.katas.infrastructure.adapters;

import software.ulpgc.katas.application.domain.model.User;
import software.ulpgc.katas.application.ports.UserLoader;
import software.ulpgc.katas.infrastructure.randomuser.UserDto;
import software.ulpgc.katas.infrastructure.randomuser.UserMapper;
import software.ulpgc.katas.infrastructure.randomuser.UserParser;
import software.ulpgc.katas.infrastructure.randomuser.UserValidator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RandomUserLoader implements UserLoader {
    private final int size;
    private final UserParser parser = new UserParser();
    private final UserValidator validator = new UserValidator();
    private final UserMapper mapper = new UserMapper();

    public RandomUserLoader(int size) {
        this.size = size;
    }

    @Override
    public List<User> load() {
        return transformUsers(fetchUsers());
    }

    private List<UserDto> fetchUsers() {
        try (HttpClient client = HttpClient.newHttpClient()) {
            return parseResponse(sendRequest(client, createRequest(createUri())));
        }
    }

    private URI createUri() {
        return URI.create("https://randomuser.me/api/?results=" + size);
    }

    private static HttpRequest createRequest(URI uri) {
        return HttpRequest.newBuilder(uri)
                .GET()
                .build();
    }

    private static HttpResponse<InputStream> sendRequest(HttpClient client, HttpRequest request) {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        } catch (IOException e) {
            throw new IllegalStateException("Error de comunicación", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Petición interrumpida", e);
        }
    }

    private List<UserDto> parseResponse(HttpResponse<InputStream> response) {
        try (Reader reader = new InputStreamReader(response.body(), StandardCharsets.UTF_8)) {
            checkStatus(response.statusCode());
            return parser.parse(reader);
        } catch (IOException e) {
            throw new IllegalStateException("Error de lectura", e);
        }
    }

    private void checkStatus(int statusCode) {
        if (statusCode != 200) {
            throw new IllegalStateException("Error HTTP: " + statusCode);
        }
    }

    private List<User> transformUsers(List<UserDto> userDtos) {
        List<User> users = new ArrayList<>();

        for (UserDto dto : userDtos) {
            if (validator.isValid(dto)) {
                users.add(mapper.map(dto));
            }
        }

        return users;
    }
}
