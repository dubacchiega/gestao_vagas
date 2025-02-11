package br.com.dubacchiega.gestao_vagas.security;

import br.com.dubacchiega.gestao_vagas.providers.JWTProvider;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter { // vai executar a autenticação apena uma vez por request

    @Autowired
    JWTProvider jwtProvider;

    @Override          // request, tudo que eu vou receber da requisição. response, tudo aquilo que eu vou querer mandar pra outra camada de filtro
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        SecurityContextHolder.getContext().setAuthentication(null);
        String header = request.getHeader("Authorization"); // pegando o header da requisição no campo authorization (pegando o token)

//        O URI é a parte da URL que identifica o recurso solicitado no servidor.
//        http://www.example.com/minha-aplicacao/produtos?id=10 -> URI: /minha-aplicacao/produtos     Query String: id=10
        if (request.getRequestURI().startsWith("/company")){
            if (header != null){
                DecodedJWT token = this.jwtProvider.validateToken(header); // aqui valida
                if (token == null){ // se o token for vazio, significa q a verificação falhou
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                List<Object> roles = token.getClaim("roles").asList(Object.class);
                List<SimpleGrantedAuthority> grants = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase()))
                        .toList();

                // seto meu company_id com o subject (id)
                request.setAttribute("company_id", token.getSubject());
//            criando o objeto de autenticação.
//            Passo meu subject
//            null para a senha, já que o token JWT é utilizado para autenticação, e a senha não é necessária nesse contexto.
//            Collections.emptyList() representando as authorities (permissões) do usuário. Neste caso, nenhuma authority específica é atribuída.
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(token.getSubject(), null, grants);

//            O contexto de segurança do Spring é atualizado para incluir o novo objeto de autenticação auth.
//            Isso informa ao Spring Security que o usuário agora está autenticado e permite que ele acesse recursos protegidos.
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }



        // se for uma rota de autenticação e o header não for preenchido, aqui vai dar erro
//        se existir outro filtro, aqui vai ser passado para o próximo
        filterChain.doFilter(request, response);
    }
}
