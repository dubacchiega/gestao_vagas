package br.com.dubacchiega.gestao_vagas.modules.candidate.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter // cria os getters e setters
@Setter
@Entity(name = "candidate")
public class CandidateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Schema(example = "Eduardo Bacchiega", requiredMode = Schema.RequiredMode.REQUIRED, description = "Nome do candidato")
    private String name;

    @NotBlank()
    // estou passando um regex para que o campo username não tenha nenhum espaço
    @Pattern(regexp = "\\S+", message = "O campo [username] não deve conter espaços")
    @Schema(example = "eduardo", requiredMode = Schema.RequiredMode.REQUIRED, description = "Username do candidato")
    private String username;

    // validação para o campo email ser um email
    @Email(message = "O campo [email] deve conter um e-mail válido")
    @Schema(example = "eduardo@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED, description = "E-mail do candidato")
    private String email;

    // tamanho mínimo e máximo da senha
    @Length(min = 8, max = 100, message = "A senha deve conter entre (8) e (100) caracteres")
    @Schema(example = "admin@2024", minLength = 8, maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED, description = "Senha do candidato")
    private String password;

    @Schema(example = "Desenvolvedor Java", requiredMode = Schema.RequiredMode.REQUIRED, description = "Breve descrição do candidato")
    private String description;
    private String curriculum;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
