# Checkbox

## Metadata

| 항목 | 값 |
| --- | --- |
| Status | Experimental |
| Since | 0.1.0-SNAPSHOT |
| Platforms | Android / iOS / Desktop / Web |
| Replaces | 없음 |
| Related | Action Button, Text Field, Segmented control |
| Last reviewed | 2026-08-03 |

## Summary

Checkbox는 사용자가 서로 독립적인 binary option을 선택하거나 해제하는 control이다. 선택 상태는 호출자가 소유하며 label, 표시 indicator와 전체 interaction 영역을 하나의 접근성 node로 제공한다.

## When to use

- 하나 이상의 독립적인 option을 각각 선택할 수 있을 때
- 설정이나 동의처럼 선택 상태가 action 이후에도 유지될 때
- 사용자가 현재 선택 여부를 즉시 확인하고 전환해야 할 때

## When not to use

- 즉시 실행되는 단일 action에는 Action Button을 사용한다.
- 상호 배타적인 option 하나만 고르는 경우에는 Radio 또는 Segmented control을 검토한다.
- 현재 상태가 아니라 기능을 즉시 켜고 끄는 설정에는 Switch를 검토한다.
- 일부 선택을 나타내는 indeterminate state에는 첫 binary API를 우회하지 않고 별도 tri-state 계약을 검토한다.

## Alternatives

| 상황 | 사용할 요소 | 이유 |
| --- | --- | --- |
| 즉시 실행 | Action Button | 지속되는 선택 상태가 아님 |
| 상호 배타적 선택 | Radio / Segmented control | 하나의 값만 선택 가능함 |
| 즉시 적용되는 on/off 설정 | Switch | 상태 변화의 즉시성이 핵심임 |
| 세 가지 선택 상태 | Tri-state Checkbox | binary Boolean으로 의미를 잃지 않아야 함 |

## Anatomy

| Slot | Required | 역할 |
| --- | --- | --- |
| `root` | Yes | 전체 layout, focus, toggle action과 semantics |
| `indicator` | Yes | checked 상태를 shape와 check mark로 표시 |
| `label` | Yes | option의 의미와 접근성 이름 제공 |

label과 indicator는 별도 action을 갖지 않으며 root 전체가 하나의 checkbox node다.

## Properties

| Property | Values | Default | 설명 |
| --- | --- | --- | --- |
| `checked` | true / false | 필수 | 현재 선택 상태 |
| `enabled` | true / false | true | 사용자 toggle 허용 여부 |

첫 API에는 variant, size, supporting slot과 indeterminate state를 추가하지 않는다. 반복되는 제품 요구가 확인되면 기존 binary 계약과 혼동되지 않는 확장을 별도로 검토한다.

## Variants

별도 variant는 없다. Checkbox의 의미는 선택 여부이며 시각적 emphasis를 호출자가 선택하지 않는다.

## Sizes

단일 표시 크기를 사용한다. indicator는 minimum touch target의 절반 크기로 렌더링하고 root interaction 영역은 `spacing.semantic.interaction.minimumTouchTarget` 이상을 유지한다.

## States

| State | Trigger | Visual response | Interaction | Semantics |
| --- | --- | --- | --- | --- |
| Unchecked | `checked=false` | neutral stroke, check mark 없음 | toggle 허용 | checkbox off |
| Checked | `checked=true` | brand container와 on-brand check mark | toggle 허용 | checkbox on |
| Focused | keyboard/accessibility focus | 인접 색과 대비되는 2dp stroke | toggle 허용 | focus 전달 |
| Pressed | pointer/touch/keyboard 실행 중 | Compose foundation indication | 진행 중 | checkbox action |
| Disabled | `enabled=false` | secondary foreground와 neutral stroke | 차단 | disabled와 on/off 상태 |
| Loading | 지원하지 않음 | Checkbox가 비동기 작업을 소유하지 않음 | 해당 없음 | 해당 없음 |
| Error | 지원하지 않음 | group 또는 form validation이 설명 | 해당 없음 | 해당 없음 |

### State precedence

```text
Disabled > Pressed > Focused > Checked / Unchecked
```

Checked와 Unchecked는 기본 값 상태이며 Focused, Pressed, Disabled와 조합된다. Disabled에서도 check mark를 유지해 색상 없이 상태를 구분한다.

## Behavior

### Input

- indicator와 label을 포함한 root 전체를 touch 또는 pointer로 누르면 반대 값을 `onCheckedChange`에 전달한다.
- keyboard focus 상태에서 Space로 toggle한다.
- disabled 상태에서는 callback을 호출하지 않는다.
- callback 이후 새 상태를 반영하는 책임은 호출자에게 있다.

### State ownership

Checkbox는 `checked`를 호출자가 소유하는 stateless API다. 내부에서 상태를 저장하거나 callback 결과를 미리 표시하지 않는다.

### Feedback

- check mark는 색상과 무관하게 선택 상태를 전달한다.
- focus는 stroke 두께와 색으로 전달한다.
- 별도 motion과 haptic feedback은 첫 계약에 포함하지 않는다.

## Layout

- root는 최소 touch target 높이를 보장하고 indicator와 label 사이에 semantic control content gap을 사용한다.
- label은 부모 constraint 안에서 줄바꿈할 수 있으며 indicator는 위쪽이 아니라 첫 줄 중심을 기준으로 배치하지 않는다. 전체 행의 세로 중앙에 정렬한다.
- 좁은 폭에서 label을 생략하거나 말줄임하지 않는다.
- Checkbox group의 세로 간격은 호출자가 관리한다.

## Responsive and adaptive behavior

