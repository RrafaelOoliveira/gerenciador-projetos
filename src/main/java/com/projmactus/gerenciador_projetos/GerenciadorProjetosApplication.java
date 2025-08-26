package com.projmactus.gerenciador_projetos;

import com.projmactus.gerenciador_projetos.model.*;
import com.projmactus.gerenciador_projetos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//Classe principal que inicia o aplicativo Spring Boot.

@Service //Indica que esta classe é um serviço Spring
public class GerenciadorProjetosApplication {
	private final EmployeeRepository employeeRepository; //Repositório de funcionários
	private final ProjectRepository projectRepository; //Repositório de projetos
	private final EmpProjRepository empProjRepository; //Repositório de associações funcionário-projeto
	private final LogRepository logRepository; //Repositório de logs

	@Autowired //Injeção de dependência para o repositório de funcionários
	public GerenciadorProjetosApplication(EmployeeRepository employeeRepository,
			ProjectRepository projectRepository,
			EmpProjRepository empProjRepository,
			LogRepository logRepository) { //Injeção de dependência para o repositório de logs
		this.employeeRepository = employeeRepository;
		this.projectRepository = projectRepository;
		this.empProjRepository = empProjRepository;
		this.logRepository = logRepository;
	}

	// Método para adicionar um funcionário a um projeto, aplicando as regras de
	// negócio
	@Transactional
	public EMP_PROJ addEmployeeToProject(Long employeeId, Long projectId) {
		// Regra de negócio: Adicionar em 3 projetos o mesmo funcionário
		// A regra é que o funcionário pode ter NO MÁXIMO 2 projetos.
		List<EMP_PROJ> projectsOfEmployee = empProjRepository.findByEmployeeId(employeeId);
		if (projectsOfEmployee.size() >= 2) {
			logOperation("FAILURE", "Tentativa de adicionar funcionário " + employeeId + " ao projeto " + projectId
					+ ": Limite de 2 projetos excedido.");
			throw new IllegalStateException("Funcionário já está em 2 projetos. Não é possível adicionar mais.");
		}
		// Regra de negócio: Adicionar em um projeto 1 funcionário com sucesso
		// Verificando se a associação já existe (evita duplicação)
		Optional<EMP_PROJ> existingAssociation = empProjRepository.findById(new EmpProjId(employeeId, projectId));
		if (existingAssociation.isPresent()) {
			logOperation("FAILURE", "Tentativa de adicionar funcionário " + employeeId + " ao projeto " + projectId
					+ ": Associação já existe.");
			throw new IllegalStateException("Funcionário já está associado a este projeto.");
		}
		// Recupera as entidades para criar a associação
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado."));
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado."));
		// Cria a nova associação
		EMP_PROJ newAssociation = new EMP_PROJ(employee, project);
		EMP_PROJ savedAssociation = empProjRepository.save(newAssociation);
		logOperation("SUCCESS", "Funcionário " + employeeId + " adicionado ao projeto " + projectId + " com sucesso.");
		return savedAssociation;
	}

	// Método para recuperar todos os funcionários que atuem em mais de um projeto
	public List<Object[]> getEmployeesOnMultipleProjects() {
		return empProjRepository.findEmployeesOnMultipleProjects();
	}

	// Método utilitário para registrar a operação de LOG
	private void logOperation(String operation, String details) {
		// A persistência do log deve ser independente da transação original
		// Por isso, vamos usar um método sem @Transactional para garantir que o log
		// seja salvo mesmo em caso de erro
		logRepository.save(new LOG(operation, details));
	}

}
