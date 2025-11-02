## Sprint 2

# 🗂 Backlog

| Rank | Prioridade | User Story | Estimativa | Sprint |
| --- | --- | --- | --- | --- |
| 1 | **Alta** | Como cliente interessado em utilizar a solução, quero visualizar um protótipo funcional com as principais funcionalidades, para entender melhor como o sistema vai resolver minhas necessidades e dar meu feedback antes da versão final. | 2 | 1 |
| 2 | **Alta** | Como aluno, quero enviar minhas seções do TG diretamente para o orientador, para facilitar a entrega e não depender de muitos canais diferentes. | 6 | 2 |
| 3 | **Alta** | Como professor orientador, quero corrigir e devolver feedback diretamente no sistema para agilizar o processo de orientação. | 6 | 2 |
| 4 | **Alta** | Como professor de TG/Orientador, quero acessar as versões anteriores das seções enviadas para acompanhar a evolução do trabalho do aluno. | 5 | 2 |
| 5 | **Alta** | Como aluno, quero receber notificações quando meu professor responder para não perder prazos importantes.<br>Como professor, quero ser notificado quando um aluno enviar uma nova versão para não deixar passar revisões pendentes. | 6 | 2 |
| 6 | **Alta** | Como aluno, quero ver o status de cada seção (pendente, revisada, aceita) para saber meu progresso no TG.<br>Como professor, quero atualizar o status das seções corrigidas para indicar a evolução ao aluno. | 3 | 2 |
| 7 | **Média** | Como aluno, quero que minhas seções, versões e correções fiquem salvas, para não perder meu histórico e acompanhar minha evolução no trabalho.<br>Como professor, quero acessar facilmente os envios e versões dos alunos, para acompanhar o progresso de cada TG e manter registrado todo o processo de orientação. | 12 | 2 |

# 📝 Definition of Ready (DoR)

### **US01 – Envio de Seções**

**Objetivo:**  
Como aluno, quero enviar minhas seções do TG diretamente para o orientador, para facilitar a entrega e centralizar os envios.

**Requisitos Específicos:**  
- O sistema deve permitir upload de arquivos nos formatos **.docx**, **.pdf** e **.md**.  
- Cada envio deve exigir que o aluno informe o **nome da seção**.  
- O sistema deve **salvar data e hora do envio**.  
- Deve existir uma **validação de tamanho máximo do arquivo**.  
- A tela deve ter campos: **Selecionar Seção**, **Selecionar Arquivo** e **Botão “Enviar”**.  
- Após o envio, o aluno deve ver uma **mensagem de confirmação** e o arquivo listado na tela.

<img width="731" height="492" alt="image" src="https://github.com/user-attachments/assets/afe34244-9744-4fb1-b5c1-6c67bc75cf76" />

  <img width="736" height="496" alt="image" src="https://github.com/user-attachments/assets/99f6f213-f556-4278-bfd2-403ace2a5efc" />


---

### **US02 – Correção e Feedback no Sistema**

**Objetivo:**  
Como professor orientador, quero corrigir e devolver feedback diretamente no sistema para agilizar o processo de orientação.

**Requisitos Específicos:**  
- O professor deve poder visualizar o arquivo enviado pelo aluno.  
- Deve haver um campo de texto para comentários ou observações.  
- O professor não pode editar o arquivo, apenas inserir comentários.  
- O botão “Enviar Feedback” deve salvar o comentário e marcar a seção como “Com Feedback”.  
- O feedback deve ficar visível ao aluno dentro da seção correspondente.  
- O sistema deve registrar a data e o autor do feedback.
  
<img width="849" height="585" alt="image" src="https://github.com/user-attachments/assets/622fa4f9-8d07-4d6b-a430-c58757165fbb" />


---

### **US03 – Acesso a Versões Anteriores**

**Objetivo:**  
Como professor de TG/Orientador, quero acessar versões anteriores das seções enviadas para acompanhar a evolução do trabalho do aluno.

**Requisitos Específicos:**  
- Cada novo upload deve gerar automaticamente uma nova versão da seção.  
- O sistema deve armazenar o número da versão (v1, v2, v3...) e a data do envio.  
- O professor deve poder visualizar e baixar qualquer versão anterior.  
- Criar na interface uma aba ou botão “Histórico de Versões” para acesso rápido.

  <img width="792" height="528" alt="image" src="https://github.com/user-attachments/assets/e1e64dba-0ba5-4439-978f-b0f6d30fc6da" />
  <img width="812" height="548" alt="image" src="https://github.com/user-attachments/assets/76d9bd84-0840-4e70-b4d9-dab053f81d10" />

---

### **US04 – Notificações**

**Objetivo:**  
**Aluno:** Quero ser notificado quando o professor enviar feedback.  
**Professor:** Quero ser notificado quando o aluno enviar nova versão.