모든 지원 환경에서 같은 API와 layout 규칙을 사용한다. Pointer 환경에서도 hit target을 줄이지 않는다.

## Internationalization

- label은 번역 길이, CJK, 복합 문자와 확대 font scale에서 줄바꿈할 수 있다.
- RTL에서는 indicator가 논리적 시작 위치에 놓이고 label이 그 뒤를 따른다.
- 상태를 check mark의 방향이나 label 언어에 의존해 전달하지 않는다.

## Content guidelines

### Do

- 사용자가 선택하는 조건이나 대상을 짧고 구체적으로 쓴다.
- 같은 group의 label은 문법 구조와 긍정/부정 방향을 일관되게 유지한다.

### Do not

- label 안에 별도의 link나 button action을 넣지 않는다.
- 선택 결과를 설명하기 위해 시각적 check mark만 사용하고 label을 생략하지 않는다.
- 하나의 Checkbox로 서로 다른 두 설정을 동시에 바꾸지 않는다.

## Accessibility

### Semantics

- root는 checkbox role, label, checked 상태와 toggle action을 제공한다.
- indicator와 label semantics는 root에 병합한다.
- disabled 상태를 보조기술에 전달하면서 현재 on/off 값도 유지한다.

### Interaction

- root interaction 영역은 semantic minimum touch target 이상이다.
- keyboard tab 순서로 focus를 받고 Space로 toggle한다.
- focus order는 화면 layout 순서를 따른다.

### Visual

- checked indicator의 `background.brand`는 인접 `background.neutral`과 3:1 이상이어야 한다.
- check mark의 `foreground.onBrand`는 `background.brand`와 4.5:1 이상이어야 한다.
- unchecked/focus stroke는 neutral surface와 3:1 이상이어야 한다.
- check mark와 stroke를 함께 사용해 색상만으로 상태를 전달하지 않는다.

자동 테스트는 role, checked/disabled state, callback, keyboard toggle, touch target과 token mapping을 확인한다. 실제 TalkBack, VoiceOver, Desktop screen reader와 browser accessibility bridge 검증은 별도로 남는다.

## Token mapping

| State | Slot | Property | Token |
| --- | --- | --- | --- |
| All | `root` | Minimum height | spacing.semantic.interaction.minimumTouchTarget |
| All | `root` | Content gap | spacing.semantic.control.contentGap |
| All | `label` | Typography | typography.semantic.label |
| Enabled | `label` | Foreground | color.semantic.foreground.primary |
| Unchecked | `indicator` | Border | color.semantic.stroke.neutral |
| Checked | `indicator` | Background | color.semantic.background.brand |
| Checked | `indicator` | Check mark | color.semantic.foreground.onBrand |
| Focused unchecked | `indicator` | Focus stroke | color.semantic.stroke.focus |
| Focused checked | `indicator` | Focus stroke | color.semantic.foreground.onBrand |
| Disabled | `label` / `indicator` | Muted foreground | color.semantic.foreground.secondary |
| Disabled | `indicator` | Border | color.semantic.stroke.neutral |

Indicator의 square shape, proportional check geometry와 transparent background는 component 구조 값이다. 별도 공개 component token이나 새 semantic shape role은 첫 구현에 추가하지 않는다.

## Compose API

```kotlin
@Composable
fun BeezCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
)
```

## Usage

```kotlin
var accepted by remember { mutableStateOf(false) }

BeezCheckbox(
    checked = accepted,
    onCheckedChange = { accepted = it },
    label = "I agree to the terms",
)
```

## Incorrect usage

Checkbox label 안에 별도 클릭 link를 넣지 않는다. 약관 link와 동의 Checkbox가 모두 필요하면 각각 독립적인 접근성 node로 배치하고 Checkbox label은 동의 상태만 설명한다.

## Platform differences

| Concern | Android | iOS | Desktop | Web |
| --- | --- | --- | --- | --- |
| Rendering | commonMain | commonMain | commonMain | commonMain |
| Interaction | touch, keyboard, TalkBack | touch, hardware keyboard, VoiceOver | pointer, Space, screen reader | pointer, touch, Space, browser semantics |
| Semantics | Compose accessibility bridge | Compose accessibility bridge | Compose accessibility bridge | Wasm/browser bridge |

의도된 platform 차이는 현재 없다. 실제 target에서 발견한 차이는 Required, Adaptive, Defect로 분류해 기록한다.

## Test matrix

### Automated

- [ ] unchecked/checked rendering과 checkbox role
- [ ] click callback과 state hoisting
- [ ] disabled callback 차단과 semantics
- [ ] minimum touch target
- [ ] keyboard focus와 Space toggle
- [ ] Light/Dark/Test Brand Catalog scenario
- [ ] Light/Dark/alternate brand Desktop visual baseline
- [ ] 긴 label, 확대 font scale과 RTL layout

### Manual / platform follow-up

- [ ] Android TalkBack
- [ ] iOS VoiceOver와 hardware keyboard
- [ ] Desktop screen reader
- [ ] Web browser semantics와 keyboard

## Catalog scenarios

- Playground: checked와 disabled 전환 및 callback 결과
- States: unchecked, checked, disabled unchecked, disabled checked
- Long label / RTL
- Light, Dark와 Test Brand theme

## Known limitations

- Indeterminate tri-state API는 제공하지 않는다.
- Label 내부 interactive content와 supporting text slot은 제공하지 않는다.
- 실제 플랫폼 보조기술 검증은 완료되지 않았다.

## Change history

| 날짜 | 변경 | 이유 |
| --- | --- | --- |
| 2026-08-03 | 최초 binary Checkbox 명세 | 초기 범위의 세 번째 component vertical slice 정의 |
