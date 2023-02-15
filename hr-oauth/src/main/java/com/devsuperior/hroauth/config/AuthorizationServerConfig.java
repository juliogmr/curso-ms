package com.devsuperior.hroauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


    @Value("${oauth.client.name}")
    private String clientName;

    @Value("${oauth.client.secret}")
    private String clientSecret;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;

    @Autowired
    private JwtTokenStore tokenStore;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(clientName) //configura o cliente
                .secret(passwordEncoder.encode(clientSecret)) //encoda a senha do aplicativo
                .scopes("read", "write") //se pode ler ou escrever
                .authorizedGrantTypes("password") //tipo do grand_type, aqui no caso, password
                .accessTokenValiditySeconds(86400); // tempo de validação do token por segundo
    }

    @Override //configura o processamento do token
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception { //configura os dois beans que usamos
        endpoints.authenticationManager(authenticationManager)  // o Manager
                .tokenStore(tokenStore) //
                .accessTokenConverter(accessTokenConverter); //
    }
}