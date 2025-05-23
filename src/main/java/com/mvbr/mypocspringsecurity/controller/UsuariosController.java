package com.mvbr.mypocspringsecurity.controller;

import com.mvbr.mypocspringsecurity.model.Usuario;
import com.mvbr.mypocspringsecurity.service.UsuarioService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuariosController {

    private final UsuarioService usuarioService;

    public UsuariosController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registrar")
    public void registrar(@RequestBody Usuario usuario) {
        this.usuarioService.registrar(usuario);
    }

}
