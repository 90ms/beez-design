# ADR-0012: Generate checked-in Kotlin token schemes from DTCG sources

- 상태: Accepted
- 결정일: 2026-08-03

## 문맥

BEEZ의 token source of truth는 `specification/tokens`의 DTCG JSON이며, Compose 소비자는 `beez-tokens`의 `BeezTokenScheme`과 `BeezTokenSchemes.light/dark` API를 사용한다.

현재 기본 Light/Dark 값은 generator 도입 전 provisional Kotlin 구현으로도 관리되고 있다. Catalog의 Test Brand override도 DTCG 원본과 Kotlin 코드에 각각 존재한다. 이 구조에서는 token 원본을 변경할 때 Kotlin scheme이나 Catalog fixture가 함께 갱신되지 않아도 JSON alias 검증만으로 drift를 발견할 수 없다.

생성 시점과 결과물 보관 방식도 정해야 한다. 소비자 build가 token generator나 Node.js 환경에 의존하면 library 사용 경계가 넓어지고, 생성물을 저장소에 두지 않으면 public API와 값의 diff를 일반 code review에서 확인하기 어렵다.

## 결정

BEEZ는 DTCG JSON을 읽는 repository-local Node.js generator로 Compose Kotlin token scheme을 생성한다.

- `specification/tokens`만 token 값과 alias의 source of truth로 사용한다.
- generator와 validator는 Node.js 표준 라이브러리만 사용하며 별도 runtime package를 추가하지 않는다.
- 생성된 Kotlin 파일은 repository에 commit하고 일반 source set에서 compile한다.
- 생성 파일에는 source, 재생성 명령과 수동 편집 금지 표시를 넣는다.
- CI는 source를 다시 해석해 commit된 생성물과 비교하고 drift가 있으면 실패한다.
- 소비자와 Gradle compile은 Node.js 또는 generator 실행에 의존하지 않는다.
- generator는 입력과 도구 버전이 같을 때 byte 단위로 같은 결과를 만들어야 한다.

초기 생성 범위는 현재 지원하는 DTCG subset으로 제한한다.

| DTCG 값 | Compose/Kotlin 결과 |
| --- | --- |
| sRGB color | `Color` |
| `dp` dimension | `Dp` |
| `sp` dimension | `TextUnit` |
| millisecond duration | millisecond `Int` |
| supported font family | `FontFamily` |
| font weight | `FontWeight` |
| typography composite | `TextStyle` |

지원하지 않는 type, unit, font family 또는 값 형태는 임의로 변환하지 않고 validation error로 처리한다.

## 공개 API와 출력 경계

ADR-0008의 public token contract는 유지한다.

- `BeezColorScheme`, `BeezTypographyScheme`, `BeezSpacingScheme`, `BeezShapeScheme`, `BeezElevationScheme`, `BeezMotionScheme`과 `BeezTokenScheme` 선언은 사람이 관리한다.
- `BeezTokenSchemes.light`와 `BeezTokenSchemes.dark`의 선언 및 값 조립은 `beez-tokens`의 생성 파일에 둔다.
- 생성 파일은 `beez.design.tokens` package를 사용하며 파일명으로 generated ownership을 표시한다.
- token path를 새로운 public Scale token accessor로 자동 노출하지 않는다.
- public token 이름이나 scheme property 변경은 generator가 추론하지 않으며 별도 specification과 compatibility 검토가 필요하다.

`themes/test-brand.theme.json`은 Showcase 전용 검증 fixture다.

- Test Brand override는 `beez-catalog`의 internal 생성 파일로 출력한다.
- Test Brand를 `beez-tokens` public API에 추가하지 않는다.
- override하지 않은 역할은 선택한 BEEZ Light/Dark base scheme에서 유지한다.
- Test Brand가 존재하지 않는 semantic path를 추가하면 validation error로 처리한다.

## 검증 계약

Token validation과 generation check는 최소한 다음을 확인한다.

- JSON parse와 repository source layout
- 중복 token path
- 누락되거나 순환하는 alias
- 지원하는 `$type`, 값 구조와 unit
- Light/Dark semantic contract 일치
- Test Brand override path 유효성
- 생성 Kotlin identifier 충돌
- 생성 결과의 결정성
- commit된 생성물과 현재 source의 일치

전경과 배경의 접근성 대비 기준, deprecated metadata와 전체 DTCG JSON Schema는 별도 결정이 완료될 때까지 현재 미검증 범위로 문서화한다.

## 변경 및 검토 흐름

Token 변경은 다음 순서를 따른다.

1. `specification/tokens`의 원본을 변경한다.
2. token validation을 실행한다.
3. generator로 Kotlin 결과를 갱신한다.
4. specification과 생성 Kotlin diff를 함께 검토한다.
5. 영향받는 theme, component, Catalog와 문서를 검증한다.
6. CI generation check와 library validation 결과를 확인한다.

생성 결과가 기존 provisional Kotlin 값과 다르면 generator가 어느 쪽을 자동으로 정답으로 선택하지 않는다. 원본 명세와 기존 구현의 차이를 검토하고, 의도한 디자인 변경이 아니면 source 또는 generator mapping을 먼저 수정한다.

## 결과

### 장점

- DTCG JSON과 Compose scheme 사이의 수동 값 복제를 제거한다.
- token 변경이 Kotlin과 Catalog에 반영되지 않은 상태를 CI에서 차단한다.
- 생성된 public API와 값의 diff를 일반 code review에서 확인할 수 있다.
- 소비자 build와 publication이 generator toolchain에 결합되지 않는다.
- Test Brand가 실제 semantic override source를 검증하는 fixture가 된다.

### 비용과 제약

- generator와 supported DTCG subset을 repository에서 유지해야 한다.
- 생성 결과도 repository diff와 clone 크기에 포함된다.
- 새로운 DTCG type이나 platform output에는 transformer와 검증을 추가해야 한다.
- 생성 파일을 수동 수정한 hotfix는 허용하지 않으며 원본이나 generator부터 수정해야 한다.

## 대안

### Gradle build마다 생성

생성 누락을 막을 수 있지만 모든 compile과 소비자 source build에 Node.js 또는 별도 generator toolchain을 요구한다. 초기 경계를 단순하게 유지하기 위해 채택하지 않는다.

### 생성물을 commit하지 않기

repository에는 원본만 남지만 public Kotlin diff를 code review에서 직접 확인하기 어렵고 IDE import 전에 생성 task가 필요하다. 채택하지 않는다.

### Runtime JSON resolver

ADR-0008에서 검토한 것처럼 runtime parsing, 오류 처리와 dependency 비용이 생긴다. compile-time Kotlin API를 제공하는 현재 계약과 맞지 않아 채택하지 않는다.

### 기존 Kotlin 값을 계속 수동 관리

추가 도구가 필요 없지만 source of truth가 실질적으로 둘이 되고 Catalog fixture까지 drift할 수 있다. 채택하지 않는다.
