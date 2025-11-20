# FiadoPay (Backend Refactoring) - Opção 1

**Aluno:** Davi Mendes Almeida, Diogo Manoel dos Anjos Negreiros, João Hugo Santana de Carvalho
**Disciplina:** Programação Orientada a Objetos Avançada
**Data:** 21/11/2025

---

## Visão Geral
Este projeto consiste na refatoração completa da camada de serviços do sistema "FiadoPay". O objetivo principal foi eliminar a alta complexidade ciclomática (uso excessivo de `if/else`) e o acoplamento rígido, aplicando conceitos avançados de Engenharia de Software como **Reflexão (Reflection)**, **Anotações Customizadas** e **Processamento Assíncrono (Multithreading)**.

O sistema agora opera com **Runtime Discovery**, sendo capaz de identificar novas estratégias de pagamento sem alteração no código fonte do serviço principal.

---

## Decisões de Design & Arquitetura

### 1. Eliminação de Condicionais (Strategy + Reflexão)
* **Problema:** O código original utilizava cadeias de `if/else` rígidas para aplicar regras de negócio (Juros, Parcelamento).
* **Solução:**
    * Criação da anotação customizada `@PaymentMethod`.
    * Implementação do padrão **Strategy** para cada método (`PixStrategy`, `CardStrategy`, `BoletoStrategy`, `DebitStrategy`).
    * Uso da biblioteca `Reflections` na Factory para varrer o classpath e registrar as classes automaticamente.
* **Ganho:** Adesão ao *Open/Closed Principle*. Para adicionar um novo método, basta criar a classe anotada.

### 2. Metadados e Organização
* **Anotações:**
    * `@AntiFraud`: Define metadados como limite de risco e ativação de regras específicas (ex: Ativo apenas para Cartão).
    * `@WebhookSink`: Marca serviços de infraestrutura responsáveis por saída de dados.

### 3. Concorrência e Performance (Threads)
* **Problema:** O envio de Webhooks bloqueava a thread da requisição HTTP.
* **Solução:**
    * Implementação de `ExecutorService` (Pool de Threads).
    * O processamento pesado e a comunicação externa ocorrem em background.
* **Resultado:** A API retorna `201 Created` imediatamente, melhorando a experiência do cliente.

### 4. Bônus: Self-Discovery API (`/capabilities`)
* **Funcionalidade Extra:** Endpoint que utiliza reflexão para ler as próprias anotações e listar dinamicamente quais métodos o sistema suporta e suas configurações de fraude.

---

## Evidências de Execução

### 1. Testes Unitários e Reflexão
*Comprovando que a lógica de juros está correta e que o mecanismo de reflexão carrega as 4 estratégias dinamicamente.*

![Evidência Testes](https://ibb.co/DHW0Hzj2)

### 2. Processamento Assíncrono (Threads)
*Comprovando via log que o pagamento é processado em uma thread dedicada (`pool-X-thread-Y`), liberando a thread HTTP.*

![Evidência Threads](https://ibb.co/gbNqzVTG)

### 3. Funcionamento da API (Polimorfismo)
*Demonstração de pagamento via **CARTÃO** (com juros aplicados) e **BOLETO** (juro zero), provando que a estratégia correta foi selecionada.*

![Evidência API](https://ibb.co/hRTRXQt0)

---

## Tecnologias Utilizadas
* **Java 21**
* **Spring Boot 3**
* **H2 Database** (Banco em memória)
* **Reflections Library** (0.10.2)
* **Lombok**
* **Swagger / OpenAPI**

---

## Como Rodar o Projeto

1.  **Clonar o Repositório:**
    ```bash
    git clone [https://github.com/jhugosc/FiadoPay.git](https://github.com/jhugosc/FiadoPay.git) ```
    cd FiadoPay

2.  **Compilar e Testar:**
    ```bash
    mvn clean install
    mvn test
    ```

3.  **Rodar a Aplicação:**
    ```bash
    mvn spring-boot:run
    ```

4.  **Acessar Documentação (Swagger):**
    * URL: `http://localhost:8080/swagger-ui.html`

5.  **Acessar Banco H2:**
    * URL: `http://localhost:8080/h2`
    * JDBC URL: `jdbc:h2:mem:fiadopay;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`
    * User: `sa` (Senha vazia)

---

## Roteiro de Teste Manual

Para validar o funcionamento completo:

1.  **Criar Lojista:** `POST /fiadopay/admin/merchants` (Copie o ID gerado).
2.  **Autenticar:** Use o token `FAKE-{ID}` no botão Authorize.
3.  **Pagamento Card:** Envie `method: CARD` com parcelas > 1. (Verifique o `total` com juros).
4.  **Pagamento Boleto:** Envie `method: BOLETO`. (Verifique o `total` sem juros).
5.  **Capabilities:** Acesse `GET /fiadopay/capabilities` para ver a lista dinâmica.
