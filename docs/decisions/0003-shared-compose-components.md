# ADR-0003: Shared Compose component model

- 상태: Accepted
- 결정일: 2026-07-30

## 배경

BEEZ는 Android, iOS, Desktop 및 Web에서 사용할 수 있는 Compose Multiplatform 디자인 시스템을 목표로 한다.

플랫폼마다 별도의 UI 컴포넌트를 구현하면 각 플랫폼 관습에 세밀하게 대응할 수 있다. 하지만 API, 상태, 접근성, 수정 사항과 문서를 여러 구현에서 반복해서 관리해야 하며 플랫폼 간 기능 차이가 커질 수 있다.

BEEZ의 우선 목적은 하나의 디자인 언어와 컴포넌트를 여러 Compose 프로젝트에서 일관되게 재사용하는 것이다.

## 결정

BEEZ 컴포넌트는 Compose Multiplatform의 공통 API와 공통 구현을 기본으로 한다.

```text
Beez component specification
             ↓
      commonMain API
             ↓
   commonMain implementation
             ↓
┌─────────┬─────────┬─────────┬─────────┐
│ Android │   iOS   │ Desktop │   Web   │
└─────────┴─────────┴─────────┴─────────┘
```

소비자는 지원 플랫폼에 관계없이 같은 BEEZ 컴포넌트 이름, property, state model과 token contract를 사용한다.

```kotlin
BeezTheme {
    BeezActionButton(
        onClick = ::continueToNext,
    ) {
        BeezText("Continue")
    }
}
```

## 공통으로 유지할 계약

다음 항목은 모든 지원 플랫폼에서 공통이어야 한다.

- 컴포넌트 이름과 공개 API
- 필수 및 선택 property
- variant와 size의 의미
- state model과 state precedence
- slot과 content contract
- semantic token 역할
- event와 state ownership
- 접근성 목적과 전달할 정보
- deprecated 및 migration 정책

플랫폼별 구현 편의를 위해 공통 계약을 다르게 만들지 않는다.

## 플랫폼에 맞게 적응할 수 있는 항목

다음 항목은 공통 의미를 유지하면서 플랫폼 입력 방식과 시스템 기능에 맞게 내부적으로 달라질 수 있다.

- Pointer, touch와 keyboard 입력 처리
- Focus 표시와 이동
- Accessibility semantics의 플랫폼 매핑
- Haptic, sound와 system feedback
- Cursor와 hover
- Window 및 safe area integration
- Text input method와 clipboard
- Platform back 또는 dismiss 동작
- Rendering 성능을 위한 내부 구현

플랫폼 차이는 공개 API 분기가 아니라 공통 abstraction의 내부 구현으로 우선 처리한다.

## `expect`와 `actual` 사용 원칙

`expect`와 `actual`은 다음 조건을 모두 만족할 때만 사용한다.

- 공통 Compose API만으로 올바른 사용자 경험을 제공할 수 없다.
- 차이가 UI 스타일이 아니라 실제 플랫폼 capability 또는 integration에서 발생한다.
- 공통 API의 의미를 유지할 수 있다.
- 각 플랫폼 구현과 fallback을 테스트할 수 있다.
- 차이와 사용 이유가 component specification 또는 ADR에 기록된다.

색상, padding, radius 또는 임의의 visual tuning을 플랫폼별로 다르게 만들기 위해 `expect`와 `actual`을 사용하지 않는다.

## Source set 원칙

```text
commonMain
├── public component API
├── component implementation
├── state and behavior
├── theme and token access
└── common semantics contract

platformMain
├── platform capability adapter
├── system integration
└── unavoidable platform behavior
```

플랫폼 source set에 전체 컴포넌트 사본을 만들지 않는다.

플랫폼별 코드가 커져 컴포넌트를 사실상 별도로 유지하게 되면 공통 abstraction이 잘못되었는지 먼저 검토한다.

## 동일함의 의미

“같은 컴포넌트”는 다음을 의미한다.

- 같은 목적
- 같은 API
- 같은 token과 variant 체계
- 같은 상태 및 핵심 동작
- 같은 수준의 접근성 정보

모든 플랫폼에서 pixel 단위로 완전히 같은 결과를 의미하지는 않는다. Font rasterization, pointer와 touch 환경, system text input 및 accessibility bridge에서 발생하는 의도된 차이는 허용한다.

의도된 차이는 문서와 screenshot 기준에 명시한다. 문서화되지 않은 차이는 결함으로 취급한다.

## 네이티브 UI toolkit 범위

초기 BEEZ는 다음 별도 컴포넌트 구현을 제공하지 않는다.

- SwiftUI용 BEEZ component
- UIKit용 BEEZ component
- Android View용 BEEZ component
- React 또는 DOM용 BEEZ component

iOS와 Web을 포함한 초기 UI 제공 범위는 Compose Multiplatform 안에서 사용하는 BEEZ component다.

플랫폼 네이티브 화면에 Compose UI를 포함하는 interoperability는 제품 integration의 관심사다. 반복되는 공통 요구가 확인되면 별도 adapter 또는 integration guide를 검토한다.

## 결과

### 장점

- 하나의 API와 구현을 여러 플랫폼에서 재사용할 수 있다.
- 수정과 접근성 개선을 모든 플랫폼에 함께 전달할 수 있다.
- 문서와 카탈로그가 하나의 component contract를 설명할 수 있다.
- 플랫폼별 기능 격차와 구현 drift를 줄일 수 있다.
- 제품 개발자가 플랫폼마다 다른 BEEZ 사용법을 학습하지 않아도 된다.

### 비용과 제약

- 공통 abstraction을 신중하게 설계해야 한다.
- Compose가 제공하지 않는 플랫폼 기능에는 adapter가 필요하다.
- 일부 native toolkit 고유 UI와 완전히 같은 동작을 제공하지 못할 수 있다.
- iOS 및 Web의 Compose 지원 변화에 맞춰 호환성을 지속해서 검증해야 한다.
- 공통 구현에서 모든 입력 환경과 접근성을 테스트해야 한다.

## 예외

특정 플랫폼에서 별도 컴포넌트 구현이 필요하다는 제안은 다음 내용을 포함한 ADR로 검토한다.

- 공통 구현으로 해결할 수 없는 사용자 문제
- 공통 API를 유지할 수 있는지 여부
- 플랫폼 구현의 장기 유지 비용
- 다른 플랫폼의 fallback
- 테스트와 문서화 계획
- 향후 공통화 가능성

성능 최적화만 필요한 경우에는 public contract를 유지하는 내부 platform implementation을 우선 검토한다.
