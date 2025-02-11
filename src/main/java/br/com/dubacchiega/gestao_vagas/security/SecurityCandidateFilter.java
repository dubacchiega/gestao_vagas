package br.com.dubacchiega.gestao_vagas.security;

import br.com.dubacchiega.gestao_vagas.providers.JWTCandidateProvider;
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
public class SecurityCandidateFilter extends OncePerRequestFilter {

    @Autowired
    private JWTCandidateProvider jwtCandidateProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        SecurityContextHolder.getContext().setAuthentication(null);

        String header = request.getHeader("Authorization");

        if(request.getRequestURI().startsWith("/candidate")){
            if (header != null){
                DecodedJWT token = this.jwtCandidateProvider.validateToken(header);
                if (token == null){
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                request.setAttribute("candidate_id", token.getSubject());
                List<Object> roles = token.getClaim("roles").asList(Object.class); // pegando as regras

                List<SimpleGrantedAuthority> grants = roles.stream()
                        .map(
                                // vou pegar cada role na minha lista de roles e mapear ela. Criando um novo objeto passando aquela role
                                // com o prefixo ROLE_ para a anotação de @PreAuthorize conseguir achar
                                role -> new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase())
                        ).toList();

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(token.getSubject(), null, grants); // passo as regras
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
