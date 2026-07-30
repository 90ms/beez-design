# BEEZ Component Specification Template

## 1. 목적

이 문서는 BEEZ 컴포넌트 명세의 공통 구조를 정의한다.

새 컴포넌트는 구현 전에 이 템플릿을 복사해 `docs/components/{component-name}.md`에 명세를 작성한다. 모든 섹션은 검토하되 해당하지 않는 항목은 삭제하지 않고 적용되지 않는 이유를 짧게 기록한다.

명세는 디자인 설명, 공개 API, 동작, 접근성 및 플랫폼 구현이 공유하는 계약이다. 코드가 명세와 다르면 어느 한쪽을 조용히 맞추지 않고 의도된 동작을 먼저 확인한다.

<!-- Copy from the next heading when creating a component specification. -->

# {Component name}

## Metadata

| 항목 | 값 |
| --- | --- |
| Status | Proposed / Experimental / Stable / Deprecated |
| Since | 미출시 또는 최초 제공 버전 |
| Platforms | Android / iOS / Desktop / Web |
| Replaces | 대체하는 컴포넌트가 있다면 이름 |
| Related | 관련 컴포넌트 및 패턴 |
| Last reviewed | YYYY-MM-DD |

## Summary

컴포넌트가 무엇이며 어떤 사용자 문제를 해결하는지 한두 문장으로 설명한다. 생김새보다 목적과 동작을 중심으로 작성한다.

## When to use

- 이 컴포넌트를 사용해야 하는 대표적인 상황
- 사용자가 기대하는 결과
- 제품 내에서 담당하는 위계와 역할

## When not to use

- 시각적으로 유사하지만 목적이 다른 상황
- 다른 컴포넌트나 패턴을 선택해야 하는 조건
- 제품 로컬 구현으로 남겨야 하는 경우

## Alternatives

| 상황 | 사용할 요소 | 이유 |
| --- | --- | --- |
| {상황} | {컴포넌트 또는 패턴} | {선택 기준} |

유사 컴포넌트가 없다면 `해당 없음`으로 기록한다.

## Anatomy

컴포넌트를 의미 있는 slot 단위로 나누고 각 역할을 설명한다.

| Slot | Required | 역할 |
| --- | --- | --- |
| `root` | Yes | 전체 레이아웃과 상호작용 영역 |
| `{slot}` | Yes / No | {역할} |

Anatomy 이미지를 제공할 경우 slot 이름은 명세와 코드에서 동일하게 사용한다.

## Properties

디자인 속성과 공개 API에서 사용자가 선택할 수 있는 축을 정의한다.

| Property | Values | Default | 설명 |
| --- | --- | --- | --- |
| `variant` | `{value}` | `{default}` | 시각적 역할과 사용 기준 |
| `size` | `{value}` | `{default}` | 크기와 사용 환경 |

다음 원칙을 따른다.

- 같은 의미를 갖는 조합을 여러 이름으로 제공하지 않는다.
- Boolean 옵션이 둘 이상 결합해 새로운 의미를 만들면 enum 또는 명시적인 타입을 검토한다.
- 스타일 내부 구현을 그대로 노출하는 옵션은 만들지 않는다.
- 기본값은 가장 빈번하고 안전한 사용 사례를 나타낸다.

## Variants

각 variant의 의미와 위계를 설명한다.

| Variant | Emphasis | 한 화면의 권장 개수 | Usage |
| --- | --- | --- | --- |
| `{variant}` | High / Medium / Low | {개수 또는 제한 없음} | {사용 목적} |

색상이나 모양만 다르고 의미가 같은 variant는 추가하지 않는다.

## Sizes

| Size | Container | Content | Touch target | Usage |
| --- | --- | --- | --- | --- |
| `{size}` | {크기 규칙} | {텍스트/아이콘 규칙} | {최소 영역} | {사용 환경} |

표시 크기와 실제 상호작용 영역이 다르면 둘을 구분해 기록한다.

## States

지원하는 상태와 조합 규칙을 정의한다.

