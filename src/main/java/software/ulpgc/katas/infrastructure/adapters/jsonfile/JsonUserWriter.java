package software.ulpgc.katas.infrastructure.adapters.jsonfile;

import software.ulpgc.katas.architecture.model.User;
import software.ulpgc.katas.architecture.ports.UserWriter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JsonUserWriter implements UserWriter {
    private final Path path;
    private final JsonUserSerializer serializer = new JsonUserSerializer();

    public JsonUserWriter(Path path) {
        this.path = path;
    }

    @Override
    public void write(List<User> users) {
        writeJson(serializer.serialize(users));
    }

    private void writeJson(String json) {
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write(json);
        } catch (IOException e) {
            throw new UncheckedIOException("Error al escribir el JSON", e);
        }
    }
}