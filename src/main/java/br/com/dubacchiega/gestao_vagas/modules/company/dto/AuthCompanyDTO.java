package br.com.dubacchiega.gestao_vagas.modules.company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthCompanyDTO { // classe para pegar apenas os atributos que eu quero

    private String password;
    private String username;
}
