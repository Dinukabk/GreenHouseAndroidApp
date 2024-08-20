package gr.aueb.cf.agronitor.validator;

import gr.aueb.cf.agronitor.dto.UVRadiationDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UVRadiationValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UVRadiationDTO.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UVRadiationDTO uvRadiationDTO = (UVRadiationDTO) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "timestamp", "empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "value", "empty");
    }
}
