package com.capgemini.santander.cliente;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class ClienteController {

    ClienteRepository repository;

    @GetMapping("/cliente")
    public List<Cliente> getAllClientes(){
        return repository.findAll();
    }

    @PostMapping("/cliente")
    public Cliente saveCliente(@RequestBody Cliente cliente){
        return repository.save(cliente);
    }

}
