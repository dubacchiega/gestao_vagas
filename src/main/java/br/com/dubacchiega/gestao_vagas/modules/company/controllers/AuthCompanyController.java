package br.com.dubacchiega.gestao_vagas.modules.company.controllers;

import br.com.dubacchiega.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.dubacchiega.gestao_vagas.modules.company.services.AuthCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/company")
public class AuthCompanyController {

    @Autowired
    private AuthCompanyService authCompanyService;

    @PostMapping("/auth")
    public ResponseEntity<Object> create(@RequestBody AuthCompanyDTO authCompanyDTO){
        try {
            var result = this.authCompanyService.execute(authCompanyDTO);
            return ResponseEntity.ok().body(result);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

    }
}
