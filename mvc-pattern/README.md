 # MVC패턴에 대해 알아보자. (Spring MVC)

[서블릿](https://azurealstn.tistory.com/137) 에 대해서도 조금 알게되었으니 이제 MVC 패턴에 대해 알아보도록 하자!

## MVC 패턴 사용 이유

MVC 패턴을 적용 안한 JSP를 예를 들어보자.  
JSP 페이지 안에는 자바 코드가 들어갈 수 있다. 따라서 비즈니스 로직을 넣을 수 있고, `Connection` 객체를 이용한 DB 연동도 할 수 있다. 또한 JSP는 클라이언트에게 View를 렌더링하는 역할도 수행한다. 여기까지만 봐도 JSP는 너무나 많은 역할을 가지고 있다.

이렇게 JSP가 많은 역할을 하게 되면 나중에 유지보수에도 굉장히 힘들어진다. JSP 페이지 안에 비즈니스 로직과 DB 연동 코드 모두 들어가 있으면 일단 코드 라인수도 많을 것이고, 비즈니스 로직을 수정하는데 여러가지 코드가 막 섞여있으면 보기가 힘들어지고, 간단한 UI하나만 수정해도 한 페이지 안에 비즈니스 로직까지 섞여있어서 개발자가 실수할 확률도 높아진다.

그래서 이렇게 많은 역할을 분리시키는 것이다. 기본적으로 가장 많이 사용되는 MVC 패턴에 대해 살펴보자

> 변경의 주기(라이프 사이클)가 다르면 분리를 고려해라.

## MVC 패턴 개념

기존에 JSP 페이지 안에 모든 로직을 처리를 했다면 **컨트롤러(Controller), 모델(Model), 뷰(View)**로 서로의 역할을 나눈다.

- 컨트롤러: HTTP 요청을 받아서 파라미터를 검증하고, 비즈니스 로직을 실행하고, 뷰에 전달할 결과 데이터를 조회해서 모델에 담는다.
  - 컨트롤러는 이미 충분히 많은 역할을 수행하기 때문에 복잡한 비즈니스 로직 같은 경우는 따로 클래스를 분리한다.
- 모델: 뷰에 출력할 데이터를 담는다. 뷰가 필요한 데이터를 모두 모델에 담아서 전달해주는 덕분에 뷰는 비즈니스 로직이나 데이터 접근을 몰라도 되고 화면을 렌더링하는 일에 집중할 수 있다.
- 뷰: 모델에 담겨있는 데이터를 사용해서 화면을 그리는 일에 집중한다. 여기서는 HTML을 생성하는 부분을 말한다.

![mvc1](https://user-images.githubusercontent.com/55525868/179016297-982081d9-0da6-4ef3-b644-95b1a4ffc2fa.png)

1. 클라이언트가 요청을 하게 되면 `Controller`는 요청을 받아서 검증을 한다. (HTTP 스펙에 맞게 제대로 요청했는지, 안맞으면 400대 오류를 발생시킨다.)
2. 검증이 제대로 되면 `Service` 로 간다.
3. `Service`에서 DB에 있는 데이터가 필요하면 `Dao`라는 인터페이스를 통해 데이터를 가져온다.
4. `Dao`는 `DB`에서 CRUD 하는 행위들을 인터페이스로 관리한다.
5. `Controller`는 `Service`에서 가져온 데이터들을 `Model`에 전달한다.
6. `Model`에 제대로 전달이 되면 그 결과를 `View`에 넘긴다.
7. 그럼 `View`는 받은 결과를 통해 `Model`에 있는 데이터를 참조한다.
8. 마지막으로 `View`를 출력해서 클라이언트에 응답한다.

> 컨트롤러는 클라이언트의 요청을 받아서 검증도 하고, 비즈니스 로직도 호출해주고, 모델에 데이터도 전달해주고, 그 결과를 뷰에 넘겨주기도 하기 때문에 결국에 컨트롤러는 중간에서 조정자 역할로서, 매우 중요한 일을 하고 있다. 

## 프론트 컨트롤러

![mvc2](https://user-images.githubusercontent.com/55525868/179336495-f327ca35-e209-465d-a617-425034bc7884.png)

기존의 MVC 패턴에는 한 가지 문제점이 있는데 바로 위 그림이다.  
그림을 보는 것처럼 각각의 클라이언트들이 필요한 리소스에 맞는 각각의 컨트롤러를 요청한다. 이 때 컨트롤러에서 작업하는 공통 코드들이 있는데 각각의 컨트롤러들은 이 공통 코드를 매번 만들어주아야 한다는 불편함이 있다. 그래서 이 공통 코드를 하나의 서블릿에서 모두 처리할 수 있도록 앞에 **프론트 컨트롤러**를 도입한다.

![mvc3](https://user-images.githubusercontent.com/55525868/179337325-0fdc1d6d-72f5-4394-a283-0dd2df828351.png)

위 그림처럼 프론트 컨트롤러는 공통 코드를 책임지고 클라이언트들의 모든 요청을 받는다. 그리고 요청에 맞는 컨트롤러를 찾아서 호출한다.

> 프론트 컨트롤러를 사용하면 프론트 컨트롤러 하나만 서블릿을 사용하고 나머지 컨트롤러는 서블릿을 사용하지 않아도 된다.

## Spring MVC

MVC 패턴에 프론트 컨트롤러를 사용한 프레임워크가 바로 **Spring MVC**이다. Spring MVC에서는 프론트 컨트롤러를 `DispatcherServlet`이라고 한다. 이 `DispatcherServlet`은 클라이언트들의 모든 요청을 받는 클래스이다. 즉, `DispatcherServlet` 클래스는 프론트 컨트롤러로서 역할을 수행하고 Spring MVC 애플리케이션의 흐름을 책임지고 있기 때문에 Spring MVC에서 가장 핵심적인 역할을 맡고 있다.

![mvc4](https://user-images.githubusercontent.com/55525868/179388155-034aa61a-bfa4-477c-a692-2cc0245f1900.png)

1. `DispatcherServlet`은 클라이언트가 보낸 요청을 받는다.
2. `DispatcherServlet`은 `HandlerMapping`에게 적절한 `Controller`를 찾도록 요구한다. `HandlerMapping`은 HTTP 요청 메시지를 기반으로 요청 URL을 확인한 후 맞는 `Controller`를 `DispatcherServlet`에게 반환한다.
3. `DispatcherServlet`은 `HandlerAdapter`에게 `Controller`의 비즈니스 로직을 실행하라고 명령한다.
4. `HandlerAdapter`는 `Controller`의 비즈니스 로직을 호출한다.
5. `Controller`는 비즈니스 로직을 실행해서 `Model`에 데이터를 담고, 반환된 `view name`을 `HandlerAdapter`에게 전달한다.
6. `DispatcherServlet`은 `HandlerAdapter`로부터 받은 `view name`을 `ViewResolver`에게 전달해서 클라이언트에게 보여줄 `View`와 이름이 일치하는지 명령한다. `ViewResolver`는 `view name`과 일치한 `View`를 반환한다.
7. `DispatcherServlet`은 `View`에게 반환된 `View`를 전달하고 렌더링을 진행하라고 명령한다.
8. `View`는 `Model` 데이터를 렌더링하고 최종적으로 응답 결과를 클라이언트에게 반환한다.

> HandlerMapping은 HTTP 요청 메시지 정보를 이용해서 적절한 컨트롤러를 찾아주는 기능을 수행한다.

> HandlerAdapter는 HandlerMapping을 통해 찾은 컨트롤러를 직접 실행하는 기능을 수행한다.
> HandlerMapping을 통해 찾은 컨트롤러를 **HandlerAdapter 목록**에서 **supports 메서드**에 대입하며 지원여부를 살핀다. 부합할 경우 **handler 메서드**를 실행하여 **ModelAndView**를 리턴한다.

## 마무리

Spring MVC는 `HandlerMapping`, `HandlerAdapter`, `ViewResolver` 모두를 개발자들이 유연하고 편하게 사용할 수 있게 인터페이스로 개발되었습니다.  
저도 MVC 패턴과 Spring MVC를 공부하면서 얕게 알고 있었던 부분들을 좀 더 자세하게 알게되었습니다. (~~더 세세하게 들어가기에는 제 머리에 한계가 올듯..~~)

만약 위 내용이 이해가 안된다면 [인프런의 스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1#) 강의에서 **MVC 프레임워크 만들기**를 참고하면 좋을 것 같네요. 저도 나중에 한번 더 따라해볼 예정이고 그 때 `자바 웹 프로그래밍 Next Step`이란 책도 같이 공부할 계획입니다! (언제가 될까요?😅) 

## References

- [인프런 - 스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1#)
- https://terasolunaorg.github.io/guideline/1.0.1.RELEASE/en/Overview/SpringMVCOverview.html
- https://joont92.github.io/spring/HandlerMapping-HandlerAdapter-HandlerInterceptor/