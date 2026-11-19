package software.ulpgc.katas.infrastructure.adapters.jsonfile;

import com.google.gson.*;
import software.ulpgc.katas.architecture.model.User;

import java.util.List;

public class JsonUserSerializer {
    private final String source;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JsonUserSerializer(String source) {
        this.source = source;
    }

    public String serialize(List<User> users) {
        return gson.toJson(createJson(users));
    }

    private JsonObject createJson(List<User> users) {
        JsonObject json = new JsonObject();

        json.add("users", createUsersArray(users));
        json.add("metadata", createMetadata(users.size()));

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

    private JsonObject createMetadata(int count) {
        JsonObject metadata = new JsonObject();
        metadata.addProperty("count", count);
        metadata.addProperty("source", source);

        return metadata;
    }
}
