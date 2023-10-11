package com.capgemini.santander.transacao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long clienteId;
    //True = Deposito False = Saque
    private Boolean tipo;
    private BigDecimal valor;
    private Timestamp dataTransacao;

    public Transacao(Long clienteId, Boolean tipo, BigDecimal valor) {        
        this.clienteId = clienteId;
        this.tipo = tipo;
        this.valor = valor;
        this.dataTransacao = Timestamp.from(Instant.now());
    }

    
    
}
