package com.mvbr.mypocspringsecurity.controller;

import com.mvbr.mypocspringsecurity.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
    Após as mudanças, o único método exposto no seu UsuariosController será o de registro de
    usuário (/api/v1/usuarios/registrar).
    O login e logout serão tratados automaticamente pelos endpoints padrão do Spring Security (/login e /logout).
    Seu controller está adequado para esse cenário.
 */
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuariosController {

    private final UsuarioService usuarioService;

    public UsuariosController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /*
        O método registrar (registro de usuário) precisa ser implementado programaticamente no seu controller/serviço,
        pois o Spring Security não fornece um endpoint pronto para cadastro de novos usuários. O filtro do
        Spring Security só gerencia autenticação (login/logout) e autorização, não o registro.

        Portanto, o cadastro de usuários deve ser feito manualmente, como está no seu método. Apenas login e logout
        podem (e devem) ser tratados pelo filtro do Spring Security.
     */
    @PostMapping("/registrar")
    public ResponseEntity<String> registrar(@RequestParam String username, @RequestParam String password) {
        String result = this.usuarioService.registrar(username, password);
        return ResponseEntity.ok(result);
    }

    /*
        OBSERVACAO:

        Você não precisa (e nem deve) criar um endpoint /api/v1/usuarios/login manualmente.
        Use o endpoint {{host}}/login já fornecido pelo Spring Security.

        O Spring Security já fornece endpoints de login (/login) por padrão usando form ou HTTP Basic.
        Se quiser autenticação via API, seu método funciona, mas normalmente o login é feito via filtro
        de autenticação, não por controller.

        A prática mais segura e mais usada nas empresas é deixar o próprio Spring Security gerenciar o login,
        utilizando seus filtros e endpoints padrão (/login), seja via formulário, HTTP Basic ou JWT.

        Motivos:

        * O Spring Security já implementa todas as proteções contra ataques comuns (CSRF, brute force, session fixation, etc).
        * Reduz riscos de falhas de segurança por implementação manual.
        * Facilita integração com autenticação baseada em token (JWT), OAuth2, SSO, etc.
        * Permite customização avançada apenas quando necessário, sem reinventar o básico.

        Resumindo: Prefira sempre o login padrão do Spring Security e só personalize se houver uma necessidade real e
        bem justificada.

     */

//    @PostMapping("/entrar")
//    public ResponseEntity<String> logar(@RequestParam String username, @RequestParam String password) {
//        String result = this.usuarioService.logar(username, password);
//        return ResponseEntity.ok(result);
//    }



    /*
        O ideal é deixar o próprio Spring Security gerenciar o logout, pois ele já trata limpeza de sessão,
        cookies e outros detalhes de segurança automaticamente.
     */
//    @RequestMapping("/sair")
//    public ResponseEntity<String> sair() {
//        String result = this.usuarioService.sair();
//        return ResponseEntity.ok(result);
//    }

}
