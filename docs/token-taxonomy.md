# BEEZ Token Taxonomy

- 상태: Draft
- 최종 수정일: 2026-07-31

## 1. 목적

이 문서는 BEEZ 디자인 토큰의 계층, 분류, 명명 규칙, 테마 확장 방식과 사용 경계를 정의한다.

구체적인 색상값, 크기값 또는 타이포그래피 값은 별도의 토큰 파일에서 관리한다. 이 문서는 값이 아니라 토큰을 구성하고 사용하는 규칙을 다룬다.

## 2. 기본 원칙

### 토큰은 디자인 결정이다

토큰은 단순한 상수 모음이 아니다. 이름, 값, 타입, 설명과 참조 관계를 통해 하나의 디자인 결정을 표현한다.

### 의미가 값보다 우선한다

제품과 컴포넌트는 `orange.500`이나 `dimension.x4`보다 `background.brand`나 `spacing.screen.gutter`처럼 사용 의도가 드러나는 토큰을 우선 사용한다.

### 하나의 원본에서 여러 플랫폼을 만든다

토큰 원본은 플랫폼에 독립적인 구조화 데이터로 관리한다. Kotlin, CSS, 문서 및 디자인 도구용 결과물은 원본에서 생성한다.

### 테마는 semantic token의 집합이다

BEEZ 기본 테마와 제품별 브랜드 테마는 동일한 semantic token 계약을 구현한다. 컴포넌트는 어떤 테마가 선택되었는지 알 필요가 없다.

### 공개 토큰은 API다

토큰 이름의 변경과 제거는 공개 API 변경으로 취급한다. 의미가 바뀌면 기존 토큰의 값을 재활용하지 않고 새로운 이름을 검토한다.

## 3. 표준 형식

토큰 원본은 Design Tokens Community Group의 Design Tokens Format Module 2025.10을 기준으로 작성한다.

- 토큰은 `$type`, `$value`, `$description`을 사용한다.
- 다른 토큰을 참조할 때는 alias 문법을 사용한다.
- 지원하지 않는 도구별 정보는 `$extensions`에 격리한다.
- 표준에 없는 자체 속성을 최상위 토큰 속성으로 추가하지 않는다.
- BEEZ가 지원하는 DTCG 기능의 범위는 JSON Schema로 제한하고 검증한다.

참고 문서: <https://www.designtokens.org/tr/2025.10/format/>

## 4. 토큰 계층

BEEZ는 Scale token과 Semantic token의 두 계층을 기본으로 사용한다.

```text
Raw value
    ↓
Scale token
    ↓
Semantic token
    ↓
Component specification
    ↓
Platform implementation
```

### 4.1 Scale token

Scale token은 BEEZ에서 사용할 수 있는 값의 유한한 집합이다.

```text
color.scale.orange.step500
dimension.scale.x4
fontSize.scale.step500
radius.scale.medium
duration.scale.fast
```

Scale token은 다음 특성을 가진다.

- 값의 크기나 계열을 표현한다.
- 특정 UI 용도를 이름에 포함하지 않는다.
- 여러 semantic token에서 재사용할 수 있다.
- 제품 코드와 컴포넌트 공개 API에서 직접 사용하는 것을 권장하지 않는다.

### 4.2 Semantic token

Semantic token은 값이 사용되는 역할과 의도를 표현하며 하나 이상의 scale token을 참조한다.

```text
color.semantic.background.brand
color.semantic.foreground.primary
color.semantic.stroke.neutral
spacing.semantic.screen.gutter
typography.semantic.screenTitle
shape.semantic.control
duration.semantic.colorTransition
```

Semantic token은 다음 특성을 가진다.

- Light, Dark 및 브랜드 테마에서 동일한 이름을 유지한다.
- 테마마다 참조하는 scale token이나 실제 값이 달라질 수 있다.
- 컴포넌트와 제품 코드가 사용하는 기본 계층이다.
- 이름만으로 역할과 예상 사용처를 이해할 수 있어야 한다.

### 4.3 Component token

초기 BEEZ는 별도의 공개 Component token 계층을 만들지 않는다.

컴포넌트 명세는 semantic token을 조합하여 variant, size, state 및 slot의 스타일을 정의한다.

```text
ActionButton
  brandSolid
    containerColor → color.semantic.background.brand
    contentColor   → color.semantic.foreground.onBrand
    shape          → shape.semantic.control
```

동일한 매핑이 여러 구현에서 반복되거나 테마만으로 컴포넌트 스타일을 교체해야 하는 실제 요구가 확인되면 다음 조건으로 Component token 도입을 검토한다.

