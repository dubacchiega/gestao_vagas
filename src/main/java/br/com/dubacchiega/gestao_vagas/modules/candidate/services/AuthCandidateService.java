package br.com.dubacchiega.gestao_vagas.modules.candidate.services;

import br.com.dubacchiega.gestao_vagas.modules.candidate.dto.AuthCandidateRequestDTO;
import br.com.dubacchiega.gestao_vagas.modules.candidate.dto.AuthCandidateResponseDTO;
import br.com.dubacchiega.gestao_vagas.modules.candidate.entities.CandidateEntity;
import br.com.dubacchiega.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

@Service
public class AuthCandidateService {

    @Value("${security.token.secret.candidate}")
    private String secreteKey;

    @Autowired
    private CandidateRepository candidateRepository;// responsável por achar o user

    @Autowired
    private PasswordEncoder passwordEncoder; // responsável por encodar

    public AuthCandidateResponseDTO execute(AuthCandidateRequestDTO authCandidateRequestDTO) throws AuthenticationException {
        CandidateEntity candidate = this.candidateRepository.findByUsername(authCandidateRequestDTO.username()) // pegando o username que eu recebi e passando para ser procurado
                .orElseThrow(()->{
                    throw new UsernameNotFoundException("Username/password incorrect");
                });

//        checando se as senhas batem
        boolean passwordMatches = this.passwordEncoder.matches(authCandidateRequestDTO.password(), candidate.getPassword());

        if (!passwordMatches) { // se as senhas nao baterem
            throw new AuthenticationException();
        }


        Algorithm algorithm = Algorithm.HMAC256(secreteKey); // definindo algoritmo
        Instant expiresIn = Instant.now().plus(Duration.ofMinutes(10)); // definindo o tempo de expiração
        String token = JWT.create() // criando o token
                .withIssuer("javagas")
                .withSubject(candidate.getId().toString()) // passando o id como subject
                .withClaim("roles", Arrays.asList("CANDIDATE")) // permissoes
                .withExpiresAt(expiresIn)
                .sign(algorithm);

//        construindo uma resposta
        AuthCandidateResponseDTO authCandidateResponse = AuthCandidateResponseDTO.builder()
                .access_token(token) // passando o token de acesso
                .expires_in(expiresIn.toEpochMilli()) // passando o tempo que expira
                .build();
        return authCandidateResponse;
    }
}
