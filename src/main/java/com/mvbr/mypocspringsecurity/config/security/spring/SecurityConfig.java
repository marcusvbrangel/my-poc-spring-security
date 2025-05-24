package com.mvbr.mypocspringsecurity.config.security.spring;

import com.mvbr.mypocspringsecurity.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static com.mvbr.mypocspringsecurity.config.constants.SecurityHoleConstants.HOLE_ADMIN;
import static com.mvbr.mypocspringsecurity.config.constants.SecurityHoleConstants.HOLE_USER;
import static org.springframework.security.config.Customizer.withDefaults;

/*
    como você não faz autenticação programática (ou seja, não faz login manualmente pelo código), você não precisa
    do AuthenticationManager no seu UsuarioService nem no seu SecurityConfig.

    O Spring Security já cuida do login/logout automaticamente pelos filtros e endpoints padrão (/login, /logout).

    Você só precisa manter o cadastro de usuário (registro) no seu controller/serviço.

    Resumindo:

    Pode remover ou comentar o AuthenticationManager do seu projeto, pois ele só é necessário se você fosse
    autenticar usuários manualmente pelo código (o que não é o caso).

    Sua configuração está correta para o uso padrão do Spring Security!
 */

@Configuration
public class SecurityConfig {

    /*
        O método abaixo é um @Bean que configura a cadeia de filtros de segurança (SecurityFilterChain) no
        contexto do Spring Security. Ele define como as requisições HTTP serão tratadas em termos de autenticação e
        autorização.

        Primeiramente, a proteção contra CSRF (Cross-Site Request Forgery) é desativada com o
        comando .csrf((csrf) -> csrf.disable()). Isso é útil em cenários de teste, como ao usar ferramentas como
        Postman, mas deve ser habilitado em ambientes de produção para maior segurança.

        Em seguida, as regras de autorização são configuradas usando .authorizeHttpRequests. Algumas rotas específicas,
        como /api/v1/user/register e /api/v1/user/login, são liberadas para acesso público com .permitAll().
        Já outras rotas, como /api/v1/produtos/cadastrar e /api/v1/produtos/listar, exigem que o usuário esteja
        autenticado, configurado com .authenticated(). Qualquer outra requisição também requer autenticação,
        definida por .anyRequest().authenticated().

        Além disso, o método ativa o formulário de login padrão do Spring Security com .formLogin(withDefaults()),
        permitindo que os usuários façam login por meio de uma interface web. Também é habilitada a autenticação
        HTTP Basic com .httpBasic(withDefaults()), o que facilita testes em ferramentas como Postman.

        Por fim, o método retorna a configuração construída com httpSecurity.build(), garantindo que o Spring Security
        aplique essas regras à aplicação.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                // Desativa csrf para facilitar testes via Postman...
                .csrf((csrf) -> csrf.disable())

                .headers(headers -> headers.frameOptions().sameOrigin())

                .authorizeHttpRequests((authorizeHttpRequests) -> {

                    authorizeHttpRequests.requestMatchers("/h2-console/**").permitAll();

                    authorizeHttpRequests.requestMatchers("/api/v1/usuarios/registrar").hasRole(HOLE_ADMIN);

                    authorizeHttpRequests.requestMatchers("/login").permitAll();
                    authorizeHttpRequests.requestMatchers("/logout").authenticated();

//                    authorizeHttpRequests.requestMatchers("/api/v1/usuarios/entrar").permitAll();
//                    authorizeHttpRequests.requestMatchers("/api/v1/usuarios/sair").authenticated();

                    authorizeHttpRequests.requestMatchers("/api/v1/produtos/listar").hasAnyRole(HOLE_USER, HOLE_ADMIN);
                    authorizeHttpRequests.requestMatchers("/api/v1/produtos/cadastrar").hasRole(HOLE_USER);

                    authorizeHttpRequests.anyRequest().authenticated();
                })

                // Ativa o formulário de login padrão...
                .formLogin(withDefaults())

                .logout(withDefaults())

                // Ativa autenticação HTTP Basic (útil para testar com tools tipo Postman)...
                .httpBasic(withDefaults());

        return httpSecurity.build();

    }

    /*
        O método abaixo é um @Bean que define um UserDetailsService no contexto do Spring Security.
        O UserDetailsService é uma interface usada para carregar os detalhes de um usuário com base no nome de
        usuário, sendo essencial para o processo de autenticação.

        Neste caso, o método cria dois usuários em memória utilizando a classe User do Spring Security.
        Cada usuário é configurado com um nome de usuário, senha e papéis (roles).
        Por exemplo, o usuário "user" é criado com o papel USER:

        Os usuários criados são então registrados em uma instância de InMemoryUserDetailsManager, que é uma
        implementação do UserDetailsService que armazena os usuários em memória. Isso é útil para testes ou
        aplicações simples, onde não é necessário persistir os dados em um banco de dados.

        Por fim, o método retorna o InMemoryUserDetailsManager configurado, permitindo que o Spring Security
        utilize esses usuários para autenticação.
     */
//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//
//        UserDetails user = User.builder()
//                .username("user")
//                .password(passwordEncoder.encode("1234"))
//                .roles(HOLE_USER)
//                .build();
//
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder.encode("1234"))
//                .roles(HOLE_USER, HOLE_ADMIN)
//                .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//
//    }



