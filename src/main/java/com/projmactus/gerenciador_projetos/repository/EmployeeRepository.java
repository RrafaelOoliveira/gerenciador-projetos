package com.projmactus.gerenciador_projetos.repository;

import com.projmactus.gerenciador_projetos.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Repositório para a entidade Employee
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
   
}
