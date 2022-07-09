# 생성자 주입을 권장하는 이유

스프링 강의를 보다보면 `필드 주입`이나 `수정자 주입`을 사용하기 보다는 `생성자 주입` 사용을 더 권장한다고 들었다.
그래서 개인 프로젝트 할 때 생성자 주입을 사용하고는 있는데 "왜 생성자 주입을 권장하는 걸까" 궁금증이 생겼다.
이 참에 다양한 의존관계 주입 방법 중에서도 굳이 생성자 주입을 권장하는 이유를 알아보자!

## 구조

![inject1](https://user-images.githubusercontent.com/55525868/178095891-41eac6ae-e5d8-4818-a044-fedb4ce41eab.png)

## 수정자 주입

수정자 주입(Setter Injection)은 이름 그대로 setter 메서드를 사용하여 객체를 주입하는 의존관계 주입이다.

- 선택적이며 변경 가능성이 있는 의존관계에 사용한다.
  - 선택적: `UserService`가 스프링 빈에 등록이 안되어도 선택적으로 사용할 수 있다. (이 때는 `@Autowired(required = false` 옵션을 주어야 한다.)
  - 아무래도 `setter` 메서드이다 보니 외부에서도 변경할수가 있다.
- 자바빈 프로퍼티 규약의 setter 메서드 방식을 사용한다. (참고 - https://docstore.mik.ua/orelly/java-ent/jnut/ch06_02.htm)

### 코드 예시

```java
package com.azurealstn.constructorinjection.inject;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public User selectUser() {
        User user = new User(1L, "userA", 30);
        userService.saveUser(user); //유저 생성
        return userService.selectUser(user.getId()); //유저 조회
    }
}
```

## 필드 주입

필드 주입(Field Injection)은 이름 그대로 필드에 바로 주입하는 방식이다.

- 코드가 제일 깔끔하게(?) 생겼다.

```java
@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public User selectUser() {
        User user = new User(1L, "userA", 30);
        userService.saveUser(user); //유저 생성
        return userService.selectUser(user.getId()); //유저 조회
    }
}
```

## 수정자 주입과 필드 주입의 단점

### 수정자 주입

- 대부분의 의존관계 주입은 한번 일어나면 애플리케이션 종료시점까지 의존관계를 변경할 일이 없다. 오히려 누군가 변경하게 된다면 문제를 발생시킬 가능성이 높아진다. 하지만 수정자 주입은 의존관계를 변경시킬 수 있다.
  - javascript 를 공부한 사람이라면 `let, const`를 비유할 수 있다. 실무에서는 누군가 변경할 수 있는 `let`보다는 변경 불가능한 `const`를 더 많이 사용한다. (여러 이점이 있다.)

### 필드 주입

- 수정자 주입과 생성자 주입처럼 의존관계가 보이지 않는다. 이는 순수한 자바 코드로 jUnit 테스트 코드 짜기가 어려워진다. (예제를 보자)
- 의존관계가 보이지 않는다는 것은 결국에 DI 컨테이너에 의존하고 있다는 의미이다. -> DI 컨테이너가 없으면 아무것도 할 수가 없다.

### 필드 주입 예제

```java
class UserServiceTest {

    @Test
    @DisplayName("Field Injection 테스트")
    void fieldInjection() {
        UserService userService = new UserService();
        //userService.setUserRepository(new UserRepository());
        User user = new User(1L, "userA", 30);
        userService.saveUser(user);
    }
}
```

위 코드는 당연히 `NullPointerException`이 뜬다. 그래서 `userService.setUserRepository` 처럼 `setter`를 열어줘야 하는데 그러면 결국엔 `setter injection`을 쓰고 말지 `Field Injection`을 굳이 사용하진 않을 것이다.

## 결론은 생성자 주입이 답이다.

생성자 주입(Constructor Injection)은 이름 그대로 생성자를 통해서 의존관계를 주입하는 것을 말한다.

- 의존관계 주입 방식중 가장 많이 사용하는 주입 방식이다.

```java
@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired //생성자가 하나 있을 때는 @Autowired 생략이 가능하다.
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //유저 생성
    public void saveUser(User user) {
        userRepository.save(user);
    }

    //유저 조회
    public User selectUser(Long id) {
        return userRepository.findById(id);
    }
}
```

생성자 주입에서 롬복(Lombok)을 쓰면 다음과 같이 더 가독성 있는 코드를 작성할 수 있다.

```java
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //유저 생성
    public void saveUser(User user) {
        userRepository.save(user);
    }

    //유저 조회
    public User selectUser(Long id) {
        return userRepository.findById(id);
    }
}
```

결론적으로 얘기하면 **생성자 주입**을 사용하는 것이 좋다! 이유를 살펴보자.

### 생성자 호출시점에 딱 1번만 호출되는 것이 보장된다. -> 불변

- 생성자 호출시점은 언제인가?? 바로 객체를 생성할 때이다.  
- 스프링 프레임워크의 특징이 무엇인가?? 바로 DI 컨테이너이다.

클라이언트가 직접 `new` 해서 객체를 생성하는 것이 아닌 DI 컨테이너가 대신 객체를 생성해주고, 이 객체를 빈으로 등록한 다음 그에 맞는 빈을 찾아 의존관계 주입을 해준다.
**여기서 포인트는 DI 컨테이너가 대신 객체를 `new` 해서 생성해주기 때문에 이 때 생성자 호출이 발생한다. 생성자 호출이 발생한다는 것은 생성자 주입이 발생한다는 것이고, 결국에 생성자 주입은 빈으로 등록한 동시에 의존관계 주입이 일어난다.**

요점은 생성자는 1번만 호출되는 것이 보장되기 때문에 다른 의존관계를 변경할 수가 없다. 이러한 특징 때문에 **불변**을 갖는다.

코드를 설계할 때 **불변**은 매우 중요하다. 협업하는 도중에 다른 사람들이 막 변경해버리면 꼬일 수 있기 때문에 최대한 변경은 막는 것이 좋다.

### DI 컨테이너에 의존하지 않고 순수한 자바 코드롤 테스트 코드를 작성할 수 있다.

```java
class UserServiceTest {

    @Test
    @DisplayName("Field Injection 테스트")
    void fieldInjection() {
        UserService userService = new UserService(new UserRepository());
        User user = new User(1L, "userA", 30);
        userService.saveUser(user);
    }
}
```

위 코드를 보면 `UserService` 클래스는 어떤 의존관계를 가지고 있는지 눈으로 바로 확인이 가능하다.

```java
UserService userService = new UserService(new UserRepository());
```

이 코드만 봐도 "`UserRepository`를 주입했구나." 알 수가 있다.  
그리고 순수한 자바 코드로 테스트코드를 짤 때 객체를 생성하는데 이 때 만약에 생성자 파라미터에 원하는 구현체를 넘겨주지 않으면 **컴파일 에러**를 발생시킨다. 즉, 실행시점이 아닌 컴파일 때 에러를 발생시켜주기 때문에 바로바로 확인할 수 있다는 장점이 있다.

### final 키워드를 사용할 수 있다.

앞서 수정자 주입이나 필드 주입은 `final` 키워드 사용을 할 수 없었는데 생성자 주입은 `final` 사용이 가능하다.

```java
private final UserRepository userRepository;

@Autowired
public UserService(UserRepository userRepository) {
    //this.userRepository = userRepository;
}
```

- `final` 키워드를 사용해서 생성자를 통해 값을 할당하지 않으면 **컴파일 에러**를 발생시킨다.
- 또한 `final` 키워드를 사용하면 값을 변경시킬 수도 없다.

#### 이제부터 생성자 주입을 사용하지 않을 이유가 없다!

## References

- [인프런 - 스프링 핵심 원리 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8#)
- https://www.geeksforgeeks.org/spring-dependency-injection-by-setter-method/
- https://www.amitph.com/spring-setter-injection-example/
- https://docstore.mik.ua/orelly/java-ent/jnut/ch06_02.htm
- [ohjongsung.io - 필드주입을 피하자](https://ohjongsung.io/2017/06/02/%ED%95%84%EB%93%9C-%EC%A3%BC%EC%9E%85-field-injection-%EC%9D%84-%ED%94%BC%ED%95%98%EC%9E%90)
- https://yaboong.github.io/spring/2019/08/29/why-field-injection-is-bad/