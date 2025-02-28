package br.com.dubacchiega.gestao_vagas.modules.candidate.services;

import br.com.dubacchiega.gestao_vagas.exceptions.JobNotFoundException;
import br.com.dubacchiega.gestao_vagas.exceptions.UserNotFoundException;
import br.com.dubacchiega.gestao_vagas.modules.candidate.entities.ApplyJobEntity;
import br.com.dubacchiega.gestao_vagas.modules.candidate.repositories.ApplyJobRepository;
import br.com.dubacchiega.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import br.com.dubacchiega.gestao_vagas.modules.company.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ApplyJobCandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplyJobRepository applyJobRepository;

    public ApplyJobEntity execute(UUID idCandidate, UUID idJob){
        // validar se o candidato existe
        this.candidateRepository.findById(idCandidate).orElseThrow(() -> {
            throw new UserNotFoundException();
        });

        // validar se a vaga existe
        this.jobRepository.findById(idJob).orElseThrow(()->{
            throw new JobNotFoundException();
        });

        // candidato se inscrever na vaga
        ApplyJobEntity applyJob = ApplyJobEntity.builder()
                .candidateId(idCandidate)
                .jobId(idJob)
                .build();
        applyJob = applyJobRepository.save(applyJob);
        return applyJob;
    }
}
