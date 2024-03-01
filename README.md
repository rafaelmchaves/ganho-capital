# ganho-capital

## Contexto
Implementação de um programa de linha de comando (CLI) que calcula o imposto a
ser pago sobre lucros ou prejuízos de operações no mercado financeiro de ações.

O programa recebe listas, uma por linha, de operações do mercado financeiro de ações em formato
json através da entrada padrão ( stdin ). Cada operação desta lista contém os seguintes campos:
```
operation: Se a operação é uma operação de compra ( buy ) ou venda ( sell )
unit-cost: Preço unitário da ação em uma moeda com duas casas decimais
quantity: Quantidade de ações negociadas
```
Exemplo de json de entrada:

```
[{"operation":"buy", "unit-cost":10.00, "quantity": 10000},
{"operation":"sell", "unit-cost":20.00, "quantity": 5000}]
[{"operation":"buy", "unit-cost":20.00, "quantity": 10000},
{"operation":"sell", "unit-cost":10.00, "quantity": 5000}]
```

## Como executar

Pode executar via IDE ou linha de comando. 
Por linha de comando, faça o seguinte:

Dentro da pasta do projeto, na raiz onde fica o arquivo pom.xml:

```
mvn package

java -cp target/my-app-1.0-SNAPSHOT.jar br.com.nubank.Main

```

