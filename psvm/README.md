# public static void main(String[] args) {}

```java
@SpringBootApplication
public class PsvmApplication {

	public static void main(String[] args) {
		SpringApplication.run(PsvmApplication.class, args);
	}

}
```

위 코드는 스프링 부트 프로젝트를 생성하고 default로 생성되는 class 이다.  
프로젝트가 제대로 생성이 되었는지 먼저 위의 main 메서드를 실행하고 `localhost:8080`을 접속해본다.

자바의 call stack을 봐도 `main()`이 가장 먼저 쌓이고, 즉 자바는 프로그램을 실행시킬 때 **main 메서드를 찾는다.**  
그럼 왜 `public static void main(String[] args) {}`일까?

## 접근제한자

자바 프로그램을 실행시키기 위해서는 `main()` 을 가장 먼저 찾는다.  
즉, main 메서드는 어디에서나 접근이 가능하게 하는 `public`을 두어야 하고, `private`이나 `protected`를 두어 접근을 제한하게 되면 자바 프로그램을 실행시키는데 제약이 있을 수 있다. (public이 아닌 다른 접근제한자를 두면 실행이 안된다.)

## Static

### static 사용 ❌

```java
class Count {
    public int count;

    public Count() {
        this.count++;
        System.out.println(this.count);
    }

}
public class StaticEx {
    public static void main(String[] args) {
        Count count1 = new Count(); //1
        Count count2 = new Count(); //1
    }
}
```

static 을 사용하지 않을 경우 위 코드와 같이 결과가 1로 같다.  
위 코드와 같이 객체(Count)를 `new` 키워드로 생성하게 되면 별도의 **Heap 영역**에 생성이 된다. 이는 Garbage Collector의 대상이 된다.

### static 사용 ⭕

```java
package com.azurealstn.psvm;

class Count2 {
    public static int num = 0;

    public Count2() {
        num++;
        System.out.println(num);
    }
}
public class StaticEx2 {
    public static void main(String[] args) {
        Count2 count1 = new Count2(); //1
        Count2 count2 = new Count2(); //2
    }
}
```

static은 **공유하다**라는 개념이 있다. 그래서 staitc 변수를 사용하게 되면 다른 객체를 생성해도 다른 결과값이 나오는 것을 알 수 있다. 

클래스로더가 바이트코드(.class)를 읽어들여 Runtime Data Area 적재하는데 static 키워드가 발견되면 **Method Area**에 메모리를 할당한다. Static Area라고도 하는데 static 영역은 모든 객체가 공유가 가능하기 때문에 동시성 문제를 일으킬 수 있다. 또한 static 영역에 있는 멤버들은 프로그램의 종료시까지 메모리에 남아있다는 특징때문에 너무 남발하게 되면 **성능 저하**를 일으킬 수 있으니 주의가 필요하다. 👀

(서론이 길었네요..😅) 본론으로 들어와서 `main()`에 static을 사용하는 이유는 `new`해서 객체를 생성하지 않아도 되고, `main()`를 부르는데 메모리 낭비를 막을 수 있고, 무엇보다 `main()`는 무조건 실행되어야 하는 메서드라 객체를 생성해서 heap 영역에 관리하게 되면 Garbage Collector의 대상이 될겁니다.

## void

자바 프로그램에서 프로그램 종료 상태를 나타내는 `return`이 따로 없다. 그래서 `void`를 사용하게 된다.

## String[] args

모든 메소드는 매개변수를 사용할 수도 있고, 안할 수도 있다.
하지만 main 메소드는 다른 곳에서 호출되는 것이 아닌 프로그램 실행 시 처음으로 수행되는 메소드이기 때문에 매개변수가 반드시 필요하다.


위에 `public`, `static`, `void`, `main`, `String[] args` 하나라도 없으면 에러가 발생한다.

## 여기서 잠깐 🔅

C나 C++는 프로그램 종료 상태를 나타내는 타입이 int인 숫자를 `return`한다. 예를 들어, Exit code가 `0`을 반환하게 되면 성공적으로 종료되었다는 의미이다. 반면에 `0`이 아닌 다른 숫자를 반환하면 에러가 발생했다는 의미이다.

그러나 Java 같은 경우에는 JVM 위에서 `main thread`가 동작한다. 이는 C/C++은 코드를 바로 기계어로 변환시키는 반면 Java는 코드를 바이트코드로 변환한 후에 그 바이트코드를 다시 기계어로 변환한다. 따라서 Java Program은 OS에 직접적인 프로세스가 아니다. 이러한 Java의 특징때문에 프로그램을 종료시키는데 `return`값이 없는 이유다.  
그러면 누가 프로그램을 종료시키는 것인가?

JVM이 바로 OS에 직접적인 프로세스이고, JVM이 Java Program을 대신 종료시켜준다. `java.lang.Runtime.exit`나 `System.exit`에 의해 종료상태를 반환하여 JVM을 종료시킬 수 있다. (다만 권장하지 않는다.)

<br>
<br>

## 참고

- https://mozi.tistory.com/553
- https://www.geeksforgeeks.org/understanding-public-static-void-mainstring-args-in-java/
- https://velog.io/@sezzzini/Java-static-%EB%9C%AF%EC%96%B4%EB%B3%B4%EA%B8%B0
- https://wikidocs.net/228
- https://coding-factory.tistory.com/526