package gr.aueb.cf.agronitor.apiclient.responses;

import java.util.Objects;

/**
 * Represents the response the app receives from the api call after adding a new greenhouse.
 */
public class AddGreenhouseResponse {
    private String id;
    private String greenhouseName;
    private String userId;

//    Constructors
    public AddGreenhouseResponse() {}

    public AddGreenhouseResponse(String id, String greenhouseName, String userId) {
        this.id = id;
        this.greenhouseName = greenhouseName;
        this.userId = userId;
    }

//    Getters and Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getGreenhouseName() {
        return greenhouseName;
    }
    public void setGreenhouseName(String greenhouseName) {
        this.greenhouseName = greenhouseName;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

//    equals(), hasCode(), toString()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddGreenhouseResponse that = (AddGreenhouseResponse) o;
        return Objects.equals(id, that.id)
                && Objects.equals(greenhouseName, that.greenhouseName)
                && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, greenhouseName, userId);
    }

    @Override
    public String toString() {
        return "AddGreenhouseResponse{" +
                "id='" + id + '\'' +
                ", greenhouseName='" + greenhouseName + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
