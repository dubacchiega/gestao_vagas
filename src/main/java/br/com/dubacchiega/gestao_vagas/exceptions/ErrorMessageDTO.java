package br.com.dubacchiega.gestao_vagas.exceptions;


import lombok.AllArgsConstructor;
import lombok.Data;

// Declaração da classe pública ErrorMessageDTO. O sufixo DTO geralmente significa "Data Transfer Object",
// que é um padrão utilizado para transportar dados entre processos. Neste caso, a classe é projetada para encapsular
// mensagens de erro que podem ser enviadas como parte de uma resposta de erro em uma API.

@Data
@AllArgsConstructor
public class ErrorMessageDTO {

    private String message;
    private String fild;
}
