# ReflectionUtil

## 소개

`ReflectionUtil`은 Java의 Reflection API를 활용하여 문자열 형태의 Java 표현식을 동적으로 파싱 및 실행하는 유틸리티 클래스로 Java 코드를 문자열로 표현하여 동적으로 코드를 실행하고 그 결과를 얻을 수 있다. 이 메서드는 Java 8 버전 이상에서 동작한다.

## 주요 기능

- 문자열로 제공된 Java 표현식의 동적 파싱 및 실행
- 실행된 표현식의 타입과 값 동시 반환

## 사용 방법

### exec 메서드

주어진 Java 표현식 문자열을 파싱하고 실행한 후, 그 결과를 반환한다.

```java
TypeValuePair result = ReflectionUtil.exec("new String(\"Hello, World!\")");
System.out.println(result.value);  // 출력: Hello, World!
```

### execMethodCallExpr, execObjectCreationExpr 등의 메서드

이러한 메서드들은 각각의 표현식 타입에 따라 내부적으로 사용되며, 일반적인 사용자가 직접 호출할 필요는 없다.

## 내부 클래스: TypeValuePair

표현식의 실행 결과와 그 타입을 함께 반환하기 위한 클래스.

- `type`: 결과의 데이터 타입
- `value`: 결과의 실제 값

```java
TypeValuePair result = ReflectionUtil.exec("1 + 1");
System.out.println(result.type);   // 출력: int
System.out.println(result.value);  // 출력: 2
```