//    @Bean
//    public UserDetailsService userDetailsService(CustomUserDetailsService customUserDetailsService) {
//        return customUserDetailsService;
//    }







    /*
        O método abaixo é um @Bean que define um PasswordEncoder no contexto do Spring Security.
        O PasswordEncoder é usado para realizar operações de codificação e verificação de senhas,
        garantindo que as senhas sejam armazenadas de forma segura.

        Neste caso, o método retorna uma instância de BCryptPasswordEncoder, que é uma implementação do
        PasswordEncoder baseada no algoritmo BCrypt. O BCrypt é amplamente utilizado devido à sua capacidade de
        gerar hashes seguros e resistentes a ataques de força bruta, pois inclui um fator de custo que aumenta
        a complexidade computacional.

        o expor o PasswordEncoder como um bean, ele pode ser injetado em outras partes da aplicação, como serviços
        ou repositórios, para codificar senhas antes de armazená-las no banco de dados ou para verificar senhas
        fornecidas durante o login. Por exemplo, ao criar um novo usuário, a senha pode ser codificada com o
        passwordEncoder.encode("senha").
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
        O método abaixo é um @Bean que define um AuthenticationManager no contexto do Spring Security.
        O AuthenticationManager é um componente central no processo de autenticação, responsável por validar
        as credenciais do usuário.

        Aqui, o método recebe como parâmetro um objeto do tipo AuthenticationConfiguration, que é uma classe
        fornecida pelo Spring Security para simplificar a configuração de autenticação. Esse objeto encapsula as
        configurações necessárias para criar e gerenciar um AuthenticationManager.

        O método chama authenticationConfiguration.getAuthenticationManager() para obter uma instância configurada
        do AuthenticationManager. Essa instância é então retornada e registrada como um bean no contexto do Spring,
        permitindo que outros componentes da aplicação a utilizem para autenticação.

        Ao expor o AuthenticationManager como um bean, ele pode ser injetado em outras partes do código, como
        controladores ou serviços, para realizar autenticações programáticas ou personalizadas.
     */
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
//            throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

}

//We can also configure multiple URLs. The configuration below requires authentication to every URL and
// will grant access to URLs starting with /admin/ to only the "admin" user. All other URLs either user can access.
//@Configuration
//@EnableWebSecurity
//public class AuthorizeUrlsSecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((authorizeHttpRequests) ->
//                        authorizeHttpRequests
//                                .requestMatchers("/admin/**").hasRole("ADMIN")
//                                .requestMatchers("/**").hasRole("USER")
//                )
//                .formLogin(withDefaults());
//        return http.build();
//    }
//

//Note that the matchers are considered in order. Therefore, the following is invalid because the
// first matcher matches every request and will never get to the second mapping:
//@Configuration
//@EnableWebSecurity
//public class AuthorizeUrlsSecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((authorizeHttpRequests) ->
//                        authorizeHttpRequests
//                                .requestMatchers("/**").hasRole("USER")
//                                .requestMatchers("/admin/**").hasRole("ADMIN")
//                );
//        return http.build();
//    }
//}
//