- 두 개 이상의 컴포넌트 구현 또는 플랫폼에서 동일한 역할이 반복된다.
- 기존 semantic token으로 표현하면 의미가 왜곡된다.
- 추가 계층이 테마 API를 불필요하게 확대하지 않는다.
- 토큰의 소유권과 공개 여부를 명확하게 정할 수 있다.

도입할 경우 `component.<component>.<variant>.<slot>.<property>` 순서를 사용한다.

```text
component.actionButton.brandSolid.root.background
component.actionButton.brandSolid.label.foreground
```

## 5. 토큰 분류

### 5.1 Color

Color는 palette scale과 역할 기반 semantic token으로 구분한다.

#### Scale

```text
color.scale.<family>.<step>
```

예:

```text
color.scale.gray.step0
color.scale.gray.step100
color.scale.orange.step500
color.scale.red.step600
```

#### Semantic

```text
color.semantic.<role>.<emphasis-or-purpose>
```

초기 role은 다음으로 제한한다.

| Role | 목적 |
| --- | --- |
| `background` | 화면과 요소의 채움 |
| `foreground` | 텍스트와 아이콘 |
| `stroke` | 테두리, 구분선과 포커스 표시 |
| `overlay` | scrim과 반투명 덮개 |

예:

```text
color.semantic.background.brand
color.semantic.background.neutral
color.semantic.background.critical
color.semantic.foreground.primary
color.semantic.foreground.secondary
color.semantic.foreground.onBrand
color.semantic.stroke.neutral
color.semantic.stroke.focus
color.semantic.overlay.scrim
```

`success`, `warning`, `critical` 같은 상태 의미가 필요한 경우 색상 이름이 아닌 사용 목적을 기준으로 추가한다.

### 5.2 Dimension

Dimension scale은 간격, 크기, 모서리 등에서 공유할 수 있는 논리적 크기의 집합이다.

```text
dimension.scale.x0
dimension.scale.x1
dimension.scale.x2
dimension.scale.x3
```

`x1`이 실제로 몇 dp인지 이름에 포함하지 않는다. 플랫폼 변환기는 논리적 dimension 값을 Compose의 `Dp` 등으로 변환한다.

### 5.3 Spacing

Spacing semantic token은 레이아웃이나 콘텐츠 관계의 의도를 표현하고 dimension scale을 참조한다.

```text
spacing.semantic.screen.gutter
spacing.semantic.screen.sectionGap
spacing.semantic.content.inlineGap
spacing.semantic.content.stackGap
spacing.semantic.control.contentGap
```

모든 padding과 gap을 semantic token으로 만들지는 않는다. 컴포넌트 내부의 고정 간격은 컴포넌트 명세가 dimension scale 또는 적절한 spacing token을 참조할 수 있다.

### 5.4 Typography

Typography는 원자적인 scale token과 역할 기반의 composite semantic token으로 구성한다.

#### Scale

```text
fontFamily.scale.sans
fontSize.scale.step100
fontSize.scale.step200
lineHeight.scale.step100
fontWeight.scale.regular
fontWeight.scale.medium
fontWeight.scale.bold
letterSpacing.scale.normal
```

#### Semantic

```text
typography.semantic.display
typography.semantic.screenTitle
typography.semantic.sectionTitle
typography.semantic.body
typography.semantic.label
typography.semantic.caption
```

Semantic typography는 font family, size, weight, line height와 letter spacing을 조합한 composite token으로 표현한다.

사용자 글꼴 크기 설정에 반응하는 스타일을 기본으로 한다. 고정 크기 스타일은 정보 전달에 영향을 주지 않는 제한적인 사례에서만 별도 이름과 근거를 갖고 추가한다.

### 5.5 Radius와 Shape

Radius scale은 원자적인 모서리 값을 제공한다.

```text
radius.scale.none
radius.scale.small
radius.scale.medium
radius.scale.large
radius.scale.full
```

Shape semantic token은 UI 역할에 따라 radius 또는 플랫폼별 shape 표현을 조합한다.

```text
shape.semantic.control
shape.semantic.container
shape.semantic.overlay
shape.semantic.round
```

브랜드 테마가 형태를 변경할 때는 우선 Shape semantic token을 교체한다.

### 5.6 Elevation

Elevation은 시각적 깊이와 표면의 관계를 표현한다.

```text
elevation.scale.level0
elevation.scale.level1
elevation.scale.level2
elevation.semantic.raised
elevation.semantic.floating
elevation.semantic.overlay
```

플랫폼마다 shadow 표현 방식이 다르므로 elevation의 의미는 공통으로 유지하되 실제 렌더링 값은 플랫폼 변환기가 결정할 수 있다.

