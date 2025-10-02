

package com.grupod.docintelia.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,//peticion
                                    @NonNull HttpServletResponse response, //respuesta
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String autHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if(autHeader == null || !autHeader.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }

        jwt = autHeader.substring(7); //esto hace que se tome desde el indice 7 el jwt
        userEmail = jwtService.getUserName(jwt); //extraigo el username del jwt que se manda
        //aca veo que el usuario no sea nulo y que no este autenticado, se sigue el proceso de autenticacion
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            // aqui validamos si el token es valido
            //si es valido sigue sino devuelve un 403
            if(jwtService.validateToken(jwt,userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);



            }
        }
        filterChain.doFilter(request,response);

    }
}
