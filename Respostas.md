# Questões

1. ### Quais são os contras de utilizar se herança entre classes? Quais alternativas você adotaria caso quisesse deixar de usar herança em um relacionamento específico? Dê um exemplo

    **Resposta:** Os principais contras de se utilizar herança é o forte acoplamento que ela pode causar, também a forte hierarquia que ela define entre as classes e problemas ao tentar reutilizar código pois ao herdar de uma classe você herda tudo mesmo que precise apenas de um método.
    As alternativas que podemos optar para não utilizar a herança é a composição e a utilização de interfaces , a composição permite construiu classes utilizando objetos de outras classes e a interface permite definir um contrato que as classes devem seguir.
    Um exemplo seria em um sistema de loja onde há o cadastro de produtos, poderia ser criado uma classe produto e os produtos como livro e eletrônicos herdariam da classe produto porem esse relacionamento de herança poderia causar problemas por exemplo se os produtos começam a ter características muito diferentes como por exemplo o livro ter um atributo autor e o eletrônico ter um atributo voltagem, nesse caso seria melhor criar uma classe de dadosProduto que conteria apenas os dados que fazem sentidos a todos os produtos e as classes livro e eletrônico teriam uma relação de composição com dadosProduto e também poderia ter uma interface produtoVenda em que poderia ter métodos que todos os produtos devem ter como getPreco.

    ----

2. ### Suponha que você tem uma classe final, da qual você não tem o código-fonte, e que você deseja adicioná-la a uma estrutura de polimorfismo, mas cuja interface pública é ligeiramente diferente da classe. Que padrão de projeto você poderia utilizar para aproveitar o código desta classe, mas fazendo com que ela atenda à interface da esperada na estrutura de polimorfismo?

    **Resposta:** Como é necessário fazer uma adaptação na classe para que ela atenda a interface esperada o padrão de projeto que pode ser utilizado é o Adapter, já que ele é um padrão que permite que objetos e interfaces incompatíveis trabalhem junto assim seria possível atender a interface e manter o polimorfismo.

    ----

3. ### Em que cenários você poderia usar um proxy? Dê um exemplo real

    **Resposta:**  Um proxy poderia ser utilizado por exemplo quando precisamos adicionar uma nova funcionalidade a um objeto adicionando uma camada entre o cliente e o objeto, sem que o cliente saiba que existe essa camada, ou seja, o cliente continua utilizando o objeto da mesma forma que utilizava antes da adição da camada, mas agora com a nova funcionalidade.
    Um exemplo real seria um proxy para adicionar uma nova funcionalidade ao PreparedStatement do JDBC que é responsável por preparar e executar instruções SQL no banco de dados, O PreparedStatement fornece métodos para substituir os parâmetros marcados (?) em umas instrução SQL, mas não fornece nenhum método para substituir parâmetros nomeados (:nome), então poderíamos criar um proxy para adicionar esse método ao PreparedStatement, ou poderíamos também adicionar uma funcionalidade de cache ao PreparedStatement, para que ele armazene os resultados de consultas que foram executadas recentemente e que podem ser reutilizados, sem que seja necessário executar a consulta novamente no banco de dados.

    ----

4. ### Você prefere utilizar domínios anêmicos ou ricos? Como avalia os prós e contras de cada?

    **Resposta:** Para decidir se vou utilizar domínios anêmicos ou ricos depende dos requisitos do projeto geralmente procuro ver qual se encaixa melhor no projeto, por exemplo se vou desenvolver um projeto utilizando algum framework prefiro utilizar domínios anêmicos o que facilita a integração com os frameworks, porem a complexidade do código aumenta já que as regras de negócio e requisitos do sistema podem ficar espalhadas em camadas diferentes, além de que boa parte dos benefícios da orientação a objetos são perdidos e o código fica mais parecido com um código procedural, por isso acredito que a utilização de domínios ricos pode ser melhor na maioria dos casos, apesar deles exigirem maior conhecimento do domínio e de boas práticas em orientações a objetos são mais fáceis de dar manutenção, as classes de domínio fazem mais sentido para o negócio e o código fica mais organizado e fácil de entender além de que pode possibilitar a reutilização de código e diminuir a complexidade do código em outras camadas.

    ----

5. ### Dê um exemplo do bom uso do princípio OCP, da sigla SOLID

    **Resposta:**  Um exemplo do princípio OCP poderia ser em um sistema de loja por exemplo onde é necessário aplicar filtros ao produtos e então temos uma classe FiltroProdutos que teria métodos como filtroPorCor() e filtroPorPreco(), porem se fosse necessário adicionar um novo filtro por exemplo filtroPorMarca() temos que alterar a classe FiltroProdutos e isso violaria o princípio OCP, então podemos criar uma interface FiltroProdutos e cada tipo de filtro seria uma classe que implementa essa interface, por exemplo teríamos a classe FiltroPorCor, FiltroPorPreco e FiltroPorMarca que implementariam a interface FiltroProdutos e teriam seus próprios métodos de filtro, assim se fosse necessário adicionar um novo filtro não seria necessário alterar a classe FiltroProdutos, apenas criar uma nova classe que implemente a interface assim respeitando o princípio de aberto para extensão mas fechado para modificação.

    ----

