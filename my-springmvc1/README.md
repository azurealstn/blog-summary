# @RequestParam과 @ModelAttribute 기능

이번에는 Spring에서 제공하는 `@RequestParam`과 `@ModelAttribute`에 대해 알아보자!

## @RequestParam

Spring에서 제공하는 `@RequestParam`을 사용하면 요청 파라미터를 매우 편리하게 사용할 수 있다. 바로 예제를 살펴보자!

## 예제1

- 요청 url: http://localhost:8080/param1?name=mike&age=20

```java
@Slf4j
@RestController
public class RequestParamEx {

    @GetMapping("/param1")
    public String param1(
            @RequestParam("name") String name,
            @RequestParam("age") int age
    ) {
        log.info("name={}, age={}", name, age);
        return "OK";
    }
}
```

로그를 확인해보면 쿼리 파라미터로 요청한 데이터를 확인할 수 있다. 또한 `@RequestParam`은 파라미터값과 변수명이 같으면 생략이 가능하다.

```java
@GetMapping("/param2")
public String param2(
        @RequestParam String name,
        @RequestParam int age
) {
    log.info("name={}, age={}", name, age);
    return "OK";
}
```

그리고 `String, int, Integer` 등의 단순 타입이면 `@RequestParam` 자체도 생략할 수 있다.

```java
@GetMapping("/param3")
public String param3(String name, int age) {
    log.info("name={}, age={}", name, age);
    return "OK";
}
```

하지만 `@RequestParam`까지 생략해버리면 스프링에 익숙하지 않다면 직관적으로 봤을 때 어떤 파라미터를 받는지 확인이 어렵다. 하지만 `@RequestParam`이 있으면 요청 파라미터로 받는지 확연히 보이니까 왠만하면 `@RequestParam`은 생략하지 말자!

## 예제2

`@RequestParam`은 옵션으로 필수여부 값을 설정할 수도 있다. 예제를 보자!

#### 요청 URL: http://localhost:8080/param4  
만약 위와 같이 요청 URL에 쿼리 파라미터를 넣지 않고 `@RequestParam`을 사용하게 되면 다음과 같은 에러가 발생한다.

```text
Required request parameter 'name' for method parameter type String is not present
```

파라미터가 필수로 있어야 된다는 에러가 발생하는데 그 이유는 `@RequestParam`의 옵션에서 `required`가 기본적으로 `true`로 설정되어 있다. 이는 필수로 파라미터를 넣어줘야 하는데 이 `required`를 `false`로 설정하면 필수가 아니게 되서 에러가 발생하지 않는다.

```java
@GetMapping("/param4")
public String param4(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) int age
) {
    log.info("name={}, age={}", name, age);
    return "OK";
}
```

하지만 위와 같이 헀는데도 에러를 발생하네요(?)..

```text
Optional int parameter 'age' is present but cannot be translated into a null value due to being declared as a primitive type
```

자바 언어에서 **Primitive Type**의 변수는 `null`을 허용하지 않는다. 따라서 타입을 **Wrapper Class**로 변경하면 정상적으로 동작한다.

```java
@GetMapping("/param4")
public String param4(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Integer age
) {
    log.info("name={}, age={}", name, age);
    return "OK";
}
```

또 하나 옵션이 있는 바로 `defaultValue`이다. 파라미터 값이 없을 때 `defaultValue` 값이 들어간다.

```java
@GetMapping("/param4")
public String param4(
    @RequestParam(required = false, defaultValue = "Mike") String name,
    @RequestParam(required = false, defaultValue = "20") Integer age
) {
    log.info("name={}, age={}", name, age);
    return "OK";
}
```

- 요청 URL: http://localhost:8080/param4?name=&age=
  - 쿼리 파라미터 값에 아무 값을 안 넣었을 때 `defaultValue` 값이 들어간다.
- 요청 URL: http://localhost:8080/param4
  - 쿼리 파라미터가 없을 때 `defaultValue` 값이 들어간다.

즉, `null`이든 `""(빈 문자열)`이든 `defaultValue` 값이 들어간다.  
또한 `required = true`이면 `defaultValue`를 쓰는 의미가 없다. (`required = false`일 때 `defaultValue`를 사용하자.)  

> @RequestParam은 String 뿐만 아니라 map으로도 받을 수 있다는 것을 참고하자.

## @ModelAttribute

Spring에서 제공하는 `@ModelAttribute`을 사용하면 요청 파라미터의 값을 자바 오브젝트에 딱 매핑시켜주는 작업을 자동화해준다.

## 예제

### TestDto 클래스

```java
@Data
public class TestDto {

   private String name;
   private int age;
}

```

### ModelAttributeEx 클래스

```java
@Slf4j
@RestController
public class ModelAttributeEx {

    @GetMapping("/model-attribute")
    public String modelAttribute(@ModelAttribute TestDto testDto) {
        log.info("name={}, age={}", testDto.getName(), testDto.getAge());
        return "OK";
    }
}
```

기존에는 `@RequestParam`으로 변수 하나하나를 선언했지만 `@ModelAttribute`을 사용하게 되면 자바 오브젝트에 바인딩되어 값을 확인할 수 있다. 하지만 객체 프로퍼티가 다르면 `null`이 나온다.

> 프로퍼티란 객체에 getName(), setName() 메서드가 있으면 이 객체는 name 이라는 프로퍼티를 가지고 있는 것이다.

