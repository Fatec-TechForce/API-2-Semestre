# TechForce - TG Control

<img width="1217" height="235" alt="image" src="https://github.com/user-attachments/assets/5aeed30e-9e5e-46f3-8f84-40e77ce31705" />

## Tópicos
<div align="center">
  
[📄 Descrição do Projeto](#descrição) | [💻 Tecnologias Utilizadas](#tecnologias-utilizadas) | [📋 Backlog do Produto](#backlog-do-produto) | [📅 Cronograma das Sprints](#cronograma-das-sprints) | [📖 Manual de Instalação](#-manual-de-instalação) | [📝 Documentação do Projeto](#-documentação-do-projeto) | [👥 Equipe](#equipe)

</div>

## Descrição

Gerenciar o ciclo dos Trabalhos de Graduação (TG) da modalidade Portfólio é um desafio para alunos, professores orientadores e coordenação, pois o processo de envio, correção e acompanhamento das seções ocorre de forma descentralizada em plataformas pouco adequadas (como e-mail institucional e MS Teams), o que gera atrasos, falha de notificações e risco de perda de mensagens; para os alunos, a dor está na demora dos feedbacks e na dificuldade de entregar o TG final em formato MD, enquanto os professores orientadores enfrentam problemas para manter histórico claro das correções e versões, e a coordenação tem dificuldade em monitorar o progresso dos trabalhos, o que torna essencial uma plataforma centralizada que organize, registre e acompanhe cada etapa, garantindo comunicação eficiente, rastreabilidade e geração facilitada do TG final.

> 
> 

## Tecnologias Utilizadas

[![Java](https://img.shields.io/badge/Java-007396?logo=java&logoColor=white)](https://www.java.com/)  [![GitHub](https://img.shields.io/badge/GitHub-181717?logo=github&logoColor=white)](https://github.com/) [![Figma](https://img.shields.io/badge/Figma-F24E1E?logo=figma&logoColor=white)](https://www.figma.com/) [![Jira](https://img.shields.io/badge/Jira-0052CC?logo=jira&logoColor=white)](https://www.atlassian.com/software/jira) [![Scene Builder](https://img.shields.io/badge/Scene%20Builder-0091EA?logo=java&logoColor=white)](https://gluonhq.com/products/scene-builder/) [![JavaFX](https://img.shields.io/badge/JavaFX-FF0000?logo=openjdk&logoColor=white)](https://openjfx.io/)

---

## Backlog do produto

| Rank | Prioridade | User Story | Estimativa | Sprint |
| --- | --- | --- | --- | --- |
| 1 | **Alta** | Como aluno, quero enviar minhas seções do TG diretamente para o orientador, para facilitar a entrega e não depender de muitos canais diferentes. | 6 | 2 |
| 2 | **Alta** | Como professor orientador, quero corrigir e devolver feedback diretamente no sistema para agilizar o processo de orientação. | 6 | 2 |
| 3 | **Alta** | Como professor de TG/Orientador, quero acessar as versões anteriores das seções enviadas para acompanhar a evolução do trabalho do aluno. | 5 | 2 |
| 4 | **Alta** | Como aluno, quero receber notificações quando meu professor responder para não perder prazos importantes.<br>Como professor, quero ser notificado quando um aluno enviar uma nova versão para não deixar passar revisões pendentes. | 6 | 2 |
| 5 | **Alta** | Como aluno, quero ver o status de cada seção (pendente, revisada, aceita) para saber meu progresso no TG.<br>Como professor, quero atualizar o status das seções corrigidas para indicar a evolução ao aluno. | 3 | 2 |
| 6 | **Média** | Como aluno, quero que minhas seções, versões e correções fiquem salvas, para não perder meu histórico e acompanhar minha evolução no trabalho.<br>Como professor, quero acessar facilmente os envios e versões dos alunos, para acompanhar o progresso de cada TG e manter registrado todo o processo de orientação. | 12 | 2 |
| 7 | **Média** | Como aluno, quero gerar automaticamente meu TG consolidado em formato MD para entregar de acordo com o padrão exigido. | 8 | 2 |
| 8| **Baixa** | Como professor de TG, quero agendar as defesas dos alunos que concluíram o Trabalho de Graduação, para organizar as apresentações e facilitar o acompanhamento das etapas finais do processo. | 5 | 3 |
| 9| **Baixa** |Como professor de TG,quero revisar e aprovar os cadastros realizados por alunos e outros professores no sistema,para garantir que apenas usuários autorizados tenham acesso e evitar cadastros indevidos. | 3 | 3 |

---

## Cronograma das Sprints

| **Sprint** | **Período** | **Entrega** | **Documentação** |
| --- | --- | --- | --- |
| Sprint 1 | 08/09 a 29/09 | Protótipos | [`Documentação`](./documentação/Sprint1.md) |
| Sprint 2 | 06/10 a 26/10 | Envio de Seções do TG, Correção e Feedback, Histórico de Versões, Notificações, Controle de Acompanhamento, Geração Automática do TG Final | [`Documentação`](./documentação/Sprint2.md) |
| Sprint 3 | 03/11 a 23/11 | Gerenciamento de Cadastros de Professores, Estatísticas de Tempo Médio de Correção e Interações | [`Documentação`](./documentação/Sprint3.md) |


---

## 📖 Manual de Instalação

### Pré-requisitos

Antes de iniciar, certifique-se de ter os seguintes programas instalados:

* Git 
* Java 18+ 
* Maven 
* IntelliJ IDEA 
* MySQL Workbench 

---

### Git

O Git é um sistema de controle de versão distribuído, usado para gerenciar projetos de software.

**Instalação:**

1. Acesse o site oficial: **[Download Git](https://git-scm.com/downloads)**
2. Escolha a versão compatível com seu sistema operacional (Windows, macOS, Linux).
3. Execute o instalador e siga as instruções padrão.
4. Para verificar se foi instalado corretamente, abra o terminal e digite:
```bash
git --version
```

---

### Java (JDK)

O Java é necessário para rodar o backend e algumas ferramentas como o Maven.

**Instalação:**

1. Acesse: **[Download Java JDK](https://www.oracle.com/java/technologies/javase-downloads.html)**
2. Baixe a versão Java SE Development Kit (18 ou superior).
3. Instale o pacote e configure a variável de ambiente `JAVA_HOME`.
4. Para verificar a instalação:
```bash
java -version
```

---

###  Maven

O Maven é uma ferramenta de automação e gerenciamento de dependências para projetos Java.

**Instalação:**

1. Baixe em: **[Download Maven](https://maven.apache.org/download.cgi)**
2. Extraia o arquivo `.zip` em uma pasta (ex: `C:\apache-maven`).
3. Configure a variável de ambiente:
   * Adicione `C:\apache-maven\bin` ao `PATH`.
4. Verifique a instalação:
```bash
mvn -version
```

---

### IntelliJ IDEA

O IntelliJ IDEA é a IDE recomendada para trabalhar com projetos Java.

**Instalação:**

1. Baixe em: **[Download IntelliJ IDEA](https://www.jetbrains.com/idea/download/)**
2. Escolha entre a versão **Community** (gratuita) ou **Ultimate** (paga, com mais recursos).
3. Instale normalmente no Windows/macOS/Linux.
4. Recomendado instalar plugins:
   * Maven Helper
   * Database Navigator
   * GitToolBox

---

### MySQL Workbench

O MySQL Workbench é uma interface gráfica para gerenciar o banco de dados MySQL.

**Instalação:**

1. Baixe em: **[Download MySQL Workbench](https://dev.mysql.com/downloads/workbench/)**
2. Escolha a versão compatível com seu sistema.
3. Instale normalmente.
4. Configure uma conexão com seu servidor MySQL.
5. Teste acessando o banco com:
```sql
SELECT VERSION();
```

---

## 📄 Documentação do Projeto

Toda a documentação do projeto está disponível na pasta [`documentação`](./documentação/)
### Conteúdo

- Definition of Ready (DoR
- Definition of Done (DoD)
- Estratégia de Branch
- Padrão de Commits
- Modelagem do Banco de Dados

## Equipe

 | Função      | Nome                                     |  Github  |
 | -           | -                                        | -        | 
 |Scrum Master | Leonardo Amon Sumiyoshi Hashimoto        |   [![GitHub Badge](https://img.shields.io/badge/GitHub-111217?style=flat-square&logo=github&logoColor=white)](https://github.com/Leonardo1022) |
 |Product Owner| Maria Eduarda Teixeira Miller de Oliveira|  [![GitHub Badge](https://img.shields.io/badge/GitHub-111217?style=flat-square&logo=github&logoColor=white)](https://github.com/maria-oliveira)|
 |Team Member  | Gabriel Valente Belarmino                |  [![GitHub Badge](https://img.shields.io/badge/GitHub-111217?style=flat-square&logo=github&logoColor=white)](https://github.com/gabrielvalentesjc)|
 |Team Member  | Natália Pereira da Silva                 |  [![GitHub Badge](https://img.shields.io/badge/GitHub-111217?style=flat-square&logo=github&logoColor=white)](https://github.com/nataliapersis)|
 |Team Member  | Niuan Spolidorio da Rocha Souza          |  [![GitHub Badge](https://img.shields.io/badge/GitHub-111217?style=flat-square&logo=github&logoColor=white)](https://github.com/NiuanSouza)|
 |Team Member  | Vitor Samuel Ribeiro de Souza            |   [![GitHub Badge](https://img.shields.io/badge/GitHub-111217?style=flat-square&logo=github&logoColor=white)](https://github.com/VitorRibeiro09)|



