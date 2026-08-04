# Text

## Metadata

| 항목 | 값 |
| --- | --- |
| Status | Experimental |
| Since | 미출시 |
| Platforms | Android / iOS / Desktop / Web |
| Replaces | 해당 없음 |
| Related | Action Button, Checkbox, Text Field, Surface |
| Last reviewed | 2026-08-04 |

## Summary

Text는 BEEZ의 semantic typography와 foreground 역할을 일반 제품 문구에 적용하는 비대화형 텍스트 컴포넌트다. 화면마다 `TextStyle`과 `Color`를 임의로 조합하는 대신 콘텐츠의 위계와 의도를 명시해 테마, 브랜드와 접근성 설정에 일관되게 반응하게 한다.

현재 BEEZ 컴포넌트와 Catalog에는 화면 제목, 섹션 제목, 본문, label과 보조 문구가 반복된다. 기존 typography와 color token을 직접 조합할 수는 있지만 조합 규칙이 호출부마다 달라질 수 있으므로, 공통 역할 매핑과 안전한 기본값을 시스템 수준에서 관리할 가치가 있다.

## When to use

- 화면 제목, 섹션 제목, 본문, label과 보조 문구처럼 제품이 소유한 일반 텍스트
- Light, Dark와 제품 브랜드 theme에서 같은 의미의 typography와 foreground를 유지해야 하는 문구
- 줄바꿈, 정렬 또는 제한된 줄 수를 명시적으로 제어해야 하는 텍스트

## When not to use

- Action Button, Checkbox, Text Field처럼 상위 컴포넌트가 label과 상태 표현을 소유하는 경우
- 여러 style, link 또는 inline content가 섞이는 rich text
- 사용자가 편집하는 값. 입력은 Text Field 또는 목적에 맞는 입력 컴포넌트를 사용한다.
- 코드 편집기, 표 데이터처럼 제품별 typography와 선택 동작이 핵심인 특수 콘텐츠

## Alternatives

| 상황 | 사용할 요소 | 이유 |
| --- | --- | --- |
| 실행 가능한 label | Action Button | action role, 입력, focus와 상태 계약이 필요하다. |
| binary 선택 label | Checkbox | checked state와 toggle semantics를 함께 제공한다. |
| 사용자 입력 | Text Field | 편집, focus, keyboard와 오류 계약이 필요하다. |
| 여러 style 또는 link가 섞인 문장 | 제품 로컬 `BasicText` 또는 검증된 rich-text pattern | 초기 Text는 하나의 semantic role과 tone만 소유한다. |
| BEEZ 역할로 표현할 수 없는 특수 typography | 제품 로컬 `BasicText`와 `BeezTheme` token | 실제 반복 요구가 확인되기 전까지 공개 role을 확대하지 않는다. |

## Anatomy

| Slot | Required | 역할 |
| --- | --- | --- |
| `root` | Yes | 부모 constraint를 전달하고 텍스트를 배치하는 비대화형 영역 |
| `text` | Yes | 하나의 typography role과 foreground tone으로 표시되는 문자열 |

Text는 leading/trailing content나 inline content slot을 제공하지 않는다.

## Properties

| Property | Values | Default | 설명 |
| --- | --- | --- | --- |
| `text` | `String` | 필수 | 화면에 표시하고 접근성 bridge에 전달할 문구 |
| `role` | `Display`, `ScreenTitle`, `SectionTitle`, `Body`, `Label`, `Caption` | `Body` | 콘텐츠 위계에 맞는 semantic typography |
| `tone` | `Primary`, `Secondary`, `Critical`, `OnBrand` | `Primary` | 인접 배경에 맞는 semantic foreground |
| `textAlign` | Compose `TextAlign` | `Start` | 논리적 시작점을 기준으로 한 줄 내부 정렬 |
| `overflow` | Compose `TextOverflow` | `Clip` | 제한된 영역을 넘는 텍스트의 시각 처리 |
| `maxLines` | `Int` | `Int.MAX_VALUE` | 표시할 최대 줄 수. 1 이상이어야 한다. |
| `modifier` | Compose `Modifier` | `Modifier` | 부모가 크기, 배치와 명시적 semantics를 확장하는 지점 |

`Color`와 `TextStyle` override는 제공하지 않는다. 호출자가 원시 스타일로 semantic 계약을 우회하게 만들고 role과 실제 렌더링의 불일치를 허용하기 때문이다.

## Variants

Text의 typography 위계는 `role`, foreground 의미는 `tone`이 담당한다. 둘은 독립적으로 조합할 수 있지만 인접 배경과 콘텐츠 의미에 맞는 조합만 사용한다.

### Roles

