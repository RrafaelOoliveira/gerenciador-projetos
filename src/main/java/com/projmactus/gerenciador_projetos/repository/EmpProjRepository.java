package com.projmactus.gerenciador_projetos.repository;

import com.projmactus.gerenciador_projetos.model.EMP_PROJ;
import com.projmactus.gerenciador_projetos.model.EmpProjId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

//Repositório para a entidade EMP_PROJ
@Repository 
public interface EmpProjRepository extends JpaRepository<EMP_PROJ, EmpProjId> {

    // Busca todos os projetos de um funcionário pelo ID do funcionário

    List<EMP_PROJ> findByEmployeeId(Long employeeId);

    // Busca funcionários que trabalham em mais projetos

    @Query("SELECT e.id, e.name, COUNT(ep) as projectCount FROM Employee e " +
            "JOIN EMP_PROJ ep ON e.id = ep.employee.id " +
            "GROUP BY e.id, e.name " +
            "HAVING COUNT(ep) > 1")
    List<Object[]> findEmployeesOnMultipleProjects();
}
