# Lista de Tarefas API

Este repositório contém uma API Restful feita em Java com Spring Boot, desenvolvida como parte de um teste técnico da Fatto Consultoria e Sistemas. A API permite acesso e manipulação de tarefas por meio de operações CRUD.

## Tecnologias Usadas

<div align="center">

![Java 17](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot 3](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Render](https://img.shields.io/badge/Render-000000?style=for-the-badge&logo=render&logoColor=white)

</div>

## Funcionalidades

* **Listagem e Pesquisa de Tarefas**
* **Criação de Tarefas**
* **Edição de Tarefas**
* **Exclusão de Tarefas**
* **Re-ordenação de Tarefas**

## Estrutura dos Arquivos

- **Controllers**: Responsáveis por definir os endpoints da API.
- **Entity**: Representam as entidades do banco de dados e seus respectivos atributos.
- **Repositories**: Arquivos que fazem a conexão com o banco de dados.
- **Services**: Contêm a lógica de negócio para manipular e consultar os dados.

## Como Executar
### Pré-requisitos

- **Java 17** ou superior
- **Maven** 3.8.1+
- **PostgreSQL**

### Passos para execução

1. Clone o repositório:
   ```bash
   git clone https://github.com/Pedroo722/Lista_de_Tarefas-API.git
   cd .\Lista_de_Tarefas-API\
   ```

2. Compile o projeto:
   ```bash
   mvn clean install
   ```

3. Execute o projeto:
   ```bash
   mvn spring-boot:run
   ```

4. Acesse a API em: `http://localhost:8080/api/tasks`.


## Endpoints

A API fornece os seguintes endpoints para a manipulação de dados relacionados a lista de tarefas. Cada endpoint permite uma das operações básicas CRUD.

- **GET** `/api/tasks`: Recupera todas as tarefas.
- **GET** `/api/tasks/{id}`: Recupera uma tarefa pelo ID.
- **POST** `/api/tasks`: Cria uma nova tarefa.
- **PUT** `/api/tasks/{id}`: Atualiza uma tarefa existente.
- **DELETE** `/api/tasks/{id}`: Remove uma tarefa pelo ID.
- **PUT** `/api/tasks/reorder`: Cuida da reordenação das tarefas.

## Estrutura dos Dados
### Tarefa

```json
{
    "id": 1,
    "nomeTarefa": "Exemplo",
    "custo": 100,
    "dataLimite": "2024-12-31",
    "ordemApresentacao": 3
}
```