| Role | Emphasis | 한 화면의 권장 개수 | Usage |
| --- | --- | --- | --- |
| `Display` | High | 핵심 진입 화면에서 1개 | 짧은 hero 또는 대표 메시지 |
| `ScreenTitle` | High | 화면별 1개 | 현재 화면의 목적을 나타내는 제목 |
| `SectionTitle` | Medium | 섹션별 1개 | 관련 콘텐츠 그룹의 제목 |
| `Body` | Medium | 제한 없음 | 설명, 안내와 일반 콘텐츠 |
| `Label` | Medium | 제한 없음 | 짧은 항목명, metadata key와 control 주변 설명 |
| `Caption` | Low | 제한 없음 | 출처, 시간, 부가 정보와 짧은 보조 문구 |

### Tones

| Tone | Emphasis | Usage |
| --- | --- | --- |
| `Primary` | High | 일반 배경 위의 제목과 핵심 본문 |
| `Secondary` | Medium | 일반 배경 위의 보조 설명과 metadata |
| `Critical` | High | 오류 또는 위험을 설명하는 문구. 색상만으로 의미를 전달하지 않는다. |
| `OnBrand` | High | `backgroundBrand`처럼 검증된 brand 배경 위의 텍스트 |

`OnBrand`는 일반 배경에서 강조색처럼 사용하지 않는다. `Critical`은 오류의 원인이나 해결 방법처럼 읽을 수 있는 문구와 함께 사용한다.

## Sizes

Text는 별도의 size property를 제공하지 않는다. 크기, 굵기와 line height는 `role`이 참조하는 semantic typography token이 함께 결정한다. 임의 크기가 필요한 콘텐츠는 제품 로컬 요구로 유지하고 반복성이 확인되면 typography taxonomy를 먼저 검토한다.

Text는 비대화형이므로 container와 touch target을 소유하지 않는다.

## States

Text 자체가 소유하는 interaction state는 없다.

| State | 지원 여부 | 이유 |
| --- | --- | --- |
| Enabled | 적용되지 않음 | Text는 action이나 입력을 제공하지 않는다. |
| Pressed | 지원하지 않음 | pointer/touch action을 소유하지 않는다. |
| Focused | 지원하지 않음 | 기본적으로 focus 대상이 아니다. |
| Selected | 지원하지 않음 | text selection은 초기 계약에 포함하지 않는다. |
| Disabled | 지원하지 않음 | 비활성 상태는 Text를 포함하는 상위 컴포넌트가 소유한다. |
| Loading | 지원하지 않음 | 진행 상태를 소유하지 않는다. |
| Error | `Critical` tone으로 내용 표현 | interactive state가 아니라 문구의 의미를 시각적으로 보조한다. |

### State precedence

지원하는 interaction state 조합이 없으므로 precedence는 적용되지 않는다. `Critical` tone은 상태 머신이 아니며 다른 tone과 동시에 적용할 수 없다.

## Behavior

### Input

Text는 touch, pointer 또는 keyboard 입력을 처리하지 않고 focus를 획득하지 않는다. click이나 link가 필요하면 Text에 `clickable`을 붙여 숨은 control을 만들지 말고 목적에 맞는 interactive component 또는 명시적인 application-level pattern을 사용한다.

### State ownership

Text는 전달받은 `String`과 표현 property만 렌더링하는 stateless API다. 상태 저장, 복원과 callback은 적용되지 않는다.

### Feedback

Press, hover, focus, haptic, sound와 motion feedback은 제공하지 않는다.

## Layout

- 고유한 minimum/maximum 크기와 padding을 추가하지 않고 부모 constraint를 존중한다.
- 기본값은 논리적 `Start` 정렬, 자동 줄바꿈과 제한 없는 줄 수다.
- `maxLines`는 1 이상이어야 하며 제한된 공간에서 `overflow`와 함께 동작한다.
- `TextOverflow.Ellipsis`는 유한한 `maxLines`와 함께 사용한다.
- 부모가 폭을 제한하지 않으면 콘텐츠의 자연스러운 폭을 사용한다.
- `modifier`로 고정 높이를 강제해 확대 font scale의 문구를 잘라내지 않는다.

## Responsive and adaptive behavior

모든 지원 환경에서 같은 API와 layout 규칙을 사용한다. 좁은 폭에서는 줄바꿈이 먼저 증가하며, 호출자가 정보 밀도를 이유로 `maxLines`를 줄이더라도 핵심 정보에 ellipsis를 사용하지 않는 것을 권장한다.

font scale이 증가하면 role의 `TextStyle`이 Compose density를 따라 확대되고 필요한 줄 수와 높이가 증가한다. Text는 화면 크기나 input 종류에 따라 role을 자동 변경하지 않는다.

## Internationalization

