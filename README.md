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