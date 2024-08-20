package gr.aueb.cf.agronitor.models;

import java.util.Objects;

/**
 * Represents a greenhouse entity
 */
public class Greenhouse {

    private Long id;
    private String greenhouseName;
    private Long userId;

    //    Constructors
    public Greenhouse(String greenhouseName) {
        this.greenhouseName = greenhouseName;
    }

    public Greenhouse(String greenhouseName, Long userId) {
        this.greenhouseName = greenhouseName;
        this.userId = userId;
    }

//    Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getGreenhouseName() {
        return greenhouseName;
    }
    public void setGreenhouseName(String greenhouseName) {
        this.greenhouseName = greenhouseName;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

//    equals(), hashCode(), toString()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Greenhouse that = (Greenhouse) o;
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
        return "Greenhouse{" +
                "id=" + id +
                ", greenhouseName='" + greenhouseName + '\'' +
                ", userId=" + userId +
                '}';
    }
}
