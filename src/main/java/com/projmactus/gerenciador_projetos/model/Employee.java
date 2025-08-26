package com.projmactus.gerenciador_projetos.model;

import jakarta.persistence.*;
import java.math.BigDecimal;


 //Entidade que representa um funcionário no sistema.

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_employee_generator") //Gera o ID automaticamente
    @SequenceGenerator(name = "seq_employee_generator", sequenceName = "seq_employee", allocationSize = 1) //Gera o ID automaticamente
    private Long id;

    @Column(name = "name", nullable = false, length = 300) //Não pode ser nulo e deve ter no máximo 300 caracteres
    private String name;

    @Column(precision = 10, scale = 2) //Precisão de 10 dígitos e 2 casas decimais
    private BigDecimal salary;

    
    public Employee() {
    }

    public Employee(String name, BigDecimal salary) {
        this.name = name;
        this.salary = salary;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        return id != null ? id.equals(employee.id) : employee.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
