# SOLID

SOLID란 로버트 마틴(클린코드의 저자)이 정의한 객체 지향 프로그래밍 및 설계의 다섯 가지 기본 원칙이다.  
유지 보수와 확장이 쉬운 시스템을 만들고자 할 때 이 원칙들을 적용할 수 있다.

![myPaint drawio](https://user-images.githubusercontent.com/55525868/163705430-66b7031b-7bde-4907-8e90-1866891f4036.png)

위 설계대로 구현을 해보았다.

![패키지1](https://user-images.githubusercontent.com/55525868/163705555-9b6778d7-53d7-44a2-b103-829d696916c4.PNG)

패키지 구성은 위와 같이 구성하였고, 모든 소스코드는 [github](https://github.com/azurealstn/blog-summary/tree/main/solid)에서 확인할 수 있다.

## SRP (단일 책임 원칙)

SRP는 Single Responsibility Principle 의 약자로,  
**한 클래스는 하나의 책임만 가져야 한다.**

## OCP (개방-폐쇄 원칙)

OCP는 Open/Closed Principle 의 약자로,  
**소프트웨어 요소는 확장에는 열려 있으나 변경에는 닫혀 있어야 한다.**

## LSP (리스코프 치환 원칙)

LSP는 Liskov Substitution Principle 의 약자로,  
**프로그램의 객체는 프로그램의 정확성을 깨뜨리지 않으면서 하위 타입의 인스턴스로 바꿀 수 있어야 한다.**

## ISP (인터페이스 분리 원칙)

ISP는 Interface Segregation Principle 의 약자로,  
**특정 클라이언트를 위한 인터페이스 여러 개가 범용 인터페이스 하나보다 낫다.**

## DIP (의존관계 역전 원칙)

DIP는 Dependency Inversion Principle 의 약자로,  
**프로그래머는 "추상화에 의존해야지, 구체화에 의존하면 안된다."**

개념은 위 글로 알아보고 바로 예제를 통해 각각 무슨 의미인지 살펴보자.  
먼저 연주 서비스 구현체에 해당하는 `PlayServiceImpl`클래스를 보자.

```java
public class PlayServiceImpl implements PlayService {

    Guitar guitar = new AcousticGuitar();
    //Guitar guitar = new ElectricGuitar();

    @Override
    public Play createPlay(Long id, String player, String song) {
        String guitarKindsName = guitar.guitarKinds(song);
        return new Play(id, player, song, guitarKindsName);
    }
}
```

`PlayServiceImpl` 클래스는 연주 서비스 역할인 `PlayService`인터페이스의 구현체다.  
연주 생성 메서드인 `createPlay()`를 오버라이드해서 구현하는데 여기서 중요한건 `Guitar guitar = new AcousticGuitar();`이 부분이다.  
`Guitar guitar = new AcousticGuitar();`와 같은 코드는 **구현 객체를 직접 생성해주고 변경이 일어날 때마다 개발자는 매번 코드를 건드려야 한다는 단점이 있다.**  
예를 들어, '통기타'가 아닌 '일렉기타'를 선택하려면 `Guitar guitar = new ElectricGuitar();`로 변경해야 한다.

### DIP 위반

클라이언트쪽(PlayServiceImpl)에 `Guitar guitar = new AcousticGuitar();`와 같은 코드는 **추상화에도 의존하고 있고, 구체화에도 의존하고 있다.**  
이는 DIP를 위반한다.

### OCP 위반

'통기타'가 아닌 '일렉기타'를 선택하려면 기존의 코드를 변경을 해야 했다. 이는 **확장에는 열려있으난 변경에도 열려있는 경우다.**  
그래서 OCP를 위반한다.

### SRP 위반

또한 클라이언트인 `PlayServiceImpl` 클래스는 **직접 객체를 생성하고 연결하고 실행하는 역할을 모두 수행하는 다중 책임을 가지고 있다.**  
이는 SRP를 위반한다.

그래서 DIP, OCP, SRP 의 원칙을 따르기 위해 `MyContainer`라는 외부 클래스를 두면 모두 해결된다.

```java
public class MyContainer {

    public PlayService playService() {
        return new PlayServiceImpl(guitar());
    }

    public Guitar guitar() {
        return new AcousticGuitar();
    }
}
```

`MyContainer`클래스의 역할은 클라이언트가 수행할 역할들인 객체를 생성해주고, 연결해주는 역할을 대신 수행해준다.  
그래서 `PlayServiceImpl`클래스를 아래와 같이 변경해준다.

```java
public class PlayServiceImpl implements PlayService {

//    Guitar guitar = new AcousticGuitar();
    private Guitar guitar;

    public PlayServiceImpl(Guitar guitar) {
        this.guitar = guitar;
    }

    @Override
    public Play createPlay(Long id, String player, String song) {
        String guitarKindsName = guitar.guitarKinds(song);
        return new Play(id, player, song, guitarKindsName);
    }
}
```

먼저 클라이언트는 구현체가 아닌 인터페이스만 의존하도록 `private Guitar guitar;`와 같이 변경해준다.  
그리고 `private Guitar guitar;` 이거만 써주면 당연히 `NullPointerException`이 발생하기 때문에 생성자를 만들어서 `Guitar`인터페이스를 주입해준다.  
이것을 **생성자 주입**이라고 한다.  

생성자 주입을 해주게 되면 `MyContainer`클래스가 아래 코드를 실행시켜서 `AcousticGuitar`클래스가 주입된다.

```java
public PlayService playService() {
    return new PlayServiceImpl(guitar());
}
```

마지막으로 아래 코드로 테스트를 해주었다.

```java
class PlayServiceTest {

    private PlayService playService; //인터페이스만 의존

    @BeforeEach
    public void beforeEach() {
        MyContainer myContainer = new MyContainer();
        playService = myContainer.playService(); //주입
    }

    @Test
    void createPlay() {
        //given
        Long id = 1L;
        String player = "sungHa";
        String song = "let it go";
        String guitarKinds = "통기타";

        //when
        Play play = playService.createPlay(1L, player, song);

        //then
        assertThat(play.getGuitarKinds()).isEqualTo(guitarKinds);
    }
}
```

## 정리

`MyContainer`클래스가 대신 객체를 생성해주고 연결해줌으로서 클라이언트인 `PlayServiceImpl`클래스는 실행해주는 역할에만 집중할 수 있다.  
즉, **하나의 역할을 수행하는 단일 책임 원칙을 지키게 된다.** - SRP

클라이언트인 `PlayServiceImpl`클래스는 `private Guitar guitar;`처럼 더 이상 구현체에 의존하고 있지 않다.  
즉, **추상화에 의존해야지 구체화에 의존하면 안된다.** - DIP

클라이언트인 `PlayServiceImpl`클래스는 인터페이스에만 의존하고 있기 때문에 '통기타'에서 '일렉기타'로 변경하기 위해 더이상 코드를 변경하지 않아도 된다.  
즉, **확장에는 열려있고, 변경에는 닫혀있다.** - OCP

추가로 LSP는 한마디로 **인터페이스의 규약을 지켜야 한다는 것**이다.  
예를 들어, `Guitar`인터페이스에서 기타 종류를 구현하는 `guitarKinds()`메서드를 이상한(?) 생선종류로 구현할 수도 있다.  
하지만 이것은 내가 원하는 것이 아니다. `Guitar`인터페이스면 그 인터페이스가 원하는 설계대로 구현 객체를 구현해야 한다.

그리고 ISP는 **범용 인터페이스 하나를 여러 개의 인터페이스로 나누는 것이다.**  
위 예제 소스에서는 `Guitar`인터페이스가 좀 범용적인 느낌이 있다.  
그래서 이 인터페이스를 `GuitarRepair`인터페이스(기타 수리)와 `GuitarPlay`인터페이스(기타 연주)로 나누는 것이 좀 더 ISP 원칙을 지킬 수 있다.

그리고 `MyContainer`클래스처럼 대신 객체를 생성해주고 연결해주는 역할을 수행하는 컨테이너가 바로 Spring의 **IoC 컨테이너 또는 DI 컨테이너**이다.

### 프레임워크 VS 라이브러리

`MyContainer`클래스처럼 대신 객체를 생성해주고 연결해주는 것을 **제어의 역전**이라고 한다. (클라이언트가 직접 `new`해서 객체를 생성하지 않고, 직접 주입하지 않는다.)  
이처럼 누군가 대신 해주는 것을 `Framework`라 하고, 클라이언트가 직접 코드를 제어하면 이것은 `Library`이다.

## 참고

위 정리는 [인프런의 스프링 핵심 원리 - 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8#) 을 통해 나름대로 정리를 해보았습니다.  
사실 위 내용은 자바를 처음 공부하는 분은 이해하기 힘들 수도 있습니다.  
그렇다면 인프런의 스프링 핵심 원리 - 기본편 강좌를 꼭 추천드립니다. 너무 좋은 강의이고 저도 3~4번은 봤네요 ㅎㅎ;

- https://ko.wikipedia.org/wiki/SOLID_(%EA%B0%9D%EC%B2%B4_%EC%A7%80%ED%96%A5_%EC%84%A4%EA%B3%84)