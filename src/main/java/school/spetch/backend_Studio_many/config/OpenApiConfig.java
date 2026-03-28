package school.spetch.backend_Studio_many.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Documentação API",
                version = "1.0",
                description = "GRUPO 5 <br> Meki <br> Aoki <br> Bill <br> Davi <br> Dereck <br> João"
        )
)
public class OpenApiConfig {
}
