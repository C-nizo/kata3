package software.ulpgc.katas.infrastructure.adapters.randomuser;

public record UserDto(Name name, Dob dob, String email) {

    public record Name(String first, String last) {
    }

    public record Dob(String date) {
    }
}
