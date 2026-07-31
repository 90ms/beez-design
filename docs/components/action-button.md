# Action Button

## Metadata

| 항목 | 값 |
| --- | --- |
| Status | Experimental |
| Since | 0.1.0-SNAPSHOT |
| Platforms | Android / iOS / Desktop / Web |
| Replaces | 없음 |
| Related | Text, Icon |
| Last reviewed | 2026-07-31 |

## Summary

Action Button은 사용자가 명확한 작업을 실행하는 단일 상호작용 요소다. 짧은 label과 선택적인 leading/trailing content를 제공하며, product theme의 semantic token으로 emphasis를 표현한다.

## When to use

- 화면이나 영역에서 사용자의 주요 작업을 실행할 때
- form 제출, 저장, 계속하기처럼 결과가 분명한 action을 노출할 때
- 한 화면의 hierarchy에 맞춰 primary, neutral, outline emphasis를 선택할 때

## When not to use

- 화면 이동 자체를 나타내는 텍스트 링크
- 여러 선택지 중 하나를 지속적으로 선택하는 Toggle 또는 Checkbox
- 긴 설명이나 여러 action을 담아야 하는 container

## Alternatives

| 상황 | 사용할 요소 | 이유 |
| --- | --- | --- |
| 단순 화면 이동 | Text 또는 Link pattern | button action semantics가 필요하지 않음 |
| 여러 선택지의 상태 유지 | Checkbox 또는 Segmented control | 선택 상태가 핵심임 |
| 위험한 작업 확인 | Action Button + confirmation pattern | 실수 방지와 설명이 필요함 |

## Anatomy

| Slot | Required | 역할 |
| --- | --- | --- |
| root | Yes | 전체 layout, focus와 click semantics |
| leadingContent | No | label 앞의 icon 또는 짧은 visual |
| label | Yes | action 목적을 설명하는 짧은 텍스트 |
| trailingContent | No | label 뒤의 icon 또는 상태 visual |

leading/trailing slot은 decorative content를 기본으로 하며, 별도의 의미가 있으면 호출자가 접근성 설명을 제공한다. label은 접근성 이름의 기본 source다.

## Properties

| Property | Values | Default | 설명 |
| --- | --- | --- | --- |
| variant | BrandSolid / Neutral / Outline | BrandSolid | 시각적 emphasis와 semantic 목적 |
| size | Small / Medium / Large | Medium | control 높이와 inset |
| enabled | true / false | true | 사용자 action 허용 여부 |
| loading | true / false | false | 중복 실행을 막고 진행 상태를 전달 |

공개 API는 label, onClick, modifier, 위 property와 두 개의 optional slot을 제공한다. 스타일 내부 색상이나 padding을 직접 받지 않는다.

## Variants

| Variant | Emphasis | 한 화면의 권장 개수 | Usage |
| --- | --- | --- | --- |
| BrandSolid | High | 주요 action 중심 1개 | 화면의 기본 또는 가장 중요한 작업 |
| Neutral | Medium | 제한 없음 | 보조적인 일반 작업 |
| Outline | Low | 제한 없음 | secondary action 또는 surface 위의 가벼운 action |

## Sizes

| Size | Container | Content | Touch target | Usage |
| --- | --- | --- | --- | --- |
| Small | compact control height | label style | semantic minimumTouchTarget 이상 | 좁은 toolbar 또는 보조 action |
| Medium | default control height | label style | semantic minimumTouchTarget 이상 | 기본 사용 |
| Large | comfortable control height | label style | semantic minimumTouchTarget 이상 | 큰 화면 또는 주요 CTA |

작은 표시 높이가 minimumTouchTarget보다 작으면 layout은 semantic minimumTouchTarget까지 확장한다. 시각적 크기와 실제 interaction 영역을 동일하게 취급하지 않는다.

## States

| State | Trigger | Visual response | Interaction | Semantics |
| --- | --- | --- | --- | --- |
| Enabled | 기본 조건 | variant token 적용 | 허용 | button role과 label |
| Pressed | pointer/touch/keyboard 실행 중 | pressed feedback | 진행 중 | button action |
| Focused | keyboard 또는 accessibility focus | focus stroke 표시 | 허용 | focus 전달 |
| Disabled | enabled=false | muted semantic color | 차단 | disabled 상태 |
| Loading | loading=true | progress indicator와 label 유지 | 중복 실행 차단 | 진행 상태 전달 |

### State precedence

```text
Disabled > Loading > Pressed > Focused > Enabled
```

Loading은 enabled가 true여도 click을 차단한다. Focused와 Pressed는 disabled/loading과 동시에 visual state로 표현하지 않는다.

## Behavior

### Input

- Touch와 pointer click을 지원한다.
- Keyboard Enter와 Space 실행을 Compose click semantics에 위임한다.
- focus 가능한 환경에서는 focus ring을 표시한다.
- Loading 중 callback을 다시 호출하지 않는다.

### State ownership

컴포넌트는 enabled와 loading을 호출자로부터 받는 stateless API다. 작업 진행과 오류 상태는 호출자가 관리한다.

### Feedback

- Pressed와 focus feedback은 색상/스트로크로 전달한다.
- Loading은 indicator와 semantics로 전달한다.
- motion reduction 환경에서는 loading indicator의 motion을 줄이거나 정적 진행 표현으로 대체한다.

