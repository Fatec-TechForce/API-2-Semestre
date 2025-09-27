Estratégia de Branch – GitHub Flow

1. Introdução
Este documento descreve a estratégia de versionamento com Git e GitHub a ser utilizada no desenvolvimento do projeto. O objetivo é garantir um fluxo de trabalho organizado, manter a estabilidade do código principal e facilitar a colaboração entre os membros da equipe.
A metodologia adotada é o GitHub Flow, um modelo simples e eficaz recomendado no Guia do GitHub da faculdade.
2. Princípios Fundamentais
A branch main é a fonte da verdade: Ela deve conter sempre a versão mais estável e funcional do software. Ninguém deve enviar código diretamente para a main.
O desenvolvimento é feito em branches separadas: Toda nova funcionalidade, correção ou tarefa deve ser desenvolvida em sua própria branch. Isso isola o trabalho em andamento e previne instabilidade na main.
Pull Requests (PRs) para integração: Todo código só é integrado à main através de um Pull Request, que permite a revisão e discussão do código antes do merge.
  
3. Estrutura das Branches
3.1. Branch Principal: main
Propósito: Representa a versão de "produção" do nosso projeto. O código nesta branch deve estar sempre compilando e funcionando.
Proteção: Idealmente, deve ser configurada no GitHub para proibir pushes diretos, forçando o uso de Pull Requests para todas as alterações.
3.2. Branches de Desenvolvimento
Para cada tarefa, uma nova branch deve ser criada a partir da versão mais atualizada da main.
Padrão de Nomenclatura
Para manter a clareza e a organização, usaremos o seguinte padrão para nomear as branches:
tipo/escopo/descricao-curta
tipo:
feature: Para novas funcionalidades.
bugfix: Para correção de erros.
docs: Para alterações na documentação.
escopo: O fluxo de usuário ou a área principal impactada (aluno, orientador, professor, geral).
descricao-curta: Um resumo da tarefa em poucas palavras (ex: cadastro-tg, dashboard-graficos).
Exemplos:
feature/aluno/tela-submissao-trabalho
feature/professor/dashboard-acompanhamento
bugfix/geral/correcao-login-lento
docs/geral/manual-de-instalacao
  
4. Fluxo de Trabalho (Passo a Passo)
Este é o ciclo que cada dupla deve seguir para desenvolver uma tarefa:
Sincronizar a Branch main Local:
Antes de iniciar, certifique-se de que sua main local está atualizada.
git checkout main
git pull origin main


Criar a Nova Branch:
Crie sua branch de trabalho a partir da main atualizada.
git checkout -b feature/escopo/nome-da-tarefa


Desenvolver e Fazer Commits:
Trabalhe na implementação da sua tarefa. Faça commits pequenos e com mensagens claras. Utilize o padrão de mensagem de commit definido no guia da faculdade:
<tipo>(<id_da_task>): <descrição>
# Exemplo de commit
git commit -m "feat(TG-21): implementa validacao do formulario de orientador"


Enviar a Branch para o Repositório Remoto:
Quando tiver um progresso significativo ou ao final do dia, envie sua branch para o GitHub.
git push origin feature/escopo/nome-da-tarefa


Abrir um Pull Request (PR):
Quando a tarefa estiver concluída e testada, acesse o GitHub e abra um Pull Request da sua branch para a main. No PR, descreva de forma resumida o que foi implementado e, se necessário, os passos para testar.
Revisão de Código (Code Review):
Marque pelo menos um membro de outra dupla como revisor.
O revisor irá analisar o código em busca de possíveis melhorias, bugs ou inconsistências.
A dupla que desenvolveu não deve aprovar o seu próprio PR. A revisão por pares é essencial para a qualidade do projeto.
Fazer o Merge:
Após o PR ser aprovado e passar por qualquer verificação automática, o autor pode realizar o merge, integrando o código à branch main.
Limpeza:
Após o merge, a branch de desenvolvimento pode ser deletada no GitHub para manter o repositório limpo.
