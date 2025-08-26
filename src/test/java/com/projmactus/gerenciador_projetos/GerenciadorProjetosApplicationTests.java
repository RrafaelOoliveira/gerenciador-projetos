package com.projmactus.gerenciador_projetos;

import com.projmactus.gerenciador_projetos.model.Employee;
import com.projmactus.gerenciador_projetos.model.Project;
import com.projmactus.gerenciador_projetos.service.ProjectEmployeeService;
import com.projmactus.gerenciador_projetos.repository.EmployeeRepository;
import com.projmactus.gerenciador_projetos.repository.ProjectRepository;
import com.projmactus.gerenciador_projetos.repository.EmpProjRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class GerenciadorProjetosApplicationTests {

	@Autowired
	private ProjectEmployeeService service;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private EmpProjRepository empProjRepository;

	private Employee testEmployee;
	private Project testProject1;
	private Project testProject2;
	private Project testProject3;

	@BeforeEach
	@Transactional
	void setUp() {
		// Limpa o banco de dados antes de cada teste
		empProjRepository.deleteAll();
		employeeRepository.deleteAll();
		projectRepository.deleteAll();

		// Cria e salva as entidades base para os testes
		testEmployee = employeeRepository.save(new Employee("Rafael Oliveira", new BigDecimal("50000.0")));
		testProject1 = projectRepository.save(new Project("Projeto A"));
		testProject2 = projectRepository.save(new Project("Projeto B"));
		testProject3 = projectRepository.save(new Project("Projeto C"));
	}

	// Teste 1: Verificar se o contexto da aplicação carrega corretamente
	@Test
	void contextLoads() {
		Assertions.assertNotNull(service, "O serviço deve ser injetado corretamente");
		Assertions.assertNotNull(employeeRepository, "O repositório de funcionários deve ser injetado corretamente");
		Assertions.assertNotNull(projectRepository, "O repositório de projetos deve ser injetado corretamente");
		Assertions.assertNotNull(empProjRepository, "O repositório de associações deve ser injetado corretamente");
	}

	// Teste 2: Adicionar um funcionário a um projeto
	@Test
	void shouldAddEmployeeToProjectSuccessfully() {
		// Ação
		service.addEmployeeToProject(testEmployee.getId(), testProject1.getId());

		// Verificação
		long associationsCount = empProjRepository.count();
		Assertions.assertEquals(1, associationsCount, "Deve haver uma associação no banco de dados.");
	}

	// Teste 3: Adicionar um funcionário a dois projetos
	@Test
	void shouldAddSameEmployeeToTwoProjectsSuccessfully() {
		// Ação
		service.addEmployeeToProject(testEmployee.getId(), testProject1.getId());
		service.addEmployeeToProject(testEmployee.getId(), testProject2.getId());

		// Verificação
		long associationsCount = empProjRepository.count();
		Assertions.assertEquals(2, associationsCount, "Deve haver duas associações no banco de dados.");
	}

	// Teste 4: Falhar ao adicionar funcionário a três projetos
	@Test
	void shouldFailToAddSameEmployeeToThreeProjects() {
		// Ação: Adiciona o funcionário aos dois primeiros projetos com sucesso
		service.addEmployeeToProject(testEmployee.getId(), testProject1.getId());
		service.addEmployeeToProject(testEmployee.getId(), testProject2.getId());

		// Verificação: A tentativa de adicionar ao terceiro projeto deve lançar uma exceção
		IllegalStateException thrown = Assertions.assertThrows(IllegalStateException.class, () -> {
			service.addEmployeeToProject(testEmployee.getId(), testProject3.getId());
		});
		
		// Verificação da mensagem de erro (corrigida para a mensagem real)
		Assertions.assertTrue(thrown.getMessage().contains("2 projetos"),
				"A mensagem de erro deve indicar que o limite de 2 projetos foi atingido.");
		
		long associationsCount = empProjRepository.count();
		Assertions.assertEquals(2, associationsCount, "A contagem de associações deve permanecer em 2.");
	}

	// Teste 5: Falhar ao adicionar funcionário ao mesmo projeto duas vezes
	@Test
	void shouldFailToAddEmployeeToSameProjectTwice() {
		// Ação: Adiciona o funcionário ao projeto uma vez
		service.addEmployeeToProject(testEmployee.getId(), testProject1.getId());

		// Verificação: A tentativa de adicionar ao mesmo projeto deve lançar uma exceção
		IllegalStateException thrown = Assertions.assertThrows(IllegalStateException.class, () -> {
			service.addEmployeeToProject(testEmployee.getId(), testProject1.getId());
		});
		
		Assertions.assertTrue(thrown.getMessage().contains("já está associado"),
				"A mensagem de erro deve indicar que o funcionário já está associado ao projeto.");
		
		long associationsCount = empProjRepository.count();
		Assertions.assertEquals(1, associationsCount, "A contagem de associações deve permanecer em 1.");
	}

	// Teste 6: Falhar ao adicionar funcionário inexistente
	@Test
	void shouldFailToAddNonExistentEmployee() {
		// Verificação: A tentativa de adicionar funcionário inexistente deve lançar uma exceção
		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			service.addEmployeeToProject(999L, testProject1.getId());
		});
		
		Assertions.assertTrue(thrown.getMessage().contains("não encontrado"),
				"A mensagem de erro deve indicar que o funcionário não foi encontrado.");
	}

	// Teste 7: Falhar ao adicionar projeto inexistente
	@Test
	void shouldFailToAddNonExistentProject() {
		// Verificação: A tentativa de adicionar projeto inexistente deve lançar uma exceção
		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			service.addEmployeeToProject(testEmployee.getId(), 999L);
		});
		
		Assertions.assertTrue(thrown.getMessage().contains("não encontrado"),
				"A mensagem de erro deve indicar que o projeto não foi encontrado.");
	}

	// Teste 8: Verificar funcionários em múltiplos projetos
	@Test
	void shouldFindEmployeesOnMultipleProjects() {
		// Ação: Adiciona funcionário a dois projetos
		service.addEmployeeToProject(testEmployee.getId(), testProject1.getId());
		service.addEmployeeToProject(testEmployee.getId(), testProject2.getId());

		// Verificação
		List<Object[]> employeesOnMultipleProjects = service.getEmployeesOnMultipleProjects();
		Assertions.assertEquals(1, employeesOnMultipleProjects.size(), "Deve encontrar um funcionário em múltiplos projetos.");
		
		Object[] result = employeesOnMultipleProjects.get(0);
		Assertions.assertEquals(testEmployee.getId(), result[0], "O ID do funcionário deve corresponder.");
		Assertions.assertEquals(testEmployee.getName(), result[1], "O nome do funcionário deve corresponder.");
		Assertions.assertEquals(2L, result[2], "O funcionário deve estar em 2 projetos.");
	}

	// Teste 9: Testar CRUD de funcionários
	@Test
	void shouldCreateAndFindEmployee() {
		// Ação: Cria um novo funcionário
		Employee newEmployee = new Employee("Raphael ", new BigDecimal("60000.0"));
		Employee savedEmployee = service.saveEmployee(newEmployee);

		// Verificação
		Assertions.assertNotNull(savedEmployee.getId(), "O funcionário deve ter um ID gerado.");
		Assertions.assertEquals("Raphael", savedEmployee.getName(), "O nome deve ser salvo corretamente.");
		Assertions.assertEquals(new BigDecimal("60000.0"), savedEmployee.getSalary(), "O salário deve ser salvo corretamente.");

		// Buscar funcionário por ID
		Optional<Employee> foundEmployee = service.findEmployeeById(savedEmployee.getId());
		Assertions.assertTrue(foundEmployee.isPresent(), "O funcionário deve ser encontrado.");
		Assertions.assertEquals("Raphael", foundEmployee.get().getName(), "O nome deve corresponder.");
	}

	// Teste 10: Testar CRUD de projetos
	@Test
	void shouldCreateAndFindProject() {
		// Ação: Cria um novo projeto
		Project newProject = new Project("Projeto D");
		Project savedProject = service.saveProject(newProject);

		// Verificação
		Assertions.assertNotNull(savedProject.getId(), "O projeto deve ter um ID gerado.");
		Assertions.assertEquals("Projeto D", savedProject.getName(), "O nome deve ser salvo corretamente.");

		// Buscar projeto por ID
		Optional<Project> foundProject = service.findProjectById(savedProject.getId());
		Assertions.assertTrue(foundProject.isPresent(), "O projeto deve ser encontrado.");
		Assertions.assertEquals("Projeto D", foundProject.get().getName(), "O nome deve corresponder.");
	}
}
