## Sprint 3

# 🗂 Backlog
| Rank | Prioridade | User Story | Estimativa | Sprint |
| --- | --- | --- | --- | --- |
| 8| **Baixa** | Como professor de TG, quero agendar as defesas dos alunos que concluíram o Trabalho de Graduação, para organizar as apresentações e facilitar o acompanhamento das etapas finais do processo. | 5 | 3 |
| 9| **Baixa** |Como professor de TG,quero revisar e aprovar os cadastros realizados por alunos e outros professores no sistema,para garantir que apenas usuários autorizados tenham acesso e evitar cadastros indevidos. | 3 | 3 |

# 📝 Definition of Ready (DoR)

### US08 – Agendamento de Defesas

**Objetivo:**  
Como professor de TG, quero agendar as defesas dos alunos que concluíram o Trabalho de Graduação, para organizar as apresentações e facilitar o acompanhamento das etapas finais do processo.

**Requisitos Específicos:**
- O sistema deve permitir ao professor criar agendamentos de defesa apenas para alunos com o TG no status “Aceito”.  
- O professor deve informar obrigatoriamente: aluno, data, hora, local e banca avaliadora.  
- O sistema deve validar conflitos de data e horário, impedindo o agendamento de duas defesas no mesmo local e horário.  
- O agendamento deve registrar automaticamente data e hora de criação.  
- Após o agendamento, o sistema deve enviar notificações ao aluno e aos membros da banca, informando data, hora e local da defesa.  
- O professor deve poder editar ou cancelar o agendamento antes da data da defesa.  
- O aluno deve visualizar em sua área do sistema os detalhes da defesa (data, hora, local e banca).  
- O sistema deve exibir todos os agendamentos em uma tela de calendário ou lista de defesas, com filtros por aluno, data ou banca.

<img width="645" height="435" alt="image" src="https://github.com/user-attachments/assets/0a5baa54-537c-4e04-a34b-ee1d768a658d" />
<img width="645" height="429" alt="image" src="https://github.com/user-attachments/assets/aa7bf6a2-8b48-46e0-a25a-c5d2f3e77d08" />


---

### US09 – Revisão e Aprovação de Cadastros

**Objetivo:**  
Como professor de TG, quero revisar e aprovar os cadastros realizados por alunos e outros professores no sistema, para garantir que apenas usuários autorizados tenham acesso e evitar cadastros indevidos.

**Requisitos Específicos:**
- O sistema deve listar todos os cadastros pendentes de aprovação (alunos e professores).  
- Cada cadastro deve exibir: nome completo, e-mail institucional, tipo de usuário (aluno ou professor) e data do cadastro.  
- O professor deve ter as opções “Aprovar” ou “Rejeitar” o cadastro.  
- Ao aprovar, o sistema deve liberar o acesso ao sistema para o usuário.  
- Ao rejeitar, o sistema deve permitir incluir um motivo opcional e registrar a decisão.  
- O sistema deve registrar data, hora e autor da ação (quem aprovou/rejeitou).  
- O usuário deve receber uma notificação por e-mail informando o resultado da revisão.  
- Cadastros rejeitados devem permanecer armazenados para auditoria, mas sem permissão de acesso.  
- A tela deve permitir filtrar e pesquisar cadastros por nome, tipo de usuário, status e data.

<img width="709" height="469" alt="image" src="https://github.com/user-attachments/assets/08bba8e8-8e61-4847-9201-4ab5ceacb693" />
<img width="791" height="530" alt="image" src="https://github.com/user-attachments/assets/44d31c95-4a02-4ca1-8dc4-450aa659b720" />



---
# 📈 Metas para Sprint

