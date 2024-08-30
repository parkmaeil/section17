package com.example.jwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jwt.entity.Customer;
import com.example.jwt.entity.CustomerUser;
import com.example.jwt.service.CustomerService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private CustomerService customerService;
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, CustomerService customerService) {
        super(authenticationManager);
        this.customerService=customerService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("인증이나 권한이 필요한 요청들은 이부분을 실행하게 된다.");
        // JWT 토큰을 받아보자.
        String jwtHeader=request.getHeader("Authorization");
        System.out.println("jwtToken:" + jwtHeader); //
        if(jwtHeader==null || !jwtHeader.startsWith("Bearer")){
            chain.doFilter(request, response);
            return;
        }
        // 정상적인 JWT 토큰이면....
        String jwtToken=request.getHeader("Authorization").replace("Bearer ","");
        // cosin
        String username= JWT.require(Algorithm.HMAC256(JwtProperties.SECRET))
                .build()
                .verify(jwtToken).getClaim("username")
                .asString();
        if(username!=null){
            Customer customer =customerService.findByUsername(username);
            CustomerUser customerUser=new CustomerUser(customer);
            Authentication authentication=new UsernamePasswordAuthenticationToken(customerUser, null,  customerUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
