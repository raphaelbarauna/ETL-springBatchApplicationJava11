# Altys - Conv 86

> Operações de carga de arquivos do Convênio 86 - Layout Atlys


Os arquivos de contestação de notas fiscais/faturas para entrega da obrigação Convênio 86/CAT 06 do Layout Atlys estão divididos em três categorias:

|Categoria|Descrição|Máscara arquivo|
|---------|---------|---------------|
|Contestada|Arquivos de notas fiscais/faturas contestadas|adj_UF_AAAAMMDD_AAAAMMDD.rpt|
|Contestada|Arquivos de notas fiscais/faturas contestadas|rcvbl_UF_AAAAMMDD_AAAAMMDD.rpt|

Considerando que o objetivo do projeto do Convênio 86 é construir uma base de dados de layout único com os arquivos de contestação de notas fiscais enviados pelos Billings, esta aplicação batch faz a carga dos arquivos fornecidos pelo Atlys para tabelas intermediárias e que representam cada categoria descrita anteriormente.

Em caso de sucesso na carga para as tabelas intermediárias, uma *stored procedure* denominada `sp_insert_atlys` é chamada pela aplicação **AtlysBatch** para realizar o join entre essas tabelas e, então, esses arquivos de origem  são compactados e movidos para uma pasta denominada `processados` no servidor da aplicação.

Caso contrário, ou seja, ocorra qualquer erro no processamento do arquivo (número de delimitadores incorreto, arquivo corrompido etc.), o destino será a pasta `bads`. Destaca-se que nenhum registro do arquivo que obteve falha em seu processamento será armazenado nas tabelas alvo deste trabalho.

Tanto para o caso de sucesso quanto para falha, o processamento de cada arquivo é registrado na tabela de controle `tb_contest_controle`.

## Requisitos

A aplicação exige o Java Runtime na versão 11 ou superior.

## Execução

Para executar o programa, baixe o jar executável em [Releases](http://10.129.178.173/conv86/src/src-kenan-spring-batch/-/releases) e execute o seguinte comando:

```
java -jar atlysBatch.jar
```

Caso queira realizar testes ou executar por meio de sua IDE, siga os seguintes passos:

*Faça o clone deste repositório*:

```
git clone http://10.129.178.173/conv86/src/src-atlys-spring-batch.git
```

Abra o `application.properties` (src/main/resources/application.properties) e edite apenas a variável `path.file` para indicar o local do arquivos a processar do layout Atlys:

```
path.file=/arquivos/contestacao/billings/a_processar/atlys/
```

Além disso, caso o banco de dados alvo seja diferente, modifique as configurações da classe `DataSourceConfig` (src/main/java/br/com/vivo/atlys/configuration/DataSourceConfig.java).
