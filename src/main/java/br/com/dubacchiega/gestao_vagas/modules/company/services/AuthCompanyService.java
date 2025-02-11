package br.com.dubacchiega.gestao_vagas.modules.company.services;

import javax.naming.AuthenticationException;
import br.com.dubacchiega.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.dubacchiega.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import br.com.dubacchiega.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.dubacchiega.gestao_vagas.modules.company.repositories.CompanyRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@Service
public class AuthCompanyService {

    @Value("${security.token.secret}")
    private String secretKey;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthCompanyResponseDTO execute(AuthCompanyDTO authCompanyDTO) throws AuthenticationException {
        // vou procurar a entidade pelo username, se retornar vazio (não achou a entidade pelo username) lança uma exceção
        CompanyEntity company = this.companyRepository.findByUsername(authCompanyDTO.getUsername()).orElseThrow(
                ()-> {
                    throw new UsernameNotFoundException("Username/password incorret");
                });

        // Verificar se a senha são iguais
        boolean passwordMatches = this.passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword()); // passo primeira a senha crua e depois a criptografada (a criptografada esta no DB)

        // Se não for igual -> Erro
        if (!passwordMatches){
            throw new AuthenticationException();
        }
            // Se for igual -> Gerar o token
//        vou usar o token para tirar o UUID do companyId, a informação que eu vou receber de qual company a vaga faz parte, vai vir do token
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        Instant expiresIn = Instant.now().plus(Duration.ofHours(2));

        String token = JWT.create().withIssuer("javagas")
                .withExpiresAt(expiresIn) // defini que o token vai expirar depois de 2 horas
                .withSubject(company.getId().toString())
                .withClaim("roles", Arrays.asList("COMPANY"))
                .sign(algorithm);

        AuthCompanyResponseDTO authCompanyResponseDTO = AuthCompanyResponseDTO.builder()
                .access_token(token)
                .expires_in(expiresIn.toEpochMilli())
                .build();

        return authCompanyResponseDTO;
    }
}
