# Lista Compras API

![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.5-brightgreen?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-latest-blue?logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?logo=docker)
![License](https://img.shields.io/badge/license-MIT-green)

API REST para gerenciamento de listas de compras. Permite criar e gerenciar listas com produtos pré-cadastrados, adicionando e removendo itens da lista ativa.

---

## Funcionalidades

- Criar uma lista de compras (apenas uma lista ativa por vez)
- Adicionar produtos à lista ativa (sem duplicatas)
- Remover produtos da lista ativa
- Desativar uma lista de compras
- Consultar a lista ativa
- Listar todas as listas com paginação
- Catálogo de produtos pré-cadastrado com ~100 itens via seed automático

---

## Tecnologias

| Tecnologia              | Versão          |
| ----------------------- | --------------- |
| Java                    | 25              |
| Spring Boot             | 4.0.5           |
| Spring Data JPA         | -               |
| Spring Validation       | -               |
| PostgreSQL              | latest          |
| Lombok                  | -               |
| Jackson Databind        | -               |
| H2 Database (testes)    | 2.2.224         |
| Docker / Docker Compose | -               |
| Maven                   | wrapper incluso |

---

## Instalação e execução

### Pré-requisitos

- Docker e Docker Compose instalados, **ou**
- Java 25+ e Maven instalados localmente com PostgreSQL disponível

### Opção 1 — Docker Compose (recomendado)

1. Clone o repositório:

   ```bash
   git clone <url-do-repositorio>
   cd listaComprasApi
   ```

2. Copie o arquivo de variáveis de ambiente e preencha os valores:

   ```bash
   cp .env.exemple .env
   ```

3. Edite o `.env` com suas credenciais:

   ```env
   POSTGRES_USER=seu_usuario
   POSTGRES_DB=lista_compras
   POSTGRES_PASSWORD=sua_senha
   POSTGRES_URL=jdbc:postgresql://db:5432/lista_compras
   ```

4. Suba os containers:
   ```bash
   docker compose up --build
   ```

A API estará disponível em `http://localhost:8081`.

---

### Opção 2 — Execução local

1. Clone o repositório e configure o `.env` conforme acima.

2. Exporte as variáveis de ambiente ou configure-as no `application.properties`:

   ```bash
   export POSTGRES_URL=jdbc:postgresql://localhost:5432/lista_compras
   export POSTGRES_USER=seu_usuario
   export POSTGRES_PASSWORD=sua_senha
   ```

3. Execute com o Maven Wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```

A API estará disponível em `http://localhost:8080`.

---

## Documentação interativa (Swagger)

Após iniciar a aplicação, acesse a interface Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

---

## Endpoints

Base path: `/api/v1/list`

| Método | Endpoint                       | Descrição                        |
| ------ | ------------------------------ | -------------------------------- |
| `GET`  | `/api/v1/list/active`          | Retorna a lista ativa            |
| `GET`  | `/api/v1/list/all`             | Lista todas as listas (paginado) |
| `POST` | `/api/v1/list/create`          | Cria uma nova lista              |
| `PUT`  | `/api/v1/list/add/product`     | Adiciona produtos à lista ativa  |
| `PUT`  | `/api/v1/list/remove/product`  | Remove produtos da lista ativa   |
| `PUT`  | `/api/v1/list/{id}/deactivate` | Desativa uma lista pelo ID       |

### Exemplos de uso

**Criar uma lista:**

```http
POST /api/v1/list/create
```

**Adicionar produtos à lista ativa:**

```http
PUT /api/v1/list/add/product
Content-Type: application/json

{
  "ids": [1, 3, 7]
}
```

**Remover produtos da lista ativa:**

```http
PUT /api/v1/list/remove/product
Content-Type: application/json

{
  "ids": [3]
}
```

**Desativar uma lista:**

```http
PUT /api/v1/list/42/deactivate
```

### Paginação

O endpoint `/all` suporta parâmetros de paginação:

```
GET /api/v1/list/all?page=0&size=10&sort=createdAt
```

---

## Seed de produtos

Na primeira inicialização, a aplicação carrega automaticamente ~100 produtos do arquivo `src/main/resources/static/products.json` (arroz, feijão, carnes, laticínios, itens de higiene, etc.). O seed é executado apenas uma vez — se o banco já contiver produtos, ele é ignorado.

---

## Testes

Execute os testes com:

```bash
./mvnw test
```

Os testes usam H2 em memória (perfil `test`) com Mockito para isolar a camada de serviço. Cobrem os cenários:

- Criação de lista com e sem lista ativa existente
- Adição de um ou mais produtos
- Tentativa de adicionar produto inexistente

---

## Estrutura do projeto

```
listaComprasApi/
├── src/
│   ├── main/
│   │   ├── java/com/alnnu/listaComprasApi/
│   │   │   ├── controllers/
│   │   │   │   └── ListController.java       # Endpoints REST
│   │   │   ├── entities/
│   │   │   │   ├── ListEntity.java           # Entidade da lista
│   │   │   │   ├── ProductEntity.java        # Entidade do produto
│   │   │   │   └── dto/
│   │   │   │       └── ProductIdsDTO.java    # DTO para IDs de produtos
│   │   │   ├── repositories/
│   │   │   │   ├── ListRepository.java
│   │   │   │   └── ProductRepository.java
│   │   │   ├── services/
│   │   │   │   └── ListService.java          # Regras de negócio
│   │   │   ├── config/
│   │   │   │   └── JacksonConfig.java
│   │   │   └── utils/
│   │   │       ├── exceptions/
│   │   │       │   ├── ActiveListException.java
│   │   │       │   └── NotFoundException.java
│   │   │       └── seed/
│   │   │           └── ProductSeed.java      # Seed automático de produtos
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           └── products.json             # Catálogo de produtos
│   └── test/
│       └── java/com/alnnu/listaComprasApi/
│           └── ListServiceTest.java
├── Dockerfile
├── docker-compose.yml
├── .env.exemple
└── pom.xml
```

---

## Regras de negócio

- Só pode existir **uma lista ativa** por vez. Tentar criar uma segunda lança `ActiveListException`.
- Ao adicionar produtos, se não houver lista ativa, uma nova é criada automaticamente.
- Um mesmo produto **não pode ser adicionado duas vezes** na mesma lista.
- Remover ou desativar uma lista requer que ela exista; caso contrário, lança `NotFoundException`.

---

## Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature:
   ```bash
   git checkout -b feature/minha-feature
   ```
3. Commit suas alterações:
   ```bash
   git commit -m "add: minha feature"
   ```
4. Envie para o repositório remoto:
   ```bash
   git push origin feature/minha-feature
   ```
5. Abra um Pull Request descrevendo as mudanças

---
