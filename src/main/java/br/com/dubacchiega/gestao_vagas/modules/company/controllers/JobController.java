package br.com.dubacchiega.gestao_vagas.modules.company.controllers;

import br.com.dubacchiega.gestao_vagas.modules.company.dto.CreateJobDTO;
import br.com.dubacchiega.gestao_vagas.modules.company.entities.JobEntity;
import br.com.dubacchiega.gestao_vagas.modules.company.services.CreateJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;



//No código, o filtro SecurityFilter está interceptando a requisição HTTP antes que ela chegue ao seu JobController.
// Ele está validando o token JWT no cabeçalho da requisição e, se o token for válido, extrai o subjectToken
// (company_id) e o atribui à requisição como um atributo com o nome "company_id".


// O filtro intercepta todas as requisições.
// Ele tenta pegar o token JWT do cabeçalho Authorization.
// Se o token for válido, ele extrai o subjectToken (provavelmente o company_id) e adiciona esse valor na requisição como um atributo: request.setAttribute("company_id", subjectToken).
// Esse atributo estará disponível para ser acessado em qualquer lugar do fluxo de requisição, incluindo o seu JobController.

// estou acessando o atributo "company_id" diretamente da requisição HTTP via request.getAttribute("company_id").
// O companyID extraído é então convertido para UUID e utilizado na construção da entidade JobEntity.

@RestController
@RequestMapping("/company/job")
public class JobController {

    @Autowired
    private CreateJobService createJobService;


    @PostMapping(path = "/")
    @PreAuthorize("hasRole('COMPANY')")
    @Tag(name="Vagas", description = "Informações das vagas") // documentação
    @Operation(summary = "Cadastro de vagas", description = "Essa função é responsável por cadastrar as vagas dentro da empresa") // documentação
    @ApiResponses({ // documentação
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = JobEntity.class))
            })
    })
    @SecurityRequirement(name = "jwt_auth") // documentação
    public JobEntity create(@Valid @RequestBody CreateJobDTO createJobDTO, HttpServletRequest request){

//        O HttpServletRequest vem de forma automática quando ocorre uma requisição HTTP.
//        Ele é importante para acessar os dados da requisição

        // pegando o id que foi passado
        Object companyID = request.getAttribute("company_id");

//        jobEntity.setCompanyId(UUID.fromString(companyID.toString()));

        JobEntity jobEntity = JobEntity.builder() // construo
                .benefits(createJobDTO.getBenefits()) // passo beneficios
                .companyId(UUID.fromString(companyID.toString())) // o companyId vindo
                .description(createJobDTO.getDescription())
                .level(createJobDTO.getLevel())
                .build();
        return this.createJobService.execute(jobEntity);
    }
}