### 5.7 Motion

Motion은 duration과 easing의 scale, 상호작용 목적을 나타내는 semantic token으로 구성한다.

```text
duration.scale.instant
duration.scale.fast
duration.scale.moderate
duration.scale.slow

easing.scale.standard
easing.scale.emphasized
easing.scale.decelerate

duration.semantic.colorTransition
duration.semantic.pressedScale
easing.semantic.enter
easing.semantic.exit
```

모션 감소 설정이 활성화된 환경에서는 semantic motion이 축소되거나 제거될 수 있어야 한다.

### 5.8 State

State는 독립적인 시각 속성 하나가 아니라 여러 토큰과 동작의 조합이다.

초기 공통 상태는 다음과 같다.

```text
enabled
pressed
focused
selected
disabled
loading
error
```

모든 컴포넌트가 모든 상태를 구현할 필요는 없다. 컴포넌트 명세에서 지원 상태, 상태 조합의 우선순위와 각 slot에 적용되는 token을 정의한다.

상태 표현을 위해 공통 opacity나 motion 값이 필요하면 다음과 같은 semantic token을 사용할 수 있다.

```text
opacity.semantic.disabled
opacity.semantic.pressedOverlay
```

## 6. 명명 규칙

### 기본 구조

토큰 경로는 가장 넓은 분류에서 구체적인 역할 순서로 작성한다.

```text
<category>.<tier>.<group>.<role>
```

모든 토큰이 네 단계일 필요는 없다. 의미 없는 중간 group을 추가하지 않는다.

### 문자 규칙

- 토큰 경로 segment는 영문 소문자 camelCase를 기본으로 한다.
- 공백, 밑줄과 임의의 약어를 사용하지 않는다.
- 숫자로 시작해야 하는 scale 단계는 `x1` 또는 `step100`처럼 유효한 식별자로 정규화한다.
- 생성기는 플랫폼 관례에 맞게 이름을 변환하되 원본 token path와의 연결 정보를 보존한다.
- `bg`, `fg`, `sm`, `lg`보다 `background`, `foreground`, `small`, `large`처럼 의미가 분명한 이름을 사용한다.

### 이름에 포함하지 않는 정보

- 실제 값: `spacing.16dp`
- 특정 화면 모드: `background.dark`
- 특정 플랫폼: `radius.android`
- 일시적인 프로젝트명이나 화면명
- 시각적 인상만 나타내는 모호한 표현: `pretty`, `subtle2`, `newGray`

모드와 플랫폼 차이는 token path가 아니라 theme mapping과 transformer에서 처리한다.

## 7. 테마 모델

하나의 BEEZ Theme은 다음 scheme의 조합으로 구성한다.

```text
BeezTheme
├── ColorScheme
├── TypographyScheme
├── ShapeScheme
├── SpacingScheme
└── MotionScheme
```

### BEEZ 기본 테마

BEEZ는 다음 기본 조합을 제공한다.

```text
BeezLightTheme
BeezDarkTheme
```

### 브랜드 테마

Compose 구현에서는 beez-tokens의 BeezTokenScheme과 beez-foundation의 BeezTheme provider가 이 계약을 제공한다. 컴포넌트는 BeezTheme에서 semantic scheme을 읽고 appearance나 브랜드 이름을 직접 분기하지 않는다.

브랜드는 BEEZ semantic token 계약을 구현하는 scheme을 제공한다.

```text
BrandA Light ColorScheme
BrandA Dark ColorScheme
BrandA ShapeScheme
```

브랜드가 모든 값을 다시 정의하도록 강제하지 않는다. BEEZ 기본 scheme을 기반으로 필요한 semantic token만 명시적으로 교체할 수 있는 빌더 또는 copy API를 검토한다.

부분 교체 API는 다음 조건을 만족해야 한다.

- 누락된 값은 BEEZ 기본값으로 안전하게 돌아간다.
- Light와 Dark에서 필요한 값이 명확하게 구분된다.
- 잘못된 전경과 배경 조합을 테스트할 수 있다.
- 런타임에서 임의의 원시 값을 컴포넌트에 주입하는 우회로가 되지 않는다.

### 초기 테마 축

초기에는 두 개의 독립적인 축만 다룬다.

```text
Brand: BEEZ 또는 제품 브랜드
Appearance: Light 또는 Dark
```

Density, high contrast, dynamic color와 같은 축은 실제 요구와 플랫폼 지원 전략을 별도 ADR로 결정한 후 추가한다.

## 8. 토큰 파일 구성

