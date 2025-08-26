package com.projmactus.gerenciador_projetos.repository;

import com.projmactus.gerenciador_projetos.model.LOG;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Repositório para a entidade LOG
@Repository
public interface LogRepository extends JpaRepository<LOG, Long> {

}
