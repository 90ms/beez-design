# ADR-0011: Migrate the Showcase to a Compose Multiplatform Catalog

- 상태: Accepted
- 결정일: 2026-07-31
- 대체: ADR-0010

## 문맥

BEEZ의 Showcase는 token, theme와 component를 실제 사용 방식으로 검토하는 카탈로그여야 한다. 현재 `showcase/`는 HTML, CSS와 JavaScript로 구현된 정적 prototype이며, BEEZ Compose component와 별도의 렌더링 경로를 사용한다.

현재 component 수가 많지 않고 `commonMain` API가 이미 존재하므로, 두 렌더링 경로를 장기간 유지하는 비용보다 실제 component를 직접 사용하는 Compose Catalog를 빠르게 구축하는 편이 검증 가치가 높다.

## 결정

기존 JavaScript Showcase를 Compose Multiplatform Web/Wasm 기반의 `beez-catalog` 애플리케이션으로 직접 마이그레이션한다.

- Catalog는 `beez-components`를 project dependency로 직접 사용한다.
- component와 Catalog 화면의 공통 UI 코드는 `commonMain`에 둔다.
- 웹 진입점은 `wasmJs { browser() }` target으로 제공한다.
- Catalog는 Material 2/3에 의존하지 않고 BEEZ의 token, foundation과 component API만 사용한다.
- 기존 정적 파일은 마이그레이션 기간 동안 참고 및 복구 지점으로 유지하고, Compose Catalog 배포가 확인되면 삭제한다.
- GitHub Pages 배포는 기존 정적 artifact 조립 대신 Wasm browser distribution을 artifact로 사용한다.

## 화면 및 상태 범위

초기 마이그레이션은 기존 Showcase의 정보 구조와 동작을 보존한다.

- Overview: BEEZ 원칙과 현재 지원 상태
- Foundations: color, typography, spacing, shape, elevation, motion
- Themes: Light/Dark appearance와 BEEZ/Test Brand mapping
- Components: Action Button과 Text Field의 실제 variant/state preview
- 한국어/영어 locale 전환
- 브라우저 기본 locale 감지
- Overview의 `Design with meaning.` 문구는 영어 고정

Catalog의 component 예제는 HTML preview를 복제하지 않고 `beez-components`의 실제 Compose API를 호출한다. 테마와 입력 상태도 Compose state로 관리한다.

## 검증 및 배포 순서

1. `beez-catalog` Gradle module과 Wasm 진입점을 추가한다.
2. 공통 Catalog shell, navigation, locale과 theme state를 구현한다.
3. token/foundation 화면과 실제 BEEZ component scenario를 연결한다.
4. Compose UI semantics 및 interaction test를 추가한다.
5. GitHub Actions에서 Wasm distribution을 빌드하고 GitHub Pages에 배포한다.
6. 배포된 Catalog를 확인한 뒤 기존 JavaScript Showcase와 정적 전용 workflow를 제거한다.

로컬 Synology 환경에서는 Gradle compile을 실행하지 않는다. 빌드, 테스트와 Pages 배포 검증은 GitHub Actions를 통해 수행한다.

## 결과

- Showcase가 실제 BEEZ 소비자 애플리케이션이 되어 public API를 지속적으로 검증한다.
- 테마, 상태, interaction과 Compose semantics의 drift를 줄인다.
- Web/Wasm은 여전히 Experimental 플랫폼으로 표시하고, 브라우저 호환성과 CI 검증 결과를 문서화한다.
- Catalog 애플리케이션은 라이브러리 배포 artifact와 분리된 workflow로 운영한다.

## ADR-0010과의 관계

ADR-0010의 정적 Showcase 우선 결정은 초기 visual feedback을 위한 단계적 선택이었다. 현재 사용자 결정에 따라 최종 Showcase 기술을 Compose Catalog로 확정하며, ADR-0010은 이 ADR로 대체한다.

## 현재 결과

Compose Catalog의 Wasm distribution과 GitHub Pages 배포가 GitHub Actions에서 성공한 뒤 기존 `showcase/` HTML, CSS와 JavaScript 파일을 제거했다. 현재 Pages의 기본 Showcase는 `beez-catalog` artifact를 사용한다.

초기 migration 이후 Components 정보 구조는 실제 component preview를 포함한 card overview와 개별 detail destination으로 발전했다. Detail은 명세를 별도 원본으로 복제하지 않고 Playground, Anatomy, Properties, Guidelines와 Accessibility 관점으로 현재 `docs/components` 계약을 설명한다. 이 변경은 Compose Catalog와 `beez-components` 직접 소비라는 기존 결정을 유지한다.
