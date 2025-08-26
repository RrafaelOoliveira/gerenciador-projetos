package com.projmactus.gerenciador_projetos.service;

import com.projmactus.gerenciador_projetos.model.Employee;
import com.projmactus.gerenciador_projetos.model.LOG;
import com.projmactus.gerenciador_projetos.model.Project;
import com.projmactus.gerenciador_projetos.model.EMP_PROJ;
import com.projmactus.gerenciador_projetos.model.EmpProjId;
import com.projmactus.gerenciador_projetos.repository.EmployeeRepository;
import com.projmactus.gerenciador_projetos.repository.LogRepository;
import com.projmactus.gerenciador_projetos.repository.ProjectRepository;
import com.projmactus.gerenciador_projetos.repository.EmpProjRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável por gerenciar operações relacionadas a funcionários e
 * projetos.
 * Fornece métodos para CRUD de funcionários e log de operações.
 */
@Service
public class ProjectEmployeeService {

    @Autowired //Injeção de dependência para o repositório de funcionários
    private EmployeeRepository employeeRepository;

    @Autowired //Injeção de dependência para o repositório de logs
    private LogRepository logRepository;

    @Autowired //Injeção de dependência para o repositório de projetos
    private ProjectRepository projectRepository;

    @Autowired //Injeção de dependência para o repositório de associações funcionário-projeto
    private EmpProjRepository empProjRepository;

     //Salva um novo funcionário no sistema
    
    public Employee saveEmployee(Employee employee) {
        // Validação de tamanho mínimo do nome
        if (employee.getName() == null || employee.getName().trim().length() < 2) {
            throw new IllegalArgumentException("Nome do funcionário deve ter pelo menos 2 caracteres");
        }
        
        Employee savedEmployee = employeeRepository.save(employee);
        logOperation("CREATE", "Funcionário criado: " + employee.getName());
        return savedEmployee;
    }

    // Busca um funcionário por ID
     
    public Optional<Employee> findEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    // Lista todos os funcionários
    
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    // Atualiza um funcionário existente
    
    public Employee updateEmployee(Employee employee) {
        if (employee.getId() == null) {
            throw new IllegalArgumentException("ID do funcionário não pode ser nulo para atualização");
        }

        Employee updatedEmployee = employeeRepository.save(employee);
        logOperation("UPDATE", "Funcionário atualizado: " + employee.getName() + " (ID: " + employee.getId() + ")");
        return updatedEmployee;
    }

    // Remove um funcionário do sistema
    
    public void deleteEmployee(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            String employeeName = employee.get().getName();
            employeeRepository.deleteById(id);
            logOperation("DELETE", "Funcionário removido: " + employeeName + " (ID: " + id + ")");
        } else {
            throw new IllegalArgumentException("Funcionário não encontrado com ID: " + id);
        }
    }

    // Verifica se um funcionário existe
    
    public boolean employeeExists(Long id) {
        return employeeRepository.existsById(id);
    }

    // Conta o total de funcionários
    
    public long countEmployees() {
        return employeeRepository.count();
    }

    // Registra uma operação no log do sistema
    
    private void logOperation(String operation, String details) {
        LOG log = new LOG(operation, details);
        logRepository.save(log);
    }

    // Busca todos os logs
    
    public List<LOG> findAllLogs() {
        return logRepository.findAll();
    }

    @Transactional
    public Project saveProject(Project project) {
        // Validação de tamanho mínimo do nome
        if (project.getName() == null || project.getName().trim().length() < 2) {
            throw new IllegalArgumentException("Nome do projeto deve ter pelo menos 2 caracteres");
        }
        
        Project savedProject = projectRepository.save(project);
        logOperation("CREATE", "Projeto criado: " + project.getName());
        return savedProject;
    }

    // Atualiza um projeto existente
    
    @Transactional
    public Project updateProject(Project project) {
        if (project.getId() == null) {
            throw new IllegalArgumentException("ID do projeto não pode ser nulo para atualização");
        }
        
        Project updatedProject = projectRepository.save(project);
        logOperation("UPDATE", "Projeto atualizado: " + project.getName() + " (ID: " + project.getId() + ")");
        return updatedProject;
    }

    // Remove um projeto do sistema
    
    @Transactional
    public void deleteProject(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            String projectName = project.get().getName();
            projectRepository.deleteById(id);
            logOperation("DELETE", "Projeto removido: " + projectName + " (ID: " + id + ")");
        } else {
            throw new IllegalArgumentException("Projeto não encontrado com ID: " + id);
        }
    }

    // Adiciona um funcionário a um projeto
    
    @Transactional
    public EMP_PROJ addEmployeeToProject(Long employeeId, Long projectId) {
        // Verifica se funcionário já está em 2 projetos
        List<EMP_PROJ> projectsOfEmployee = empProjRepository.findByEmployeeId(employeeId);
        if (projectsOfEmployee.size() >= 2) {
            throw new IllegalStateException("Funcionário já está em 2 projetos. Não é possível adicionar mais.");
        }

        // Verifica se a associação já existe
        Optional<EMP_PROJ> existingAssociation = empProjRepository.findById(new EmpProjId(employeeId, projectId));
        if (existingAssociation.isPresent()) {
            throw new IllegalStateException("Funcionário já está associado a este projeto.");
        }

        // Recupera as entidades
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado."));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado."));

        // Cria a nova associação
        EMP_PROJ newAssociation = new EMP_PROJ(employee, project);
        EMP_PROJ savedAssociation = empProjRepository.save(newAssociation);
        logOperation("ASSOCIATION", "Funcionário " + employee.getName() + " associado ao projeto " + project.getName());
        return savedAssociation;
    }

    // Construtor para injeção de dependência
    
    @Autowired
    public ProjectEmployeeService(ProjectRepository projectRepository, EmployeeRepository employeeRepository,
            EmpProjRepository empProjRepository, LogRepository logRepository) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.empProjRepository = empProjRepository;
        this.logRepository = logRepository;
    }

    public List<LOG> getAllLogs() {
        return logRepository.findAll();
    }

    public List<Object[]> getEmployeesOnMultipleProjects() {
        return empProjRepository.findEmployeesOnMultipleProjects();
    }

    // Lista todos os projetos
    
    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    // Busca um projeto por ID
    
    public Optional<Project> findProjectById(Long id) {
        return projectRepository.findById(id);
    }
}
