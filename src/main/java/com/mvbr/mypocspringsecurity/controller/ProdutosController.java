package com.mvbr.mypocspringsecurity.controller;

import com.mvbr.mypocspringsecurity.model.Produto;
import com.mvbr.mypocspringsecurity.service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/produtos")
public class ProdutosController {

    private final ProdutoService produtoService;

    public ProdutosController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Produto> cadastrar(@RequestBody Produto produto) {
        Produto result = this.produtoService.cadastrar(produto);
        return ResponseEntity.ok(result);
    }

    // todo: implementar response entity...
    @GetMapping("/listar")
    public ResponseEntity<List<Produto>> listar() {
        List<Produto> result = this.produtoService.listar();
        return ResponseEntity.ok(result);
    }

}