| State | Trigger | Visual response | Interaction | Semantics |
| --- | --- | --- | --- | --- |
| Enabled | 기본 조건 | {표현} | 허용 | {의미} |
| Pressed | 포인터 또는 터치 입력 | {표현} | 진행 중 | {의미} |
| Focused | 키보드 또는 접근성 포커스 | {표현} | 허용 | {의미} |
| Selected | 선택된 옵션 | {표현} | 허용 | {의미} |
| Disabled | 상호작용 불가 | {표현} | 차단 | 비활성 상태 전달 |
| Loading | 작업 진행 중 | {표현} | 중복 실행 방지 | 진행 상태 전달 |
| Error | 오류 조건 | {표현} | 컴포넌트별 정의 | 오류 상태 전달 |

지원하지 않는 공통 상태는 표에서 제거하지 말고 이유를 기록한다.

### State precedence

동시에 여러 조건이 참일 때의 우선순위를 정의한다.

```text
예: Disabled > Loading > Pressed > Focused > Enabled
```

Selected처럼 다른 상호작용 상태와 결합할 수 있는 상태는 덮어쓰기 여부가 아니라 조합 결과를 별도로 설명한다.

## Behavior

### Input

- Touch 및 pointer 동작
- Keyboard 동작과 단축키
- Focus 획득과 이동
- 중복 입력과 debounce 정책

### State ownership

컴포넌트가 상태를 소유하는지, 호출자가 상태를 전달하는지 정의한다.

- Stateless API
- Stateful convenience API가 필요한 경우 그 이유
- 상태 저장 및 복원 요구사항
- 이벤트 callback과 상태 변경의 순서

### Feedback

- Press, hover, focus feedback
- Haptic 또는 sound feedback
- Loading과 progress feedback
- Motion 감소 설정에서의 대체 표현

## Layout

- 최소 및 최대 크기
- content padding과 slot 간격
- 정렬 규칙
- 부모 constraint에 대한 동작
- 긴 콘텐츠와 overflow 처리
- 가로 및 세로 배치 전환 조건
- window size 또는 breakpoint 대응

고정된 화면 크기나 특정 기기 모델에 의존하는 규칙은 만들지 않는다.

## Responsive and adaptive behavior

- 좁은 폭과 넓은 폭에서의 차이
- 폰트 스케일 증가 시 레이아웃 변화
- pointer와 touch 입력 환경의 차이
- orientation 또는 window class에 따른 동작

차이가 없다면 `모든 지원 환경에서 동일한 규칙을 사용한다`고 기록한다.

## Internationalization

- 번역으로 텍스트가 길어질 때의 동작
- 줄바꿈 및 최대 줄 수
- RTL layout에서 slot과 아이콘의 배치
- 숫자, 날짜 또는 단위의 locale 처리
- CJK 및 복합 문자에서의 typography 고려사항

## Content guidelines

### Do

- 권장하는 문구와 콘텐츠 구성

### Do not

- 피해야 하는 문구와 콘텐츠 구성

아이콘만 사용하는 경우처럼 시각적 콘텐츠와 접근성 이름이 다른 사례도 명시한다.

## Accessibility

### Semantics

- 역할 또는 accessibility trait
- 접근성 이름
- 상태와 값 전달 방식
- 사용자 action
- 여러 slot을 하나의 semantics node로 병합할지 여부

### Interaction

- 최소 touch target
- keyboard 탐색 및 실행
- focus order
- switch access 또는 보조 입력 지원

### Visual

- 전경과 배경의 대비 기준
- 색상 이외의 상태 표현
- font scaling
- high contrast 및 motion reduction 고려사항

자동 검사로 확인할 항목과 수동 보조기술 검증이 필요한 항목을 구분한다.

## Token mapping

각 property, state 및 slot이 사용하는 token을 기록한다.

| Variant | State | Slot | Property | Token |
| --- | --- | --- | --- | --- |
| `{variant}` | Enabled | `root` | Background | `color.semantic.{token}` |
| `{variant}` | Enabled | `{slot}` | Foreground | `color.semantic.{token}` |
| `{variant}` | Pressed | `root` | Overlay | `color.semantic.{token}` |

원시 값은 사용할 수 없다. 적합한 token이 없으면 기존 token을 임의로 재사용하지 않고 신규 token의 필요성을 검토한다.

## Compose API

의도하는 최소 공개 API를 실제 Kotlin 문법으로 작성한다.

```kotlin
@Composable
fun Beez{ComponentName}(
    // 필수 데이터와 callback
    modifier: Modifier = Modifier,
    // 선택 속성
)
```

