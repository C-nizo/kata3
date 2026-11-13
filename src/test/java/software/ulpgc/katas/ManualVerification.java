
package software.ulpgc.katas;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import software.ulpgc.katas.application.domain.model.User;
import software.ulpgc.katas.infrastructure.adapters.JsonUserWriter;
import software.ulpgc.katas.infrastructure.randomuser.*;

import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ManualVerification {
    private static final String VALID = """
            {"results":[
              {"name":{"first":"Ana","last":"García"},
               "dob":{"date":"2000-05-15T10:30:00Z"},
               "email":"ana@example.com"},
              {"name":{"first":"Luis","last":"Pérez"},
               "dob":{"date":"1995-01-03T00:00:00Z"},
               "email":"luis@example.com"}
            ]}
            """;

    private static final String MIXED = """
            {"results":[
              {"name":{"first":"Ana","last":"García"},
               "dob":{"date":"2000-05-15T10:30:00Z"},
               "email":"ana@example.com"},
              {"name":{"first":"Luis","last":"Pérez"},
               "dob":{"date":"1995-01-03T00:00:00Z"},
               "email":"luis@example.com"},
              {"name":{"first":"","last":"López"},
               "dob":{"date":"2000-01-01T00:00:00Z"},
               "email":"juan@example.com"},
              {"name":{"first":"Eva","last":"Ruiz"},
               "dob":{"date":"fecha-incorrecta"},
               "email":"eva@example.com"},
              {"name":{"first":"Mario","last":"Díaz"},
               "dob":{"date":"2000-01-01T00:00:00Z"},
               "email":"correo-invalido"}
            ]}
            """;

    static void main() throws Exception {
        List<User> valid = process(new StringReader(VALID));
        check(valid.size() == 2, "Entrada válida");
        System.out.println("Entrada válida: OK");

        List<User> mixed = process(new StringReader(MIXED));
        check(mixed.size() == 2, "Registros inválidos");
        System.out.println("Registros inválidos descartados: OK");

        List<User> empty = process(new StringReader("{\"results\":[]}"));
        check(empty.isEmpty(), "Entrada vacía");
        System.out.println("Entrada vacía: OK");

        boolean rejected = false;
        try {
            process(new StringReader("{\"results\": [}"));
        } catch (JsonParseException e) {
            rejected = true;
        }
        check(rejected, "JSON malformado");
        System.out.println("JSON malformado detectado: OK");

        Path output = Files.createTempFile("kata3-output-", ".json");
        try {
            new JsonUserWriter(output).write(valid);

            try (Reader reader = Files.newBufferedReader(output)) {
                JsonObject json = JsonParser.parseReader(reader)
                        .getAsJsonObject();

                check(json.getAsJsonArray("users").size() == 2,
                        "Usuarios serializados");
                check(json.getAsJsonObject("metadata")
                                .get("count").getAsInt() == 2,
                        "Metadatos");
                check(json.getAsJsonArray("users")
                                .get(0).getAsJsonObject()
                                .get("email").getAsString().equals("ana@example.com"),
                        "Correo serializado");
            }

            System.out.println("JSON generado y reinterpretado: OK");
        } finally {
            Files.deleteIfExists(output);
        }

        Path input = Files.createTempFile("kata3-input-", ".json");
        try {
            Files.writeString(input, VALID, StandardCharsets.UTF_8);

            try (Reader reader = Files.newBufferedReader(input)) {
                check(process(reader).equals(valid),
                        "Origen fichero frente a memoria");
            }

            System.out.println("Origen fichero vs memoria: OK");
        } finally {
            Files.deleteIfExists(input);
        }

        System.out.println("Todas las verificaciones superadas.");
    }

    private static List<User> process(Reader reader) {
        UserParser parser = new UserParser();
        UserValidator validator = new UserValidator();
        UserMapper mapper = new UserMapper();

        List<User> users = new ArrayList<>();

        for (UserDto dto : parser.parse(reader)) {
            if (validator.isValid(dto)) {
                users.add(mapper.map(dto));
            }
        }

        return users;
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("Falló: " + message);
        }
    }
}