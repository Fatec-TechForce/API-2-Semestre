# 📖 Manual de Instalação

## Pré-requisitos

Antes de iniciar, certifique-se de ter os seguintes programas instalados:

- **Java 18+** (Obrigatório)
- **MySQL Server** (Obrigatório)
- **MySQL Workbench** (Recomendado)

---

## 🔹 Java (JDK)

O Java é necessário para executar o sistema TG CONTROL.

### Instalação:

1. Acesse: [Download Java JDK](https://www.oracle.com/java/technologies/javase-downloads.html)
2. Baixe a versão **Java SE Development Kit (18 ou superior)**.
3. Instale o pacote e configure a variável de ambiente `JAVA_HOME` (opcional).
4. Para verificar a instalação:

```
java -version
```

---

## 🔹 MySQL Server

O MySQL Server é o banco de dados utilizado pelo sistema.

### Instalação:

1. Acesse: [Download MySQL Server](https://dev.mysql.com/downloads/mysql/)
2. Escolha a versão compatível com seu sistema operacional.
3. Execute o instalador e siga as instruções.
4. Durante a instalação, defina uma **senha para o usuário root** (anote essa senha!).
5. Configure o MySQL para iniciar automaticamente.

### Verificar Instalação:

```
mysql --version
```

---

## 🔹 MySQL Workbench

O MySQL Workbench é uma interface gráfica para gerenciar o banco de dados MySQL.

### Instalação:

1. Acesse: [Download MySQL Workbench](https://dev.mysql.com/downloads/workbench/)
2. Escolha a versão compatível com seu sistema.
3. Instale normalmente.
4. Configure uma conexão com seu servidor MySQL:
   - **Hostname:** localhost
   - **Port:** 3306
   - **Username:** root
   - **Password:** (senha definida na instalação)
5. Teste acessando o banco com:

```
SELECT VERSION();
```

---

## 🗄️ Configuração do Banco de Dados

### 1. Criar o Banco de Dados

Abra o MySQL Workbench, conecte-se ao servidor e execute:

```sql
CREATE DATABASE tg_management;
USE tg_management;
```

### 2. Executar o Script de Criação

1. Localize o arquivo **`database-script.sql`** na pasta de instalação
2. No MySQL Workbench, vá em **File > Open SQL Script**
3. Selecione o arquivo e execute o script
4. Verifique se as tabelas foram criadas:

```sql
SHOW TABLES;
```

---

## ⚙️ Configuração do Sistema

### 1. Editar o Arquivo de Configuração

Localize ou crie o arquivo **`application.properties`** na mesma pasta do executável.

### 2. Adicionar as Configurações

Edite o arquivo com as seguintes informações:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tg_management
spring.datasource.username=root
spring.datasource.password=SUA_SENHA_AQUI
spring.jpa.hibernate.ddl-auto=update
server.port=8080
```

**⚠️ IMPORTANTE:** Substitua `SUA_SENHA_AQUI` pela senha do seu MySQL!

---

## 🚀 Executando o Sistema

### Método 1: Duplo Clique

1. Navegue até a pasta onde está o arquivo **`tg-control.jar`**
2. Dê um **duplo clique** no arquivo
3. Aguarde a mensagem: "Started Application in X seconds"

### Método 2: Usando Scripts

#### Windows (`iniciar.bat`):

```batch
@echo off
echo Iniciando TG CONTROL...
java -jar tg-control.jar
pause
```

#### Linux/Mac (`iniciar.sh`):

```bash
#!/bin/bash
echo "Iniciando TG CONTROL..."
java -jar tg-control.jar
```

### Método 3: Linha de Comando

```
java -jar tg-control.jar
```

---

## ✅ Verificação da Instalação

Após executar, você deverá ver no console:

```
Started Application in X.XXX seconds
Server is running on port 8080
```

Abra o navegador e acesse:

```
http://localhost:8080
```

---

## 🛠️ Solução de Problemas

### ❌ "Java não reconhecido como comando"

**Solução:** O Java não está instalado ou não está no PATH.
- Reinstale o Java
- Reinicie o computador

### ❌ "Cannot load driver class"

**Solução:** Problema na conexão com o MySQL.
- Verifique se o MySQL está rodando
- Verifique as credenciais no `application.properties`

### ❌ "Port 8080 is already in use"

**Solução:** Altere a porta no `application.properties`:

```properties
server.port=8081
```

E acesse: `http://localhost:8081`

### ❌ "Access denied for user 'root'"

**Solução:** Senha incorreta.
- Verifique a senha no `application.properties`
- Teste a senha no MySQL Workbench
