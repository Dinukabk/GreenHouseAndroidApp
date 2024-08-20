package gr.aueb.cf.agronitor.validator;

import gr.aueb.cf.agronitor.dto.SoilHydrationDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class SoilHydrationValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return SoilHydrationDTO.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        SoilHydrationDTO soilHydrationDTO = (SoilHydrationDTO) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "timestamp", "empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "value", "empty");
    }
}
