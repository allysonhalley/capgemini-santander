package com.capgemini.santander.transacao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.santander.cliente.Cliente;
import com.capgemini.santander.cliente.ClienteRepository;

import jakarta.transaction.TransactionScoped;
import lombok.AllArgsConstructor;
import lombok.val;


@RestController
@AllArgsConstructor
public class TransacaoController {

    TransacaoRepository transacaoRepository;
    ClienteRepository clienteRepository;    

    @GetMapping("/transacao")
    public List<Transacao> getAllTransacoes(){
        return transacaoRepository.findAll();
    }

    @PostMapping("/transacao/depositar")
    public Transacao postDepositar(@RequestBody Long clienteId, @RequestBody BigDecimal valor){
        
        Cliente cliente = clienteRepository.findById(clienteId).get();
        atualizarSaldo(cliente, valor);
        Transacao transacao = new Transacao(atualizarSaldo(cliente, valor).getId(), true, valor);

        return transacaoRepository.save(transacao);
    }
    
    @PostMapping(value="transacao/sacar")
    public Transacao postSacar(@RequestBody Long clienteId, @RequestBody BigDecimal valor){        

        Cliente cliente = clienteRepository.findById(clienteId).get();        
        if (!cliente.getPlanoExclusive()) {
            taxasSaque(cliente, valor);
        }
        return transacaoRepository.save(new Transacao(clienteId, false, valor));
    }

    @PostMapping(value="transacao/by-cliente")
    public List<Transacao> postFindByCliente(@RequestBody Cliente cliente) {        
        return transacaoRepository.findByCliente(cliente);
    }
    
    private Cliente atualizarSaldo(Cliente cliente, BigDecimal valor){

        valor.add(cliente.getSaldo());
        cliente.setSaldo(valor);

        return clienteRepository.save(cliente);
    }

    private Cliente taxasSaque(Cliente cliente, BigDecimal valor){

        BigDecimal bg100 = new BigDecimal("100.00");
        BigDecimal bg300 = new BigDecimal("300.00");
        BigDecimal tx004 = new BigDecimal("1.004");
        BigDecimal tx01 = new BigDecimal("1.1");
        BigDecimal saldo = cliente.getSaldo();

        if (cliente.getPlanoExclusive()) {
            saldo = saldo.subtract(valor);
            cliente.setSaldo(saldo);
        }else{
            switch (valor.compareTo(bg100)) {
                case -1:                    
                    cliente.setSaldo(saldo.subtract(valor));
                    break;
                case 0:
                    cliente.setSaldo(saldo.subtract(valor));
                    break;
                case 1:
                    if (valor.compareTo(bg300) == -1 || valor.compareTo(bg300) == 0) {
                        saldo = saldo.subtract(valor.multiply(tx004));                        
                        cliente.setSaldo(saldo);
                    }else{
                        saldo = saldo.subtract(valor.multiply(tx01));                        
                        cliente.setSaldo(valor);
                    }
                    break;
                default:
                    break;
            }

        }
        
        return clienteRepository.save(cliente);

    }
    
}
