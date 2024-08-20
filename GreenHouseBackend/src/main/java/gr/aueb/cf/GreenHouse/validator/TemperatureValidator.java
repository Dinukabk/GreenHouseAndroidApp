package gr.aueb.cf.agronitor.validator;

import gr.aueb.cf.agronitor.dto.TemperatureDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class TemperatureValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return TemperatureDTO.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        TemperatureDTO temperatureDTO = (TemperatureDTO) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "timestamp", "empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "value", "empty");
    }
}
