package gr.aueb.cf.agronitor.apiclient.responses;

import java.util.Objects;

/**
 * Represents the response of the api after registering a new user.
 */
public class RegisterResponse {
    private Long id;

//    Constructors
    public RegisterResponse() {}

    public RegisterResponse(Long id) {
        this.id = id;
    }

//    Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    equals(), hasCode(), toString()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterResponse that = (RegisterResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "RegisterResponse{" +
                "id=" + id +
                '}';
    }
}
