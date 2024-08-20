package gr.aueb.cf.agronitor.models;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a User entity
 */
public class User extends JSONObject {
    private Long id;
    private String username;
    private String email;
    private String password;
    private ArrayList<Greenhouse> greenhouseArrayList;

//    Constructor
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username) {
        this.username = username;
    }

//    Getters and Setters
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public ArrayList<Greenhouse> getGreenhouseArrayList() {
        return greenhouseArrayList;
    }
    public void setGreenhouseArrayList(ArrayList<Greenhouse> greenhouseArrayList) {
        this.greenhouseArrayList = greenhouseArrayList;
    }

//    equals(), hashCode(), toString()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id)
                && Objects.equals(username, user.username)
                && Objects.equals(email, user.email)
                && Objects.equals(password, user.password)
                && Objects.equals(greenhouseArrayList, user.greenhouseArrayList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, greenhouseArrayList);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", greenhouseArrayList=" + greenhouseArrayList +
                '}';
    }
}
