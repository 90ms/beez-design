# BEEZ Design System

> [한국어](#한국어) · [English](#english)

## 한국어

BEEZ는 Android, iOS, Desktop, Web에서 같은 컴포넌트 API를 사용할 수 있도록 설계하는 Compose Multiplatform 디자인 시스템입니다.

Material 3 컴포넌트에 핵심 구현을 의존하지 않고, BEEZ만의 토큰·테마·컴포넌트 명세를 기준으로 공통 UI를 제공합니다.

### 현재 상태

현재는 초기 아키텍처와 Foundation을 정리하는 단계입니다. provisional semantic token source, Kotlin scheme API와 Compose Multiplatform Web/Wasm Catalog 초기 화면을 추가했습니다. 기존 JavaScript Showcase는 Compose Catalog로 전환되어 제거되었으며, 아직 Stable 컴포넌트 artifact를 배포하지 않았습니다.

쇼케이스: [90ms.github.io/beez-design](https://90ms.github.io/beez-design/)

Gradle 프로젝트 골격은 생성되어 있으며, 컴파일과 테스트는 Synology 로컬이 아닌 GitHub Actions에서 검증합니다. 첫 library validation workflow가 성공했으며, iOS Simulator 테스트는 Linux runner 제약으로 건너뜁니다.

### 모듈

```text
beez-catalog (Compose Web/Wasm application)
        ↓
beez-components → beez-foundation → beez-tokens
```

- `beez-tokens`: 토큰 값, scheme, 생성 토큰 API
- `beez-foundation`: 테마, CompositionLocal, 공통 UI 기반
- `beez-components`: 공개 BEEZ 컴포넌트 API와 구현
- `beez-catalog`: 실제 BEEZ 컴포넌트를 사용하는 Compose Web/Wasm 쇼케이스 애플리케이션

첫 번째 공통 컴포넌트로 Action Button 명세와 provisional implementation을 추가했습니다. GitHub Actions에서 library build와 공통 테스트가 통과했으며, API는 아직 Experimental입니다.

두 번째 컴포넌트로 단일 행 Text Field 명세와 provisional implementation을 Experimental 상태로 추가했습니다. value state, 오류/읽기 전용 상태와 접근성 semantics를 정의했으며 오류 전용 semantic color token도 추가했습니다. GitHub Actions에서 Android/Desktop/Wasm compile, 공통 테스트와 Compose UI semantics 테스트가 통과했습니다.

Catalog는 실제 `beez-components`를 사용하는 Compose Multiplatform 애플리케이션으로 제공됩니다. icons, adapters와 documentation tooling은 실제 사용 사례가 확인된 뒤 추가합니다.

### 핵심 원칙

- 구현보다 명세를 먼저 작성합니다.
- 원시 값보다 semantic token을 사용합니다.
- BEEZ 핵심 모듈은 Material 3와 독립적입니다.
- 공개 컴포넌트 API와 기본 구현은 `commonMain`에 둡니다.
- 접근성과 지원 입력 방식은 완료 조건입니다.
- 구현, 테스트, 카탈로그, 문서를 함께 변경합니다.

### 지원 기준

- Android: API 24 이상
- iOS: iOS 14 이상
- Desktop: JDK 17 이상
- Web: WasmGC 지원 브라우저, Experimental

도구 버전과 플랫폼별 검증 상태는 [호환성 문서](docs/compatibility.md)에서 관리합니다.

### 문서

- [프로젝트 헌장](docs/charter.md)
- [토큰 분류 및 명명 규칙](docs/token-taxonomy.md)
- [컴포넌트 명세 템플릿](docs/component-template.md)
- [완료 기준](docs/definition-of-done.md)
- [플랫폼 정책](docs/platform-policy.md)
- [호환성](docs/compatibility.md)
- [배포 가이드](docs/publishing.md)
- [테마 가이드](docs/theme.md)
- [토큰 원본](specification/tokens/)
- [Action Button 명세](docs/components/action-button.md)
- [Text Field 명세](docs/components/text-field.md)
- [Showcase 가이드](docs/showcase.md)
- [아키텍처 결정](docs/decisions/)
- [에이전트 작업 규칙](AGENTS.md)
- [BEEZ Design System Skill](skills/beez-design-system/)

## English

BEEZ is a Compose Multiplatform design system designed to provide one shared component API across Android, iOS, Desktop, and Web.

The core does not depend on Material 3 components. BEEZ defines its own tokens, themes, and component specifications, then uses them to deliver shared UI behavior across platforms.

### Status

BEEZ is currently in its initial architecture and foundation stage. A provisional semantic token source, Kotlin scheme API, and an initial Compose Multiplatform Web/Wasm Catalog are present. The legacy JavaScript Showcase has been replaced and removed, and no stable component artifact has been released yet.

Showcase: [90ms.github.io/beez-design](https://90ms.github.io/beez-design/)

The Gradle project skeleton is present, and compile/test validation runs on GitHub Actions instead of the Synology host. The first library validation workflow passed; iOS Simulator tests are skipped because the runner is Linux.

### Modules

```text
beez-catalog (Compose Web/Wasm application)
        ↓
beez-components → beez-foundation → beez-tokens
```

- `beez-tokens`: token values, schemes, and generated token APIs
- `beez-foundation`: theme, CompositionLocal, and shared UI foundations
- `beez-components`: public BEEZ component APIs and implementations
- `beez-catalog`: Compose Web/Wasm Showcase application consuming the actual BEEZ components

The first Action Button specification and provisional implementation are present. The library build and common tests pass on GitHub Actions, but the API remains Experimental.

The second component, a single-line Text Field specification and provisional implementation, is now Experimental. Its value state, error/read-only behavior, and accessibility semantics are defined, with dedicated critical semantic color roles added. Android/Desktop/Wasm compilation, common tests, and Compose UI semantics tests pass in GitHub Actions.

The Catalog is being migrated to a Compose Multiplatform application that consumes the actual `beez-components` APIs. Icons, adapters, and documentation tooling will be added when validated use cases require them.

### Principles

- Write specifications before implementation.
- Prefer semantic tokens over raw values.
- Keep the BEEZ core independent from Material 3.
- Put public component APIs and default implementations in `commonMain`.
- Treat accessibility and supported input methods as completion requirements.
- Evolve implementation, tests, catalog scenarios, and documentation together.

### Support baseline

- Android: API 24 or newer
- iOS: iOS 14 or newer
- Desktop: JDK 17 or newer
- Web: WasmGC-capable browsers, Experimental

Tool versions and platform verification status are maintained in the [compatibility document](docs/compatibility.md).

### Documentation

- [Project charter](docs/charter.md)
- [Token taxonomy](docs/token-taxonomy.md)
- [Component specification template](docs/component-template.md)
- [Definition of Done](docs/definition-of-done.md)
- [Platform policy](docs/platform-policy.md)
- [Compatibility](docs/compatibility.md)
- [Publishing guide](docs/publishing.md)
- [Theme guide](docs/theme.md)
- [Token sources](specification/tokens/)
- [Action Button specification](docs/components/action-button.md)
- [Text Field specification](docs/components/text-field.md)
- [Showcase guide](docs/showcase.md)
- [Architecture decisions](docs/decisions/)
- [Agent workflow rules](AGENTS.md)
- [BEEZ Design System Skill](skills/beez-design-system/)

## License

The license has not been selected yet. It will be added before the first public library release.
