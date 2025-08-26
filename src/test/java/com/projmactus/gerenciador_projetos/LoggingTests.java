package com.projmactus.gerenciador_projetos;

import com.projmactus.gerenciador_projetos.model.Employee;
import com.projmactus.gerenciador_projetos.model.LOG;
import com.projmactus.gerenciador_projetos.model.Project;
import com.projmactus.gerenciador_projetos.repository.LogRepository;
import com.projmactus.gerenciador_projetos.service.ProjectEmployeeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
 
// Testes de logging
@SpringBootTest
class LoggingTests {

    @Autowired
    private ProjectEmployeeService service;

    @Autowired
    private LogRepository logRepository;

    private Employee testEmployee;
    private Project testProject;

    @BeforeEach
    @Transactional
    void setUp() {
        // Limpa os logs antes de cada teste
        logRepository.deleteAll();

        // Cria entidades de teste com nomes únicos baseados no timestamp
        String timestamp = String.valueOf(System.currentTimeMillis());
        testEmployee = new Employee("Funcionário Teste " + timestamp, new BigDecimal("50000.0"));
        testProject = new Project("Projeto Teste " + timestamp);
    }

    @Test
    void shouldLogEmployeeCreation() {
        // Ação
        Employee savedEmployee = service.saveEmployee(testEmployee);

        // Verificação
        List<LOG> logs = service.findAllLogs();
        Assertions.assertEquals(1, logs.size(), "Deve haver um log de criação");

        LOG log = logs.get(0);
        Assertions.assertEquals("CREATE", log.getOperation(), "A operação deve ser CREATE");
        Assertions.assertTrue(log.getDetails().contains("Funcionário criado"), 
            "Os detalhes devem conter 'Funcionário criado'");
        Assertions.assertTrue(log.getDetails().contains("Funcionário Teste"), 
            "Os detalhes devem conter o nome do funcionário");
    }

    @Test
    void shouldLogEmployeeUpdate() {
        // Preparação: Cria um funcionário primeiro
        Employee savedEmployee = service.saveEmployee(testEmployee);
        logRepository.deleteAll(); // Limpa logs de criação

        // Ação: Atualiza o funcionário
        savedEmployee.setName("Funcionário Atualizado");
        service.updateEmployee(savedEmployee);

        // Verificação
        List<LOG> logs = service.findAllLogs();
        Assertions.assertEquals(1, logs.size(), "Deve haver um log de atualização");

        LOG log = logs.get(0);
        Assertions.assertEquals("UPDATE", log.getOperation(), "A operação deve ser UPDATE");
        Assertions.assertTrue(log.getDetails().contains("Funcionário atualizado"), 
            "Os detalhes devem conter 'Funcionário atualizado'");
        Assertions.assertTrue(log.getDetails().contains("Funcionário Atualizado"), 
            "Os detalhes devem conter o nome atualizado");
    }

    @Test
    void shouldLogEmployeeDeletion() {
        // Preparação: Cria um funcionário primeiro
        Employee savedEmployee = service.saveEmployee(testEmployee);
        logRepository.deleteAll(); // Limpa logs de criação

        // Ação: Remove o funcionário
        service.deleteEmployee(savedEmployee.getId());

        // Verificação
        List<LOG> logs = service.findAllLogs();
        Assertions.assertEquals(1, logs.size(), "Deve haver um log de exclusão");

        LOG log = logs.get(0);
        Assertions.assertEquals("DELETE", log.getOperation(), "A operação deve ser DELETE");
        Assertions.assertTrue(log.getDetails().contains("Funcionário removido"), 
            "Os detalhes devem conter 'Funcionário removido'");
        Assertions.assertTrue(log.getDetails().contains("Funcionário Teste"), 
            "Os detalhes devem conter o nome do funcionário");
    }

    @Test
    void shouldLogProjectCreation() {
        // Ação
        Project savedProject = service.saveProject(testProject);

        // Verificação
        List<LOG> logs = service.findAllLogs();
        Assertions.assertEquals(1, logs.size(), "Deve haver um log de criação de projeto");

        LOG log = logs.get(0);
        Assertions.assertEquals("CREATE", log.getOperation(), "A operação deve ser CREATE");
        Assertions.assertTrue(log.getDetails().contains("Projeto criado"), 
            "Os detalhes devem conter 'Projeto criado'");
        Assertions.assertTrue(log.getDetails().contains("Projeto Teste"), 
            "Os detalhes devem conter o nome do projeto");
    }

