Gerenciador de Projetos
### Desenvolvido por Rafael Oliveira
Este é um sistema de gerenciamento de projetos e funcionários, desenvolvido com Spring Boot e Spring Data JPA. A aplicação fornece uma API RESTful para realizar operações CRUD (Criar, Ler, Atualizar, Deletar) em funcionários e projetos, além de gerenciar a associação entre eles.

Arquitetura do Projeto
O projeto segue uma arquitetura em camadas bem definida para separar as responsabilidades:

controller: Contém a camada de apresentação, responsável por expor os endpoints da API REST. Ele recebe as requisições HTTP e as delega para a camada de serviço.

service: Onde a lógica de negócios está implementada. Ele lida com a validação de dados, aplica as regras de negócio (como o limite de projetos por funcionário) e orquestra as operações de persistência.

repository: Interfaces que utilizam o Spring Data JPA para interagir com o banco de dados. Elas fornecem métodos para operações de persistência sem a necessidade de escrever código SQL manual.

model: Define as entidades (Employee, Project, LOG, etc.) que representam a estrutura de dados no banco de dados. A classe EMP_PROJ é usada para gerenciar a relação de muitos-para-muitos entre funcionários e projetos.

Endpoints da API
A URL base para todos os endpoints é /api.

Funcionários (/api/employees)
Método	URL	Descrição
POST	/employees	Cria um novo funcionário.
GET	/employees	Lista todos os funcionários.
GET	/employees/{id}	Busca um funcionário por ID.
PUT	/employees/{id}	Atualiza um funcionário existente.
DELETE	/employees/{id}	Remove um funcionário.

Exportar para as Planilhas
Projetos (/api/projects)
Método	URL	Descrição
POST	/projects	Cria um novo projeto.
GET	/projects	Lista todos os projetos.
GET	/projects/{id}	Busca um projeto por ID.
PUT	/projects/{id}	Atualiza um projeto existente.
DELETE	/projects/{id}	Remove um projeto.

Exportar para as Planilhas
Relacionamentos e Estatísticas
Método	URL	Descrição
POST	/employees/{employeeId}/projects/{projectId}	Associa um funcionário a um projeto, aplicando a regra de negócio que um funcionário pode estar em no máximo 2 projetos.
GET	/employees/multiple-projects	Lista os funcionários que estão em mais de um projeto.
GET	/logs	Retorna todos os logs de operação do sistema.
GET	/stats	Retorna estatísticas gerais do sistema, como o número de funcionários e o número de funcionários em múltiplos projetos.

Exportar para as Planilhas
Tecnologias
Spring Boot: Framework para simplificar a criação de aplicações Java.

Spring Data JPA: Abstração para o acesso a dados.

Hibernate: Implementação da JPA.

Banco de Dados: O projeto está configurado para ser compatível com um banco de dados relacional.

Como Executar
Clone o repositório.

Configure as credenciais do banco de dados no arquivo application.properties.

Execute a classe principal GerenciadorProjetosApplication.java como uma aplicação Spring Boot.

A API estará disponível em http://localhost:8080.