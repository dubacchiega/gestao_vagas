package br.com.dubacchiega.gestao_vagas.modules.candidate.services;

import br.com.dubacchiega.gestao_vagas.exceptions.UserFoundException;
import br.com.dubacchiega.gestao_vagas.modules.candidate.entities.CandidateEntity;
import br.com.dubacchiega.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service // passo que é um serviço
public class CreateCandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CandidateEntity execute(CandidateEntity candidateEntity){
        this.candidateRepository
                .findByUsernameOrEmail(candidateEntity.getUsername(), candidateEntity.getEmail()) // vou buscar pelo username ou email
                .ifPresent((user) -> { // se ele estiver presente (existir). eu vou receber o usuario no parametro (não vou fazer nada com ele, apenas capturar ele)
                    throw new UserFoundException(); // lanço uma exceção
                });

        String password = passwordEncoder.encode(candidateEntity.getPassword());
        candidateEntity.setPassword(password);

        return this.candidateRepository.save(candidateEntity);

    }
}
