package br.com.dubacchiega.gestao_vagas.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

//Não precisa chamar explicitamente o méthodo securityFilterChain no código.
// O Spring Security reconhece automaticamente a configuração desse méthodo por causa da anotação @Bean.
// Ao usar essa anotação, o Spring gerencia a criação e injeção desse objeto no ciclo de vida da aplicação.

//No seu caso, como o méthodo securityFilterChain está anotado com @Bean dentro de uma classe anotada com @Configuration, o Spring vai automaticamente:
//Instanciar o SecurityFilterChain quando a aplicação for inicializada.
//Aplicar a configuração de segurança que você definiu no méthodo, como desabilitar CSRF, permitir acesso a certas rotas,
// exigir autenticação para outras, e adicionar o SecurityFilter antes do BasicAuthenticationFilter.

//Como funciona:
//Spring Security detecta automaticamente a configuração do filtro de segurança no momento em que a aplicação sobe, e a
// aplica em todas as requisições HTTP da aplicação.
//O méthodo securityFilterChain configura o filtro de segurança usando a classe HttpSecurity, e o SecurityFilter é adicionado
// com addFilterBefore para ser executado antes da autenticação básica.


@Configuration // uma das primeiras anotações a ser lida
@EnableMethodSecurity // para usar o preAuthorize
public class SecurityConfig {

    @Autowired
    SecurityFilter securityFilter;

    @Autowired
    SecurityCandidateFilter securityCandidateFilter;

    private static final String[] PERMIT_ALL_LIST = {
      "/swagger-ui/**",
      "/v3/api-docs/**",
      "/swagger-resources/**",
            "/actuator/**"
    };

    @Bean // cria um objeto que o Spring vai gerenciar e usar para configurar a segurança da aplicação
//    SecurityFilterChain conjunto de filtros que verifica se as requisições são seguras e autorizadas
//    HttpSecurity: É uma ferramenta que permite configurar regras de segurança, como quem pode acessar o quê e outras proteções.
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.disable()) // desabilita o csrf
                .authorizeHttpRequests(auth -> { // define quem pode acessar determinada rota
                    auth.requestMatchers("/candidate/").permitAll() // se a requisição for no /candidate, vai permitir
                            .requestMatchers("/company/").permitAll() // se a requisição for no /company, vai permitir
                            .requestMatchers("/company/auth").permitAll()
                            .requestMatchers("/candidate/auth").permitAll()
                            .requestMatchers(PERMIT_ALL_LIST).permitAll()
                            .requestMatchers("/actuator/prometheus").permitAll();
                    auth.anyRequest().authenticated(); // qualquer outra rota será com autenticação
                })
                .addFilterBefore(securityCandidateFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(securityFilter, BasicAuthenticationFilter.class); // adicionando um filtro. Ele vai ser executado antes de qualquer autenticação básica (BasicAuthenticationFilter)

        return http.build(); // constrói e retorna a configuração de segurança nas regras que foram definidas
    }

    @Bean
    public PasswordEncoder passwordEncoder(){ // está sendo usada indiretamente quando eu chamo o PasswordEncoder, então quando eu uso PasswordEncoder, eu to usando o BCryptPasswordEncoder
        return new BCryptPasswordEncoder();
    }
}
