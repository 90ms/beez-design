# ADR-0009: BEEZ theme provider API

- 상태: Accepted
- 결정일: 2026-07-31

## 문맥

Token scheme은 foundation과 component가 읽을 수 있는 공통 제공 방식이 필요하다. 소비자가 테마를 직접 parameter로 모든 component에 전달하면 API가 반복되고, 전역 mutable 상태를 사용하면 Compose recomposition과 preview 격리가 어려워진다.

## 결정

beez-foundation은 BeezTheme object를 제공한다.

- BeezTheme { ... }는 기본 BEEZ Light scheme을 descendant에 제공한다.
- BeezTheme.Light { ... }와 BeezTheme.Dark { ... }는 기본 appearance를 명시한다.
- BeezTheme.Provide(scheme) { ... }는 브랜드 scheme 또는 제품별 scheme을 주입한다.
- BeezTheme.colors, typography, spacing, shapes, elevation, motion은 현재 CompositionLocal scheme에서 semantic 역할을 읽는다.
- CompositionLocal의 기본값은 BEEZ Light scheme으로 두어 provider 바깥의 preview와 작은 sample이 안전하게 동작하도록 한다.

## 경계

- theme provider는 token scheme만 전달하며 Material theme이나 Material type을 사용하지 않는다.
- theme provider는 component state, product navigation, remote configuration을 소유하지 않는다.
- Shape scheme은 semantic radius를 제공하고 실제 Compose Shape 변환은 component/foundation 구현에서 수행한다.
- 런타임에 여러 브랜드를 자동 병합하지 않는다. 제품은 명시적으로 copy한 완전한 scheme을 주입한다.

## 결과

- component API가 theme parameter 반복 없이 semantic token을 읽을 수 있다.
- Light, Dark, test brand를 동일한 provider 경로로 검증할 수 있다.
- CompositionLocal 경계가 명확해 preview와 nested theme 검증이 가능하다.
- provider API는 Compose Runtime에만 의존하며 Material 3 버전과 독립적이다.

## 대안

### 모든 component에 scheme parameter 전달

명시성은 높지만 공개 API가 반복되고 component composition이 복잡해진다. 기본 접근 경로로 채택하지 않는다.

### 전역 singleton mutable theme

호출은 간단하지만 여러 preview/window와 recomposition 격리를 보장하기 어렵다. 채택하지 않는다.
