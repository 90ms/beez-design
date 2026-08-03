# BEEZ Platform Policy

- 상태: Draft
- 최종 수정일: 2026-08-03

## 1. 목적

이 문서는 BEEZ가 지원하는 플랫폼의 범위, 공통성 기준, 플랫폼별 구현 상태와 품질 기대치를 정의한다.

최소 OS 및 toolchain 버전은 `docs/compatibility.md`에서 관리한다.

## 2. 지원 모델

BEEZ는 Compose Multiplatform을 통해 하나의 component API와 구현을 여러 플랫폼에 제공한다.

| 등급 | 플랫폼 | UI 기술 | 초기 기대 수준 |
| --- | --- | --- | --- |
| Tier 1 | Android | Compose | 개발, 자동 검증 및 수동 접근성 검증의 기준 |
| Tier 2 | iOS | Compose Multiplatform | 공통 API와 주요 동작 지원 |
| Tier 2 | Desktop | Compose Multiplatform | 공통 API, keyboard와 pointer 동작 지원 |
| Experimental | Web | Compose Multiplatform/Wasm | 카탈로그와 소비 가능성 검증 |

지원 등급은 중요도의 순위가 아니라 현재 보장 가능한 검증 수준을 의미한다.

## 3. 공통 API 정책

- Public component는 `commonMain`에 선언한다.
- 플랫폼마다 component 이름이나 property 이름을 다르게 제공하지 않는다.
- 공통 state와 event model을 사용한다.
- 플랫폼 전용 타입을 공통 public API에 노출하지 않는다.
- 플랫폼 capability가 필요한 경우 BEEZ가 정의한 공통 abstraction을 우선 사용한다.
- 특정 플랫폼에서 지원할 수 없는 기능은 조용히 무시하지 않고 상태와 fallback을 문서화한다.

## 4. 공통 구현 정책

- Component layout과 behavior는 `commonMain` 구현을 기본으로 한다.
- 플랫폼 source set에 전체 component를 복제하지 않는다.
- `expect`와 `actual`은 system integration 또는 capability 차이에 제한한다.
- 플랫폼별 visual override보다 theme token과 공통 adaptive rule을 우선한다.
- 플랫폼별 조건문이 component 전반에 퍼지지 않도록 adapter 경계에 모은다.

## 5. 플랫폼 적응 영역

### Android

- Touch와 keyboard 입력
- TalkBack semantics
- Android focus 및 back integration
- Window inset와 system UI integration
- Haptic 및 system feedback

### iOS

- Touch와 hardware keyboard 입력
- VoiceOver semantics
- UIKit text input 및 clipboard integration
- Safe area와 system gesture
- Haptic 및 system feedback

### Desktop

- Mouse, trackpad와 keyboard 입력
- Hover, cursor와 focus ring
- Desktop screen reader semantics
- Window, clipboard와 shortcut integration
- Touch target과 pointer target의 차이

### Web

- Pointer, touch와 keyboard 입력
- Browser focus 및 accessibility semantics
- Browser zoom과 responsive viewport
- Clipboard와 text input
- Wasm runtime 및 browser capability 제약

이 목록은 플랫폼별 component fork를 허용하는 목록이 아니다. 공통 behavior를 플랫폼 기능에 연결할 때 검토할 영역이다.

## 6. 지원 등급별 완료 기준

### Tier 1

- Component implementation 상태가 Ready다.
- 모든 필수 automated test를 CI에서 실행한다.
- 기준 screenshot과 의도된 시각 변경을 관리한다.
- 대표 기기 또는 emulator에서 수동 interaction 검증을 수행한다.
- 대표 screen reader로 수동 접근성 검증을 수행한다.
- Stable release의 차단 결함을 정의하고 적용한다.

### Tier 2

- 공통 API와 핵심 behavior를 제공한다.
- 빌드 및 공통 behavior test를 CI에서 실행한다.
- platform input과 semantics의 대표 경로를 검증한다.
- 알려진 차이와 미지원 capability를 문서화한다.
- Stable로 표시한 component는 주요 사용 사례를 실제 target에서 검증한다.

### Experimental

- 빌드와 실행 가능 여부를 지속해서 확인한다.
- 지원하는 component와 알려진 제약을 명시한다.
- API 호환성을 의도적으로 깨지 않지만 동작과 제공 범위는 변경될 수 있다.
- Stable과 동일한 품질 보장을 주장하지 않는다.

