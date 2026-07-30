# ADR-0002: Material-independent core

- 상태: Accepted
- 결정일: 2026-07-30

## 배경

BEEZ는 여러 Compose Multiplatform 프로젝트가 dependency로 사용하는 독립적인 디자인 시스템이다.

핵심 컴포넌트를 Material 3 컴포넌트의 wrapper로 구현하면 초기 개발 속도는 높일 수 있다. 하지만 BEEZ와 소비자 애플리케이션이 서로 다른 Material 3 버전을 요구할 경우 최종 dependency graph는 하나의 버전을 선택해야 한다.

Material 3를 `implementation` dependency로 선언하면 Material 타입이 소비자의 compile classpath와 BEEZ 공개 API에 노출되는 범위는 줄일 수 있다. 그러나 BEEZ를 실행하는 데 필요한 runtime dependency와 버전 호환성은 남는다.

이 결합은 다음 문제를 만들 수 있다.

- 소비자가 선택한 Material 3 버전에 따라 BEEZ의 동작이나 렌더링이 달라질 수 있다.
- BEEZ가 컴파일한 버전보다 오래된 Material 3가 선택되면 runtime 호환 문제가 발생할 수 있다.
- Material 3의 API와 구현 변경이 BEEZ의 릴리스 및 지원 범위에 전파될 수 있다.
- BEEZ의 디자인 명세가 Material 3의 variant, token 및 behavior 구조에 제약될 수 있다.

## 결정

BEEZ의 핵심 모듈은 Material 3에 의존하지 않는다.

BEEZ 컴포넌트는 BEEZ token과 component specification을 기준으로 Compose Runtime, UI, Foundation, Animation 및 Resources의 primitive를 조합하여 구현한다.

```text
Design specification
        ↓
Beez tokens
        ↓
Beez foundation
        ↓
Beez components
```

다음과 같은 Material 3 API를 핵심 구현 기반으로 사용하지 않는다.

- `MaterialTheme`
- Material 3 component
- Material 3 token 또는 theme type
- Material 3 전용 modifier 및 composition local

BEEZ의 공개 API에는 Material 3 타입을 노출하지 않는다.

## 허용하는 Compose 기반

핵심 모듈은 필요에 따라 다음 Compose 계층을 사용할 수 있다.

- Compose Runtime
- Compose UI
- Compose Foundation
- Compose Animation
- Compose Multiplatform Resources
- 플랫폼별 Compose integration API

예상하는 구현 primitive에는 다음이 포함된다.

```text
Layout
Box / Row / Column
BasicText / BasicTextField
Canvas
Modifier
InteractionSource
Semantics
Focus
Pointer and keyboard input
Animation primitives
```

허용 목록이 해당 API를 무조건 사용해도 된다는 의미는 아니다. public API, 접근성, 플랫폼 호환성과 binary compatibility에 미치는 영향을 검토한다.

## 선택형 Material 3 adapter

Material 3와 BEEZ를 함께 사용하는 제품을 위해 별도의 선택형 adapter를 제공할 수 있다.

```text
beez-tokens
beez-foundation
beez-components

beez-material3-adapter  → optional
```

Adapter는 다음과 같은 명시적인 경계 역할만 담당한다.

- BEEZ color scheme과 Material `ColorScheme` 간 변환
- BEEZ typography와 Material `Typography` 간 변환
- BEEZ shape와 Material `Shapes` 간 변환
- 두 theme context를 함께 제공하는 integration helper

Adapter가 BEEZ 핵심 컴포넌트의 필수 runtime dependency가 되어서는 안 된다.

Adapter의 공개 API에는 목적상 Material 타입이 포함될 수 있다. 이 API와 호환성 정책은 핵심 BEEZ API와 별도로 문서화한다.

## Dependency 경계

### 핵심 모듈

- Material 2와 Material 3 dependency를 선언하지 않는다.
- Material 타입을 public 또는 internal implementation에 사용하지 않는다.
- 소비자가 Material 3를 추가하지 않아도 동작해야 한다.
- Material component를 복사한 코드나 internal token에 의존하지 않는다.

### Adapter 모듈