#### 요청 URL: http://localhost:8080/model-attribute?name=Mike&age=Mike

또한 `TestDto` 클래스의 `age`의 타입이 `int`인데 요청 URL을 위와 같이 요청하게 되면 `BindException` 예외를 발생시킨다.

`@ModelAttribute` 또한 생략이 가능하다!

```java
@GetMapping("/model-attribute")
public String modelAttribute(TestDto testDto) {
    log.info("name={}, age={}", testDto.getName(), testDto.getAge());
    return "OK";
}
```

### 참고!

기본적으로 `@RequestParam`이나 `@ModelAttribute`는 요청 파라미터 즉, 쿼리 파라미터에 대한 값을 받아올 수 있다고 생각했지만 **POST 요청**으로도 값을 받을 수 있다. 다만 **HTML Form - POST 전송**일 경우에만 해당된다.

그 이유는 쿼리 파라미터의 데이터 구조와 HTML Form 태그에서의 POST 전송할 때 데이터 구조가 동일하기 때문이다. (name=Mike&age=30)  
따라서 `@RequestParam`, `@ModelAttribute`는 GET 요청의 쿼리 파라미터이거나 `Content-Type`이 `x-www-form-urlencoded`인 HTML Form태그 POST 요청하면 데이터를 받을 수 있다. (이외에 Http Messsage Body의 데이터를 `@RequestParam`이나 `@ModelAttribute`로 조회할 수 없다.)

## @ModelAttribute의 또 다른 기능

`@ModelAttribute`의 또 다른 기능은 `Model`에다가 `@ModelAttribute`로 지정한 객체를 자동으로 담아준다. 

```java
@PostMapping("/model-attributeV2")
public String modelAttributeV2(@ModelAttribute("testDto12") TestDto testDto) {
    //model.addAttribute("testDto12", testDto); 생략 가능
    modelService.save();
    return "OK";
}
```

원래라면 `@ModelAttribute`를 사용하면 요청 파라미터의 데이터를 객체에 바인딩되어 그 객체를 또 `Model`에 담는 행위를 해야하는데 그러지 않아도 된다. 위 코드처럼 `@ModelAttribute("testDto12")`에서 파라미터에 `name`을 지정해주면 주석처리된 부분(`model.addAttribute("testDto12", testDto);`)을 생략할 수 있다.

또한 `@ModelAttribute("testDto12")`에서 파라미터에 `name`을 생략할 수도 있다. 대신에 생략하면 아래 코드처럼 동작한다.

```java
@PostMapping("/model-attributeV2")
public String modelAttributeV2(@ModelAttribute TestDto testDto, Model model) {
    //model.addAttribute("testDto", testDto); 생략 가능
    modelService.save();
    return "OK";
}
```

즉, `name`을 생략하면 `TestDto` 객체의 첫번째 알파벳을 소문자로 변경해서 `Model`에 담게 된다.

- TestDto -> testDto
- `@ModelAttribute`도 생략이 가능하다. (하지만 스프링에 익숙하지 않다면 굳이 생략하지 말고 명시적으로 적어두자! - 이유는 헷갈린다..)

## !참고 (@ModelAttribute의 기능 한 가지 더)

```java
@Controller
public class AnotherModelAttribute {

    @GetMapping("/ex1")
    public String ex1(TestDto reqDto, Model model) {
        List<String> list = new ArrayList<>();
        list.add("키보드");
        list.add("마우스");
        list.add("모니터");

        model.addAttribute("list", list);
        return "hello/test";
    }
    @GetMapping("/ex2")
    public String ex2(TestDto reqDto, Model model) {
        List<String> list = new ArrayList<>();
        list.add("키보드");
        list.add("마우스");
        list.add("모니터");

        model.addAttribute("list", list);
        return "hello/test";
    }
    @GetMapping("/ex3")
    public String ex3(TestDto reqDto, Model model) {
        List<String> list = new ArrayList<>();
        list.add("키보드");
        list.add("마우스");
        list.add("모니터");

        model.addAttribute("list", list);
        return "hello/test";
    }
}
```

위 코드처럼 컨트롤러마다 `List`를 생성해서 데이터를 추가하고 `Model`에 담는 반복적인 코드가 있다. 이 반복을 줄일 수 있는 방법을 스프링은 제공한다.

```java
@Controller
public class AnotherModelAttribute {

    @ModelAttribute("list")
    public List<String> list() {
        List<String> list = new ArrayList<>();
        list.add("키보드");
        list.add("마우스");
        list.add("모니터");
        return list;
    }

    @GetMapping("/ex1")
    public String ex1(TestDto reqDto, Model model) {
        return "hello/test";
    }
    @GetMapping("/ex2")
    public String ex2(TestDto reqDto, Model model) {
        return "hello/test";
    }
    @GetMapping("/ex3")
    public String ex3(TestDto reqDto, Model model) {
        return "hello/test";
    }
}
```

`list`를 반환하는 메서드를 따로 만들어서 `@ModelAttribute("list")` 애노테이션을 사용하면 컨트롤러 호출마다 해당 리턴값을 `Model`에 담아준다.



## 마무리

`@RequestParam`과 `@ModelAttribute`를 그 동안 왜 써야하는지 모르고 그냥 복붙해서 사용해왔었는데 이제는 나름대로 잘 활용할 수 있을 것 같다. 🎉

## References

- [인프런 - 스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1#)
- https://amagrammer91.tistory.com/106
 