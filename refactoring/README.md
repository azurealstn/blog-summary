# Java. 무수히 많은 if else문 개선해보자.

이번에 회사에서 반복되는 if else문을 인터페이스(Interface)를 사용하여 개선하였습니다. 기존에 소스는 한 메서드에 대략 700줄을 차지 했는데 그렇게 차지한 이유는 무수히 많은 if else문 때문이었습니다. 메서드의 라인수가 700라인이나 되기 때문에 메서드의 역할을 파악하기가 힘들고, 유지보수하기가 굉장히 까다로워지기 때문에 개선할 필요성을 느꼈습니다.

> 개선한 내용은 https://jessyt.tistory.com/47 글을 적극적으로 활용하였습니다.
> 추가로 아래 소스는 회사의 소스와는 무관합니다.

> 소스코드: https://github.com/azurealstn/blog-summary/tree/main/refactoring

## 기존 예제소스

기존 기능에 대해 간단히 설명하면,  

- 리포트의 서식(타입)에 따라 다른 데이터를 가져와야 한다. 
- 가져온 데이터로 엑셀 다운로드를 진행한다.  
- 문제는 서식에 따라 다른 데이터를 가져올 때 if else문을 사용하기 때문에 메서드의 라인수가 굉장히 길어졌다.

이제 아래 예제소스를 보자.

### ConsultingReportService

```java
@Slf4j
@RequiredArgsConstructor
@Service
public class ConsultingReportService {

    private final ConsultingReportRepository consultingReportRepository;

    public String getConsultingReportListExcel(ConsultingReportDto consultingReportDto) {
        ConsultingReportDto typeDto = consultingReportRepository.findById(1L);
        String fxdForm = typeDto.getFxdForm();
        if ("consultingReportType01".equals(fxdForm)) {
            log.info("많은 로직이 있다고 가정");
            return "Type1 엑셀 정보를 가져온다.";
        } else if ("consultingReportType02".equals(fxdForm)) {
            log.info("많은 로직이 있다고 가정");
            return "Type2 엑셀 정보를 가져온다.";
        } else if ("consultingReportType03".equals(fxdForm)) {
            log.info("많은 로직이 있다고 가정");
            return "Type3 엑셀 정보를 가져온다.";
        }
        
        return null;
    }
}
```

리포트 서식(타입)에 따라 다른 로직을 수행하고 있다.  

> DB 저장되어 있는 fxdForm 값을 가져와서 그 타입에 따라 각각 다른 데이터를 리턴

위의 소스를 보면 if else문 안에 로직은 정말 많은 로직이 있다고 가정한다. (많은 로직이란 DB에서 데이터를 가져와서 어떤 DTO에 담고 그 DTO를 리턴하는 형태여야 하지만 그냥 String을 반환하도록 하였음.)

당장 2 ~ 3개면 많아보이지 않겠지만 6 ~ 7개면 말이 달라진다. 심지어 계속 늘어날 수 있기 떄문에 유지보수를 위해서도 반드시 개선해야 했다.

<br>

## 개선 코드

