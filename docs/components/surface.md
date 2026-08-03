# Surface

## Metadata

| 항목 | 값 |
| --- | --- |
| Status | Experimental |
| Since | 0.1.0-SNAPSHOT |
| Platforms | Android / iOS / Desktop / Web |
| Replaces | 없음 |
| Related | Text, Icon, Card |
| Last reviewed | 2026-08-03 |

## Summary

Surface는 관련 콘텐츠를 하나의 시각적 영역으로 묶고 주변 콘텐츠와의 깊이 관계를 표현하는 비대화형 container다. BEEZ의 neutral background, container shape와 semantic elevation을 일관되게 적용한다.

## When to use

- 관련된 정보나 control을 하나의 시각적 영역으로 묶을 때
- screen background 위에 raised 또는 floating container를 표현할 때
- 제품별로 임의의 container shape와 shadow를 반복하지 않게 할 때

## When not to use

- 눌러서 action을 실행하는 영역에는 Action Button 또는 목적에 맞는 interactive component를 사용한다.
- 선택 가능한 항목에는 Checkbox 등 상태와 semantics를 함께 제공하는 component를 사용한다.
- modal layer와 scrim을 함께 관리해야 하는 경우에는 Overlay 또는 Dialog pattern을 별도로 검토한다.
- 단순한 layout grouping만 필요하고 시각적 surface가 필요하지 않으면 `Box`나 `Column`을 사용한다.

## Alternatives

| 상황 | 사용할 요소 | 이유 |
| --- | --- | --- |
| action 실행 | Action Button | button role, focus와 activation behavior가 필요함 |
| 선택 가능한 카드 | 향후 Interactive Card pattern | 선택 상태와 action semantics를 Surface에 숨기지 않아야 함 |
| 단순 layout | Compose layout primitive | 시각적 container contract가 필요하지 않음 |
| modal content | 향후 Overlay/Dialog pattern | stacking, dismiss와 focus trapping 계약이 필요함 |

## Anatomy

| Slot | Required | 역할 |
| --- | --- | --- |
| `root` | Yes | background, shape와 elevation을 적용하는 container |
| `content` | Yes | 호출자가 배치하는 비대화형 또는 독립적인 semantics node |

Surface는 content semantics를 병합하지 않는다. 내부의 control과 text는 각각 자신의 semantics를 유지한다.

## Properties

| Property | Values | Default | 설명 |
| --- | --- | --- | --- |
| `elevation` | `Flat`, `Raised`, `Floating` | `Flat` | 주변 surface와의 시각적 깊이 관계 |

첫 API는 neutral surface만 제공한다. Brand 또는 critical surface는 foreground 전달 계약 없이 추가하면 잘못된 대비를 만들 수 있으므로 `BeezText`와 content color 정책을 검증한 뒤 확장한다.

## Variants

별도 variant는 없다. 첫 Surface는 일반 콘텐츠를 위한 neutral container다. 색상만 바꾸는 자유 형식 variant나 원시 color 주입 API는 제공하지 않는다.

## Sizes

Surface는 고정 size를 제공하지 않고 부모 constraint와 content 크기를 따른다. 내부 padding, minimum size와 child 간격은 Surface 자체의 의미가 아니므로 호출자가 semantic spacing token으로 구성한다.

## States

| State | Trigger | Visual response | Interaction | Semantics |
| --- | --- | --- | --- | --- |
| Flat | `elevation=Flat` | shadow 없음 | 없음 | 추가하지 않음 |
| Raised | `elevation=Raised` | raised elevation | 없음 | 추가하지 않음 |
| Floating | `elevation=Floating` | floating elevation | 없음 | 추가하지 않음 |
| Pressed | 지원하지 않음 | Surface는 action이 아님 | 해당 없음 | 해당 없음 |
| Focused | 지원하지 않음 | Surface는 focus target이 아님 | 해당 없음 | 해당 없음 |
| Selected | 지원하지 않음 | Surface는 선택 상태를 소유하지 않음 | 해당 없음 | 해당 없음 |
| Disabled | 지원하지 않음 | Surface는 interaction을 소유하지 않음 | 해당 없음 | 해당 없음 |
| Loading | 지원하지 않음 | Surface는 비동기 작업을 소유하지 않음 | 해당 없음 | 해당 없음 |
| Error | 지원하지 않음 | 오류는 content 또는 상위 pattern이 전달함 | 해당 없음 | 해당 없음 |

