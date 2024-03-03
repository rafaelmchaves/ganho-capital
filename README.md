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

## Decisões arquiteturais

A classe Main inicia todo o processo, quando executa a aplicação. Essa classe invoca a classe InputProcessing, 
que lê as linhas do stdin. E, logo depois de ter lido todos as operações de entrada, OperationsProcessor é chamado para 
processar todas as operações, e retorna todos os impostos.

Decidi criar uma classe para processar toda a entrada de dados(InputProcessing) para deixar separado onde é lido a entrada.
Assim, se quisermos fazer mudanças futuras na entrada de dados, por exemplo, acrescentar outro tipo de entrada(além de stdin), 
a mudança será feita apenas nessa parte, ou não interferirá na lógica central do cálculo do imposto.

Para calcular o imposto sobre operações, criamos duas classes: SellingOperationProcessor e BuyingOperationProcessor, para 
calcular imposto sobre a venda, e sobre a compra, respectivamente. Além disso, são feitos cálculos e executados regras
sobre cada tipo de operação.

Por exemplo, no caso de compra, atualizamos o preço médio. No caso de venda, calculamos o prejuízo ou lucro e o imposto
seguindo as regras de cálculo de imposto.


