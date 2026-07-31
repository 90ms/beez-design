# Text Field

## Metadata

| 항목 | 값 |
| --- | --- |
| Status | Proposed |
| Since | 미출시 (0.1.0-SNAPSHOT 이전 설계) |
| Platforms | Android / iOS / Desktop / Web |
| Replaces | 없음 |
| Related | Text, Action Button, Form validation pattern |
| Last reviewed | 2026-07-31 |

## Summary

Text Field는 사용자가 짧은 문자열을 입력하고 수정하는 단일 행 form control이다. 값과 변경 이벤트를 호출자가 소유하며, BEEZ semantic token으로 label, focus, 오류와 disabled 상태를 일관되게 표현한다.

이번 제안은 single-line 입력만 다룬다. multiline 입력은 줄바꿈, 높이, 키보드 action과 selection 정책을 별도 검토한 뒤 확장한다.

## When to use

- 이름, 이메일, 검색어, 코드처럼 짧은 문자열을 입력받을 때
- form field의 label, 현재 값, 오류 설명과 focus 상태를 함께 제공할 때
- 제품 테마에 따라 동일한 입력 경험을 유지할 때

## When not to use

- 장문 또는 여러 줄의 서술형 콘텐츠를 입력할 때
- 날짜, 숫자, 비밀번호처럼 formatting 또는 보안 정책이 핵심인 입력
- 선택지가 제한되어 직접 입력보다 Picker, Checkbox 또는 Segmented control이 적합한 경우
- 자동 완성, masking, validation orchestration이 컴포넌트 자체의 책임인 경우

## Alternatives

| 상황 | 사용할 요소 | 이유 |
| --- | --- | --- |
| 장문 입력 | 추후 Multiline Text Field 검토 | 줄바꿈과 높이 정책이 필요함 |
| 제한된 선택지 | Checkbox, Segmented control 또는 Picker | 자유 입력보다 선택 action이 명확함 |
| 읽기 전용 값 표시 | Text 또는 Description pattern | 입력 focus와 편집 semantics가 불필요함 |

## Anatomy

| Slot | Required | 역할 |
| --- | --- | --- |
| `root` | Yes | 전체 field layout과 focus semantics |
| `label` | Yes | 입력 목적을 설명하는 접근성 이름과 시각적 label |
| `input` | Yes | 값 편집과 selection을 담당하는 텍스트 영역 |
| `placeholder` | No | 값이 비어 있을 때의 입력 예시 |
| `leadingContent` | No | 입력 의미를 보조하는 icon 또는 짧은 visual |
| `trailingContent` | No | 지우기, 검색 등 field action을 담는 visual |
| `supportingText` | No | 도움말 또는 오류 설명 |

`leadingContent`와 `trailingContent`는 decorative content를 기본으로 한다. 실제 action을 넣는 경우 해당 slot의 semantics와 focus 순서를 호출자가 책임진다.

## Properties

| Property | Values | Default | 설명 |
| --- | --- | --- | --- |
| `value` | String | 필수 | 현재 입력 값. 호출자가 소유한다. |
| `onValueChange` | `(String) -> Unit` | 필수 | 사용자가 입력 값을 변경할 때 호출한다. |
| `label` | String | 필수 | 입력 목적을 설명하는 접근성 이름이다. |
| `placeholder` | String 또는 null | null | 값이 비어 있고 focus되지 않은 경우의 예시다. |
| `supportingText` | String 또는 null | null | 도움말 또는 오류 설명이다. |
| `enabled` | Boolean | true | false이면 focus와 편집을 차단한다. |
| `readOnly` | Boolean | false | focus와 복사는 허용하지만 값을 변경하지 않는다. |
| `isError` | Boolean | false | 오류 시각 표현과 오류 semantics를 활성화한다. |
| `leadingContent` | composable slot 또는 null | null | 입력 앞의 보조 content다. |
| `trailingContent` | composable slot 또는 null | null | 입력 뒤의 보조 content다. |

