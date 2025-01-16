# Magic Formula - Análise de Ações

Este projeto é uma aplicação em Java que realiza a raspagem de dados do site da Fundamentus para calcular a "Magic Formula" de ações. A "Magic Formula" é uma estratégia de investimento desenvolvida
por Joel Greenblatt, que busca identificar ações que estão subvalorizadas e que possuem um bom retorno sobre o capital investido.

Para entender mais: https://clubedovalor.com.br/blog/magic-formula/

## Descrição do Projeto

A aplicação coleta dados financeiros de empresas listadas na bolsa de valores brasileira através da raspagem do site da Fundamentus. Em seguida, utiliza esses dados para calcular a 
classificação das ações com base na fórmula mágica, depois exportar para o Excel para facilitar a leitura; o programa usa os três criterios principais:

1. **Retorno sobre o Capital Investido (ROIC)**: Mede a eficiência de uma empresa em gerar lucros a partir do capital que possui.
2. **Preço sobre Lucro (P/L)**: Indica se uma ação está subvalorizada em relação ao seu lucro.
3. **Ev/Ebitda**:  é um indicador financeiro que compara o valor total de uma empresa (EV) com o seu lucro antes de juros, impostos, depreciação e amortização (EBITDA).

A combinação desses destes três critérios ajuda os investidores a identificar ações que têm um bom potencial de valorização.

## Funcionalidades

- Raspagem de dados financeiros do site da Fundamentus.
- ROIC, Ev/Ebitda e P/L para as ações.
- Classificação decrescente das ações com base na "Magic Formula".
- Anula ações com Ev/Ebitda negativa
- Escolhe a com maior liquidez entre as "iguais" (ex: AMER3, AMER4, AMER11)
- Excluir o Setor Bancário, Serviços Públicos e Seguradoras.
- Exibição dos resultados em um formato legível.
- 

## Tecnologias Utilizadas

- Java
- Bibliotecas para raspagem de dados (ex: Jsoup)
- MAVEN
- JUnit
- Excel

## Como Usar

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/magic-formula.git