- 번역 길이를 예상해 기본적으로 줄 수를 제한하지 않는다.
- `Start`와 `End` 정렬은 layout direction을 따르며 RTL에서 자동으로 반전된다.
- 문구의 Unicode grapheme, CJK 줄바꿈과 복합 문자는 Compose text engine에 위임한다.
- 숫자, 날짜, 통화와 단위의 locale formatting은 호출자가 완료된 문자열로 제공한다.
- locale별로 글꼴을 강제로 축소하지 않는다. 필요한 글꼴 coverage는 제품과 Catalog resource에서 검증한다.

## Content guidelines

### Do

- `ScreenTitle`은 현재 화면을 짧고 구체적으로 설명한다.
- `SectionTitle`은 뒤따르는 콘텐츠 그룹을 예측할 수 있게 작성한다.
- `Secondary`와 `Caption`은 없어도 핵심 작업을 이해할 수 있는 보조 정보에 사용한다.
- `Critical` 문구는 무엇이 잘못됐고 사용자가 무엇을 할 수 있는지 함께 설명한다.

### Do not

- 글자 크기만 맞추기 위해 콘텐츠 위계와 다른 role을 선택하지 않는다.
- `OnBrand`를 일반 배경의 장식용 강조색으로 사용하지 않는다.
- 색상 하나만으로 성공, 경고 또는 오류를 전달하지 않는다.
- 중요한 문구를 한 줄 ellipsis로 숨긴 뒤 대체 접근 경로를 제공하지 않는 패턴을 만들지 않는다.

## Accessibility

### Semantics

- Compose text primitive의 text semantics를 그대로 제공한다.
- 별도 role, action, selected/disabled state를 추가하거나 descendant semantics를 병합하지 않는다.
- 시각적으로 줄임표가 적용돼도 원본 `text`를 접근성 bridge의 텍스트 값으로 유지한다.
- heading trait을 자동 추론하지 않는다. 플랫폼 간 공통 heading 계약이 검증되기 전까지 화면 구조 semantics는 상위 pattern이 소유한다.

### Interaction

Text는 touch target, keyboard 탐색과 focus order를 만들지 않는다. 선택, 복사, link 탐색이 필요한 콘텐츠는 별도 pattern이 그 interaction과 semantics를 함께 소유해야 한다.

### Visual

- `Primary`, `Secondary`, `Critical`은 검증된 일반 배경, `OnBrand`는 brand 배경 조합에서 사용한다.
- theme fixture의 foreground/background 조합은 WCAG 2.2 AA text contrast 검증 대상이다.
- 확대 font scale에서 텍스트 자체 크기를 고정하지 않으며 부모의 고정 높이로 정보를 자르지 않는다.
- 색상 외에도 실제 문구로 critical 의미를 전달한다.
- motion은 사용하지 않는다.

자동 테스트는 token mapping, text semantics, RTL, 긴 콘텐츠와 확대 font scale을 확인한다. TalkBack, VoiceOver와 browser accessibility tree에서의 실제 읽기는 수동 플랫폼 검증으로 남긴다.

## Token mapping

### Typography

| Role | Token |
| --- | --- |
| `Display` | `typography.semantic.display` |
| `ScreenTitle` | `typography.semantic.screenTitle` |
| `SectionTitle` | `typography.semantic.sectionTitle` |
| `Body` | `typography.semantic.body` |
| `Label` | `typography.semantic.label` |
| `Caption` | `typography.semantic.caption` |

### Foreground

| Tone | Token | 지원 배경 |
| --- | --- | --- |
| `Primary` | `color.semantic.foreground.primary` | `background.neutral` |
| `Secondary` | `color.semantic.foreground.secondary` | `background.neutral` |
| `Critical` | `color.semantic.foreground.critical` | `background.neutral` |
| `OnBrand` | `color.semantic.foreground.onBrand` | `background.brand` |

## Compose API

```kotlin
public enum class BeezTextRole {
    Display,
    ScreenTitle,
    SectionTitle,
    Body,
    Label,
    Caption,
}

public enum class BeezTextTone {
    Primary,
    Secondary,
    Critical,
    OnBrand,
}

@Composable
public fun BeezText(
    text: String,
    modifier: Modifier = Modifier,
    role: BeezTextRole = BeezTextRole.Body,
    tone: BeezTextTone = BeezTextTone.Primary,
    textAlign: TextAlign = TextAlign.Start,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
)
```

`modifier`는 첫 번째 선택 인자다. 공개 API는 Compose common 타입만 사용하며 Material 또는 플랫폼 타입을 노출하지 않는다.

## Usage

### Basic

```kotlin
BeezText(text = "주문 내역")
```

### Roles and tones

```kotlin
Column {
    BeezText(
        text = "결제 정보",
        role = BeezTextRole.SectionTitle,
    )
    BeezText(
        text = "카드 번호를 다시 확인해 주세요.",
        tone = BeezTextTone.Critical,
    )
}
```

### Overflow

