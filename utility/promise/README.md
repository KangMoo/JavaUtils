# PromiseManager

`PromiseManager`는 비동기 작업의 성공, 실패, 타임아웃 상황을 관리하는 Java 유틸리티입니다. 중복 실행을 걱정하지 않고 안전하게 비동기 작업을 관리할 수 있습니다.

## 주요 기능

- **비동기 작업의 상태 관리**: 각 작업에 대한 성공, 실패, 타임아웃 콜백을 관리합니다. 이 콜백들은 **단 한 번만** 실행됩니다.
- **메모리 저장소**: 작업과 관련된 데이터를 임시로 저장하고 검색할 수 있는 메모리 저장소를 제공합니다.
- **동시성 관리**: 내부적으로 `ConcurrentHashMap`을 사용하여 동시성을 보장합니다.
- **스케줄링**: 작업의 타임아웃 시간을 기반으로 작업의 타임아웃 콜백을 스케줄링합니다.

## 사용 방법

### PromiseInfo 생성

```java
PromiseManager manager = PromiseManager.getInstance();
PromiseInfo promise = manager.createPromiseInfo(
    "uniqueKey",
    () -> System.out.println("Success!"),
    () -> System.out.println("Fail!"),
    () -> System.out.println("Timeout!"),
    5000L
);
```

이 코드는 5초 후에 "Timeout!"을 출력하는 `PromiseInfo` 객체를 생성합니다.

### PromiseInfo 작업 상태 관리

성공 상태로 변경하려면:

```java
promise.procSuccess();
```

실패 상태로 변경하려면:

```java
promise.procFail();
```

타임아웃 상태로 변경하려면:

```java
promise.procTimeout();
```

상태 관리 메서드는 **단 한 번만** 수행되며, 다른 상태로 변경이 불가능합니다.  예를 들어, `procSuccess()`를 두 번 호출해도 "Success!"는 최초 한번만 출력됩니다. `procSuccess()`호출 이후 `procFail()` 또는 `procTimeout()` 를 호출해도 동작하지 않습니다.

### 메모리 저장소 사용

데이터 저장:

```java
promise.putObject("dataKey", "Some Data");
```

데이터 검색:

```java
Optional<Object> data = promise.getObject("dataKey");
```

## 주의 사항

- `PromiseInfo`의 키는 유일해야 합니다. 동일한 키로 여러 개의 `PromiseInfo`를 생성하려고 하면 `IllegalArgumentException`이 발생합니다.
- 작업의 타임아웃 시간이 지나면 해당 `PromiseInfo`는 자동으로 타임아웃 상태가 되며, 타임아웃 콜백이 호출됩니다.