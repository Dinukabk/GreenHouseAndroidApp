package gr.aueb.cf.agronitor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@Data
@NoArgsConstructor
@ApiIgnore
public class GreenhouseNoUserDTO {

    private Long id;
    private String greenhouseName;
    private Long userId;

    public GreenhouseNoUserDTO(Long id, String greenhouseName, Long userId) {
        this.id = id;
        this.greenhouseName = greenhouseName;
        this.userId = userId;
    }
}
