# Projeto de carga de arquivos com Spring Batch

## Requisitos

A aplicação exige o Java Runtime na versão 11 ou superior.

## Execução

```
java -jar batch.jar
```

Caso queira realizar testes ou executar por meio de sua IDE, siga os seguintes passos:



Abra o `application.properties` (src/main/resources/application.properties) e edite apenas a variável `path.file` para indicar o local do arquivos a processar do layout:

```
Exemplo: path.file=/arquivos/
```

E altere as configurações de banco de dados
