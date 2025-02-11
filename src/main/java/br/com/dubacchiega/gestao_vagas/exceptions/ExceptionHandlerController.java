package br.com.dubacchiega.gestao_vagas.exceptions;


import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice // manipulador de exceção. Posso tratar exceção de qualquer controller aqui
public class ExceptionHandlerController {// controlo sobre as validações de espaço em username, tamanho da senha, email, etc

    private MessageSource messageSource; // MessageSource é usada para obter mensagens localizadas

    public ExceptionHandlerController(MessageSource messageSource){
        this.messageSource = messageSource;
    }
//    Esta anotação indica que o methodo que segue deve ser chamado sempre que uma exceção do tipo MethodArgumentNotValidException
//    essa exceção ocorre quando uma validação falha
    @ExceptionHandler(MethodArgumentNotValidException.class) // informa que quando ocorrer um exception desse tipo, vai cair no meu method
    public ResponseEntity<List<ErrorMessageDTO>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){ // recebo um parametro do tipo da exceção
        List<ErrorMessageDTO> dto = new ArrayList<>();

//         getBindingResult retorna um objeto do tipo BindingResult, que contém informações sobre o resultado da vinculação (binding) e validação dos dados de entrada que causaram a exceção
//        getFieldErrors Retorna uma lista de objetos FieldError. Cada FieldError representa um erro específico que ocorreu em um campo do objeto que estava sendo validado
        e.getBindingResult().getFieldErrors().forEach(err -> {
//            getMessage tenta encontrar uma mensagem correspondente ao código fornecido (neste caso, o código que está contido no objeto err) e retornar a mensagem localizada no idioma apropriado.
//            LocaleContextHolder é útil porque permite que a aplicação adapte suas mensagens para o idioma preferido do usuário
            String message = messageSource.getMessage(err, LocaleContextHolder.getLocale());

//            crio uma classe para formatar a mensagem de erro. Passando a mensagem (definida na minha entidade) e o campo que ocorreu
            ErrorMessageDTO error = new ErrorMessageDTO(message, err.getField());
            dto.add(error); // adciono o erro a minha lista
        });

//        ResponseEntity é uma classe do Spring que representa uma resposta HTTP completa, incluindo o corpo da resposta, cabeçalhos e status.
//        eu passo o dto como corpo e o código de status
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);

    }
}