    @Test
    void shouldLogProjectUpdate() {
        // Preparação: Cria um projeto primeiro
        Project savedProject = service.saveProject(testProject);
        logRepository.deleteAll(); // Limpa logs de criação

        // Ação: Atualiza o projeto
        savedProject.setName("Projeto Atualizado");
        service.updateProject(savedProject);

        // Verificação
        List<LOG> logs = service.findAllLogs();
        Assertions.assertEquals(1, logs.size(), "Deve haver um log de atualização de projeto");

        LOG log = logs.get(0);
        Assertions.assertEquals("UPDATE", log.getOperation(), "A operação deve ser UPDATE");
        Assertions.assertTrue(log.getDetails().contains("Projeto atualizado"), 
            "Os detalhes devem conter 'Projeto atualizado'");
        Assertions.assertTrue(log.getDetails().contains("Projeto Atualizado"), 
            "Os detalhes devem conter o nome atualizado");
    }

    @Test
    void shouldLogProjectDeletion() {
        // Preparação: Cria um projeto primeiro
        Project savedProject = service.saveProject(testProject);
        logRepository.deleteAll(); // Limpa logs de criação

        // Ação: Remove o projeto
        service.deleteProject(savedProject.getId());

        // Verificação
        List<LOG> logs = service.findAllLogs();
        Assertions.assertEquals(1, logs.size(), "Deve haver um log de exclusão de projeto");

        LOG log = logs.get(0);
        Assertions.assertEquals("DELETE", log.getOperation(), "A operação deve ser DELETE");
        Assertions.assertTrue(log.getDetails().contains("Projeto removido"), 
            "Os detalhes devem conter 'Projeto removido'");
        Assertions.assertTrue(log.getDetails().contains("Projeto Teste"), 
            "Os detalhes devem conter o nome do projeto");
    }

    @Test
    void shouldLogEmployeeProjectAssociation() {
        // Preparação: Cria funcionário e projeto
        Employee savedEmployee = service.saveEmployee(testEmployee);
        Project savedProject = service.saveProject(testProject);
        logRepository.deleteAll(); // Limpa logs de criação

        // Ação: Associa funcionário ao projeto
        service.addEmployeeToProject(savedEmployee.getId(), savedProject.getId());

        // Verificação
        List<LOG> logs = service.findAllLogs();
        Assertions.assertEquals(1, logs.size(), "Deve haver um log de associação");

        LOG log = logs.get(0);
        Assertions.assertEquals("ASSOCIATION", log.getOperation(), "A operação deve ser ASSOCIATION");
        Assertions.assertTrue(log.getDetails().contains("associado"), 
            "Os detalhes devem conter 'associado'");
        Assertions.assertTrue(log.getDetails().contains("Funcionário Teste"), 
            "Os detalhes devem conter o nome do funcionário");
        Assertions.assertTrue(log.getDetails().contains("Projeto Teste"), 
            "Os detalhes devem conter o nome do projeto");
    }

    @Test
    void shouldLogMultipleOperations() {
        // Ação: Executa múltiplas operações
        Employee savedEmployee = service.saveEmployee(testEmployee);
        Project savedProject = service.saveProject(testProject);
        service.addEmployeeToProject(savedEmployee.getId(), savedProject.getId());

        // Verificação
        List<LOG> logs = service.findAllLogs();
        Assertions.assertEquals(3, logs.size(), "Deve haver 3 logs (criação funcionário, criação projeto, associação)");

        // Verifica se há logs de CREATE
        long createLogs = logs.stream()
            .filter(log -> "CREATE".equals(log.getOperation()))
            .count();
        Assertions.assertEquals(2, createLogs, "Deve haver 2 logs de CREATE");

        // Verifica se há log de ASSOCIATION
        long associationLogs = logs.stream()
            .filter(log -> "ASSOCIATION".equals(log.getOperation()))
            .count();
        Assertions.assertEquals(1, associationLogs, "Deve haver 1 log de ASSOCIATION");
    }
}