### API rules

- `modifier`는 첫 번째 선택 인자로 제공한다.
- 상태는 가능한 한 호출자가 소유하고 이벤트를 위로 전달한다.
- 공개 컴포넌트 API와 기본 구현은 `commonMain`에 둔다.
- `CompositionLocal`은 테마처럼 트리 전체에 적용되는 관심사에 제한한다.
- slot API는 유연성뿐 아니라 허용되는 콘텐츠 계약을 함께 정의한다.
- 플랫폼 타입을 공통 공개 API에 노출하지 않는다.
- 핵심 컴포넌트는 Material 구현과 타입에 의존하지 않는다.
- Material 연동은 별도 adapter의 명시적인 API에서만 제공한다.
- 접근성에 필요한 데이터는 호출자가 제공하거나 안전한 기본값을 갖는다.

## Usage

### Basic

```kotlin
Beez{ComponentName}(
    // 가장 일반적인 사용 예
)
```

### Variants and states

각 variant와 상태의 대표적인 예제를 제공한다.

### Incorrect usage

컴파일은 되지만 디자인 또는 접근성 규칙을 위반하는 예와 대안을 제공한다.

## Platform differences

| Concern | Android | iOS | Desktop | Web |
| --- | --- | --- | --- | --- |
| Rendering | {내용} | {내용} | {내용} | {내용} |
| Interaction | {내용} | {내용} | {내용} | {내용} |
| Semantics | {내용} | {내용} | {내용} | {내용} |

공통 동작을 제공할 수 없는 경우 다음을 기록한다.

- 차이가 필요한 이유
- 사용자 경험에 미치는 영향
- 대체 동작
- 향후 통합 계획 또는 `Not Planned` 판단

## Test matrix

### Automated

- [ ] 기본 렌더링
- [ ] 모든 public property
- [ ] 지원하는 모든 state
- [ ] 의미 있는 property와 state 조합
- [ ] Light 및 Dark theme
- [ ] BEEZ 기본 및 테스트 브랜드 theme
- [ ] LTR 및 RTL
- [ ] 기본 및 확대 font scale
- [ ] semantics와 accessibility action
- [ ] 입력 및 callback
- [ ] 상태 저장과 복원

### Visual

- [ ] 기준 screenshot
- [ ] 크기와 variant matrix
- [ ] 긴 콘텐츠와 좁은 constraint
- [ ] 플랫폼별 의도된 차이

### Manual

- [ ] Android TalkBack
- [ ] iOS VoiceOver
- [ ] Desktop keyboard 및 screen reader
- [ ] Web keyboard 및 screen reader

지원하지 않거나 아직 준비되지 않은 플랫폼 항목은 플랫폼 상태와 추적 이슈를 함께 기록한다.

## Catalog scenarios

카탈로그에서 제공할 예제를 나열한다.

- Playground
- Variant 및 size matrix
- State matrix
- Theme 비교
- Font scale 및 긴 콘텐츠
- RTL
- Accessibility semantics

## Open questions

명세 검토 중 해결하지 못한 질문과 선택지를 기록한다. `Stable` 전환 전에는 공개 API나 핵심 동작에 영향을 주는 열린 질문이 없어야 한다.

## Changelog

| Date | Change | Reason |
| --- | --- | --- |
| YYYY-MM-DD | 최초 제안 | {배경} |

<!-- End of the copyable component specification. -->

# 템플릿 사용 규칙

- 새 컴포넌트 제안은 `Metadata`, `Summary`, `When to use`, `When not to use`, `Anatomy`를 먼저 작성한다.
- 설계 검토가 끝나기 전에 공개 API 구현을 시작하지 않는다.
- 명세와 구현을 같은 pull request에서 변경할 수 있지만 커밋에서 변경 의도를 구분한다.
- 표의 빈칸은 허용하지 않는다. 미정이면 `TBD`와 결정 예정 시점을 기록한다.
- 이미지가 없어도 명세는 이해할 수 있어야 한다.
- 코드에서만 알 수 있는 규칙을 남기지 않는다.
- 플랫폼별 차이를 공통 API로 억지로 숨기지 않는다.
- 컴포넌트가 Deprecated 상태가 되더라도 마이그레이션이 끝날 때까지 문서를 유지한다.
