# 📌 Padrão de Commits

Adotamos a especificação **[Conventional Commits](https://www.conventionalcommits.org/)** para padronizar nossas mensagens de commit.  

Esse padrão nos ajuda a:
- Criar um histórico de projeto mais claro.  
- Gerar automaticamente o **CHANGELOG**.  
- Determinar a próxima versão semântica (**SemVer**) de forma automatizada.  

---

## 📝 Estrutura da Mensagem
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]


---

## 🔖 Tipos de Commit Recomendados

| **type**   | **Descrição**                                                                 | **Relação com o SemVer** |
|------------|-------------------------------------------------------------------------------|--------------------------|
| feat       | Uma nova funcionalidade para o código.                                        | MINOR                    |
| fix        | Uma correção de bug no código.                                                | PATCH                    |
| docs       | Alterações na documentação (ex: README, CONTRIBUTING.md).                     | Não se aplica            |
| style      | Alterações de formatação de código (espaços em branco, ponto e vírgula, etc.). | Não se aplica           |
| refactor   | Alteração de código que não corrige bug nem adiciona recurso.                 | Não se aplica            |
| perf       | Alteração de código que melhora o desempenho.                                 | Não se aplica            |
| test       | Adição, remoção ou correção de testes.                                        | Não se aplica            |
| build      | Alterações que afetam o sistema de build ou dependências externas.            | Não se aplica            |
| ci         | Alterações nos arquivos e scripts de integração contínua (CI).                | Não se aplica            |
| chore      | Outras alterações que não modificam arquivos de produção.                     | Não se aplica            |
| revert     | Reverte um commit anterior.                                                   | Varia                    |

---

## ⚠️ Alterações que Quebram Compatibilidade (BREAKING CHANGES)

Qualquer commit que introduza uma alteração **não compatível** com versões anteriores deve ser sinalizado.  
Isso resulta em um **MAJOR** na versão semântica.

### 🔹 Formas de indicar:

✅ **Com o símbolo `!` no cabeçalho:**


feat(api)!: remover o endpoint /v1 de legado


✅ **Com o rodapé `BREAKING CHANGE:`**


fix: corrigir problema de validação no login

BREAKING CHANGE: O campo 'username' agora aceita apenas caracteres alfanuméricos.


---

## 💡 Exemplos Práticos

| **Exemplo**                                           | **Explicação**                                        |
|-------------------------------------------------------|-------------------------------------------------------|
| `feat(auth): adicionar autenticação de dois fatores`  | Nova funcionalidade no módulo de autenticação.        |
| `fix: corrigir erro de renderização no Safari`        | Correção de bug sem escopo.                           |
| `refactor(db): simplificar query de busca por usuário`| Refatoração no código do banco de dados.              |
| `docs: atualizar seções de uso da API`                | Alteração na documentação.                            |
| `chore: configurar o linter com as regras do Airbnb`  | Tarefa de infraestrutura/configuração.                |

---
