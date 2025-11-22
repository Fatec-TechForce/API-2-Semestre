# Seção 4: Desenvolvimento Inicial do Sistema

## 1. Visão Geral da Implementação
Nesta etapa, foi desenvolvido o núcleo da aplicação **TG Control**, focando na arquitetura **MVC (Model-View-Controller)** e na persistência de dados. O objetivo foi garantir que o login, a navegação básica e o cadastro de tarefas estivessem funcionais.

> *"A arquitetura bem definida é o primeiro passo para um software manutenível."*

## 2. Modelagem de Dados (MySQL)
O banco de dados foi normalizado até a 3ª Forma Normal. Abaixo, a estrutura final da tabela principal de usuários:

| Campo | Tipo | Descrição |
| :--- | :--- | :--- |
| `email` | VARCHAR(255) | Chave Primária (PK) |
| `passwordHASH` | VARCHAR(255) | Senha criptografada |
| `status` | ENUM | 'Active' ou 'Inactive' |

## 3. Diagramas de Sequência
Atendendo ao feedback da entrega anterior, segue o fluxo detalhado de autenticação:

**Fluxo: Realizar Login**
1.  **Usuário** insere credenciais na *View*.
2.  **LoginController** captura os dados e envia para *DatabaseUtils*.
3.  **DatabaseUtils** consulta a *View SQL* `vw_user_login`.
4.  **Banco de Dados** retorna o perfil (Aluno/Professor).
5.  **LoginController** redireciona para o Dashboard apropriado.

## 4. Código Fonte (Backend)
Abaixo, apresentamos a classe `SessaoManager`, responsável por manter o estado do usuário logado (padrão *Singleton*). Note a correção na indentação conforme solicitado:

```java
public class SessaoManager {

    private static SessaoManager instance;
    private Usuario usuarioLogado;

    private SessaoManager() {}

    public static SessaoManager getInstance() {
        if (instance == null) {
            instance = new SessaoManager();
        }
        return instance;
    }

    public void setUsuario(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    public Usuario getUsuario() {
        return this.usuarioLogado;
    }
}