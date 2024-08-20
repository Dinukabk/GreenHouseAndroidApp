package gr.aueb.cf.agronitor.validator;

import gr.aueb.cf.agronitor.dto.UserDTO;
import gr.aueb.cf.agronitor.service.IUserService;
import gr.aueb.cf.agronitor.service.exceptions.EntityAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final IUserService userService;

    @Autowired
    public UserValidator(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userToRegister = (UserDTO) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "empty");
        if (userToRegister.getUsername().length() < 3 || userToRegister.getUsername().length() > 32) {
            errors.rejectValue("username", "size");
        }

        try {
            if (userService.usernameAlreadyExists(userToRegister.getUsername())) {
                errors.rejectValue("email", "duplicate");
            }
        } catch (EntityAlreadyExistsException e) {
            throw new RuntimeException(e);
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "empty");
        if (userToRegister.getPassword().length() < 3 || userToRegister.getPassword().length() > 32) {
            errors.rejectValue("password", "size");
        }
        
    }
}
