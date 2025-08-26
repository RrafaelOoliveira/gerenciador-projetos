package com.projmactus.gerenciador_projetos.controller;

import com.projmactus.gerenciador_projetos.model.Employee;
import com.projmactus.gerenciador_projetos.model.EMP_PROJ;
import com.projmactus.gerenciador_projetos.model.LOG;
import com.projmactus.gerenciador_projetos.model.Project;
import com.projmactus.gerenciador_projetos.service.ProjectEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller REST para gerenciar funcionários e projetos.
 * Fornece endpoints para operações CRUD e regras de negócio.
 */
@RestController //Indica que esta classe é um controlador REST
@RequestMapping("/api") //Mapeia a URL base para este controlador
public class ProjectEmployeeController {
    private final ProjectEmployeeService service; //Serviço para gerenciar funcionários e projetos

    @Autowired
    public ProjectEmployeeController(ProjectEmployeeService service) {
        this.service = service;
    }

    @PostMapping("/employees") //Mapeia a URL para adicionar um funcionário
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        Employee savedEmployee = service.saveEmployee(employee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/employees") //Mapeia a URL para buscar todos os funcionários
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = service.findAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/employees/{id}") //Mapeia a URL para buscar um funcionário por ID
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employee = service.findEmployeeById(id);
        return employee.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/employees/{id}") //Mapeia a URL para atualizar um funcionário
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        employee.setId(id);
        Employee updatedEmployee = service.updateEmployee(employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/employees/{id}") //Mapeia a URL para deletar um funcionário
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        service.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    // Serviço para incluir um projeto
    @PostMapping("/projects")
    public ResponseEntity<Project> addProject(@RequestBody Project project) {
        Project savedProject = service.saveProject(project);
        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    @GetMapping("/projects") //Mapeia a URL para buscar todos os projetos
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = service.findAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/projects/{id}") //Mapeia a URL para buscar um projeto por ID
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Optional<Project> project = service.findProjectById(id);
        return project.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/projects/{id}") //Mapeia a URL para atualizar um projeto
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project project) {
        project.setId(id);
        Project updatedProject = service.updateProject(project);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/projects/{id}") //Mapeia a URL para deletar um projeto
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        service.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    // Serviço para adicionar um projeto a um funcionário
    // O ID do funcionário será informado na URL do serviço
    @PostMapping("/employees/{employeeId}/projects/{projectId}") //Mapeia a URL para adicionar um projeto a um funcionário
    public ResponseEntity<?> addProjectToEmployee(@PathVariable Long employeeId, @PathVariable Long projectId) {
        try {
            EMP_PROJ association = service.addEmployeeToProject(employeeId, projectId);
            return new ResponseEntity<>(association, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            // Captura a exceção da regra de negócio (ex: mais de 2 projetos)
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            // Captura a exceção de funcionário ou projeto não encontrados
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Serviço para recuperar todos os funcionários que atuem em mais de um projeto
    @GetMapping("/employees/multiple-projects") 
    public ResponseEntity<?> getEmployeesOnMultipleProjects() {
        return ResponseEntity.ok(service.getEmployeesOnMultipleProjects());
    }

    // Serviço para recuperar todos os logs do sistema
    @GetMapping("/logs")
    public ResponseEntity<List<LOG>> getAllLogs() {
        List<LOG> logs = service.findAllLogs();
        return ResponseEntity.ok(logs);
    }

    // Serviço para recuperar estatísticas do sistema
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getSystemStats() {
        long totalEmployees = service.countEmployees();
        List<Object[]> employeesOnMultipleProjects = service.getEmployeesOnMultipleProjects();
        
        Map<String, Object> stats = Map.of(
            "totalEmployees", totalEmployees,
            "employeesOnMultipleProjects", employeesOnMultipleProjects.size()
        );
        
        return ResponseEntity.ok(stats);
    }
}
