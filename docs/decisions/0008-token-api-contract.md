# ADR-0008: BEEZ token API contract

- 상태: Accepted
- 결정일: 2026-07-31

## 문맥

BEEZ의 token taxonomy는 semantic token 계약을 정의하지만, Compose 소비자가 어떤 Kotlin 타입과 기본 scheme을 사용할지는 아직 정해지지 않았다. 이 경계를 먼저 고정하지 않으면 foundation과 component가 각자 token access API를 만들게 된다.

## 결정

beez-tokens는 다음 public 타입을 commonMain에서 제공한다.

| Semantic 영역 | Kotlin 타입 |
| --- | --- |
| Color | BeezColorScheme |
| Typography | BeezTypographyScheme |
| Spacing | BeezSpacingScheme |
| Shape | BeezShapeScheme |
| Elevation | BeezElevationScheme |
| Motion | BeezMotionScheme |
| 전체 계약 | BeezTokenScheme |

기본 appearance는 BeezTokenSchemes.light와 BeezTokenSchemes.dark로 제공한다. 제품 브랜드는 scheme의 copy API 또는 withColors, withShapes 같은 명시적 확장 함수로 semantic 역할만 교체한다. 누락한 역할은 기존 scheme에서 유지한다.

## 경계

- token module은 Material, Foundation, Component module에 의존하지 않는다.
- public token 타입은 Compose Runtime과 UI에서 제공하는 공통 타입만 사용한다.
- Shape는 token module에서 구체적인 UI Shape 객체를 만들지 않고 semantic radius를 제공한다. 실제 Shape 변환은 foundation이 담당한다.
- token source of truth는 specification/tokens의 DTCG JSON이다.
- 현재 Kotlin 값은 generator가 도입되기 전의 명시적 provisional implementation이다. 생성 파이프라인 도입 시 JSON을 원본으로 유지하고 Kotlin 결과를 교체한다.
- 원시 token은 public product API로 권장하지 않으며 component와 제품은 semantic scheme을 사용한다.

## 결과

- foundation과 components가 동일한 semantic 계약을 공유한다.
- Light/Dark와 브랜드 변경이 component API 변경 없이 가능하다.
- token module이 Compose Foundation의 shape 구현에 결합되지 않는다.
- DTCG JSON과 생성된 Kotlin scheme diff를 함께 검토하고 CI drift 검사를 통과해야 한다.

## ADR-0012와의 관계

[ADR-0012](0012-token-generation-pipeline.md)는 이 문서의 public scheme 계약을 유지하면서 DTCG JSON에서 기본 Light/Dark 값과 Catalog 전용 Test Brand override를 생성하는 방식을 결정한다. 생성 파이프라인 도입 후에도 scheme type과 extension API는 사람이 관리하며, generator는 새로운 public token API를 임의로 만들지 않는다.

## 대안

### Foundation에 theme과 token을 함께 두기

초기 구현은 단순하지만 token을 직접 사용하는 소비자가 foundation까지 의존해야 하고, token과 theme 경계가 흐려진다. 채택하지 않는다.

### Runtime JSON token resolver 제공

테마를 동적으로 읽을 수 있지만 library runtime dependency, parsing 비용, schema 오류 처리가 추가된다. 초기 범위에서 채택하지 않는다.
