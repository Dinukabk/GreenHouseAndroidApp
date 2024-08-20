package gr.aueb.cf.agronitor.dto;

import gr.aueb.cf.agronitor.model.Greenhouse;
import lombok.Data;
import lombok.NoArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;


@Data
@NoArgsConstructor
@ApiIgnore
public class UserDTO {

    private Long id;

    @Size(min = 3, message = "Username must have at least ${min} characters")
    private String username;

    @Size(min = 8, message = "Password must have at least ${min} characters")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?\\d).*$")
    private String password;

    @Email
    private String email;

    public UserDTO(Long id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
