package br.com.dubacchiega.gestao_vagas.modules.candidate.services;

import br.com.dubacchiega.gestao_vagas.exceptions.JobNotFoundException;
import br.com.dubacchiega.gestao_vagas.exceptions.UserNotFoundException;
import br.com.dubacchiega.gestao_vagas.modules.candidate.entities.CandidateEntity;
import br.com.dubacchiega.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import br.com.dubacchiega.gestao_vagas.modules.company.repositories.JobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// informo que eu desejo usar o MockitoExtension, que ajuda a integrar o Mockito com o JUnit 5, fornecendo recursos como: Injeção de Mocks:
@ExtendWith(MockitoExtension.class)
public class ApplyJobCandidateServiceTest {

    @InjectMocks // injeção de dependencias para testes (usa dados mockado)
    private ApplyJobCandidateService applyJobCandidateService;

    @Mock // uso o mock para informar ao meu teste, que essa classe vai ser uma dependencia que eu tenho no meu @InjectMocks
    private CandidateRepository candidateRepository;

    @Mock
    private JobRepository jobRepository;

    @Test
    @DisplayName("Shoud not be able to apply job with candidate not found")
    public void should_not_be_able_to_apply_job_with_candidate_not_found(){
        try { // vou tentar executar (vai dar erro pois eu passei null)
            applyJobCandidateService.execute(null, null);
        }catch (Exception e){ // vai capturar a exceção
            assertThat(e).isInstanceOf(UserNotFoundException.class); // se a exceção for instancia de UserNotFoundException, o teste passa
        }
    }

    @Test
    @DisplayName("Shoud not be able to apply job with job not found")
    public void should_not_be_able_to_apply_job_with_job_not_found(){
        UUID idCandidate = UUID.randomUUID(); // id aleatório para teste

        var candidate = new CandidateEntity();
        candidate.setId(idCandidate);

//        Configura um mock do candidateRepository para que, QUANDO chamar o méthodo findById com o ID do candidato, ele retorne o objeto candidate.
        when(candidateRepository.findById(idCandidate)).thenReturn(Optional.of(candidate));

        try {
            applyJobCandidateService.execute(idCandidate, null);
        }catch (Exception e){
            assertThat(e).isInstanceOf(JobNotFoundException.class);
        }
    }
}