초기 원본 파일은 변경 이유와 검토 범위를 명확히 하기 위해 category 단위로 나눈다.

```text
specification/tokens/
├── scale/
│   ├── color.tokens.json
│   ├── dimension.tokens.json
│   ├── typography.tokens.json
│   ├── radius.tokens.json
│   ├── elevation.tokens.json
│   └── motion.tokens.json
├── semantic/
│   ├── color-light.tokens.json
│   ├── color-dark.tokens.json
│   ├── typography.tokens.json
│   ├── spacing.tokens.json
│   ├── shape.tokens.json
│   ├── elevation.tokens.json
│   └── motion.tokens.json
└── themes/
    └── beez.theme.json
```

브랜드 토큰은 동일 저장소에서 관리할 필요가 확인되기 전까지 BEEZ 핵심 토큰과 분리한다.

## 9. 예시

다음 예시는 구조를 설명하기 위한 것이며 BEEZ의 실제 색상값을 확정하지 않는다.

```json
{
  "color": {
    "scale": {
      "$type": "color",
      "orange": {
        "step500": {
          "$description": "Orange palette step 500",
          "$value": {
            "colorSpace": "srgb",
            "components": [1, 0.42, 0.08],
            "alpha": 1
          }
        }
      }
    },
    "semantic": {
      "background": {
        "brand": {
          "$type": "color",
          "$description": "Primary brand background",
          "$value": "{color.scale.orange.step500}"
        }
      }
    }
  }
}
```

실제 파일에서는 scale과 semantic mode 파일을 분리하며, 빌드 과정에서 alias를 해석하고 스키마를 검증한다.

현재 repository에는 이 구조의 provisional 원본이 specification/tokens/에 추가되어 있다. Compose public token 계약은 beez-tokens/src/commonMain의 scheme 타입으로 제공하며, token generator가 도입되기 전까지 JSON 원본과 Kotlin 값을 같은 변경 단위로 검토한다.

## 10. 사용 규칙

### 제품 코드

- semantic token을 우선 사용한다.
- 원시 `Color`, `Dp`, `TextStyle` 값을 반복해서 사용하지 않는다.
- 제품에만 필요한 값은 먼저 제품 로컬 token으로 관리한다.
- 공통성이 확인되면 BEEZ token 승격을 제안한다.

### 컴포넌트 구현

- semantic token 또는 컴포넌트 명세에 승인된 scale token만 사용한다.
- 테마 값을 Composable 내부에 복사하거나 하드코딩하지 않는다.
- 상태별 token mapping을 컴포넌트 명세와 일치시킨다.
- 플랫폼 예외가 필요하면 코드뿐 아니라 명세에도 이유를 기록한다.

### 브랜드 테마

- palette 값보다 semantic mapping을 검토 단위로 삼는다.
- 전경과 배경 token을 쌍으로 검증한다.
- 형태 변경은 shape scheme 범위 안에서 수행한다.
- 컴포넌트 구조와 동작 변경은 theme이 아니라 컴포넌트 API 또는 별도 패턴으로 다룬다.

## 11. 변경과 폐기

- 토큰 추가에는 목적, 사용처, 기존 토큰으로 해결할 수 없는 이유가 필요하다.
- 기존 토큰의 의미를 다른 용도로 바꾸지 않는다.
- 이름 변경은 기존 token을 deprecated 처리하고 대체 token을 안내한다.
- 제거는 major version 변경 또는 명시한 안정성 정책을 따른다.
- scale 값 변경은 해당 값을 참조하는 모든 semantic token의 영향을 검토한다.
- semantic mapping 변경은 Light, Dark와 지원 브랜드 조합을 모두 검증한다.

## 12. 검증 요구사항

토큰 빌드는 최소한 다음을 자동 검증해야 한다.

- JSON 및 DTCG subset schema 유효성
- token path 중복
- 존재하지 않는 alias 참조
- 순환 참조
- 지원하지 않는 token type
- Light와 Dark semantic token 계약의 일치
- 생성된 Kotlin 식별자 충돌
- deprecated token의 대체 정보
- Color 전경 및 배경 조합의 접근성 기준

## 13. 후속 결정

다음 항목은 별도 문서 또는 ADR에서 결정한다.

- Scale token의 실제 값과 단계
- Color space와 접근성 대비 기준
- 논리적 dimension의 기준 단위와 플랫폼 변환 규칙
- Typography scale과 시스템 폰트 정책
- DTCG subset JSON Schema
- 테마 상속 또는 copy API의 Kotlin 형태
- 생성 코드의 package와 public/internal 경계
- Figma variable naming과 token path 매핑
