package software.ulpgc.katas.infrastructure.randomuser;

import com.google.gson.Gson;

import java.io.Reader;
import java.util.List;

public class UserParser {
    private final Gson gson = new Gson();

    public List<UserDto> parse(Reader reader) {
        RandomUserResponse response = gson.fromJson(reader, RandomUserResponse.class);

        if (!hasResults(response)) return List.of();

        return response.results();
    }

    private static boolean hasResults(RandomUserResponse response) {
        return response != null && response.results() != null;
    }

    private record RandomUserResponse(List<UserDto> results) {
    }
}