O foco da Sprint 3 é aprimorar o sistema TG Control com funcionalidades voltadas para o controle das etapas finais do Trabalho de Graduação e a gestão segura de usuários. Nesta sprint, o objetivo principal é permitir que os professores possam organizar as defesas de forma eficiente, garantindo que apenas alunos com o TG aprovado possam agendar sua apresentação. O sistema deve fornecer ferramentas para registrar todas as informações importantes, como data, hora, local e composição da banca, além de evitar conflitos de horários e locais para diferentes defesas. Os alunos e membros da banca devem receber notificações automáticas sobre os agendamentos, e o professor deve ter a possibilidade de alterar ou cancelar os agendamentos sempre que necessário. A visualização das defesas deve ser clara e intuitiva, seja em formato de calendário ou lista, com opções de filtro por aluno, data ou banca.

Além disso, a sprint contempla o controle de cadastros de usuários no sistema. Professores devem ser capazes de revisar novos registros de alunos e outros docentes, garantindo que apenas pessoas autorizadas tenham acesso. O sistema precisa permitir aprovar ou rejeitar cadastros, registrar a ação, e notificar o usuário sobre a decisão. Cadastros rejeitados devem ser mantidos para fins de auditoria, sem permitir login, e a interface deve oferecer filtros e ferramentas de pesquisa para facilitar a gestão de todos os cadastros pendentes.

Com essas melhorias, ao final da Sprint 3, o TG Control terá maior controle sobre o processo final do TG, assegurando organização, segurança e transparência tanto para professores quanto para aluno

---
# ✅ Definition of Done (DoD)

## 1. Escopo da Sprint
Esta sprint cobre as novas funcionalidades do sistema **TG Control**, incluindo:  
- Agendamento de defesas de TCC pelos professores.  
- Revisão e aprovação de cadastros de alunos e professores.  

---

## 2. Critérios de Conclusão da Sprint

### 2.1 💻 Código e Desenvolvimento
- O código das funcionalidades de agendamento de defesas e aprovação de cadastros foi implementado conforme as regras de negócio descritas no DoR das US08 e US09.  
- O módulo de agendamento permite selecionar aluno, data, hora, local e banca, com verificação de conflitos de horário e gravação correta no banco de dados.  
- A funcionalidade de revisão de cadastros permite listar, aprovar e rejeitar usuários, registrando data, hora e responsável pela ação.  
- O sistema envia notificações automáticas para alunos, professores e banca conforme definido.  
- O código foi revisado e aprovado em code review sem falhas críticas.  
- As novas features foram integradas ao sistema principal sem quebrar funcionalidades anteriores.  
- O banco de dados foi atualizado para incluir as novas tabelas e relações necessárias (Defesas, Aprovações de Usuários, etc.).  

---

### 2.2 🧪 Testes e Qualidade
- Testes manuais confirmam o funcionamento completo do agendamento de defesas, incluindo criação, edição, cancelamento e notificação.  
- Testes manuais e unitários garantem a aprovação e rejeição de cadastros, com bloqueio correto de acesso para usuários não aprovados.  
- Todos os critérios de aceitação das US08 e US09 foram validados com sucesso.  
- Nenhum erro crítico permanece aberto nas novas implementações.  
- O sistema mantém estabilidade e performance adequadas após a integração das novas features.  
- Logs de auditoria são gerados corretamente para ações de aprovação, rejeição e agendamento.  

---

### 2.3 🎨 Usabilidade e Interface
- As telas de Agendamento de Defesas e Revisão de Cadastros seguem o mesmo padrão visual das demais partes do sistema.  
- Campos obrigatórios (aluno, data, hora, local, banca, status de aprovação) possuem validação visual e mensagens de erro adequadas.  
- O layout das telas é intuitivo, consistente e responsivo, garantindo boa experiência tanto para professores quanto alunos.  
- Ícones, cores e mensagens refletem corretamente os status de ações:  
  - Agendado, Editado, Cancelado (para defesas).  
  - Pendente, Aprovado, Rejeitado (para cadastros).  
- Mensagens de confirmação e sucesso aparecem após cada ação concluída (ex.: “Defesa agendada com sucesso”, “Cadastro aprovado com sucesso”).  


---

# 📊 Burndown da Sprint
<img width="398" height="250" alt="image" src="https://github.com/user-attachments/assets/e812d6a0-505b-46b5-bc25-84c785012077" />