### State precedence

상호작용 상태가 없으므로 precedence가 적용되지 않는다. Elevation은 동시에 하나만 선택한다.

## Behavior

### Input

Surface 자체는 touch, pointer, keyboard와 focus 입력을 처리하지 않는다. Surface 전체에 `clickable`을 추가해 임의의 interactive card로 만드는 방식은 BEEZ component contract에 포함되지 않는다. 제품에서 반복되는 interactive card 요구가 확인되면 role, focus, selected state와 minimum target을 포함한 별도 component 또는 pattern으로 정의한다.

### State ownership

Surface는 mutable state를 소유하지 않는다. Elevation은 호출자가 전달하는 정적인 semantic property다.

### Feedback

Pressed, hover, focus, haptic, sound와 motion feedback을 제공하지 않는다.

## Layout

- Root는 부모 modifier와 constraint를 존중하고 content 크기에 맞춰진다.
- `shape.semantic.container`를 container outline에 적용한다.
- Surface는 content padding을 암묵적으로 추가하지 않는다.
- Content가 shape 밖으로 그려지지 않도록 container outline으로 clip한다.
- Shadow는 clip 이전에 적용해 elevation이 container 바깥에 렌더링되게 한다.
- 긴 콘텐츠의 줄바꿈, overflow와 child 배치는 content가 책임진다.

## Responsive and adaptive behavior

모든 지원 환경에서 같은 API와 layout 규칙을 사용한다. Surface는 breakpoint나 input 방식에 따라 elevation을 자동 변경하지 않는다.

## Internationalization

Surface 자체는 문자열이나 방향성 있는 slot을 소유하지 않는다. Content는 CJK, 복합 문자, 번역 길이와 RTL layout을 독립적으로 처리하며 Surface는 부모의 `LayoutDirection`을 그대로 전달한다.

## Content guidelines

### Do

- 하나의 주제나 task에 관련된 content를 묶는다.
- 내부 padding과 간격에 BEEZ semantic spacing token을 사용한다.
- neutral background 위에 `foreground.primary` 또는 `foreground.secondary`를 사용한다.

### Do not

- 서로 무관한 여러 task를 하나의 Surface에 묶지 않는다.
- Surface 자체가 button이나 selectable item인 것처럼 보이게 하면서 semantics를 생략하지 않는다.
- Brand 또는 critical raw color를 modifier로 덮어써서 별도 variant처럼 사용하지 않는다.

## Accessibility

### Semantics

- Surface 자체는 role, accessibility name, state 또는 action을 추가하지 않는다.
- Content semantics를 병합하지 않는다.
- 의미 있는 grouping이 필요하면 소비자가 화면 문맥에 맞는 semantics를 명시한다.

### Interaction

Surface 자체는 focus target이나 interaction target이 아니다. 내부 interactive content는 각 component의 touch target과 keyboard 규칙을 따른다.

### Visual

- Neutral Surface의 text와 icon은 승인된 foreground/background 대비 조합을 사용한다.
- Elevation만으로 중요한 상태나 정보를 전달하지 않는다.
- Shadow가 보이지 않는 환경에서도 content 순서와 의미가 유지되어야 한다.
- Font scaling은 content layout에만 영향을 주며 Surface가 정보를 자르지 않아야 한다.

자동 테스트는 elevation mapping, content 노출, semantics 부재, constraint와 RTL 확대 font scale을 확인한다. 실제 플랫폼 shadow rendering과 screen reader grouping은 시각 및 수동 검증 대상으로 남긴다.

## Token mapping

