package br.com.dubacchiega.gestao_vagas.modules.candidate.controllers;

import br.com.dubacchiega.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import br.com.dubacchiega.gestao_vagas.modules.candidate.entities.CandidateEntity;
import br.com.dubacchiega.gestao_vagas.modules.candidate.services.CreateCandidateService;
import br.com.dubacchiega.gestao_vagas.modules.candidate.services.ListAllJobsByFilterService;
import br.com.dubacchiega.gestao_vagas.modules.candidate.services.ProfileCandidateService;
import br.com.dubacchiega.gestao_vagas.modules.company.entities.JobEntity;
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
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/candidate")
@Tag(name = "Candidato", description = "Informações do candidato") // documentação
public class CandidateController {


    @Autowired
    private CreateCandidateService createCandidateService;

    @Autowired
    private ProfileCandidateService profileCandidateService;

    @Autowired
    private ListAllJobsByFilterService listAllJobsByFilterService;

    @PostMapping("/")
    // passo primeiro para validar e depois informo que o candidateEntity vai vir do corpo de uma requisição
//    passo que o tipo é um ResponseEntity<Object> para eu poder retornar qualquer coisa no meu try/catch
    @Operation(summary = "Cadastro de candidato", description = "Essa função é responsável por cadastrar um candidato") // operation serve para documentar endpoints
    @ApiResponses({ // possiveis respostas na documentação
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = CandidateEntity.class))
            }),
            @ApiResponse(responseCode = "400", description = "Usuário já existe")
    })
    public ResponseEntity<Object> create(@Valid @RequestBody CandidateEntity candidateEntity){
//        como o serviço lança uma exceção, eu trato ela aqui
        try {
            CandidateEntity result = this.createCandidateService.execute(candidateEntity);
            return ResponseEntity.ok().body(result); // retorno o código ok e a minha entidade criada, no corpo da request (a entidade vai ser mostrada no postman/insomnia)
        } catch (Exception e) { // capturo a exceção
            return ResponseEntity.badRequest().body(e.getMessage()); // passo o código de erro e no corpo eu passo a mensagem da minha exceção
        }
    }


    @GetMapping("/")
    @PreAuthorize("hasRole('CANDIDATE')") // pre autorização. Tem que ter a role CANDIDATE pra poder acessar
    @Operation(summary = "Perfil do candidato", description = "Essa função é responsável por buscar as informações do perfil do usuário") // documentação
    @ApiResponses({ // documentação
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ProfileCandidateResponseDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    @SecurityRequirement(name = "jwt_auth") // documentação
    public ResponseEntity<Object> get(HttpServletRequest request){
        Object idCandidate = request.getAttribute("candidate_id");
        try {
            var profile = this.profileCandidateService.execute(UUID.fromString(idCandidate.toString()));
            return ResponseEntity.ok().body(profile);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/job")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Listagem de vagas disponíveis para o candidato", description = "Essa função é responsável por listar todas as vagas disponíveis, baseadas no filtro") // documentação
    @ApiResponses({ // documentação
            @ApiResponse(responseCode = "200", content = {
                    @Content(
                            array = @ArraySchema(schema = @Schema(implementation = JobEntity.class))
                    )
            })
    })
    @SecurityRequirement(name = "jwt_auth") // documentação
    public List<JobEntity> findJobByFilter(@RequestParam String filter){
        return this.listAllJobsByFilterService.execute(filter);
    }
}