`supportingText`는 validation을 실행하지 않는다. 오류 판단, 메시지 생성과 값 저장은 호출자 또는 form pattern의 책임이다.

## Variants

초기 버전은 의미가 다른 variant를 제공하지 않는다. 모든 field는 outlined 형태와 semantic state로 목적을 표현한다. Filled, compact 또는 search 전용 variant는 둘 이상의 제품 사용 사례와 token 계약을 확인한 후 별도 제안한다.

## Sizes

초기 버전은 하나의 기본 크기만 제공한다.

| Size | Container | Content | Touch target | Usage |
| --- | --- | --- | --- | --- |
| Default | control comfortable height를 기준으로 하는 outlined container | body typography | semantic minimumTouchTarget 이상 | 일반 form과 검색 입력 |

표시 높이는 font scale과 supporting text에 따라 늘어날 수 있다. 고정 높이로 label, 값 또는 오류 설명을 잘라내지 않는다.

## States

| State | Trigger | Visual response | Interaction | Semantics |
| --- | --- | --- | --- | --- |
| Enabled | 기본 조건 | neutral stroke와 primary content | 편집 허용 | text field role, label과 value |
| Focused | keyboard, pointer 또는 accessibility focus | focus stroke 표시 | 편집 허용 | focus 전달 |
| ReadOnly | readOnly=true | enabled와 같은 content, 편집 feedback 없음 | focus와 복사 허용, 변경 차단 | read-only 상태 전달 |
| Disabled | enabled=false | muted content와 neutral container | focus와 편집 차단 | disabled 상태 전달 |
| Error | isError=true | critical stroke와 supporting text 강조 | enabled/readOnly 규칙 유지 | 오류 상태와 설명 전달 |

### State precedence

```text
Disabled > Error > Focused > ReadOnly > Enabled
```

Error는 Focused와 함께 참일 수 있지만 border와 supporting text의 critical 표현이 focus 표현보다 우선한다. Disabled는 error flag가 함께 전달되어도 disabled 표현과 상호작용 규칙을 우선한다. ReadOnly는 Disabled와 다르게 focus와 selection을 허용한다.

## Behavior

### Input

- Touch, pointer와 keyboard로 focus를 획득한다.
- 키보드 입력, selection, copy와 paste는 Compose text input primitive에 위임한다.
- 단일 행이므로 Enter는 줄바꿈을 삽입하지 않는다. IME action의 구체적인 submit 처리는 form pattern에서 담당한다.
- `enabled=false`이면 input focus와 편집을 차단한다.
- `readOnly=true`이면 focus와 selection은 허용하지만 `onValueChange`를 호출하지 않는다.

### State ownership

Text Field는 stateless API다. `value`와 `onValueChange`는 호출자가 소유하며, 내부에 입력 값을 저장하지 않는다. 제품이 저장 복원이나 validation debounce를 필요로 하면 상위 state 또는 별도 form pattern에서 처리한다.

### Feedback

- Focus는 stroke로 전달하고 색상만으로 상태를 구분하지 않는다.
- Error는 critical stroke와 supporting text로 전달한다.
- Disabled는 muted semantic color와 input action 차단으로 전달한다.
- 별도 loading 상태는 제공하지 않는다. 비동기 validation이나 검색 진행은 field 외부 pattern에서 표현한다.

## Layout

- root는 label, input row, supporting text를 세로로 배치한다.
- input row는 leadingContent, input, trailingContent를 논리적 시작-끝 순서로 배치한다.
- 값과 placeholder는 한 줄로 표시하며, 부모 폭에 맞춰 trailing content와 함께 축소된다.
- 긴 값은 임의로 잘라내지 않는다. 입력 primitive의 horizontal scrolling 동작을 따른다.
- label과 supportingText는 font scale 증가 시 줄바꿈할 수 있다.
- 실제 input row의 interaction 영역은 semantic minimumTouchTarget 이상이어야 한다.