| Elevation | Slot | Property | Token |
| --- | --- | --- | --- |
| All | `root` | Background | color.semantic.background.neutral |
| All | `root` | Shape | shape.semantic.container |
| Flat | `root` | Elevation | 없음 |
| Raised | `root` | Elevation | elevation.semantic.raised |
| Floating | `root` | Elevation | elevation.semantic.floating |

Shadow의 구체적인 rasterization은 Compose와 플랫폼 rendering이 담당한다. Surface 구현은 semantic elevation의 `Dp`를 공통으로 전달하며 플랫폼별 raw shadow 값을 분기하지 않는다.

## Compose API

```kotlin
enum class BeezSurfaceElevation {
    Flat,
    Raised,
    Floating,
}

@Composable
fun BeezSurface(
    modifier: Modifier = Modifier,
    elevation: BeezSurfaceElevation = BeezSurfaceElevation.Flat,
    content: @Composable BoxScope.() -> Unit,
)
```

## Usage

```kotlin
BeezSurface(
    elevation = BeezSurfaceElevation.Raised,
) {
    Column(
        modifier = Modifier.padding(BeezTheme.spacing.screenGutter),
    ) {
        // Related content
    }
}
```

## Incorrect usage

```kotlin
// Surface는 action semantics와 interaction feedback을 제공하지 않는다.
BeezSurface(
    modifier = Modifier.clickable(onClick = ::openDetails),
) {
    // Entire surface behaves like an undocumented button.
}
```

전체 container가 action이면 목적에 맞는 interactive component 또는 명시적인 application-level pattern을 사용한다. 독립적인 button이 Surface 안에 있는 것은 허용한다.

## Platform differences

| Concern | Android | iOS | Desktop | Web |
| --- | --- | --- | --- | --- |
| Rendering | common shape/elevation, Android shadow rasterization | common shape/elevation, Skia shadow rasterization | common shape/elevation, Skia shadow rasterization | common shape/elevation, Canvas/Wasm shadow rasterization |
| Interaction | 없음 | 없음 | 없음 | 없음 |
| Semantics | 추가 node 없음 | 추가 node 없음 | 추가 node 없음 | 추가 node 없음 |

Shadow의 antialiasing과 blur는 rendering backend에 따라 소폭 다를 수 있는 Adaptive 차이다. Shape, background와 semantic elevation 역할은 공통이다.

## Test matrix

### Automated

- [ ] Flat/Raised/Floating token mapping (test added, remote run pending)
- [ ] content rendering (test added, remote run pending)
- [ ] role과 click action을 암묵적으로 추가하지 않음 (test added, remote run pending)
- [ ] 부모 constraint 존중 (test added, remote run pending)
- [ ] RTL과 확대 font scale content 전달 (test added, remote run pending)
- [ ] Light/Dark/Test Brand Catalog scenario (test added, remote run pending)
- [ ] Light/Dark/alternate brand Desktop visual baseline

### Manual / platform follow-up

- [ ] Android shadow rendering과 TalkBack grouping
- [ ] iOS shadow rendering과 VoiceOver grouping
- [ ] Desktop shadow rendering과 screen reader grouping
- [ ] Web shadow rendering과 browser semantics

## Catalog scenarios

- Playground: Flat, Raised와 Floating 전환
- Elevation matrix
- 긴 content와 좁은 constraint
- RTL content
- Light, Dark와 Test Brand theme
- 비대화형 Surface와 clickable Catalog navigation card의 책임 차이

## Known limitations

- Brand와 critical Surface variant를 제공하지 않는다.
- Content color를 암묵적으로 제공하지 않는다.
- Interactive Card API를 제공하지 않는다.
- 실제 플랫폼 shadow와 보조기술 grouping 검증은 완료되지 않았다.
- Desktop visual baseline은 CI rendering 결과를 기록하기 전까지 pending이다.

## Change history

| 날짜 | 변경 | 이유 |
| --- | --- | --- |
| 2026-08-03 | 최초 neutral Surface 명세와 commonMain 구현 | 초기 범위의 네 번째 component vertical slice 정의 |
