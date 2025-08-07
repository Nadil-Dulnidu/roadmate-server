package com.roadmateserver.root.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "RoadMate API Documentation",
                version = "1.0",
                description = "This API powers the core backend functionality of the RoadMate, a web-based vehicle rental platform, including user authentication, vehicle management, booking operations, payment handling, and role-based access control.",
                contact = @Contact(name = "Developer", email = "dulniduthennakoon@gmail.com"),
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0")
        )
)
public class OpenApiConfig {}
