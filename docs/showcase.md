# BEEZ Showcase Guide

- 상태: Active
- 기준일: 2026-07-31

BEEZ Showcase는 token, theme, component를 실제 BEEZ API로 시각적으로 검토하는 Compose Multiplatform Catalog다. `beez-catalog` Web/Wasm 애플리케이션이 GitHub Pages의 기본 Showcase를 제공하며, 기존 HTML/CSS/JavaScript prototype은 배포 전환 후 제거되었다.

## 실행

배포된 Catalog를 연다.

[90ms.github.io/beez-design](https://90ms.github.io/beez-design/)

로컬 Synology에서는 Gradle을 실행하지 않는다. GitHub Actions가 Wasm distribution을 빌드하고 GitHub Pages에 배포한다.

## 페이지 구성

- Overview: BEEZ 원칙, 지원 플랫폼, 현재 maturity
- Foundations: color, typography, spacing, shape, elevation, motion
- Themes: Light, Dark, BEEZ, Test Brand 전환
- Components: Action Button variant, size, state matrix와 callback preview; Text Field 입력, error, read-only, disabled preview
- Accessibility: touch target, focus, loading, long label과 font scale 안내

## 언어와 locale

페이지는 별도 runtime dependency 없이 한국어와 영어를 제공한다.

- 첫 방문 시 브라우저의 `navigator.languages`를 확인하며, `ko`로 시작하면 한국어를 기본값으로 선택한다.
- 한국어가 아니면 영어를 기본값으로 사용한다.
- 상단의 언어 전환 버튼으로 즉시 변경할 수 있다.
- 번역되지 않은 token key와 API 예시는 원본 식별자를 보존해 문서와 구현을 대조할 수 있도록 한다.

Wasm 런타임이 브라우저의 비표준 locale 문자열(`en-US@posix` 등)을 만나도 Compose 텍스트 locale 초기화가 실패하지 않도록 배포 HTML에서 locale을 정규화한다.

## Source of truth

| 표시 영역 | 원본 |
| --- | --- |
| Token 값과 alias | specification/tokens |
| Theme 역할과 확장 규칙 | docs/token-taxonomy.md, docs/theme.md |
| Component API와 동작 | docs/components |
| 현재 구현 | beez-tokens, beez-foundation, beez-components, beez-catalog |

Catalog 화면은 원본 명세를 복제하지 않고 Kotlin token scheme과 `beez-components` API를 직접 사용한다. 시각적 차이가 발견되면 Catalog를 임의로 고치는 대신 token 또는 component 명세를 먼저 검토한다.

## Compose Catalog

Compose Catalog는 다음을 실제 BEEZ component와 Compose state로 제공한다.

- 브랜드 accent와 semantic foreground/background 조합
- Light/Dark theme 대비
- 상단 Light/Dark toggle과 Themes Appearance/Brand mapping 전환
- Action Button variant와 size hierarchy
- disabled, loading, focus 표현
- 버튼 callback, disabled/loading 전환과 reset 동작
- Text Field value 입력, error/read-only/disabled 전환과 reset 동작
- 긴 label, narrow viewport와 responsive layout

Catalog는 HTML preview나 별도 CSS component를 복사하지 않고 `beez-components`의 `commonMain` API를 직접 호출한다. 공통 UI 테스트는 GitHub Actions의 Wasm browser test로 실행한다.

Overview의 `Design with meaning.` 문구는 브랜드 메시지로 취급해 locale과 관계없이 영어로 고정한다. 나머지 설명과 control label은 브라우저 locale 또는 상단 언어 선택을 따른다.

## 배포 상태

- `beez-catalog` Compose Web/Wasm 모듈: navigation, locale, theme와 실제 Action Button/Text Field를 포함한 초기 vertical slice 및 공통 UI 테스트
- GitHub Pages: Compose Web/Wasm distribution 배포 workflow 구성됨
- Compose Web Catalog: Active (initial vertical slice)
- Stable 문서 사이트: 미정

배포 HTML은 `html`과 `body`를 viewport 크기로 고정한다. Compose `ComposeViewport`가 문서의 콘텐츠 높이를 viewport로 오인하지 않도록 하는 Web/Wasm 런타임 전제다.

GitHub repository Settings에서 Pages의 publishing source를 GitHub Actions로 선택해야 실제 배포 URL이 활성화된다. Pages artifact는 `beez-catalog` Compose Web/Wasm distribution을 포함한다.

배포된 Catalog: [90ms.github.io/beez-design](https://90ms.github.io/beez-design/)

## 기여 규칙

- 새 component를 추가할 때 명세, showcase scenario와 문서 링크를 함께 추가한다.
- token 값을 HTML, CSS, JavaScript에 직접 복사하지 않는다.
- Catalog에서만 가능한 시각 표현은 실제 component API와 semantics를 기준으로 유지한다.
- component API나 token 역할을 바꾸면 관련 ADR과 showcase를 같은 변경 단위로 갱신한다.
