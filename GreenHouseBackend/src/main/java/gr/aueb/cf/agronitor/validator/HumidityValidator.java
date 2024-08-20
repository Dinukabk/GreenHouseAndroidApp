package gr.aueb.cf.agronitor.validator;

import gr.aueb.cf.agronitor.dto.HumidityDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class HumidityValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return HumidityDTO.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        HumidityDTO humidityDTO = (HumidityDTO) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "timestamp", "empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "value", "empty");
    }
}
