package gr.aueb.cf.agronitor;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(info = @Info(title = "Agronitor API",
                                version = "0.9",
                                description = "Provides api services for the Agronitor android app"))
public class AgronitorBackendApp {
    public static void main(String[] args) {
        SpringApplication.run(AgronitorBackendApp.class, args);
    }
}
