package software.ulpgc.katas.infrastructure.adapters;

import com.google.gson.*;
import software.ulpgc.katas.application.domain.model.User;
import software.ulpgc.katas.application.ports.UserOutput;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JsonUserOutput implements UserOutput {
    private final Path path;
    private final String source;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JsonUserOutput(Path path, String source) {
        this.path = path;
        this.source = source;
    }

    @Override
    public void write(List<User> users) {
        writeJson(createJson(users));
    }

    private JsonObject createJson(List<User> users) {
        JsonObject json = new JsonObject();

        json.add("users", createUsersArray(users));
        json.add("metadata", createMetadata(users.size(), source));

        return json;
    }

    private JsonArray createUsersArray(List<User> users) {
        JsonArray array = new JsonArray();

        for (User user : users) {
            array.add(createUserJson(user));
        }

        return array;
    }

    private JsonObject createUserJson(User user) {
        JsonObject json = new JsonObject();

        json.addProperty("name", user.name());
        json.addProperty("birthday", user.birthday().toString());

        return json;
    }

    private JsonElement createMetadata(int count, String source) {
        JsonObject metadata = new JsonObject();

        metadata.addProperty("count", count);
        metadata.addProperty("source", source);

        return metadata;
    }

    private void writeJson(JsonObject json) {
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            gson.toJson(json, writer);
        } catch (IOException e) {
            throw new UncheckedIOException("Error al escribir el JSON", e);
        }
    }
}