## Responsive and adaptive behavior

모든 지원 환경에서 동일한 API와 semantic hierarchy를 사용한다. 좁은 폭에서도 slot을 삭제하거나 label을 placeholder로 대체하지 않는다. window class별 variant 전환은 호출자 layout의 책임이다.

## Internationalization

- label, placeholder와 supportingText는 번역 후 길어질 수 있다.
- CJK와 복합 문자를 보존하며, 입력 문자열을 임의로 lower/upper case 변환하지 않는다.
- RTL에서는 leading/trailing slot과 text cursor의 논리적 방향을 Compose text primitive에 위임한다.
- 숫자, 날짜, 이메일 validation과 formatting은 Text Field가 해석하지 않는다.

## Content guidelines

### Do

- label에 입력 목적을 구체적으로 작성한다. 예: `Email address`
- placeholder에는 형식 예시나 짧은 힌트를 사용한다.
- 오류 원인과 해결 방법을 supportingText에 짧게 설명한다.

### Do not

- label을 placeholder로만 대체해 focus 후 목적을 잃게 하지 않는다.
- 오류 색상만 사용하고 오류 설명을 생략하지 않는다.
- 비밀번호, 카드번호 등 민감한 입력을 기본 Text Field로 처리하지 않는다. masking과 보안 요구를 별도 API로 검토한다.

## Accessibility

### Semantics

- root/input은 text field role, label, 현재 value와 editable/read-only 상태를 전달한다.
- `enabled=false`는 disabled semantics를 전달하고 focus 대상에서 제외한다.
- `isError=true`는 오류 상태를 전달한다. supportingText가 있으면 오류 설명과 연결한다.
- label은 placeholder와 독립적인 접근성 이름의 source다.
- decorative leading/trailing content는 input semantics를 방해하지 않도록 병합한다.

### Interaction

- 최소 interaction 영역은 semantic minimumTouchTarget 이상이다.
- keyboard focus, text insertion, selection, copy와 paste를 지원한다.
- focus order는 화면의 layout 순서를 따른다.
- read-only input은 focus와 selection을 유지하면서 변경 action만 차단한다.

### Visual

- foreground/background와 stroke는 semantic token 조합을 사용한다.
- focused, error, disabled 상태는 색상 외에도 stroke, 설명과 interaction 차단으로 구분한다.
- 확대된 font scale에서 label, value와 supportingText가 잘리지 않는다.

## Token mapping

| State | Slot | Property | Token |
| --- | --- | --- | --- |
| All | root | Container | transparent platform value |
| Enabled / ReadOnly | root | Border | `color.semantic.stroke.neutral` |
| Focused | root | Border | `color.semantic.stroke.focus` |
| Error | root | Border | `color.semantic.stroke.critical` (추가 필요) |
| Enabled / ReadOnly | label/input | Foreground | `color.semantic.foreground.primary` |
| Empty | placeholder | Foreground | `color.semantic.foreground.secondary` |
| Error | label/supportingText | Foreground | `color.semantic.foreground.critical` (추가 필요) |
| Disabled | label/input/supportingText | Foreground | `color.semantic.foreground.secondary` |
| All | input | Typography | `typography.semantic.body` |
| All | label | Typography | `typography.semantic.label` |
| All | supportingText | Typography | `typography.semantic.caption` |
| All | root | Shape | `shape.semantic.control` |
| All | input row | Height / inset / gap | `spacing.semantic.control.comfortable*`, `spacing.semantic.control.contentGap` |
| All | root | Minimum touch target | `spacing.semantic.interaction.minimumTouchTarget` |

`color.semantic.stroke.critical`과 `color.semantic.foreground.critical`은 현재 semantic color contract에 없어 구현 전에 추가해야 한다. `background.critical`을 border나 text에 재사용하지 않는다.

## Compose API

```kotlin
@Composable
public fun BeezTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    supportingText: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
)
```

