# Spring Rest API

_from Alura_

**SpringDevTools** does not work natively in IntelliJ. Need to add options in Settings.

### 1. Controllers

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

#### 1.1 Use of DTO (Data Transfer Object Pattern)

It is not good practice to return a Domain Class, in the above case, the class `Topico`, because this class has lots of attributes, and perhaps I just want to return one or two of them.

Data Transfer Object (DTO) or simply Transfer Object is a design pattern widely used in Java for transporting data between different components of a system, different instances or processes of a distributed system or different systems via serialization.
The idea is basically to group a set of attributes in a simple class in order to optimize communication.
In a remote call, it would be inefficient to pass each attribute individually. Likewise, it would be inefficient or even cause errors to pass a more complex entity.
Also, often the data used in the communication does not exactly reflect the attributes of your model. So, a DTO would be a class that provides exactly what is needed for a given process.

### 2. SpringData JPA

First step is to add Spring JPA and the databases in the `pom.xml`, in this case we are using H2 (in-memory database). Then configure them in the `application.properties`. You could use multiple databases, but in order to do that you need to create different repository packages.

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

#### 2.1 H2

Spin Database `cd ~/h2/bin` the `./h2.sh`.
Then connect it with `URL:jdbc:h2:mem:alura-forum`. Alternatively you could also use Database connection via IntelliJ automatically.

#### 2.2 JPA Entities

Need to add JPA annotations to the Entities in the model Classes.

In order to map Model Classes to JPA Entities, one need to use the following annotations: @Entity, @Id, @GeneratedValue, @ManyToOne, @OneToMany and @Enumerated;

This is necessary, so Spring will build tables in the database accordingly.

**Warning:** At least make sure you have a no-argument constructor in the Entity classes. You can have other constructors, but need to have a POJO one.

A JavaBean is a POJO that is serializable, has a no-argument constructor, and allows access to properties using getter and setter methods that follow a simple naming convention.

```java
@Entity
public class Usuario {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String email;
	private String senha;

//No-argument Constructor
    Usuario(){};
```

Notice that IntellJ also checks the relationships, for example, `@ManyToOne` between different Entities. It is a good practice to have a schema of your SQL databases.

#### 2.3 H2 Database

H2 is a in-memory database, so every SpringBoot startup it creates a db from scratch. However, you can define some sql scripts to add tables and data.
In this project, we add file `data.sql` at the resources directory. Spring automatically executes this script.

```sql
INSERT INTO USUARIO(nome, email, senha) VALUES('Aluno', 'aluno@email.com', '123456');

INSERT INTO CURSO(nome, categoria) VALUES('Spring Boot', 'Programação');
INSERT INTO CURSO(nome, categoria) VALUES('HTML 5', 'Front-end');

INSERT INTO TOPICO(titulo, mensagem, data_criacao, status, autor_id, curso_id) VALUES('Dúvida', 'Erro ao criar projeto', '2019-05-05 18:00:00', 'NAO_RESPONDIDO', 1, 1);
INSERT INTO TOPICO(titulo, mensagem, data_criacao, status, autor_id, curso_id) VALUES('Dúvida 2', 'Projeto não compila', '2019-05-05 19:00:00', 'NAO_RESPONDIDO', 1, 1);
INSERT INTO TOPICO(titulo, mensagem, data_criacao, status, autor_id, curso_id) VALUES('Dúvida 3', 'Tag HTML', '2019-05-05 20:00:00', 'NAO_RESPONDIDO', 1, 2);
```

#### 2.4 Spring JPA Repository, interfaces and methods

