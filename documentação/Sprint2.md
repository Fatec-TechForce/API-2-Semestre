## Sprint 2

# 🗂 Backlog

| Rank | Prioridade | User Story | Estimativa | Sprint |
| --- | --- | --- | --- | --- |
| 1 | **Alta** | Como aluno, quero enviar minhas seções do TG diretamente para o orientador, para facilitar a entrega e não depender de muitos canais diferentes. | 6 | 2 |
| 2 | **Alta** | Como professor orientador, quero corrigir e devolver feedback diretamente no sistema para agilizar o processo de orientação. | 6 | 2 |
| 3 | **Alta** | Como professor de TG/Orientador, quero acessar as versões anteriores das seções enviadas para acompanhar a evolução do trabalho do aluno. | 5 | 2 |
| 4 | **Alta** | Como aluno, quero receber notificações quando meu professor responder para não perder prazos importantes.<br>Como professor, quero ser notificado quando um aluno enviar uma nova versão para não deixar passar revisões pendentes. | 6 | 2 |
| 5 | **Alta** | Como aluno, quero ver o status de cada seção (pendente, revisada, aceita) para saber meu progresso no TG.<br>Como professor, quero atualizar o status das seções corrigidas para indicar a evolução ao aluno. | 3 | 2 |
| 6 | **Média** | Como aluno, quero que minhas seções, versões e correções fiquem salvas, para não perder meu histórico e acompanhar minha evolução no trabalho.<br>Como professor, quero acessar facilmente os envios e versões dos alunos, para acompanhar o progresso de cada TG e manter registrado todo o processo de orientação. | 12 | 2 |
| 7 | **Média** | Como aluno, quero gerar automaticamente meu TG consolidado em formato MD para entregar de acordo com o padrão exigido. | 8 | 2 |

# 📝 Definition of Ready (DoR)

| ID | User Story | Objetivo | Requisitos Específicos |
| --- | --- | --- | --- |
| US01 | Envio de Seções | Como aluno, quero enviar minhas seções do TG diretamente para o orientador, para facilitar a entrega e centralizar os envios. | - Permitir upload de arquivos .docx, .pdf e .md<br>- Informar o nome da seção no envio<br>- Salvar data e hora do envio<br>- Validar tamanho máximo do arquivo<br>- Tela com campos: Selecionar Seção, Selecionar Arquivo, Botão “Enviar”<br>- Após envio, mostrar confirmação e listar arquivo |
| US02 | Correção e Feedback no Sistema | Como professor orientador, quero corrigir e devolver feedback diretamente no sistema para agilizar a orientação. | - Visualizar arquivos enviados pelo aluno<br>- Campo de texto para comentários<br>- Professor não edita o arquivo<br>- Botão “Enviar Feedback” salva comentário e marca seção como “Com Feedback”<br>- Feedback visível ao aluno na seção correspondente<br>- Registrar data e autor do feedback |
| US03 | Acesso a Versões Anteriores | Como professor de TG/Orientador, quero acessar versões anteriores das seções enviadas para acompanhar a evolução do trabalho do aluno. | - Cada upload gera nova versão<br>- Armazenar número da versão (v1, v2...) e data<br>- Professor pode visualizar e baixar versões anteriores<br>- Interface com aba/botão “Histórico de Versões” |
| US04 | Notificações | Aluno: ser notificado quando houver feedback.<br>Professor: ser notificado quando houver nova versão. | - Notificações no sistema e opcional por e-mail<br>- Texto padrão aluno: “Sua seção X foi revisada pelo orientador.”<br>- Texto padrão professor: “O aluno X enviou uma nova versão da seção Y.”<br>- Geradas automaticamente ao enviar feedback ou nova seção<br>- Armazenamento e painel de notificações |
| US05 | Status e Progresso | Aluno: ver status das seções.<br>Professor: atualizar status das seções corrigidas. | - Status: Pendente, Em Revisão, Com Feedback, Aceito<br>- Mostrar status com cores e ícones<br>- Apenas professor altera o status<br>- Exibir confirmação ao alterar status<br>- Atualização visível em tempo real para o aluno |
| US06 | Implementação Inicial do Banco de Dados | Como aluno, quero que seções, versões e correções fiquem salvas de forma segura no sistema. | - Tabelas: Alunos, Seções, Versões, Feedbacks, Notificações<br>- Versão vinculada a aluno, seção e data<br>- Feedback vinculado à versão específica<br>- Tela de histórico completo<br>- Filtros: aluno, status, data de envio<br>- Nenhuma versão é apagada automaticamente |
| US07 | Geração Automática do TG Consolidado (Formato MD) | Como aluno, quero gerar automaticamente meu TG consolidado em formato Markdown (.md), seguindo o padrão exigido. | - Juntar todas as seções aceitas na ordem correta<br>- Converter seções para Markdown (#, ##, etc.)<br>- Gerar único arquivo .md consolidado<br>- Botão “Gerar TG Consolidado (.md)” na tela principal<br>- Mensagem de sucesso e download direto |

# ✅ Definition of Done (DoD)

| Categoria | Critérios de Conclusão | Status |
| --- | --- | --- |
| Código e Desenvolvimento | - Implementação de todas as funcionalidades seguindo boas práticas e padrões da equipe<br>- Regras de negócio das US01–US07 respeitadas<br>- Telas principais (envio, feedback, histórico, status e geração de TG) navegáveis e integradas<br>- Banco de dados funcional, armazenando corretamente alunos, seções, versões, feedbacks e notificações<br>- Código revisado e aprovado em code review<br>- Integração das features concluída sem regressões | ⬜ |
|Testes e Qualidade | - Testes manuais executados para todos os fluxos principais (envio, feedback, geração, notificações)<br>- Testes unitários básicos criados para funções de controle e persistência de dados<br>- Todos os critérios de aceitação das US01–US07 validados com sucesso<br>- Nenhum erro crítico ou de bloqueio permanece aberto<br>- Performance e estabilidade adequadas para uso interno e demonstração | ⬜ |
|  Usabilidade e Interface | - Telas com layout consistente, intuitivo e funcional<br>- Botões, mensagens de erro e feedback ao usuário implementados corretamente<br>- Interface reflete protótipo definido na documentação<br>- Ícones e cores de status aplicados conforme padrão visual (ex.: Pendente, Em Revisão, Aceito) | ⬜ |
|  Documentação e Entregáveis | - README e documentação técnica atualizados com as US concluídas<br>- Estrutura do banco de dados registrada<br>- Cada US marcada como “Done” no backlog com evidências (print, teste ou commit)<br>- Sistema pronto para apresentação e feedback do cliente (protótipo funcional) | ⬜ |

# 📊 Burndown da Sprint
