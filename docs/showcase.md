# BEEZ Showcase Guide

- 상태: Migration in progress
- 기준일: 2026-07-31

BEEZ Showcase는 token, theme, component를 실제 BEEZ API로 시각적으로 검토하는 Compose Multiplatform Catalog다. 기존 HTML/CSS/JavaScript prototype을 `beez-catalog` Web/Wasm 애플리케이션으로 직접 마이그레이션한다. 마이그레이션 중에는 기존 정적 파일을 참고용으로 유지한다.

## 현재 실행

현재 배포된 prototype을 확인하려면 repository root에서 정적 서버를 실행한다.

```text
python3 -m http.server 8080
```

브라우저에서 http://localhost:8080/showcase/ 를 연다. `beez-catalog`의 Wasm distribution 실행 방법은 GitHub Actions 배포 전환 단계에서 추가한다.

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
- 상단의 언어 전환 버튼으로 즉시 변경할 수 있으며, 선택한 언어는 같은 브라우저의 다음 방문에도 유지한다.
- 번역되지 않은 token key와 API 예시는 원본 식별자를 보존해 문서와 구현을 대조할 수 있도록 한다.

## Source of truth

| 표시 영역 | 원본 |
| --- | --- |
| Token 값과 alias | specification/tokens |
| Theme 역할과 확장 규칙 | docs/token-taxonomy.md, docs/theme.md |
| Component API와 동작 | docs/components |
| 현재 구현 | beez-tokens, beez-foundation, beez-components |

Showcase JavaScript는 원본을 복제하지 않고 token loader와 rendering adapter만 제공한다. 시각적 차이가 발견되면 showcase를 임의로 고치는 대신 token 또는 component 명세를 먼저 검토한다.

## Compose Catalog 마이그레이션

Compose Catalog는 다음을 실제 BEEZ component와 Compose state로 제공한다.

- 브랜드 accent와 semantic foreground/background 조합
- Light/Dark theme 대비
- 상단 Light/Dark toggle과 Themes Appearance/Brand mapping 전환
- Action Button variant와 size hierarchy
- disabled, loading, focus 표현
- 버튼 callback, disabled/loading 전환과 reset 동작
- Text Field value 입력, error/read-only/disabled 전환과 reset 동작
- 긴 label, narrow viewport와 responsive layout

기존 prototype은 마이그레이션 기간의 시각 비교와 복구 지점으로만 사용한다. 최종 Catalog는 HTML preview나 별도 CSS component를 복사하지 않고 `beez-components`의 `commonMain` API를 직접 호출한다.

Overview의 `Design with meaning.` 문구는 브랜드 메시지로 취급해 locale과 관계없이 영어로 고정한다. 나머지 설명과 control label은 브라우저 locale 또는 상단 언어 선택을 따른다.

## 배포 상태

- 로컬 정적 서버: 현재 사용 가능
- `beez-catalog` Compose Web/Wasm 모듈: navigation, locale, theme와 실제 Action Button/Text Field를 포함한 초기 vertical slice
- GitHub Pages: .github/workflows/showcase-pages.yml로 workflow 구성됨
- Compose Web Catalog: Migration in progress (initial vertical slice)
- Stable 문서 사이트: 미정

GitHub repository Settings에서 Pages의 publishing source를 GitHub Actions로 선택해야 실제 배포 URL이 활성화된다. 마이그레이션 완료 후 Pages artifact는 Compose Web/Wasm distribution을 포함한다.

## 기여 규칙

- 새 component를 추가할 때 명세, showcase scenario와 문서 링크를 함께 추가한다.
- token 값을 HTML, CSS, JavaScript에 직접 복사하지 않는다.
- Prototype에서만 가능한 시각 표현은 화면에 prototype 상태를 표시한다.
- component API나 token 역할을 바꾸면 관련 ADR과 showcase를 같은 변경 단위로 갱신한다.
