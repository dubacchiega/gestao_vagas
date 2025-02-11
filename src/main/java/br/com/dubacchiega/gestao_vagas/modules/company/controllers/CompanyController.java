package br.com.dubacchiega.gestao_vagas.modules.company.controllers;

import br.com.dubacchiega.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.dubacchiega.gestao_vagas.modules.company.services.CreateCompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping(path = "/company")
public class CompanyController {

    @Autowired
    private CreateCompanyService createCompanyService;

    @PostMapping(path = "/")
    public ResponseEntity<Object> create(@Valid @RequestBody CompanyEntity companyEntity){
        try {
            CompanyEntity result = this.createCompanyService.execute(companyEntity);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}




