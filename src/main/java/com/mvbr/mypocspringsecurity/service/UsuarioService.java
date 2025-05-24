package com.mvbr.mypocspringsecurity.service;

import com.mvbr.mypocspringsecurity.model.Usuario;
import com.mvbr.mypocspringsecurity.repository.UsuarioRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.mvbr.mypocspringsecurity.config.constants.SecurityHoleConstants.HOLE_USER;

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

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
//    private final AuthenticationManager authenticationManager;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registrar(String username, String password) {

        if (usuarioRepository.findByUsername(username).isPresent()) {
            throw new EntityExistsException("Usuário já existe");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setRole(HOLE_USER);

        this.usuarioRepository.save(usuario);

        return "Registro efetuado com sucesso!";
    }

//    public String logar(String username, String password) {
//
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(username, password);
//
//        Authentication authentication = authenticationManager.authenticate(authenticationToken);
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        return "Login efetuado com sucesso!";
//
//    }

//    public String sair() {
//        SecurityContextHolder.clearContext();
//        return "Logout efetuado com sucesso!";
//    }



















    // Nao e necessario, somente para experimentacao...
//    public void autenticarUsuarioProgramaticamente(String username, String password) {
//        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
//        authenticationManager.authenticate(authentication);
//    }

    // Nao e necessario, somente para experimentacao...
//    public Optional<Usuario> retornarUsuarioLogado() {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null && authentication.isAuthenticated()) {
//            String username = authentication.getName();
//            return Optional.ofNullable(usuarioRepository.findByUsername(username)
//                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado")));
//        }
//
//        return Optional.empty();
//
//    }




//implements UserDetailsService {
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Usuario user = usuarioRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
//
//        return User.builder()
//                .username(user.getUsername())
//                .password(user.getPassword())
//                .roles(user.getRole())
//                .build();
//
//    }

}
