package com.capgemini.santander.transacao;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.capgemini.santander.cliente.Cliente;


public interface TransacaoRepository extends JpaRepository<Transacao, Long>{

    List<Transacao> findByCliente(Cliente cliente);
    
}
