package com.mvbr.mypocspringsecurity.service;

import com.mvbr.mypocspringsecurity.model.Usuario;
import com.mvbr.mypocspringsecurity.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public void registrar(Usuario usuario) {
        Usuario user = new Usuario();
        user.setUsername(usuario.getUsername());
        user.setPassword(passwordEncoder.encode(usuario.getPassword()));
        user.setRole("USER");
        this.usuarioRepository.save(user);
    }

    // Nao e necessario, somente para experimentacao...
    public void autenticarUsuarioProgramaticamente(String username, String password) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        authenticationManager.authenticate(authentication);
    }

    // Nao e necessario, somente para experimentacao...
    public Optional<Usuario> retornarUsuarioLogado() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return Optional.ofNullable(usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado")));
        }

        return Optional.empty();

    }


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
