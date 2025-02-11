package br.com.dubacchiega.gestao_vagas.modules.candidate.controllers;

import br.com.dubacchiega.gestao_vagas.modules.candidate.dto.AuthCandidateRequestDTO;
import br.com.dubacchiega.gestao_vagas.modules.candidate.services.AuthCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/candidate")
public class AuthCandidateController {

    @Autowired
    private AuthCandidateService authCandidateService; // reponsável pela criação do token

    @PostMapping("/auth")
    public ResponseEntity<Object> auth(@RequestBody AuthCandidateRequestDTO authCandidateRequestDTO){ // vou receber um dto

        try {
            var token = this.authCandidateService.execute(authCandidateRequestDTO);
            return ResponseEntity.ok().body(token); // se autenticar eu retorno o token
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

    }
}
