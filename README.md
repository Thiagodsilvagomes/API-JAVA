# Desenvolva uma aplicação com Spring Boot que deverá ficar ouvindo na porta 8080.Sua aplicação deve possuir pelo menos 4 endpoints, sendo pelo menos um para cada verbo HTTP. (GET,POST,DELETE,PUT).
No Endpoint que receberá um POST você deverá receber um JSON com pelo menos um campo do tipo String, um do tipo número e um array de qualquer tipo.No endpoint do tipo GET você deverá receber 2 parâmetros opcionais.
Algum de seus métodos deve tratar a requisição do usuário, em caso de erro retorne algum código de erro e em caso de sucesso retorne 200.
Em um dos seus endpoints você deverá consumir alguma API externa à sua escolha.
Você deverá converter a resposta dessa chamada de JSON para um objeto java. Imprima(com Log) o status code dessa resposta.
Você deverá criar os testes para o seus métodos. Pelo menos um dos testes deverá ter um assertThrows. Não utilize System.out e sim o mecanismo de LOG. Utilize o Lombok.