![interface](https://user-images.githubusercontent.com/55525868/207850719-8d105c29-879a-4907-b805-feb072b17aed.PNG)

`ConsultingReportType` 인터페이스를 두고 구현체로 `ConsultingReportType01`, `ConsultingReportType02`, `ConsultingReportType03`클래스를 생성하여 구현한다.

### ConsultingReport

```java
public interface ConsultingReport {

    String process();
}
```

`ConsultingReport` 인터페이스를 생성하여 `ConsultingReportService` 클래스에서 사용했던 if else문 안에 로직을 `process()` 메서드에 구현할 것입니다.

<br>

### ConsultingReportType01

```java
@Slf4j
@Service
public class ConsultingReportType01 implements ConsultingReport {

    @Override
    public String process() {
        log.info("많은 로직이 있다고 가정");
        return "Type1 엑셀 정보를 가져온다.";
    }
}
```

### ConsultingReportType02

```java
@Slf4j
@Service
public class ConsultingReportType02 implements ConsultingReport {

    @Override
    public String process() {
        log.info("많은 로직이 있다고 가정");
        return "Type2 엑셀 정보를 가져온다.";
    }
}
```

### ConsultingReportType03

```java
@Slf4j
@Service
public class ConsultingReportType03 implements ConsultingReport {

    @Override
    public String process() {
        log.info("많은 로직이 있다고 가정");
        return "Type3 엑셀 정보를 가져온다.";
    }
}
```

`ConsultingReport` 인터페이스의 구현체를 만들어 `process()` 메서드를 위와 같이 작성한다.

<br>

### ConsultingReportFactory

```java
@Slf4j
@RequiredArgsConstructor
@Service
public class ConsultingReportFactory {

    private final String CONSULTING_REPORT_TYPE_01 = "consultingReportType01";
    private final String CONSULTING_REPORT_TYPE_02 = "consultingReportType02";
    private final String CONSULTING_REPORT_TYPE_03 = "consultingReportType03";

    private final ConsultingReportType01 consultingReportType01;
    private final ConsultingReportType02 consultingReportType02;
    private final ConsultingReportType03 consultingReportType03;

    private final ConsultingReportRepository consultingReportRepository;

    public ConsultingReport getConsultingReport(ConsultingReportDto consultingReportDto) {
        ConsultingReport consultingReport = null;
        ConsultingReportDto typeDto = consultingReportRepository.findById(consultingReportDto.getId());
        String fxdForm = typeDto.getFxdForm();
        if (CONSULTING_REPORT_TYPE_01.equals(fxdForm)) {
            consultingReport = consultingReportType01;
        } else if (CONSULTING_REPORT_TYPE_02.equals(fxdForm)) {
            consultingReport = consultingReportType02;
        } else if (CONSULTING_REPORT_TYPE_03.equals(fxdForm)) {
            consultingReport = consultingReportType03;
        }

        return consultingReport;
    }
}
```

중요한 로직을 담당하는 `ConsultingReportFactory` 클래스이다.  
`ConsultingReportFactory` 클래스의 역할은 분기문 처리를 대신 해주는 역할을 해준다.  
즉, `ConsultingReportFactory` 클래스를 따로 만들어서 비즈니스 로직의 복잡도를 분산시키는 것이다.  
이렇게 되면 비즈니스 로직을 담당하는 서비스단에서는 `ConsultingReportFactory` 클래스를 가져다 쓰기만 하면 되기 때문에 서비스단에서는 코드가 확 줄어든다.

> 위와 같은 디자인 패턴을 팩토리 패턴이라고 한다.

### ConsultingReportServiceV2

```java
@Slf4j
@RequiredArgsConstructor
@Service
public class ConsultingReportServiceV2 {

    private final ConsultingReportFactory consultingReportFactory;

    public String getConsultingReportListExcel(ConsultingReportDto consultingReportDto) {
        ConsultingReport consultingReport = consultingReportFactory.getConsultingReport(consultingReportDto);
        return consultingReport.process();
    }
}
```

변경된 `ConsultingReportServiceV2` 클래스를 보면 `ConsultingReportFactory` 클래스를 의존성 받고 있고, 로직 부분에서는 코드가 확 줄어든 것을 볼 수 있다.  
즉, ConsultingReportFactory 클래스에 분기를 판단하는 파라미터를 넘겨서 관련 데이터를 리턴받는다.

## 테스트코드

마지막으로 테스트코드를 작성해 값을 검증해본다.

```java
@SpringBootTest
class ConsultingReportServiceV2Test {

    @Autowired
    private ConsultingReportServiceV2 consultingReportServiceV2;

    @Autowired
    private ConsultingReportRepository consultingReportRepository;

    @Test
    @DisplayName("Factory를 이용한 분기문 처리 Type1")
    void consultingReportType01() {
        //given
        ConsultingReportDto consultingReportDto = new ConsultingReportDto(1L, "consultingReportType01");
        consultingReportRepository.save(consultingReportDto);

        ConsultingReportDto findConsultingReportDto = consultingReportRepository.findById(1L);

        //when
        String result = consultingReportServiceV2.getConsultingReportListExcel(findConsultingReportDto);

        //then
        assertThat(result).isEqualTo("Type1 엑셀 정보를 가져온다.");
    }

    @Test
    @DisplayName("Factory를 이용한 분기문 처리 Type2")
    void consultingReportType02() {
        //given
        ConsultingReportDto consultingReportDto = new ConsultingReportDto(1L, "consultingReportType02");
        consultingReportRepository.save(consultingReportDto);

        ConsultingReportDto findConsultingReportDto = consultingReportRepository.findById(1L);

        //when
        String result = consultingReportServiceV2.getConsultingReportListExcel(findConsultingReportDto);

        //then
        assertThat(result).isEqualTo("Type2 엑셀 정보를 가져온다.");
    }

    @Test
    @DisplayName("Factory를 이용한 분기문 처리 Type3")
    void consultingReportType03() {
        //given
        ConsultingReportDto consultingReportDto = new ConsultingReportDto(1L, "consultingReportType03");
        consultingReportRepository.save(consultingReportDto);

        ConsultingReportDto findConsultingReportDto = consultingReportRepository.findById(1L);

        //when
        String result = consultingReportServiceV2.getConsultingReportListExcel(findConsultingReportDto);

        //then
        assertThat(result).isEqualTo("Type3 엑셀 정보를 가져온다.");
    }
}
```

![testcode](https://user-images.githubusercontent.com/55525868/207864010-8d038640-f6ca-431d-ab8a-fcb8c7d03ba8.PNG)

마지막으로 테스트코드를 통해 각각 다른 데이터가 리턴된 것을 확인하 수 있었다.  
이것으로 한 메서드에 700라인 정도 되는 코드양을 확 줄일 수 있었다. 이제는 리포트 서식이 추가될 때마다 `ConsultingReport` 인터페이스의 구현체를 만들어나가면 된다. 

<br>

## Reference

- https://jessyt.tistory.com/47