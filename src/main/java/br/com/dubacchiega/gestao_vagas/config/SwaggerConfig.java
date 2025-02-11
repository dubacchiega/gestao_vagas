package br.com.dubacchiega.gestao_vagas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){

//        crio uma nova doc
        return new OpenAPI()
//                crio uma nova info, informando o título, descrição e a versão
                .info(new Info().title("Gestão Vagas").description("API Responsável pela gestão de vagas").version("1"))
                .schemaRequirement("jwt_auth", createSecurityScheme()); // crio um requisito de esquema

//        se eu quisesse adicionar a segurança em todas as camadas:
//                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
//                .components(new Components().addSecuritySchemes("Bearer Authentication", createSecurityScheme()));
    }

    private SecurityScheme createSecurityScheme(){
//        crio um esquema de segurança com o nome de jwt_auth, informo que o tipo é de HTTP, passo o esquema e o gormato esperado do token (JWT no caso)
        return new SecurityScheme().name("jwt_auth").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT");
    }
}