`KeyboardOptions`, `KeyboardActions`, `VisualTransformation`, multiline line count와 selection state는 1차 public API에서 제외한다. 실제 제품 요구가 확인되면 별도 API 확장 또는 전문 input component로 검토한다.

## Usage

### Basic

```kotlin
var email by remember { mutableStateOf("") }

BeezTextField(
    value = email,
    onValueChange = { email = it },
    label = "Email address",
    placeholder = "name@example.com",
)
```

### Error and read-only states

```kotlin
BeezTextField(
    value = email,
    onValueChange = { email = it },
    label = "Email address",
    supportingText = "Enter a valid email address.",
    isError = true,
)

BeezTextField(
    value = accountId,
    onValueChange = {},
    label = "Account ID",
    readOnly = true,
)
```

### Incorrect usage

```kotlin
// label을 생략하거나 placeholder만 접근성 이름으로 사용하지 않는다.
// 장문 입력은 1차 Text Field API의 범위가 아니다.
```

## Platform differences

| Concern | Android | iOS | Desktop | Web |
| --- | --- | --- | --- | --- |
| Rendering | commonMain | commonMain | commonMain | commonMain |
| Interaction | touch, keyboard, TalkBack | touch, keyboard, VoiceOver | pointer, keyboard, screen reader | pointer, touch, keyboard, browser semantics |
| Text input | Compose text input bridge | Compose text input bridge | Compose text input bridge | Wasm/browser text input bridge |
| Semantics | Compose accessibility bridge | Compose accessibility bridge | Compose accessibility bridge | Wasm/browser accessibility bridge |

의도된 API 차이는 없다. IME action, clipboard와 accessibility bridge의 세부 동작은 플랫폼 검증에서 차이가 발견되면 별도로 기록한다.

## Test matrix

### Automated

- [ ] 기본 value/onValueChange 입력과 recomposition
- [ ] placeholder, supportingText와 label
- [ ] enabled, readOnly, isError state
- [ ] Light/Dark와 test brand theme
- [ ] 긴 label/value, 좁은 constraint와 font scale
- [ ] LTR/RTL slot 방향과 text alignment
- [ ] semantics role, label, value, error와 disabled/read-only state
- [ ] keyboard focus, selection과 callback

### Visual

- [ ] enabled, focused, readOnly, disabled, error screenshot
- [ ] 긴 label와 supportingText가 있는 좁은 layout
- [ ] Light/Dark와 test brand 비교

### Manual

- [ ] Android TalkBack text input과 error announcement
- [ ] iOS VoiceOver text input과 read-only state
- [ ] Desktop keyboard, selection과 paste
- [ ] Web keyboard와 browser semantics

GitHub Actions의 공통 build/test는 구현 단계에서 추가한다. 실제 text input bridge, 보조기기와 screenshot 검증은 각 플랫폼 환경이 필요하며 Linux runner에서는 iOS Simulator 검증을 수행하지 않는다.

## Catalog scenarios

- Playground: value editing과 callback
- Empty, filled, focused, read-only, disabled와 error state matrix
- Leading/trailing slot 조합
- Light/Dark와 BEEZ/test brand 비교
- 긴 label/value, font scale과 좁은 폭
- LTR/RTL
- Semantics와 keyboard interaction 안내

## Open questions

- `color.semantic.stroke.critical`과 `color.semantic.foreground.critical`의 scale mapping 및 contrast 기준
- 1차 구현에서 `KeyboardOptions`와 IME action을 노출할 실제 제품 요구
- Multiline, password와 visual transformation을 별도 component로 둘지 확장 API로 둘지
- trailing action slot의 semantics와 focus order를 공통 primitive로 제공할지

## Changelog

| Date | Change | Reason |
| --- | --- | --- |
| 2026-07-31 | 단일 행 Text Field 명세 초안 | 두 번째 공통 입력 component의 범위와 접근성 계약 정의 |