## Layout

- label이 길어지면 한 줄에 고정하지 않고 부모 constraint에 맞춰 처리한다.
- leading/trailing slot은 label과 semantic control content gap을 사용한다.
- 좁은 폭에서 content를 임의로 잘라내지 않는다.
- RTL에서는 leading/trailing이 논리적 시작/끝 위치를 따른다.

## Responsive and adaptive behavior

모든 지원 환경에서 같은 API와 semantic hierarchy를 사용한다. Size는 window class가 아니라 호출자 선택으로 바꾸며, pointer 환경은 hover를 추가할 수 있지만 click semantics는 변경하지 않는다.

## Internationalization

- label은 번역 후 길어질 수 있으므로 고정 폭을 가정하지 않는다.
- CJK, RTL, 복합 문자와 font scale 증가를 지원한다.
- icon만 제공하는 별도 API는 만들지 않으며, icon-only action은 접근성 이름을 포함한 별도 검토가 필요하다.

## Accessibility

### Semantics

- root는 button role과 label을 제공한다.
- disabled와 loading 상태를 보조기술에 전달한다.
- loading indicator는 indeterminate progress semantics를 갖는다.
- leading/trailing decorative content는 label semantics를 방해하지 않는다.

### Interaction

- 실제 interaction 영역은 semantic minimumTouchTarget 이상이다.
- keyboard focus와 실행을 지원한다.
- focus order는 화면 layout 순서를 따른다.

### Visual

- semantic foreground/background 쌍을 사용한다.
- focus를 색상만으로 전달하지 않고 stroke로 함께 표시한다.
- font scale 증가 시 label이 잘리지 않는다.

## Token mapping

| Variant | State | Slot | Property | Token |
| --- | --- | --- | --- | --- |
| BrandSolid | Enabled | root | Background | color.semantic.background.brand |
| BrandSolid | Enabled | label | Foreground | color.semantic.foreground.onBrand |
| Neutral | Enabled | root | Background | color.semantic.background.neutral |
| Neutral | Enabled | label | Foreground | color.semantic.foreground.primary |
| Outline | Enabled | root | Background | transparent platform value |
| Outline | Enabled | root | Border | color.semantic.stroke.neutral |
| Outline | Enabled | label | Foreground | color.semantic.foreground.primary |
| All | Focused | root | Focus stroke | color.semantic.stroke.focus |
| All | All | label | Typography | typography.semantic.label |
| All | All | root | Shape | shape.semantic.control |
| All | All | root | Content gap | spacing.semantic.control.contentGap |
| All | All | root | Height/inset | spacing.semantic.control.* |
| All | Disabled | label/root | Muted role | color.semantic.foreground.secondary / background.neutral |

Transparent은 구조상 필요한 platform-independent 값이며, 브랜드 색상이나 component identity를 표현하는 token으로 사용하지 않는다.

## Compose API

```kotlin
@Composable
fun BeezActionButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: BeezActionButtonVariant = BeezActionButtonVariant.BrandSolid,
    size: BeezActionButtonSize = BeezActionButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
)
```

## Usage

```kotlin
BeezTheme {
    BeezActionButton(
        label = "Continue",
        onClick = onContinue,
    )
}
```

## Platform differences

| Concern | Android | iOS | Desktop | Web |
| --- | --- | --- | --- | --- |
| Rendering | commonMain | commonMain | commonMain | commonMain |
| Interaction | touch, keyboard, TalkBack | touch, keyboard, VoiceOver | pointer, keyboard, screen reader | pointer, touch, keyboard, browser semantics |
| Semantics | Compose accessibility bridge | Compose accessibility bridge | Compose accessibility bridge | Wasm/browser bridge |

의도된 platform 차이는 현재 없다. 실제 target 검증에서 차이가 발견되면 Required, Adaptive, Defect 중 하나로 분류한다.

## Test matrix

### Automated

- [ ] 기본 rendering과 callback
- [ ] variant와 size matrix
- [ ] disabled/loading state와 중복 입력 차단
- [ ] Light/Dark와 test brand theme
- [ ] LTR/RTL, 긴 label과 font scale
- [ ] button semantics와 focus action

### Visual

- [ ] variant/size/state 기준 screenshot
- [ ] 좁은 constraint와 긴 label

### Manual

- [ ] Android TalkBack
- [ ] iOS VoiceOver
- [ ] Desktop keyboard
- [ ] Web keyboard와 browser semantics

현재 scaffold가 compile-verified 상태가 아니므로 위 체크는 아직 완료로 표시하지 않는다.

## Catalog scenarios

- Playground
- Variant와 size matrix
- Enabled, focused, disabled, loading state matrix
- Light/Dark와 BEEZ/test brand 비교
- 긴 label, font scale, RTL
- accessibility semantics

## Open questions

- Loading indicator의 motion과 reduced-motion fallback을 어느 공통 primitive로 제공할지
- Icon-only action을 별도 component로 승격할 실제 사용 사례가 있는지
- hover 전용 visual을 semantic token 계약에 추가할 필요가 있는지

## Changelog

| Date | Change | Reason |
| --- | --- | --- |
| 2026-07-31 | 최초 제안 | 첫 공통 component vertical slice |
