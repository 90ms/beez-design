# ADR-0004: Namespace and artifact naming

- 상태: Accepted
- 결정일: 2026-07-30

## 배경

BEEZ의 Gradle 프로젝트, Kotlin package, Maven publication과 공개 API에 일관된 이름이 필요하다.

Git 작성자 이름으로 사용하는 `90ms`는 숫자로 시작하므로 Kotlin package segment로 직접 사용할 수 없다. BEEZ라는 프로젝트 이름은 유효한 Kotlin identifier이며 디자인 시스템의 공개 브랜드와도 일치한다.

## 결정

BEEZ는 다음 이름을 사용한다.

| 대상 | 이름 |
| --- | --- |
| 브랜드 | `BEEZ` |
| 저장소 및 Gradle root project | `beez-design` |
| Kotlin root package | `beez.design` |
| 초기 Maven group | `beez.design` |
| Artifact prefix | `beez-` |
| Public API prefix | `Beez` |

Kotlin package는 기능 경계에 따라 root package 아래에서 구성한다.

```text
beez.design
beez.design.token
beez.design.theme
beez.design.component
beez.design.icon
beez.design.adapter.material3
```

공개 Compose API는 BEEZ 브랜드를 명확하게 식별할 수 있는 이름을 사용한다.

```kotlin
BeezTheme
BeezText
BeezIcon
BeezActionButton
```

## Artifact 규칙

배포 artifact는 `beez-` prefix를 사용한다.

```text
beez-tokens
beez-foundation
beez-components
beez-icons
beez-material3-adapter
```

최종 module 구성은 각 artifact를 독립적으로 배포하고 사용할 실질적인 필요가 있는지 검증한 뒤 결정한다. Gradle module을 나눈다는 이유만으로 모든 module을 public artifact로 배포하지 않는다.

통합 dependency가 필요하면 core artifact를 재패키징하지 않고 필요한 public artifact를 모으는 얇은 aggregate artifact를 검토한다.

## Package 규칙

- 모두 소문자 package segment를 사용한다.
- `internal`, `impl`, `util` package의 declaration을 public API에 노출하지 않는다.
- 플랫폼 이름은 실제 platform adapter package에만 사용한다.
- 컴포넌트마다 불필요하게 깊은 package hierarchy를 만들지 않는다.
- source set이 달라도 같은 declaration은 같은 package contract를 유지한다.
- 생성 코드 package도 `beez.design` 아래에 두고 생성 영역을 명확히 구분한다.

## Maven publication 고려사항

`beez.design`은 초기 개발과 로컬 publication에 사용할 namespace다.

공개 Maven 저장소는 group ID에 대한 namespace 소유권 증명을 요구할 수 있다. 최초 외부 publication 전에 다음을 확인한다.

- 공개 저장소의 namespace 검증 정책
- BEEZ가 소유하거나 검증할 수 있는 domain 또는 source hosting namespace
- 기존에 배포된 동일 group 및 artifact와의 충돌
- package와 Maven group을 동일하게 유지할 필요가 있는지 여부

검증 가능한 namespace가 따로 필요하면 첫 Stable 외부 릴리스 전에 ADR을 통해 Maven group만 변경할 수 있다. Kotlin root package 변경은 소비자 source compatibility에 직접 영향을 주므로 더 엄격하게 검토한다.

## 결과

### 장점

- 프로젝트 브랜드와 코드 식별자가 일치한다.
- 숫자로 시작하는 package 문제를 피한다.
- import와 공개 API만으로 BEEZ 소속을 식별할 수 있다.
- Material adapter를 핵심 package와 명확히 분리할 수 있다.

### 제약

- `beez.design` Maven group의 외부 저장소 사용 가능성은 publication 전에 검증해야 한다.
- 외부에 배포한 뒤 Kotlin root package를 변경하면 대규모 migration이 필요하다.
- Artifact를 지나치게 세분화하지 않도록 실제 소비 방식을 먼저 검증해야 한다.
