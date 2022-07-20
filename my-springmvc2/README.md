# HttpEntity, @RequestBody, @ResponseBody의 기능 (HTTP 메시지 컨버터)

`@RequestParam`과 `@ModelAttribute`는 요청 파라미터 즉, 쿼리 파라미터의 데이터를 조회하는데 사용되고, Http Message Body의 데이터는 조회할 수 없었다. (단, HTML Form에서 POST 요청은 제외)  
그렇다면 Http Message Body의 데이터는 어떻게 조회할 수 있을까? 바로 예제를 통해 알아보자. 예제에서 다룰 데이터 포맷은 `JSON` 위주로 다룰 것이다. (제일 많이 사용하니까!)

## HttpEntity

`HttpEntity`는 HTTP Message Body 데이터를 직접 조회할 수 있다. 또한 HTTP header와 body 정보를 편리하게 조회할 수 있다.

```java
@Slf4j
@Controller
public class RequestBodyJsonController {

    @PostMapping("/request-body-json")
    public HttpEntity<String> requestBodyJson(HttpEntity<Hello> httpEntity) {
        Hello hello = httpEntity.getBody();
        log.info("name={}, age={}", hello.getName(), hello.getAge());
        log.info("headers={}", httpEntity.getHeaders());
        return new HttpEntity<>(HttpStatus.OK.getReasonPhrase());
    }
}
```

`HttpEntity`는 응답에도 사용할 수 있다. 스프링에서는 `HttpEntity`의 제네릭 타입을 객체로 지정해주면 요청한 JSON 데이터를 자바 객체로 매핑시켜준다.

### RequestEntity, ResponseEntity

`HttpEntity`의 자식 클래스인 `RequestEntity`와 `ResponseEntity`를 사용하면 더 다양한 기능을 사용할 수 있다.

```java
@PostMapping("request-body-json-v2")
public ResponseEntity<HttpStatus> requestBodyJsonV2(RequestEntity<Hello> requestEntity) {
    Hello hello = requestEntity.getBody();
    log.info("name={}, age={}", hello.getName(), hello.getAge());
    log.info("url={}", requestEntity.getUrl());
    return new ResponseEntity<>(HttpStatus.OK);
}
```

HTTP Message Body의 요청 데이터를 받을 때는 `RequestEntity`를 사용하고, 응답할 때는 `ResponseEntity`를 사용하면 된다. `RequestEntity`는 헤더 정보뿐만 아니라 URL과 HTTP Method 정보도 얻을 수 있다. `ResponseEntity`는 파라미터로 HTTP 상태코드를 받을 수 있다.

## @RequestBody, @ResponseBody

HTTP Message Body의 데이터를 조회할 때 `HttpEntity`보다 더 편리하게 사용할 수 있는 것이 바로 `@RequestBody`이다.

```java
@Slf4j
@Controller
public class RequestBodyJson2Controller {

    @ResponseBody
    @PostMapping("/request-body-json2")
    public String requestBodyJson2(@RequestBody Hello hello) {
        log.info("name={}, age={}", hello.getName(), hello.getAge());
        return "OK";
    }
}
```

파라미터에 `@RequestBody Hello hello` 이렇게 적어주면 HTTP Message Body의 데이터(JSON)가 Hello 객체에 매핑된다. 이것이 가능한 이유는 스프링이 JSON을 자바 객체로 변환해주는 작업을 자동화해주었고, 만약 스프링을 사용하지 않는다면 Jackson 라이브러리인 `ObjectMapper`라는 객체를 이용해서 자바 객체로 변환해주어야 한다.  
반환 타입을 `String`이 아닌 객체를 사용해도 된다.

```java
@ResponseBody
@PostMapping("/request-body-json2-v2")
public Hello requestBodyJson2V2(@RequestBody Hello hello) {
    log.info("name={}, age={}", hello.getName(), hello.getAge());
    return hello;
}
```

응답의 경우 `@ResponseBody`를 사용하게 되면 객체 타입을 JSON으로 변환해준다.

- `String`을 반환하는 경우 `@ResponseBody`을 사용하지 않으면 뷰 리졸버가 실행되어서 뷰를 찾고 렌더링한다.
- 하지만 `@ResponseBody`을 사용하면 HTTP Message Body에 응답 데이터를 출력한다.
- 앞서 `ResponseEntity`는 HTTP 상태코드를 넣을 수 있었지만 `@ResponseBody`는 지정할 수 없다. 그래서 `@ResponseStatus(HttpStatus.OK)` 라고 애노테이션을 추가해주면 된다.
- `@ResponseBody`를 생략할 수 있는 방법이 있는데 `@Controller` 대신에 `@RestController`를 사용하면 된다.

## HTTP 메시지 컨버터

**HTTP 메시지 컨버터**는 자바 객체를 JSON 타입으로 변화해주고(직렬화), JSON 타입을 자바 객체로 변화해주는(역직렬화) 역할을 수행한다. 앞에서 스프링이 JSON을 자바 객체로 변환해주는 작업을 자동화해준다고 했는데 그것이 바로 HTTP 메시지 컨터버가 하는 일이다.
 
`HttpEntity(RequestEntity, ResponseEntity)`, `@RequestBody`, `@ResponseBody`를 사용하면 HTTP 메시지 컨버터가 JSON을 자바 객체로 또는 자바 객체를 JSON으로 변환해준다. 이 때 사용되는 HTTP 메시지 컨버터는 `MappingJackson2HttpMessageConverter`이다.

- `RequestEntity`, `@RequestBody`를 사용하게 되면 JSON 요청시에 HTTP 메시지 컨버터가 동작하게 되고 `MappingJackson2HttpMessageConverter`로 인해 자바 객체로 변환된다.
- `ResponseEntity`, `@ResponseBody`를 사용하게 되면 HTTP 메시지 컨버터가 동작하게 되고 `MappingJackson2HttpMessageConverter`로 인해 자바 객체를 JSON 타입으로 변환해주고 이 JSON 데이터를 HTTP 메시지 바디에 담아서 응답해준다.

스프링은 기본적으로 HTTP 메시지 컨버터로 아래와 같이 여러 개를 등록해두었다.

- `ByteArrayHttpMessageConverter`
- `StringHttpMessageConverter`
- `MappingJacksonHttpMessageConverter`
- 등등 ..

`ByteArrayHttpMessageConverter`는 이름 그대로 `byte[]` 타입으로 HTTP 메시지 바디 데이터를 받을 수 있고 응답할 수 있다.  
`StringHttpMessageConverter`는 역시 이름 그대로 `String` 타입으로 HTTP 메시지 바디 데이터를 받을 수 있고 응답할 수 있다.  
`MappingJacksonHttpMessageConverter`는 JSON 요청 -> Java Object 로 변환해서 HTTP 메시지 바디 데이터를 받을 수 있고, Java Object -> JSON으로 변환해서 응답할 수 있다.

> MappingJacksonHttpMessageConverter 같은 경우는 MIME 타입(미디어 타입 또는 Content-Type)이 application/json 이어야 JSON을 처리할 수 있는 HTTP 메시지 컨버터가 실행된다.



## References

- [인프런 - 스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1#)
- [www.springcloud.io - HTTP 메시지 컨버터](https://www.springcloud.io/post/2022-02/understand-spring-httpmessageconverter/#gsc.tab=0)
- [직렬화 - 역직렬화](https://hanbulkr.tistory.com/14)