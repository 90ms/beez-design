# ADR-0010: BEEZ showcase strategy

- 상태: Superseded by ADR-0011
- 결정일: 2026-07-31

> 이 결정은 초기 정적 Showcase prototype을 위한 단계적 전략이었다. 최종 Showcase를 Compose Multiplatform Catalog로 직접 마이그레이션하는 현재 결정은 [ADR-0011](0011-compose-catalog-migration.md)을 따른다.

## 문맥

BEEZ는 token, theme, component를 눈으로 검토하고 조정할 수 있는 catalog가 필요하다. 현재 Compose Multiplatform Gradle scaffold는 compile 검증 전이고 Web/Wasm은 Experimental이므로, 첫 visual feedback loop를 Compose Web build에만 의존하면 반복 속도가 느리고 환경 문제의 영향을 크게 받는다.

## 결정

Showcase를 두 단계로 운영한다.

1. 정적 Showcase prototype을 먼저 제공한다.
2. token과 component contract가 안정되면 같은 scenario를 Compose Web Catalog로 옮긴다.

정적 prototype은 최종 component 구현의 source of truth가 아니다. specification/tokens의 JSON과 component 명세를 읽어 시각 검토용 결과를 제공하는 consumer다.

## 정적 Showcase prototype

- repository 안의 showcase/ 디렉터리에 둔다.
- HTML, CSS, JavaScript만 사용하며 새로운 runtime dependency나 Gradle module을 추가하지 않는다.
- specification/tokens의 DTCG JSON을 fetch하고 alias를 해석해 CSS custom property와 token card를 만든다.
- Light, Dark, BEEZ 기본 테마와 test brand를 같은 semantic role contract에서 전환한다.
- Action Button의 variant, size, state, long label, focus와 loading scenario를 제공한다.
- prototype 상태와 compile 미검증 상태를 화면과 문서에 표시한다.

## Compose Web Catalog 전환

다음 조건을 만족하면 beez-catalog 또는 동등한 catalog module을 검토한다.

- 하나 이상의 component API가 실제 consumer sample에서 검증되었다.
- 공통 scenario model과 상태 matrix가 정리되었다.
- Web/Wasm toolchain을 반복적으로 실행할 수 있다.
- 접근성 semantics와 screenshot 검증을 catalog에서 자동화할 수 있다.

Compose Web Catalog는 실제 BEEZ commonMain component를 사용해야 하며 정적 prototype의 별도 CSS 구현을 source로 복사하지 않는다.

## 데이터와 scenario 경계

- token 값과 alias의 원본은 specification/tokens다.
- component 목적, API, state와 token mapping의 원본은 docs/components다.
- showcase JavaScript는 loader와 rendering adapter만 소유한다.
- 동일한 token이나 component 값을 showcase 파일에 수동 복제하지 않는다.
- 시나리오 이름은 component 명세와 일치시킨다.

## 배포

- 첫 단계는 로컬 정적 서버와 GitHub Pages로 제공한다.
- 기본 배포 대상은 main branch의 showcase prototype이다.
- Maven artifact release와 Showcase 배포를 하나의 workflow에 결합하지 않는다.
- PR preview와 custom domain은 실제 사용 필요가 확인된 뒤 추가한다.

## 결과

- Gradle이 멈추는 환경에서도 token과 component의 시각 조정을 시작할 수 있다.
- 정적 prototype과 Compose implementation 사이의 drift 위험을 source-of-truth 규칙으로 제한한다.
- 최종적으로는 실제 Compose component를 사용하는 Catalog로 품질 검증을 강화할 수 있다.
- 초기에는 두 rendering 경로를 유지하는 비용이 발생한다.

## 대안

### Compose Web Catalog부터 구축

최종 구현과 동일한 결과를 얻지만 현재 Web/Wasm 빌드 불안정성과 반복 속도 문제가 첫 visual feedback을 지연시킨다. 2단계로 미룬다.

### React 또는 Vite 기반 문서 사이트를 최종 catalog로 채택

문서 사이트에는 적합하지만 Compose component와 별도 UI 구현이 되어 component behavior와 semantics 검증에 한계가 있다. 최종 catalog 기본 경로로 채택하지 않는다.