6. ### Qual é a diferença entre requisitos funcionais, não-funcionais e regras de negócio. Dê um exemplo de cada

    **Resposta:**
    - Funcional: específica alguma funcionalidade (geralmente do ponto de vista de negócio) que o sistema deve ter.

         - Exemplo: O sistema deve possibilitar o cadastro de clientes

    - Não-funcional: específica alguma restrição sob a qual o sistema deve operar ou atributo de qualidade que ele deve possuir (geralmente do ponto de vista técnico).

        - Exemplo: Todo cliente deve ter a data do registro no sistema vinculada ao seu cadastro.

    - Regra de negócio: Descreve algum aspecto do negócio especificando alguma restrição ou regra que o sistema deve seguir.

        - Exemplo: Todos os campos de cadastro de cliente são de preenchimento obrigatório , exceto o campo de complemento do endereço.

    ----

7. ### Quais estratégias de diagramação você utiliza em seus projetos? Quais diagramas e por quê?

    **Resposta:**  Os diagramas que mais utilizo são o de classes, pois acredito que o diagrama de classes é uma boa forma de visualizar a estrutura do sistema e como as classes se relacionam entre si, e o  diagrama de entidade relacionamento que considero uma boa forma de visualizar o modelo de dados do sistema e definir como os dados serão armazenados.
    Outro diagrama que também utilizo, mas com menos frequência é o de sequência que também considero muito importante para visualizar e definir o fluxo de execução do sistema.

    ----

8. ### Você está utilizando GitFlow e precisa fazer uma alteração na versão em desenvolvimento de um projeto. Quais etapas você teria que realizar?

    **Resposta:** As etapas que realizaria seria criar uma nova branch a partir da branch de desenvolvimento, essa nova branch seria uma nova feature com as alterações que preciso fazer, após realizar todas as alterações abriria uma solicitação de pull request para a branch de desenvolvimento, para que as alterações que fiz possam ser revisadas e aprovadas antes de entrar na versão de desenvolvimento do projeto.

    ----

9. ### O que você deve ter feito para que uma funcionalidade que você pegou para implementar seja considerada como 'done'?

    **Resposta:** Para que a funcionalidade seja considerada como 'done' é preciso entender o que a funcionalidade deve fazer e quais requisitos ela deve atender, depois de entender os requisitos é preciso implementar a funcionalidade e realizar os testes para garantir que está funcionando corretamente, em seguida é preciso revisar o código para garantir que o código está seguindo as boas práticas e que não causara bugs no futuro, e por fim é preciso documentar a nova funcionalidade como ela funciona e como deve ser utilizada

    ----

10. ### Quais são as cerimônias do SCRUM e como você avalia a importância de cada?

    **Resposta:** As cerimônias do SCRUM são:
    - Sprint Planning: esta é a primeira cerimônia de cada sprint onde toda a equipe scrum definem quais serão as tarefas que serão realizadas no sprint e como elas serão realizadas. Esta cerimônia é importante para definir as metas do sprint e garantir que a equipe está alinhada.

    - Daily Scrum: É uma reunião diária onde o time de desenvolvimento se reúne para discutir o que foi feito no dia anterior, o que será feito no dia atual e se existem impedimentos para a realização das tarefas .Esta reunião é importante para que haja comunicação entre a equipe, para identificar os problemas que podem atrasar o planejamento  e também para encontrar soluções mais rápidas para estes problemas.

    - Sprint Review: é uma reunião em que a equipe se reúne para revisar o que foi concluído e o que não foi e também para apresentar o que foi feito aos Stakeholders. ela é importante para obter o feedback e garantir que o projeto está seguindo o caminho certo.

    - Sprint Retrospective: é uma reunião que ocorre após o sprint Review onde a equipe se reúne para discutir o que deu certo e o que deu errado na sprint e como podem melhorar para a próxima sprint. Esta reunião é importante para garantir que a equipe continue melhorando.

    ----

11. ### Você conhece e utiliza Docker nos seus projetos? Se sim, para que?

    **Resposta:**  Conheço e utilizo Docker nos meus projetos, mas não tenho um conhecimento muito aprofundado, geralmente utilizo para executar banco de dados, para realizar testes por exemplo com a ferramenta Testcontainers em conjunto com o Docker e também utilizo  para executar aplicações, pois ele simplifica o processo de configuração necessário para executar a aplicação em diferentes ambientes.
