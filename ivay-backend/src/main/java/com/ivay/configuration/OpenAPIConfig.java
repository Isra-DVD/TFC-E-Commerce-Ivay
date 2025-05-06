package com.ivay.configuration;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
		info = @Info(
				title = "IVAY APIREST",
				description = "Our app provides a number of endpoints for interacting with our e-commerce database",
				termsOfService = "www.accesodatos/terminos_y_condiciones",
				version = "1.0.0",
				contact = @Contact(
						name = "Israel Castro Delgado & Álvaro Negrín Hernández",
						url = "http://salesianos-lacuesta.com",
						email = "isra@salesianos-lacuesta.net"
				),
				license = @License(
						name = "Standard Software Use License for Acceso a Datos",
						url = "http://lacuesta.salesianos.edu/licence"
				)
		),
		servers = {			
			@Server(
					description = "Server URL in Development enviroment",
					url = "http://localhost:8081"
			),
			@Server(
					description = "Server URL in Production enviroment",
					url = "http://localhost:8081"
			)
		} 
)
public class OpenAPIConfig {

}