## 7. 플랫폼별 상태 표시

각 component는 다음 상태 중 하나를 플랫폼별로 표시한다.

| 상태 | 의미 |
| --- | --- |
| Not Planned | 제공하지 않기로 결정했으며 이유가 있음 |
| Planned | 지원 범위에 있으나 구현 전 |
| In Progress | 구현 또는 검증 중 |
| Experimental | 사용할 수 있으나 제약이 있음 |
| Ready | 명세와 해당 platform 품질 기준을 충족 |
| Deprecated | 신규 사용을 권장하지 않음 |

전체 component maturity와 platform status를 혼합하지 않는다.

예:

| Component | Maturity | Android | iOS | Desktop | Web |
| --- | --- | --- | --- | --- | --- |
| Action Button | Experimental | Experimental | Experimental | Experimental | Experimental |
| Checkbox | Experimental | Experimental | Experimental | Experimental | Experimental |
| Text Field | Experimental | Experimental | Experimental | Experimental | Experimental |
| Surface | Experimental | In Progress | In Progress | In Progress | In Progress |

## 8. 차이 관리

플랫폼 차이는 다음 세 종류로 분류한다.

### Required

운영체제 capability, input 방식 또는 접근성 bridge 때문에 반드시 필요한 차이다.

### Adaptive

동일한 목적을 유지하면서 window size, font scale 또는 input 환경에 적응하는 차이다.

### Defect

명세에 근거가 없거나 다른 플랫폼 구현과 우연히 달라진 상태다.

Required와 Adaptive 차이는 component specification에 이유와 결과를 기록한다. 기록되지 않은 차이는 우선 Defect로 분류한다.

## 9. 테스트 원칙

### 공통 테스트

- State transition
- Event와 state ownership
- Layout constraint
- Token mapping
- Semantics contract
- LTR과 RTL
- Font scale과 긴 콘텐츠

### 플랫폼 테스트

- 실제 input dispatch
- Focus navigation
- Accessibility bridge
- Text input과 clipboard
- Window와 system integration
- Rendering screenshot

공통 테스트가 통과한다는 이유로 플랫폼 검증을 생략하지 않는다.

## 10. 카탈로그

카탈로그는 동일한 scenario model을 모든 target에서 실행한다.

```text
Component scenario
        ↓
Android / iOS / Desktop / Web catalog
```

각 target의 카탈로그가 별도 예제 데이터를 갖지 않게 하고 다음 scenario를 공유한다.

- Playground
- Variant와 size matrix
- State matrix
- Light와 Dark
- BEEZ와 test brand theme
- Font scale
- LTR과 RTL
- Accessibility semantics

플랫폼 전용 integration 예제만 해당 source set에 추가할 수 있다.

## 11. 네이티브 toolkit과의 관계

초기 BEEZ는 SwiftUI, UIKit, Android View, React 및 DOM용 component를 별도로 제공하지 않는다.

제품이 native UI와 Compose를 함께 사용할 수는 있지만 BEEZ의 UI component contract는 Compose Multiplatform을 기준으로 한다.

공통 요구가 확인되면 다음 형태를 별도로 검토한다.

- Native host integration guide
- Theme 또는 token bridge
- Platform capability adapter
- Compose embedding sample

## 12. 호환성 정책

다음 버전 정보는 `docs/compatibility.md`에서 관리한다.

- Kotlin
- Compose Compiler
- Compose Multiplatform
- Gradle
- Android Gradle Plugin
- Android minimum SDK
- iOS minimum version
- Desktop JDK 및 운영체제
- Web browser와 WasmGC 요구사항

버전은 관성적으로 최신만 지원하거나 근거 없이 오래된 환경까지 확대하지 않는다. 실제 소비 프로젝트, upstream 지원 범위, CI 비용과 보안 업데이트를 함께 검토한다.

## 13. 승격과 강등

플랫폼 지원 등급을 올릴 때는 다음을 확인한다.

- 실제 소비 프로젝트 또는 명확한 사용 계획
- 자동 및 수동 검증 환경
- 접근성과 system integration 검증 가능성
- 결함 대응과 릴리스 유지 역량

지원 등급을 낮추거나 제거할 때는 영향받는 component, 소비자와 migration 대안을 문서화한다.