- Material dependency를 명시적으로 선언한다.
- 지원하는 Compose 및 Material 버전 범위를 문서화한다.
- 핵심 모듈의 dependency 방향을 역전시키지 않는다.
- Adapter를 사용하지 않는 소비자의 dependency graph에 포함되지 않아야 한다.

```text
허용:
beez-material3-adapter → beez-components

금지:
beez-components → beez-material3-adapter
beez-components → material3
```

## 대안 검토

### Material 3 wrapper

Material 3 component를 내부 구현으로 사용하고 BEEZ API로 감싸는 방식이다.

초기 구현은 빠르지만 runtime version 결합과 디자인 제약이 남기 때문에 선택하지 않았다.

### `implementation`으로 Material 3 숨기기

공개 API 노출은 줄지만 runtime dependency를 제거하지 못하므로 핵심 해결책으로 선택하지 않았다.

### `compileOnly` Material 3

실행 시 소비자가 호환되는 Material 3를 직접 제공해야 한다. 누락과 runtime 오류 가능성을 소비자에게 전가하므로 선택하지 않았다.

### Material 3 shading 또는 relocation

Compose compiler, Kotlin metadata, resource 및 멀티플랫폼 artifact를 고려할 때 복잡성과 배포 위험이 크다. BEEZ가 Material 3 사본을 사실상 유지하게 되므로 선택하지 않았다.

### Compose primitive 기반 구현

BEEZ가 동작, 접근성과 플랫폼 검증을 직접 책임져야 하므로 비용이 증가한다. 대신 디자인 명세와 dependency 경계를 제어할 수 있어 핵심 구현 방식으로 선택했다.

## 결과

### 장점

- 소비자가 Material 3를 사용하지 않아도 BEEZ를 사용할 수 있다.
- BEEZ와 소비자 사이의 Material 3 버전 결합을 제거한다.
- BEEZ의 token, component anatomy와 behavior를 독립적으로 설계할 수 있다.
- Material 3 변경이 BEEZ 핵심 API에 직접 전파되는 것을 줄인다.
- Material integration을 필요한 프로젝트만 선택할 수 있다.

### 비용과 책임

- interaction, focus, semantics와 state behavior를 직접 설계하고 검증해야 한다.
- Text Field, Dialog, Sheet와 같은 복잡한 컴포넌트의 구현 비용이 증가한다.
- 플랫폼 관습과 보조기술 동작을 지속해서 테스트해야 한다.
- Compose Runtime, UI 및 Foundation과의 버전 호환성은 여전히 관리해야 한다.

## 품질 보완책

- 컴포넌트 구현 전에 state, semantics와 keyboard behavior를 명세한다.
- Foundation primitive를 다시 구현하지 않고 적절한 저수준 API를 사용한다.
- Tier 1 플랫폼에서 자동 및 수동 접근성 검증을 Stable 조건으로 둔다.
- 지원하는 Kotlin, Compose Compiler 및 Compose Multiplatform 버전을 호환성 표로 관리한다.
- 최소 지원 Compose 버전과 대표 최신 버전으로 소비자 호환 테스트를 구성한다.
- 복잡한 컴포넌트는 작은 interaction primitive와 behavior test부터 만든다.

## 예외 정책

핵심 모듈에서 Material dependency가 필요하다는 제안은 다음 내용을 포함한 새로운 ADR로 검토한다.

- Foundation primitive로 해결할 수 없는 구체적인 이유
- 추가되는 transitive dependency와 버전 영향
- public API 노출 여부
- Material을 사용하지 않는 대안
- 지원 플랫폼과 접근성에 미치는 영향
- 향후 dependency 제거 또는 격리 방법

편의나 초기 구현 속도만을 근거로 예외를 허용하지 않는다.

## 참고 자료

- Gradle API와 implementation dependency 분리: <https://docs.gradle.org/current/userguide/java_library_plugin.html>
- Gradle dependency conflict resolution: <https://docs.gradle.org/current/userguide/dependency_constraints_conflicts.html>
- Compose Multiplatform과 Jetpack Compose 관계: <https://kotlinlang.org/docs/multiplatform/compose-multiplatform-and-jetpack-compose.html>
- Compose Multiplatform 호환성: <https://kotlinlang.org/docs/multiplatform/compose-compatibility-and-versioning.html>
