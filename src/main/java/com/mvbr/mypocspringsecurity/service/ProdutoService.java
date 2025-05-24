package com.mvbr.mypocspringsecurity.service;

import com.mvbr.mypocspringsecurity.model.Produto;
import com.mvbr.mypocspringsecurity.model.Usuario;
import com.mvbr.mypocspringsecurity.repository.ProdutoRepository;
import com.mvbr.mypocspringsecurity.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.mvbr.mypocspringsecurity.config.constants.SecurityHoleConstants.HOLE_ADMIN;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;

    public ProdutoService(ProdutoRepository produtoRepository, UsuarioRepository usuarioRepository) {
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Produto cadastrar(Produto produto) {

        // obtem o username do usuario autenticado...
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // busca usuario do banco...
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // define o proprietario do produto...
        produto.setProprietario(usuario);

        // salva o produto com o seu respectivo proprietario...
        return produtoRepository.save(produto);

    }

    /*
        REGRA:
        * Usuário do tipo USER só pode ver os próprios produtos...
        * Usuário do tipo ADMIN pode ver todos os produtos...
     */
    public List<Produto> listar() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch((auth) -> auth.getAuthority().equals(HOLE_ADMIN));

        if (isAdmin) {
            return produtoRepository.findAll();
        } else {
            return produtoRepository.findByProprietarioUsername(username);
        }

    }

}






