package com.projmactus.gerenciador_projetos.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

//Entidade que representa a associação entre um funcionário e um projeto.

@Entity
@Table(name = "EMP_PROJ")
public class EMP_PROJ {

    @EmbeddedId
    private EmpProjId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("empId") // Mapeia o campo "empId" da chave composta para esta relação
    @JoinColumn(name = "emp_id") //Mapeia o campo "emp_id" da chave composta para esta relação
    @JsonIgnore //Ignora a serialização do funcionário para evitar loops
    private Employee employee; 

    @ManyToOne(fetch = FetchType.LAZY) 
    @MapsId("projId") // Mapeia o campo "projId" da chave composta para esta relação
    @JoinColumn(name = "proj_id") //Mapeia o campo "proj_id" da chave composta para esta relação
    @JsonIgnore //Ignora a serialização do projeto para evitar loops
    private Project project; 

    // Construtores, Getters e Setters
    public EMP_PROJ() {
    }

    public EMP_PROJ(Employee employee, Project project) {
        this.employee = employee;
        this.project = project;
        this.id = new EmpProjId(employee.getId(), project.getId());
    }

    // Getters e Setters
    public EmpProjId getId() {
        return id;
    }

    public void setId(EmpProjId id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
