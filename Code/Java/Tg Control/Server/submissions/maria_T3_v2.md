# Seção 3: Metodologia e Implementação

## 1. Introdução
O presente trabalho adota uma abordagem **ágil** para o desenvolvimento do sistema *TG Control*. O foco principal é facilitar a comunicação entre orientadores e alunos através de uma interface intuitiva.

> "A simplicidade é o grau máximo de sofisticação." — *Leonardo da Vinci*

## 2. Tecnologias Utilizadas
Para a construção da solução, foram selecionadas ferramentas robustas e de mercado:

* **Linguagem:** Java (JDK 17+)
* **Interface:** JavaFX com FXML
* **Banco de Dados:** MySQL 8.0
* **Bibliotecas:**
    1.  `Flexmark` (Renderização Markdown)
    2.  `MySQL Connector` (Persistência)

## 3. Estrutura do Código
Abaixo, um exemplo de como realizamos a conexão com o banco de dados utilizando o padrão *Singleton*:

```java
public class DatabaseConnect {
    
    private static Connection conn;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(URL, USER, PASS);
            } catch (SQLException e) {
                System.err.println("Erro fatal: " + e.getMessage());
            }
        }
        return conn;
    }
}