package br.com.dubacchiega.gestao_vagas.modules.company.services;

import br.com.dubacchiega.gestao_vagas.exceptions.UserFoundException;
import br.com.dubacchiega.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.dubacchiega.gestao_vagas.modules.company.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateCompanyService {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // aqui eu to usando o BCryptPasswordEncoder indiretamente

    public CompanyEntity execute(CompanyEntity companyEntity){
        Optional<CompanyEntity> result = this.companyRepository.findByUsernameOrEmail(companyEntity.getUsername(), companyEntity.getEmail());
        if (result.isPresent()){
            throw new UserFoundException();
        }

        String password = passwordEncoder.encode(companyEntity.getPassword()); // encodando a senha
        companyEntity.setPassword(password); // setando a senha

        return this.companyRepository.save(companyEntity);
    }
}
