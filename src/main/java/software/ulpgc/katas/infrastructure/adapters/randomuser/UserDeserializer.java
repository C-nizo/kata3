package software.ulpgc.katas.infrastructure.adapters.randomuser;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.List;

public class UserDeserializer {
    private final Gson gson = new Gson();

    public List<UserDto> deserialize(String json) {
        RandomUserResponse response =
                gson.fromJson(json, RandomUserResponse.class);

        if (!hasResults(response)) {
            throw new JsonParseException("La respuesta no contiene results");
        }

        return response.results();
    }

    private boolean hasResults(RandomUserResponse response) {
        return response != null && response.results() != null;
    }

    private record RandomUserResponse(List<UserDto> results) {
    }
}