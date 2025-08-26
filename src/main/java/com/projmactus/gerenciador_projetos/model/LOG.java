package com.projmactus.gerenciador_projetos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

//Entidade que representa um log de operação no sistema.

@Entity
@Table(name = "LOG")
public class LOG {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Gera o ID automaticamente
    private Long id;
    @Column(nullable = false) //Não pode ser nulo
    private String operation;
    @Column(nullable = false) //Não pode ser nulo
    private String details;
    @Column(nullable = false) //Não pode ser nulo
    private LocalDateTime timestamp; 

    public LOG() {
    }

    public LOG(String operation, String details) {
        this.operation = operation;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
