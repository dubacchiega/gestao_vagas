package br.com.dubacchiega.gestao_vagas.exceptions;

public class UserFoundException extends RuntimeException{

    public UserFoundException(){
        super("Usuário já existe"); // estou passando pro construtor da classe RuntimeException a mensagem que eu quero no erro
    }
}
