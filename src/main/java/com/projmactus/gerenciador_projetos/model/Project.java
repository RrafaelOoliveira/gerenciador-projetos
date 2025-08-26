package com.projmactus.gerenciador_projetos.model;

import jakarta.persistence.*;
//Entidade que representa um projeto no sistema.
@Entity 
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_project_generator")
    @SequenceGenerator(name = "seq_project_generator", sequenceName = "seq_project", allocationSize = 1)
    private Long id;
    @Column(unique = true, nullable = false, length = 300)
    private String name;

    
    public Project() {}

    public Project(String name) {
        this.name = name;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
