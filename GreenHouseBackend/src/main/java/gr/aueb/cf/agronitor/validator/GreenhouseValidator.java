package gr.aueb.cf.agronitor.validator;

import gr.aueb.cf.agronitor.dto.GreenhouseDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class GreenhouseValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return GreenhouseDTO.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "greenhouseName", "empty");
    }
}
