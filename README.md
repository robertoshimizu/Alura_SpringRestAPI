# Spring Rest API
from Alura

SpringDevTools does not work natively in IntelliJ. Need to add options in Settings.

### Controller
The first difference from a traditional SpringMVC, is that you need to add `@ResponseBody` after the `@RequestMapping("/")` so that Spring will not look for a page (JSP or html) to return, but rather the API response in `json`.
```java
@Controller
public class TopicosController {
    @RequestMapping("/topicos")
    @ResponseBody
    public List<Topico> lista() {
        Topico topico = new Topico("Primeiros Passos Spring Boot", "Curso introdutorio de Spring", new Curso("Spring101", "Java"));
        return Arrays.asList(topico, topico, topico);
    }
}
```
Alternatively, you may use the annotation `@RestController` which in this case Spring will know already that this is a RESTAPI and you do not need to add `@ResponseBody`.

#### Use of DTO (Data Transfer Object Pattern)
It is not good practice to return a Domain Class, in the above case, the class `Topico`, because this class has lots of attributes, and perhaps I just want to return one or two of them.

Data Transfer Object (DTO) or simply Transfer Object is a design pattern widely used in Java for transporting data between different components of a system, different instances or processes of a distributed system or different systems via serialization.
The idea is basically to group a set of attributes in a simple class in order to optimize communication.
In a remote call, it would be inefficient to pass each attribute individually. Likewise, it would be inefficient or even cause errors to pass a more complex entity.
Also, often the data used in the communication does not exactly reflect the attributes of your model. So, a DTO would be a class that provides exactly what is needed for a given process.

### SpringData JPA

```java
        # data source
        spring.datasource.driverClassName=org.h2.Driver
        spring.datasource.url=jdbc:h2:mem:alura-forum
        spring.datasource.username=sa
        spring.datasource.password=

        # jpa
        spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
        spring.jpa.hibernate.ddl-auto=update

        # Nova propriedade a partir da versao 2.5 do Spring Boot:
        spring.jpa.defer-datasource-initialization=true

        # h2
        spring.h2.console.enabled=true
        spring.h2.console.path=/h2-console
```

### H2

Spin Database `cd ~/h2/bin` the `./h2.sh`.
Then connect with `URL:jdbc:h2:mem:alura-forum`. You could also use Database connection via IntelliJ.

### JPA Entities

Need to add JPA annotations to the Entities in the model Classes.
This is necessary, so Spring will build tables in the database accordingly.

```java
@Entity
public class Usuario {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String email;
	private String senha;
```
Notice that IntellJ also checks the relationships, for example, `@ManyToOne`.

#### Database
It is a in-memory database, so every SpringBoot startup it creates a db from scratch. However, you can define some sql scripts to add tables and data.
In this project, we add file `data.sql` at the resources directory. Spring automatically executes this script.

```sql
INSERT INTO USUARIO(nome, email, senha) VALUES('Aluno', 'aluno@email.com', '123456');

INSERT INTO CURSO(nome, categoria) VALUES('Spring Boot', 'Programação');
INSERT INTO CURSO(nome, categoria) VALUES('HTML 5', 'Front-end');

INSERT INTO TOPICO(titulo, mensagem, data_criacao, status, autor_id, curso_id) VALUES('Dúvida', 'Erro ao criar projeto', '2019-05-05 18:00:00', 'NAO_RESPONDIDO', 1, 1);
INSERT INTO TOPICO(titulo, mensagem, data_criacao, status, autor_id, curso_id) VALUES('Dúvida 2', 'Projeto não compila', '2019-05-05 19:00:00', 'NAO_RESPONDIDO', 1, 1);
INSERT INTO TOPICO(titulo, mensagem, data_criacao, status, autor_id, curso_id) VALUES('Dúvida 3', 'Tag HTML', '2019-05-05 20:00:00', 'NAO_RESPONDIDO', 1, 2);
```