**Requisitos Específicos:**  
- Notificações devem aparecer no sistema e também podem ser enviadas por e-mail.  
- Texto padrão para aluno: “Sua seção X foi revisada pelo orientador.”  
- Texto padrão para professor: “O aluno X enviou uma nova versão da seção Y.”  
- A notificação é gerada automaticamente quando o professor clica em **“Enviar Feedback”** ou o aluno clica em **“Enviar Seção”**.  
- As notificações devem ser armazenadas e exibidas em um painel de notificações.

  <img width="758" height="528" alt="image" src="https://github.com/user-attachments/assets/41fa6523-a082-4ada-b192-741044501839" />


---

### **US05 – Status e Progresso**

**Objetivo:**  
**Aluno:** Quero ver o status de cada seção.  
**Professor:** Quero atualizar o status das seções corrigidas.

**Requisitos Específicos:**  
- Status possíveis: Pendente, Em Revisão, Com Feedback, Aceito.  
- O sistema deve mostrar o status ao lado do nome da seção, com cores e ícones.  
- Apenas o professor pode alterar o status.  
- Ao alterar o status, deve ser exibida uma confirmação (“Deseja atualizar o status para Aceito?”).
  
<img width="837" height="556" alt="image" src="https://github.com/user-attachments/assets/da0d3651-6524-41c1-9c82-c2458445a252" />


---

### **US06 – Implementação Inicial do Banco de Dados**

**Objetivo:**  
Como aluno, quero que seções, versões e correções fiquem salvas de forma segura no sistema.

**Requisitos Específicos:**  
- Criar tabelas para **Alunos**, **Seções**, **Versões**, **Feedbacks** e **Notificações**.  
- Cada versão deve ter referência ao **aluno**, **seção** e **data de envio**.  
- Cada feedback deve estar vinculado à **versão específica do arquivo**.  
- O histórico completo deve poder ser **consultado em uma tela**.  
- Permitir **filtros** por nome do aluno, status e data de envio.  
- Definir que **nenhuma versão será apagada automaticamente**.  

---

### **US07 – Geração Automática do TG Consolidado (Formato MD)**

**Objetivo:**  
Como aluno, quero gerar automaticamente meu TG consolidado em formato **Markdown (.md)**, seguindo o padrão exigido.

**Requisitos Específicos:**  
- O sistema deve juntar todas as seções aceitas na **ordem correta.  
- Cada seção deve ser convertida para formato Markdown padrão, com títulos (#, ##, etc.).  
- Deve ser gerado um único arquivo .md consolidado com todas as seções.  
- O aluno acessa um botão “Gerar TG Consolidado (.md)” na tela principal.  
- Após gerar, o sistema exibe a mensagem “Arquivo gerado com sucesso” e permite o download direto.
  
 <img width="754" height="501" alt="image" src="https://github.com/user-attachments/assets/3c625bdf-4629-40f2-829b-a9306db7dd8f" />

---
# 📈 Metas para Sprint

---
# ✅ Definition of Done (DoD)

## 1. Escopo da Sprint
Esta sprint cobre as funcionalidades principais do sistema **TG Control**, incluindo:
- Envio de seções do TG pelo aluno.
- Correção e feedback pelo professor.
- Controle de versões anteriores.
- Sistema de notificações.
- Exibição de status e progresso.
- Estrutura inicial do banco de dados.
- Geração automática do TG consolidado em formato Markdown.

---

## 2. Critérios de Conclusão da Sprint

### 2.1 💻 Código e Desenvolvimento
- O código de todas as funcionalidades foi implementado seguindo boas práticas e padrões da equipe.  
- Todas as regras de negócio descritas no DoR das US01–US07 foram respeitadas.  
- As telas principais (envio, feedback, histórico, status e geração de TG) estão navegáveis e integradas.  
- O banco de dados foi criado e está funcional, armazenando corretamente alunos, seções, versões, feedbacks e notificações.  
- O código foi revisado e aprovado em code review.  
- Integração das features concluída sem causar regressões nas partes já implementadas.

---

### 2.2 🧪 Testes e Qualidade
- Testes manuais foram executados para todos os fluxos principais (envio, feedback, geração, notificações).  
- Testes unitários básicos foram criados para as funções de controle e persistência de dados.  
- Todos os critérios de aceitação das US01–US07 foram validados com sucesso.  
- Nenhum erro crítico ou de bloqueio permanece aberto.  
- A performance e estabilidade estão adequadas para uso interno e demonstração.  

---

### 2.3 🎨 Usabilidade e Interface
- As telas possuem layout consistente, intuitivo e funcional.  
- Botões, mensagens de erro e feedback ao usuário estão implementados corretamente.  
- A interface reflete o **protótipo definido** na documentação.  
- Ícones e cores de status estão aplicados conforme padrão visual (ex.: Pendente, Em Revisão, Aceito).  

---

# 📊 Burndown da Sprint
