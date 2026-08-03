# ADR-0006: Initial core module boundaries

- 상태: Accepted
- 결정일: 2026-07-30

## 배경

BEEZ의 첫 Gradle 프로젝트를 구성하려면 token, theme foundation과 component의 코드 경계를 정해야 한다.

하나의 module은 초기 구성이 단순하지만 dependency 경계를 자동으로 검증하기 어렵다. 반대로 icon, adapter, catalog와 tooling까지 처음부터 세분화하면 실제 코드 없이 module과 publication만 늘어난다.

초기 목표는 `Token → Foundation → Component` 수직 흐름을 검증할 수 있는 최소 module 집합이다.

## 결정

BEEZ 핵심 library를 다음 세 Gradle module로 시작한다.

```text
:beez-tokens
      ↓
:beez-foundation
      ↓
:beez-components
```

의존 방향은 아래쪽 module이 위쪽 module을 사용하는 단방향 구조다.

```text
beez-components → beez-foundation → beez-tokens
```

역방향 dependency와 순환 dependency를 허용하지 않는다.

## `beez-tokens`

책임:

- 생성된 Scale 및 Semantic token
- Token value와 scheme type
- Color, Typography, Shape, Spacing, Elevation 및 Motion scheme 계약
- Token metadata 또는 source 연결 정보

허용 dependency:

- Kotlin standard library
- 공개 token type에 필요한 Compose Runtime 및 UI type

금지:

- Component
- CompositionLocal 기반 theme 제공
- Material
- 플랫폼별 전체 token 사본

## `beez-foundation`

책임:

- `BeezTheme`
- CompositionLocal과 theme access
- BEEZ 기본 Light 및 Dark scheme 조합
- 브랜드 theme 확장 경계
- 공통 interaction, semantics 또는 layout primitive
- 접근성 및 adaptive foundation

Dependency:

```text
beez-foundation → beez-tokens
```

금지:

- 제품 수준 component 모음
- Material
- 특정 component variant에만 필요한 구현

## `beez-components`

책임:

- 공개 BEEZ component API
- 공통 component implementation
- Component별 state, behavior와 token mapping
- Component 테스트와 공통 catalog scenario의 원천

Dependency:

```text
beez-components → beez-foundation
```

`beez-foundation`이 공개하는 token과 primitive는 transitive API로 사용할 수 있지만, component가 scale token에 무분별하게 직접 결합하지 않는다.

## 초기 비포함 module

다음 module은 실제 요구가 생길 때 추가한다.

- `beez-icons`
- `beez-material3-adapter`
- `beez-catalog`
- Token generator용 별도 Gradle module 또는 plugin
- Documentation website
- Aggregate artifact

Catalog는 첫 component 수직 단면을 구현할 때 추가한다. Material adapter는 실제 소비 프로젝트의 integration 요구가 확인될 때 추가한다.

## Target 정책

세 핵심 module은 같은 public target 집합을 제공한다.

```text
android
iosArm64
iosSimulatorArm64
desktop JVM
web Wasm
```

특정 핵심 module이 편의상 일부 target을 누락하지 않는다. Target의 실제 검증 수준은 `docs/compatibility.md`와 `docs/platform-policy.md`에 별도로 표시한다.

## Publication

각 module은 다음 artifact 후보와 대응한다.

| Gradle module | Maven artifact |
| --- | --- |
| `beez-tokens` | `beez-tokens` |
| `beez-foundation` | `beez-foundation` |
| `beez-components` | `beez-components` |

Publication task를 구성할 수는 있지만 외부 저장소 배포는 별도 릴리스 결정 이후 수행한다.

## 결과

### 장점

- Token, theme와 component의 의존 방향을 Gradle이 검증한다.
- Token 또는 foundation만 필요한 소비자가 선택할 수 있다.
- Material-independent core 경계를 module 단위로 검사할 수 있다.
- 첫 component 수직 단면에 필요한 module만 만든다.

### 비용

- 세 module이 유사한 KMP target 구성을 가진다.
- Compose와 Kotlin 업그레이드 시 여러 module을 함께 검증해야 한다.
- 너무 작은 코드에서도 module 간 public/internal API를 구분해야 한다.

## 후속 방향

ADR-0012에 따라 repository-local Node.js token generator를 추가했지만 별도 Gradle module이나 plugin은 만들지 않았다. 생성된 Kotlin은 기존 `beez-tokens`와 `beez-catalog` source set에 속하므로 이 문서의 module dependency 방향은 바뀌지 않는다.

반복되는 Gradle 설정이 안정되면 convention plugin을 검토한다. 실제 반복이 확인되기 전에 build logic abstraction을 만들지 않는다.

Module을 합치거나 추가할 때는 다음을 근거로 검토한다.

- 소비자가 독립적으로 dependency를 선택할 필요
- API와 implementation boundary
- build 및 publication 비용
- 순환 dependency 가능성
- 플랫폼 target 일관성
