package software.ulpgc.katas.verification;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import software.ulpgc.katas.architecture.model.User;
import software.ulpgc.katas.infrastructure.adapters.jsonfile.JsonUserSerializer;
import software.ulpgc.katas.infrastructure.adapters.jsonfile.JsonUserWriter;
import software.ulpgc.katas.infrastructure.adapters.randomuser.UserDeserializer;
import software.ulpgc.katas.infrastructure.adapters.randomuser.UserDto;
import software.ulpgc.katas.infrastructure.adapters.randomuser.UserTransformer;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ManualVerification {
    static void main() throws IOException {
        List<User> users = verifyValidInput();
        verifyInvalidRecords();
        verifyEmptyInput();
        verifyMalformedJson();
        verifySerialization(users);
        verifyFileOutput(users);

        System.out.println("Todas las verificaciones superadas.");
    }

    private static List<User> verifyValidInput() {
        List<User> users = transform("known_input.json");
        check(users.size() == 2, "Se esperaban dos usuarios válidos");
        System.out.println("Entrada válida: OK");
        return users;
    }

    private static void verifyInvalidRecords() {
        List<User> users = transform("mixed_input.json");
        check(users.size() == 2, "Debían descartarse tres de los cinco registros");
        check(users.get(0).name().equals("Ana Pérez"), "El primer usuario válido no coincide");
        check(users.get(1).name().equals("Luis Gómez"), "El segundo usuario válido no coincide");
        System.out.println("Registros inválidos descartados: OK");
    }

    private static void verifyEmptyInput() {
        List<User> users = transform("empty_input.json");
        JsonElement json = parse(new JsonUserSerializer().serialize(users));
        check(users.isEmpty(), "Se esperaba una lista vacía");
        check(json.getAsJsonObject().getAsJsonArray("users").isEmpty(),
                "El array users debería estar vacío");
        check(json.getAsJsonObject().getAsJsonObject("metadata").get("count").getAsInt() == 0,
                "El contador debería ser cero");
        System.out.println("Entrada vacía: OK");
    }

    private static void verifyMalformedJson() {
        try {
            transform("invalid_input.json");
        } catch (JsonParseException e) {
            System.out.println("JSON malformado detectado: OK");
            return;
        }
        throw new AssertionError("El JSON malformado debería provocar un error");
    }

    private static void verifySerialization(List<User> users) {
        JsonElement actual = parse(new JsonUserSerializer().serialize(users));
        JsonElement expected = parse(resource("known_expected.json"));
        check(actual.equals(expected), "La salida no coincide con el resultado esperado");
        System.out.println("JSON generado y reinterpretado: OK");
    }

    private static void verifyFileOutput(List<User> users) throws IOException {
        Path path = Files.createTempFile("kata3-verification-", ".json");
        try {
            new JsonUserWriter(path).write(users);
            JsonElement fromFile = parse(Files.readString(path, StandardCharsets.UTF_8));
            JsonElement fromMemory = parse(new JsonUserSerializer().serialize(users));
            check(fromFile.equals(fromMemory), "El contenido del fichero no coincide con el generado");
        } finally {
            Files.deleteIfExists(path);
        }
        System.out.println("Origen fichero vs memoria: OK");
    }

    private static List<User> transform(String fileName) {
        List<UserDto> dtos = new UserDeserializer().deserialize(resource(fileName));
        return new UserTransformer().transform(dtos);
    }

    private static JsonElement parse(String json) {
        return JsonParser.parseString(json);
    }

    private static void check(boolean valid, String message) {
        if (!valid) {
            throw new AssertionError(message);
        }
    }

    private static String resource(String name) {
        try (InputStream input = ManualVerification.class.getResourceAsStream("/verification/" + name)) {
            if (input == null) {
                throw new IllegalStateException("No se encontró el fichero: " + name);
            }
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer el fichero: " + name, e);
        }
    }
}
