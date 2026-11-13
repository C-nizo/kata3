package software.ulpgc.katas.infrastructure.adapters;

import com.google.gson.*;
import software.ulpgc.katas.application.domain.model.User;
import software.ulpgc.katas.application.ports.UserWriter;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JsonUserWriter implements UserWriter {
    private final Path path;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JsonUserWriter(Path path) {
        this.path = path;
    }

    @Override
    public void write(List<User> users) {
        writeJson(createJson(users));
    }

    private JsonObject createJson(List<User> users) {
        JsonObject json = new JsonObject();

        json.add("users", createUsersArray(users));
        json.add("metadata", createMetadata(users.size()));

        return json;
    }

    private JsonArray createUsersArray(List<User> users) {
        JsonArray array = new JsonArray();

        for (User user: users) {
            array.add(createUserJson(user));
        }

        return array;
    }

    private JsonObject createUserJson(User user) {
        JsonObject json = new JsonObject();

        json.addProperty("name", user.name());
        json.addProperty("birthday", user.birthday().toString());
        json.addProperty("email", user.email());

        return json;
    }

    private JsonElement createMetadata(int count) {
        JsonObject metadata = new JsonObject();

        metadata.addProperty("count", count);

        return metadata;
    }

    private void writeJson(JsonObject json) {
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            gson.toJson(json, writer);
        } catch (IOException e) {
            throw new IllegalStateException("Error al escribir el JSON", e);
        }
    }
}
