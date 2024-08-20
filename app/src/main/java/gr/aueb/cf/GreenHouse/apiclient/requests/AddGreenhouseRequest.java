package gr.aueb.cf.agronitor.apiclient.requests;

import java.util.Objects;

/**
 * Represents the object used for the api call to insert a new greenhouse
 */
public class AddGreenhouseRequest {
    private String greenhouseName;

//    Constructors
    public AddGreenhouseRequest() {}

    public AddGreenhouseRequest(String greenhouseName) {
        this.greenhouseName = greenhouseName;
    }

//    Getter and Setter
    public String getGreenhouseName() {
        return greenhouseName;
    }
    public void setGreenhouseName(String greenhouseName) {
        this.greenhouseName = greenhouseName;
    }

//    equals(), hasCode(), toString()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddGreenhouseRequest that = (AddGreenhouseRequest) o;
        return Objects.equals(greenhouseName, that.greenhouseName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(greenhouseName);
    }

    @Override
    public String toString() {
        return "AddGreenhouseRequest{" +
                "greenhouseName='" + greenhouseName + '\'' +
                '}';
    }
}
