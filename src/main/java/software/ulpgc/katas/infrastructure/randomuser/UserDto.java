package software.ulpgc.katas.infrastructure.randomuser;

public record UserDto(Name name, Dob dob, String email) {
    record Name(String first, String last) {
    }

    record Dob(String date) {
    }
}
