# BEEZ Showcase Guide

- 상태: Draft
- 기준일: 2026-07-31

BEEZ Showcase는 token, theme, component를 시각적으로 검토하는 페이지다. 현재는 Gradle과 독립적인 정적 prototype이며, 이후 실제 Compose Web Catalog로 확장할 수 있다.

## 실행

repository root에서 정적 서버를 실행한다.

```text
python3 -m http.server 8080
```

브라우저에서 http://localhost:8080/showcase/ 를 연다. file:// 로 직접 열면 JSON fetch가 차단될 수 있으므로 정적 서버를 사용한다.

## 페이지 구성

- Overview: BEEZ 원칙, 지원 플랫폼, 현재 maturity
- Foundations: color, typography, spacing, shape, elevation, motion
- Themes: Light, Dark, BEEZ, Test Brand 전환
- Components: Action Button variant, size, state matrix
- Accessibility: touch target, focus, loading, long label과 font scale 안내

## Source of truth

| 표시 영역 | 원본 |
| --- | --- |
| Token 값과 alias | specification/tokens |
| Theme 역할과 확장 규칙 | docs/token-taxonomy.md, docs/theme.md |
| Component API와 동작 | docs/components |
| 현재 구현 | beez-tokens, beez-foundation, beez-components |

Showcase JavaScript는 원본을 복제하지 않고 token loader와 rendering adapter만 제공한다. 시각적 차이가 발견되면 showcase를 임의로 고치는 대신 token 또는 component 명세를 먼저 검토한다.

## Prototype와 최종 Catalog

현재 정적 prototype은 다음을 빠르게 조정하기 위한 consumer다.

- 브랜드 accent와 semantic foreground/background 조합
- Light/Dark theme 대비
- Action Button variant와 size hierarchy
- disabled, loading, focus 표현
- 긴 label, narrow viewport와 responsive layout

prototype은 실제 Compose semantics나 platform accessibility bridge를 검증하지 않는다. Web/Wasm build 환경이 안정되고 scenario model이 검증되면 동일한 시나리오를 Compose Web Catalog로 옮긴다.

## 배포 상태

- 로컬 정적 서버: 현재 사용 가능
- GitHub Pages: .github/workflows/showcase-pages.yml로 workflow 구성됨
- Compose Web Catalog: Planned
- Stable 문서 사이트: 미정

GitHub repository Settings에서 Pages의 publishing source를 GitHub Actions로 선택해야 실제 배포 URL이 활성화된다. 활성화 후 기본 URL은 repository Pages 주소이며, root는 showcase/로 redirect한다.

## 기여 규칙

- 새 component를 추가할 때 명세, showcase scenario와 문서 링크를 함께 추가한다.
- token 값을 HTML, CSS, JavaScript에 직접 복사하지 않는다.
- Prototype에서만 가능한 시각 표현은 화면에 prototype 상태를 표시한다.
- component API나 token 역할을 바꾸면 관련 ADR과 showcase를 같은 변경 단위로 갱신한다.
