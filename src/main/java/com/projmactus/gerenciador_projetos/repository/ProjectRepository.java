package com.projmactus.gerenciador_projetos.repository;

import com.projmactus.gerenciador_projetos.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Repositório para a entidade Project
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {


}