[Spring Data Jpa reference documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#preface)

It is also not a good practice to invoke database transactions directly from the Controller. Rather, use the Repository pattern.

In Spring, you can create an interface Repository, which inherits from JPARepository from Spring Data JPA. This interface has several methods that encapsulates database queries. There is a specific pattern to create queries called [`Property Expressions`](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-property-expressions), **just build a signature method using nomeclature, concatenating atributes** for example `findbyCursoNome` -> it SELECT attribute `curso` in the Table Topico and then JOIN attribute `nome` in the Curso Table, all under the hood.

```java
public interface TopicoRepository extends JpaRepository<Topico,Long> {
    List<Topico> findByCurso_Nome(String nomeCurso);
}
```

Notice that it must be a sql relationship between these atributes. You can concatenate several attributes, for example: `findByCurso_Categoria_Nome`.... The underline `_` is helpful to desambiguate a potential single atribute named CursoCategoriaNome.

But if do not want to use JPA nomenclature to name your method, build a manual query and annotate with @Query.

```java
@Query("SELECT t FROM Topico t WHERE t.curso.nome = :nomeCurso")
List<Topico> carregarPorNomeDoCurso(@Param("nomeCurso") String nomeCurso);

```

#### 3. POST Request

In this case, the Controller needs a `PostMapping`, specify the entry URL, and control the format of the body. Then it invokes the repository using another DTO to execute the operation (add a record) in the database. If everything is succesful, it is a good practice to return a status code **201** back to the client, or manage any errors.

The `@RequestBody` annotation maps the HttpRequest body to a transfer (DTO) or domain object, enabling automatic deserialization (using Jackson) of the inbound HttpRequest body onto a Java object.

Spring automatically deserializes the JSON into a Java type, assuming an appropriate one is specified. In our example we specified a `TopicoForm` type, which must correspond to the JSON sent from our client-side controller.

```java
@Autowired
private TopicoRepository topicoRepository;

@Autowired
private CursoRepository cursoRepository;

@PostMapping
    public ResponseEntity<TopicoDTO> cadastrar(@RequestBody TopicoForm form, UriComponentsBuilder uriBuilder) {

        //  Need to convert TopicoForm to Topico
        // For that we encapsulate conversion as method in TopicoForm
        // TopicoForm only has the nomeCurso, but Topico needs a Curso object
        // Therefore we create a Curso Repository and injects in the converter
        // so the implementation of method converter can query nomeCurso and return the Curso object
        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDTO(topico));

    }
```

For that we define a method `cadastrar` that returns a ResponseEntity of type `created` that will yield a status code 201. For that this method needs an URI of the newly resource created so it can be sent back to the client along with a body that displays a new TopicDTO.

Spring has methods to create URI, and it uses the same prefix from the client, that's why it is invoked in the signature of cadastra method after the BodyRequest. With this, it can create the uri, specifying only the latter path ("/topicos/id"), and get the newly created id from topico.getid().

#### 4. Bean Validation

How to ensure the data coming from the client is correct, so the controller can execute the Repository correctly? We could build manually a series of data checks (fields missing, fields wrong type values, fields with values not in Range, etc) and return error messages back to the client. Luckly Spring has a library called [`Bean Validation`](https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/validation.html) that makes it easier to do it. First thing is to add this dependency in POM.xml

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

Then add the annotations in the Entity that will receive data from client-side, in our case, the entity `TopicoForm`.

There are several built-in annotations, such as @NotNull, @NotEmpty, ..., however you can create a custom annotation, for example, to validate CPF, etc.

```java
public class TopicoForm {
    @NotNull @NotEmpty @Length(min = 5, max = 50)
    private String titulo;
    @NotNull @NotEmpty @Length(min = 5, max = 300)
    private String message;
    @NotNull @NotEmpty @Length(min = 5, max = 30)
    private String nomeCurso;

```

Lastly, you need to inform the Controller that object coming from @BodyRequest should be validated. For that, annotate with `@Valid`.

```java
@PostMapping
    public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
```

Now, when the post request comes with data that is not Valid by Bean Validation, then Spring automatically rejects is and send back a response code 400 - Bad Request followed by a extense `Stack Trace` Error in JSON. In the next section, we will customize/simplify the message.

##### 4.1 Customizing BadResponse message to client

A good practice is to create a package/class to handle error treatment. So every time there is an validation error, this handler class will intercept the bad request message and treat it as you define.

1. Create a Handler annotated with `@RestControllerAdvice`, that automatically will be injected a `MethodArgumentNotValidException.class`, meaning that any exception that happens in the controller, will be sent to the Handler.

```java
@RestControllerAdvice
public class ErroDeValidacaoHandler {
    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErroDeFormularioDto> handleError(MethodArgumentNotValidException exception){
        List<ErroDeFormularioDto> dto = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        fieldErrors.forEach(e -> {
            String message = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            ErroDeFormularioDto erro = new ErroDeFormularioDto(e.getField(),message);
            dto.add(erro);
        });
        return dto;
    }
}
```

2. Create a DTO to be sent to client with the customized fields. Handler will adapt the messages and return it to the client.

```java
public class ErroDeFormularioDto {

    private String campo;
    private String erro;

    public ErroDeFormularioDto(String campo, String erro) {
        this.campo = campo;
        this.erro = erro;
    }
```

#### 5. Get Request (one element)

Example of requesting to get a specific topic by its `id`.

```java
@GetMapping("/{id}")
    public TopicoDetalhadoDTO oneTopico(@PathVariable("id") Long id){
        Topico topico = topicoRepository.getReferenceById(id);
        TopicoDetalhadoDTO dto = new TopicoDetalhadoDTO(topico);;
        return dto;
    }
```

Here again, the controller delivers a DTO (TopicoDetalhadoDTO).

```java
// Constructor
 public TopicoDetalhadoDTO(Topico topico) {
        this.id = topico.getId();
        this.titulo = topico.getTitulo();
        this.mensagem = topico.getMensagem();
        this.dataCriacao = topico.getDataCriacao();
        this.curso= topico.getCurso();
        this.status=topico.getStatus();
        this.autor = topico.getAutor();
        //this.respostas = topico.getRespostas();
        this.respostas.addAll(topico.getRespostas().stream().map(RespostaDto::new).collect(Collectors.toList()));
    }
```

### 6. Other CRUD Operations

Here we also included a verification whether the `id` in the request exists. For that we make a request `topicoRepository.findById(id)` to return a `Optional`. If the Optional exists, the make the transaction, otherwise return a `ResponseEntity.notFound().build()`.

#### 6.1 Update

Here we also use a specific DTO, because we may update just a certain number of fields rather than all of them. Therefore we create the class `AtualizacaoTopicoForm`.

And then we use a method `atualizar` that encapsulates the mapping from the form to the `Topico`. The annotation `@Transactional` make sure the databse record is updated.

```java
@PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDTO> update(@PathVariable("id") Long id,@RequestBody @Valid AtualizacaoTopicoForm form) {
        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            form.atualizar(id, topicoRepository);
            return ResponseEntity.ok(new TopicoDTO(topico.get()));
        }
        return ResponseEntity.notFound().build();
    }
```

#### 6.2 Delete

This is similar as above.

```java
@DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<?> deleteTopic(@PathVariable("id") Long id){
        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
```
