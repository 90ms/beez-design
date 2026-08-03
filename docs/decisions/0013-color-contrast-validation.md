# ADR-0013: Semantic color contrast validation

- 상태: Accepted
- 결정일: 2026-08-03

## 문맥

BEEZ는 semantic foreground/background와 stroke 조합을 테마 계약으로 제공하지만, 현재 token validator는 값 구조와 alias만 확인한다. [ADR-0012](0012-token-generation-pipeline.md)는 접근성 대비 기준을 후속 결정 전까지 미검증 범위로 남겼다.

브랜드는 semantic color를 교체할 수 있으므로 기본 Light/Dark 값만 수동으로 확인해서는 잘못된 조합을 지속적으로 차단할 수 없다. 반대로 모든 foreground와 모든 background의 곱을 검사하면 실제로 인접하지 않는 조합까지 실패시켜 semantic 역할의 의미를 왜곡한다. 검증 대상 쌍과 기준을 명시적으로 결정해야 한다.

WCAG 2.2 Level AA는 일반 텍스트에 4.5:1 이상의 대비를 요구하고, UI component와 상태를 식별하는 필수 시각 정보에는 인접 색상 대비 3:1 이상을 요구한다. 비활성 UI는 규범상 예외지만, BEEZ의 `foreground.secondary`는 활성 보조 텍스트에도 사용되므로 비활성 전용 역할로 간주할 수 없다.

- [WCAG 2.2 SC 1.4.3 Contrast (Minimum)](https://www.w3.org/TR/WCAG22/#contrast-minimum)
- [WCAG 2.2 SC 1.4.11 Non-text Contrast](https://www.w3.org/TR/WCAG22/#non-text-contrast)

## 현재 감사 결과

WCAG 2.x sRGB relative luminance 공식을 원본 DTCG 값에 적용한 결과는 다음과 같다. 수치는 표시를 위해 소수 둘째 자리로 반올림했지만, 실제 판정은 반올림하지 않는다.

### Text pairs

| Foreground | Background | Light | Dark | Test Brand |
| --- | --- | ---: | ---: | ---: |
| `foreground.primary` | `background.neutral` | 17.13 | 13.27 | 17.13 |
| `foreground.secondary` | `background.neutral` | 6.45 | 10.05 | 6.45 |
| `foreground.critical` | `background.neutral` | 6.46 | 10.09 | 6.46 |
| `foreground.onBrand` | `background.brand` | 7.31 | 9.05 | 5.79 |

### Non-text pairs

| Foreground | Background | Light | Dark | Test Brand |
| --- | --- | ---: | ---: | ---: |
| `stroke.neutral` | `background.neutral` | 4.55 | 5.41 | 4.55 |
| `stroke.focus` | `background.neutral` | 7.31 | 9.72 | 5.79 |
| `stroke.critical` | `background.neutral` | 6.46 | 10.09 | 6.46 |

명시한 token pair는 제안 기준을 모두 통과한다. `background.critical`은 현재 core component의 content container로 사용하지 않으므로 임의의 foreground pair를 만들지 않는다. 새로운 component가 이 배경 위에 텍스트나 필수 상태 표시를 배치하면 해당 명세와 pair registry를 함께 추가해야 한다.

Token pair와 별개로 Action Button의 현재 focus border는 모든 variant에서 `stroke.focus`를 사용한다. BrandSolid에서는 `stroke.focus`와 `background.brand`가 기본 Light/Dark 및 Test Brand에서 같은 값이므로 내부 border가 시각적으로 구분되지 않는다. 대비 validator 구현 단계에서 BrandSolid의 focus indicator를 variant-aware mapping으로 수정하고 keyboard/visual test를 추가해야 한다.

## 결정

BEEZ token validation에 WCAG 2.2 Level AA 기반의 명시적 semantic pair registry를 추가한다.

1. 일반 크기에서도 재사용하는 foreground text pair는 4.5:1 이상이어야 한다.
2. control 경계, focus와 error state를 식별하는 non-text pair는 3:1 이상이어야 한다.
3. reusable semantic role에는 large-text 3:1 예외를 적용하지 않는다. 실제 typography 크기와 무관하게 안전한 조합을 제공한다.
4. disabled state의 규범상 예외를 token role 전체에 적용하지 않는다. 비활성 전용 component 표현은 component 명세에서 별도로 검토한다.
5. 판정은 반올림하지 않은 비율로 수행한다.
6. 초기 pair의 foreground와 background는 불투명 sRGB여야 한다. `overlay.scrim`처럼 합성 배경이 필요한 역할은 backing color와 compositing 규칙을 별도 pair로 정의하기 전까지 제외한다.
7. Light, Dark와 repository에 포함된 모든 theme fixture에 같은 registry를 적용한다.
8. validator 통과는 실제 component adjacency, focus indicator, disabled 표현과 플랫폼 렌더링 검증을 대체하지 않는다.

## 구현

다음 항목을 한 단계로 반영한다.

- `scripts/token-tools.mjs`에 relative luminance, contrast ratio와 pair validator를 추가한다.
- 초기 구현은 Text 4개와 non-text 3개 pair로 시작한다. 새 component가 실제로 사용하는 인접 조합은 component 명세와 함께 registry에 추가하고 Light/Dark/Test Brand에서 검증한다.
- 경계값, 실패 pair, alpha와 현재 repository context를 Node.js test로 검증한다.
- `validate-tokens.mjs` 출력에 검사한 pair와 최소 비율을 표시한다.
- Action Button의 BrandSolid focus indicator를 인접 container와 구분되는 variant-aware color로 수정하고 keyboard 및 visual test를 추가한다.
- token taxonomy, theme guide, Action Button 명세와 ADR-0012의 현재 결과를 갱신한다.
- Library Validation과 Pages 배포를 확인한다.

새 runtime dependency나 공개 Kotlin API는 추가하지 않는다. 별도의 `stroke.onBrand` 같은 public semantic role이 필요하다고 판단되면 이 ADR에서 암묵적으로 추가하지 않고 token API 변경으로 다시 검토한다.

## 대안

### 모든 color role의 조합을 검사

구현은 단순하지만 실제로 인접하지 않는 조합이 실패하고 semantic 역할의 사용 계약을 표현하지 못한다. 채택하지 않는 것을 제안한다.

### 현재 값만 report하고 CI를 실패시키지 않음

초기 감사에는 유용하지만 브랜드 override의 회귀를 차단하지 못한다. 전환 기간이 필요하지 않은 현재 초기 단계에서는 채택하지 않는 것을 제안한다.

### APCA를 단독 기준으로 사용

향후 더 발전된 perceptual contrast 평가를 검토할 수 있으나, 초기 공개 기준은 현재 명세와 도구 생태계에서 널리 검증 가능한 WCAG 2.2 Level AA로 둔다. APCA 도입은 기준 변경 ADR로 별도 검토한다.

## 결과

- 기본 theme과 Test Brand의 의도된 semantic pair가 token source 변경 때마다 자동 검증된다.
- 새 theme fixture는 기존 semantic contract뿐 아니라 동일한 대비 기준도 통과해야 한다.
- Checkbox는 실제 인접 조합인 `background.brand` on `background.neutral` non-text pair를 registry에 추가한다.
- pair registry에 없는 임의 조합을 안전하다고 주장하지 않으며 component adjacency 검토는 계속 필요하다.
- 대비 기준이나 pair 분류를 바꾸는 경우 이 ADR을 대체하는 결정을 작성한다.
