# Catálogo de Produtos

Este é um projeto de monitoramento de preços do mercado de energia com um back-end em Java com Spring Boot e um front-end em React com TypeScript e Vite.

## Configuração do Ambiente

### Variáveis de Ambiente do Back-end

No diretório `backend`, crie um arquivo `.env` com as seguintes variáveis de ambiente para a conexão com o banco de dados MySQL em produção:

```
DB_HOST=localhost
DB_PORT=3306
DB_NAME=precos
DB_USER=root
DB_PASSWORD=secret
```

### Variáveis de Ambiente do Front-end

No diretório `frontend`, crie um arquivo `.env` para desenvolvimento:

```
VITE_API_URL=http://localhost:8080
```

## Como rodar o back-end

### Pré-requisitos

- Java 24 ou superior
- Maven

### Passos

1.  Clone o repositório:
    ```bash
    git clone https://github.com/clwro/MonitoramentoDePrecos.git
    ```
2.  Navegue até o diretório do back-end:
    ```bash
    cd MonitoramentoDePrecos/backend
    ```
3.  Instale as dependências:
    ```bash
    mvn install
    ```
4.  Rode a aplicação:
    ```bash
    ./mvnw spring-boot:run
    ```

A aplicação estará rodando em `http://localhost:8080`.

## Como rodar os testes

1.  Navegue até o diretório do back-end:
    ```bash
    cd MonitoramentoDePrecos/backend
    ```
2.  Rode o seguinte comando:
    ```bash    
    ./mvnw test
    ```

## Como rodar o front-end

### Pré-requisitos

- Node.js 18 ou superior
- npm ou bun

### Passos

1.  Navegue até o diretório do front-end:
    ```bash
    cd MonitoramentoDePrecos/frontend
    ```
2.  Instale as dependências:
    ```bash
    npm install
    # ou
    bun install
    ```
3.  Rode a aplicação:
    ```bash
    npm run dev
    # ou
    bun dev
    ```

A aplicação estará rodando em `http://localhost:8081`.

## Como rodar com Docker

### Usando Docker Compose (Recomendado)

1.  Certifique-se de ter o Docker e o Docker Compose instalados.
2.  Na raiz do projeto, rode o seguinte comando:
    ```bash
    docker-compose up --build
    ```

A aplicação estará disponível em `http://localhost:8081` e o back-end em `http://localhost:8080`.

## Dependências


### Back-end

- Spring Boot
- H2 Database
- MySQL
- Lombok
- Jakarta Validation
- JPA
- Apache POI

### Front-end

- React
- TypeScript
- Vite
- Axios
- React Router DOM
- Tailwind CSS
- Shadcn UI
- React Hook Form