```kotlin
BeezText(
    text = productName,
    maxLines = 2,
    overflow = TextOverflow.Ellipsis,
)
```

### On brand

```kotlin
Box(Modifier.background(BeezTheme.colors.backgroundBrand)) {
    BeezText(
        text = "BEEZ",
        tone = BeezTextTone.OnBrand,
    )
}
```

### Incorrect usage

```kotlin
// Text는 action semantics와 focus/feedback을 제공하지 않는다.
BeezText(
    text = "계속",
    modifier = Modifier.clickable(onClick = ::continueFlow),
)
```

실행 가능한 문구에는 `BeezActionButton`처럼 interaction 계약을 소유한 컴포넌트를 사용한다.

## Platform differences

| Concern | Android | iOS | Desktop | Web |
| --- | --- | --- | --- | --- |
| Rendering | commonMain + Android text rasterization | commonMain + Skia text rasterization | commonMain + Skia text rasterization | commonMain + Canvas/Wasm text rasterization |
| Interaction | 없음 | 없음 | 없음 | 없음 |
| Semantics | Compose accessibility bridge | Compose accessibility bridge | Compose accessibility bridge | Wasm/browser bridge |

API와 의도된 동작 차이는 없다. 글꼴 fallback, 줄바꿈 지점과 glyph rasterization은 플랫폼 text engine에 따라 소폭 달라질 수 있는 Adaptive 차이다. 정보 손실 또는 semantic text 차이는 Defect로 분류한다.

| Platform | 구현 상태 |
| --- | --- |
| Android | Experimental |
| iOS | Experimental |
| Desktop | Experimental |
| Web | Experimental |

## Test matrix

### Automated

- [x] 기본 text rendering과 semantics
- [x] role 전체의 typography token mapping
- [x] tone 전체의 foreground token mapping
- [x] `textAlign`, `overflow`와 `maxLines`
- [x] 유효하지 않은 `maxLines` 거부
- [x] Light/Dark와 test brand theme
- [x] LTR/RTL의 `Start` 정렬
- [x] 긴 문구, 좁은 constraint와 확대 font scale
- [x] interaction role/action을 암묵적으로 추가하지 않음

State, callback과 상태 저장은 비대화형 stateless Text에 적용되지 않는다.

### Visual

- [x] Light/Dark/alternate brand의 role과 tone matrix
- [x] 긴 콘텐츠와 좁은 constraint
- [x] RTL과 확대 font scale

공통 단위/UI 테스트가 role, tone, layout, semantics, theme, RTL과 확대 font scale 계약을 검증한다. Desktop 시각 회귀 테스트는 Light, Dark와 alternate brand에서 대표 role/tone, 긴 콘텐츠, RTL과 1.5배 font scale을 검증하며, Catalog의 카드·상세 guide와 theme scenario도 원격 library validation 및 Pages workflow를 통과했다. 실제 플랫폼별 보조기술 검증은 아래 Manual 항목으로 남아 있으므로 현재 maturity는 Experimental이다.

### Manual

- [ ] Android TalkBack text reading
- [ ] iOS VoiceOver text reading
- [ ] Desktop screen reader
- [ ] Web browser accessibility tree

keyboard action은 Text에 적용되지 않는다.

## Catalog scenarios

- Playground: role, tone, alignment, 최대 줄 수와 overflow
- Typography role matrix
- Foreground tone과 올바른 배경 조합
- Light/Dark와 BEEZ/test brand 비교
- 긴 한국어/영어 문구, 좁은 폭과 ellipsis
- 확대 font scale과 LTR/RTL
- 비대화형 semantics와 interactive component 선택 안내

## Open questions

- `AnnotatedString`과 inline link를 별도 Rich Text component로 승격할 실제 제품 요구가 있는지
- 선택과 복사를 Text의 property로 제공할지 별도 selectable-content pattern이 소유할지
- heading semantics를 플랫폼 간 공통 abstraction으로 제공할 수 있는지

이 질문들은 초기 `String` 기반 Text의 role, tone과 layout 계약을 변경하지 않으며 실제 사용 사례가 확인되기 전까지 범위를 확장하지 않는다.

## Changelog

| Date | Change | Reason |
| --- | --- | --- |
| 2026-08-04 | 최초 Proposed 명세 | 초기 범위의 semantic typography vertical slice 정의 |
| 2026-08-04 | commonMain 구현과 단위/UI 테스트 추가 | role, tone, layout과 비대화형 semantics 계약 검증 시작 |
| 2026-08-04 | Catalog 카드와 상세 사용 가이드 추가 | 실제 API의 role, tone, layout과 접근성 선택 기준 제공 |
| 2026-08-04 | Experimental 전환 | 공통 테스트, Desktop 시각 기준선과 원격 Catalog 검증 완료 |
