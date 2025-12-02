package edu.dosw.AmaterasuWalletBackEnd.AmaterasuWalletBack.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI/Swagger para el microservicio Wallet.
 * Proporciona documentación interactiva para consumir la API REST.
 *
 * @author Equipo Amaterasu
 * @version 1.0.0
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configuración personalizada de OpenAPI para el servicio de Wallet.
     *
     * @return configuración OpenAPI con información del servicio
     */
    @Bean
    public OpenAPI walletOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Wallet Service API - ECIEXPRESS")
                        .description("API REST para la gestión del monedero virtual de los usuarios en el sistema ECIEXPRESS. "
                                + "Este microservicio administra el saldo del usuario, recargas, "
                                + "movimientos financieros y validaciones de disponibilidad de fondos.")
                        .version("1.0.0"));
    }
}

