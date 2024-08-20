package gr.aueb.cf.agronitor.configuration;

import gr.aueb.cf.agronitor.dto.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_12)
                .ignoredParameterTypes(GreenhouseDTO.class,
                                       GreenhouseNoUserDTO.class,
                                       HumidityDTO.class,
                                       LoggedInUserDTO.class,
                                       LoginDTO.class,
                                       MeasurementsDTO.class,
                                       SoilHydrationDTO.class,
                                       TemperatureDTO.class,
                                       UserDTO.class,
                                       UVRadiationDTO.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("gr.aueb.cf.agronitor.rest"))
                .paths(PathSelectors.any())
                .build();
    }
}