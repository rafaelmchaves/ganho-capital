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

É necessário ter o java 17 e o maven instalado no computador.

Na raiz do projeto, onde está o arquivo pom.xml, rodar o seguinte comando:

```
mvn clean install
```

Com esse comando acima, os testes unitários também serão executados.

Se quiser rodar apenas os testes automatizados:

```
mvn test
```

Ainda na raiz do projeto tem um arquivo chamado ganho-capital. Execute o 
arquivo dessa forma:

```
./ganho-capital < caminho_do_arquivo
```

Por exemplo, se o arquivo chama input.txt está na raiz):

```
./ganho-capital < input.txt
```

## Decisões técnicas e arquiteturais

A classe Main inicia todo o processo, quando executa a aplicação. Essa classe invoca a classe InputProcessor, 
que lê as linhas do stdin. E, logo depois de ter lido todos as operações de entrada, a classe OperationsProcessor é chamado para 
processar todas as operações, e retorna todos os impostos.

Decidi criar uma classe para processar toda a entrada de dados(InputProcessor) para deixar separado onde é lido a entrada.
Assim, se quisermos fazer mudanças futuras na entrada de dados, por exemplo, acrescentar outro tipo de entrada(além de stdin), 
a mudança será feita apenas nessa parte, e não interferirá na lógica central do cálculo do imposto.

Para calcular o imposto sobre operações, criei duas classes: SellingOperationProcessor e BuyingOperationProcessor, para 
calcular o imposto sobre a venda, e sobre a compra, respectivamente. Além disso, são feito cálculos e executados regras
sobre cada tipo de operação. Essas classes implementam a mesma interface "OperationsProcessor" e são criadas via strategy.

Por exemplo, no caso de compra, atualizamos o preço médio. No caso de venda, calculamos o prejuízo ou lucro. Ambas as classes,
tem um método para calcular o imposto. Dessa forma, os cálculos de imposto de renda sobre compra e venda ficam separados.
Apesar de que quando a operação é do tipo compra não se cobra imposto atualmente, o código já prevê que se um dia
isso se alterar, cobrando imposto na compra, é só implementar a regra na classe BuyingOperationProcessor. A mesma coisa é para 
a venda, caso as regras de impostos mudem, é só alterar o código dentro do método calculateTax na classe SellingOperationProcessor.

Foi criado a classe Tax como o modelo de retorno de output.

Foi criado testes automatizados para a regra de negócio do cálculo de imposto para cada lista de operações.

Usei a biblioteca ```com.fasterxml.jackson.core``` para poder ler a string e transformá-la em Json de forma simplificada e vice-versa, usando 
o objectMapper.
Usei a biblioteca ```junit-jupiter-engine``` para implementar os teste unitários usando o JUnit 5, assim, fica super
simples de qualquer mudança no código ou refatoração, retesta a lógica facilmente.

## Melhorias futuras

Em caso de ler uma grande quantidade de dados, milhões ou bilhões, a solução poderia evoluir criando threads para processar 
uma quantidade de linhas em paralelo, permitindo o resultado da execução programa de forma muito mais rápida.

