# ✅ Definition of Ready (DoR)

### 🎯 **Objetivo**
Estabelecer critérios obrigatórios para que uma User Story (US) seja considerada **Pronta para Desenvolvimento**, minimizando incertezas e evitando bloqueios durante a sprint.

---

## 1. 🧩 Critérios Gerais do DoR

Uma User Story só pode entrar na fila de desenvolvimento quando **todos os seguintes pontos forem validados**:

### 1.1 Clareza de Escopo
- Papel do usuário definido.  
- Descrição da funcionalidade clara e sem ambiguidades.  
- Objetivo esperado alinhado à necessidade real do usuário.  

### 1.2 Regras de Negócio Definidas
- Regras funcionais e de uso documentadas.  
- Estados ou fluxos possíveis (transições de status) descritos.  
- Comportamentos esperados do sistema (notificações, validações, formatos) claros.  

### 1.3 Critérios de Aceitação Documentados
- Critérios de validação objetivos.  
- Fluxos principais e de exceção mapeados.  

### 1.4 Dependências Identificadas
- Dependências técnicas e funcionais mapeadas.  
- Integrações externas necessárias documentadas.  

### 1.5 Viabilidade Técnica
- O time possui conhecimento e recursos para implementação.  
- Protótipos ou mockups de alta fidelidade aprovados.  

---

## 2. 🧠 DoR Específico por User Story

---

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

---

### **US02 – Correção e Feedback no Sistema**

**Objetivo:**  
Como professor orientador, quero corrigir e devolver feedback diretamente no sistema para agilizar o processo de orientação.

**Requisitos Específicos:**  
- O professor deve poder **visualizar o arquivo** enviado pelo aluno.  
- Deve haver um **campo de texto** para comentários ou observações.  
- O professor **não pode editar o arquivo**, apenas inserir comentários.  
- O botão **“Enviar Feedback”** deve salvar o comentário e marcar a seção como **“Com Feedback”**.  
- O feedback deve ficar **visível ao aluno** dentro da seção correspondente.  
- O sistema deve **registrar a data e o autor do feedback**.  

---

### **US03 – Acesso a Versões Anteriores**

**Objetivo:**  
Como professor de TG/Orientador, quero acessar versões anteriores das seções enviadas para acompanhar a evolução do trabalho do aluno.

**Requisitos Específicos:**  
- Cada novo upload deve gerar automaticamente uma **nova versão da seção**.  
- O sistema deve armazenar o **número da versão (v1, v2, v3...)** e a **data do envio**.  
- O professor deve poder **visualizar e baixar** qualquer versão anterior.  
- Criar na interface uma **aba ou botão “Histórico de Versões”** para acesso rápido.  

---

### **US04 – Notificações**

**Objetivo:**  
**Aluno:** Quero ser notificado quando o professor enviar feedback.  
**Professor:** Quero ser notificado quando o aluno enviar nova versão.

**Requisitos Específicos:**  
- Notificações devem aparecer **no sistema** e também podem ser enviadas **por e-mail**.  
- Texto padrão para aluno: “Sua seção X foi revisada pelo orientador.”  
- Texto padrão para professor: “O aluno X enviou uma nova versão da seção Y.”  
- A notificação é gerada automaticamente quando o professor clica em **“Enviar Feedback”** ou o aluno clica em **“Enviar Seção”**.  
- As notificações devem ser **armazenadas e exibidas** em um painel de notificações.  

---

### **US05 – Status e Progresso**

**Objetivo:**  
**Aluno:** Quero ver o status de cada seção.  
**Professor:** Quero atualizar o status das seções corrigidas.

**Requisitos Específicos:**  
- Status possíveis: **Pendente**, **Em Revisão**, **Com Feedback**, **Aceito**.  
- O sistema deve mostrar o **status ao lado do nome da seção**, com cores e ícones.  
- Apenas o **professor pode alterar o status**.  
- Ao alterar o status, deve ser exibida uma **confirmação** (“Deseja atualizar o status para Aceito?”).  
- O aluno visualiza o **status atualizado em tempo real**.  

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
- O sistema deve juntar todas as **seções aceitas** na **ordem correta**.  
- Cada seção deve ser **convertida para formato Markdown padrão**, com títulos (#, ##, etc.).  
- Deve ser gerado um **único arquivo .md** consolidado com todas as seções.  
- O aluno acessa um **botão “Gerar TG Consolidado (.md)”** na tela principal.  
- Após gerar, o sistema exibe a **mensagem “Arquivo gerado com sucesso”** e permite o **download direto**.  

---

### **US08 – Estatísticas de Tempo Médio de Correção e Interações**

**Objetivo:**  
Como professor de TG, quero visualizar estatísticas sobre o tempo médio de correção e quantidade de interações com os alunos, para melhorar a gestão acadêmica.

**Requisitos Específicos:**  
- O sistema deve calcular automaticamente o **tempo médio entre o envio do aluno e o feedback do professor**.  
- Mostrar também o **número total de feedbacks enviados** por aluno.  
- Exibir os dados em formato de **tabela**.  
- Permitir **filtrar por período (mês/semestre)** e por **aluno**.  
- O painel deve mostrar **média geral da turma** e **ranking dos alunos com mais interações**.  
- Disponibilizar opção de **exportar relatório em PDF**.  

---

### **US09 – Visualização de Alunos com TG Concluído**

**Objetivo:**  
Como professor de TG, quero visualizar quais alunos já concluíram todas as seções, para acompanhar o andamento e encerrar o processo de orientação.

**Requisitos Específicos:**  
- O sistema deve marcar automaticamente o **status “Concluído”** quando todas as seções do aluno estiverem com status **“Aceito”**.  
- Criar uma **tela de listagem de alunos**, exibindo: Nome, Curso, Número de Seções Aceitas, Status Final.  
- Permitir **filtrar por curso, turma e status (Em andamento / Concluído)**.  
- Permitir **abrir o TG completo** do aluno (versões e feedbacks).  
- Exibir um **indicador visual** para alunos concluídos.  

---

### **US10 – Gerenciamento de Cadastros de Professores**

**Objetivo:**  
Como administrador, quero gerenciar cadastros de professores para garantir acesso controlado e seguro ao sistema.

**Requisitos Específicos:**  
- Criar **tela de administração de usuários (professores)** com opções de **adicionar, editar, ativar/inativar e remover**.  
- Cada professor deve ter: **nome completo**, **e-mail institucional**, **senha temporária**, **curso vinculado**.  
- Enviar **e-mail automático de boas-vindas** com link para definir senha no primeiro acesso.  
- Permitir **bloquear o acesso** de professores inativos (sem excluir dados).  
- Registrar **data de criação e último acesso**.  
- Garantir que apenas usuários com perfil **“Administrador”** possam acessar essa tela.  

