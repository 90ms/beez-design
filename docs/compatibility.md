# BEEZ Compatibility

- 상태: Draft
- 최종 수정일: 2026-08-03

## 1. 목적

이 문서는 BEEZ를 빌드하고 사용하는 데 필요한 toolchain, 플랫폼 및 검증 상태를 관리한다.

ADR은 기준을 선택한 이유를 기록하고, 이 문서는 실제로 검증한 버전과 알려진 제약을 계속 갱신한다.

## 2. 현재 빌드 기준선

| 항목 | 버전 | 상태 |
| --- | --- | --- |
| Kotlin | 2.4.10 | Verified in GitHub Actions |
| Kotlin Multiplatform plugin | 2.4.10 | Verified in GitHub Actions |
| Compose Compiler plugin | 2.4.10 | Verified in GitHub Actions |
| Compose Multiplatform plugin | 1.11.1 | Verified in GitHub Actions |
| Android Gradle Plugin | 9.1.0 | Verified in GitHub Actions |
| Gradle distribution | 9.3.1 | Verified in GitHub Actions |
| Gradle wrapper | Not committed | Deferred |
| Gradle runtime JDK | 17 이상 | Verified in GitHub Actions |
| Android compile SDK | API 37 | Compiles with AGP warning |

`Selected`는 프로젝트 구성에 사용할 버전이며 아직 모든 target build가 검증되었다는 의미는 아니다. Gradle 실행은 현재 GitHub Actions workflow가 `9.3.1`을 직접 설치하는 방식으로 수행한다. 초기 기준은 [첫 성공 실행](https://github.com/90ms/beez-design/actions/runs/30609163681)이다. Semantic color contrast, token source와 생성물 drift, Action Button/Checkbox/Text Field/Surface 공통 UI 및 Catalog ChromeHeadless 테스트, Desktop visual baseline, 전체 KMP staging publication, artifact audit와 독립 Android/Desktop consumer compile을 포함한 library validation은 [Run #30880978121](https://github.com/90ms/beez-design/actions/runs/30880978121)에서 성공했다. 반응형 component card overview, 한국어·영어 상세 guide와 한글 font coverage를 포함한 같은 revision의 GitHub Pages Wasm distribution 배포는 [Run #30880978066](https://github.com/90ms/beez-design/actions/runs/30880978066)에서 성공했으며, 두 실행 모두 Node.js 24 기반 action major를 사용한다. `0.1.0-alpha.1` 좌표의 versioned staging dry-run과 artifact 보관은 [Run #30789231140](https://github.com/90ms/beez-design/actions/runs/30789231140)에서 성공했다.

## 2.1 GitHub Actions 검증

- workflow: `.github/workflows/library-validation.yml`
- runner: `ubuntu-latest`
- GitHub-hosted action runtime: Node.js 24 기반 action major 사용
- runtime JDK: Temurin 17
- Gradle: `gradle/actions/setup-gradle`로 9.3.1 설치
- Token source 검증: JSON layout, type, unit, alias, Light/Dark semantic contract, Test Brand override, Kotlin identifier와 Light/Dark/Test Brand의 등록된 semantic color contrast pair
- Token generation 검증: Node.js tooling test와 commit된 Kotlin 생성물 drift 검사
- Library 검증: 모든 library target의 `build` task
- Publication 검증: 세 공개 모듈의 Android, Desktop, iOS arm64, iOS Simulator arm64, Wasm 및 root metadata를 repository-local Maven staging 경로에 publish하고 독립 consumer fixture에서 Android 및 Desktop compile
- Publication artifact 검사: 18개 publication의 POM/Gradle module identity, primary/source artifact, SHA-256 checksum, 정렬된 내부 dependency와 Material 비포함
- Desktop visual 검증: Action Button, Checkbox, Text Field와 Surface의 Light/Dark/alternate brand normalized baseline 비교
- Catalog 검증: `beez-catalog` Wasm browser test와 production distribution 배포
- repository mode: Kotlin/Wasm의 Node.js toolchain repository를 사용할 수 있도록 project repository를 우선

현재 Synology 로컬 환경에서는 Gradle을 실행하지 않는다. 로컬 성능 이슈를 피하고, CI runner의 결과를 compile/test 검증의 기준으로 사용한다.

첫 성공 실행에서 확인된 알려진 warning:

- `wasmJs` target 선언은 `ExperimentalWasmDsl` opt-in이 필요하다.
- Compose dependency shorthand 일부가 deprecated 상태다.
- AGP 9.1.0은 compile SDK 37에 대해 공식 테스트 범위가 36.1까지라는 warning을 출력한다.
- iOS Simulator test는 Linux runner에서 실행할 수 없어 skip된다.

## 3. 최소 플랫폼

| Target | 최소 환경 | 검증 상태 |
| --- | --- | --- |
| Android | API 24 | Verified in CI build |
| iOS arm64 | iOS 14 | Compiles in CI; device test pending |
| iOS Simulator arm64 | iOS 14 | Blocked on Linux; test skipped |
| Desktop JVM | JDK 17 | Verified in CI build |
| Web Wasm | WasmGC 지원 브라우저 | Compiles and test task passes in CI |

필요성이 확인되기 전까지 iOS x64 simulator와 추가 native architecture를 공개 지원 대상으로 약속하지 않는다. Target 추가는 사용 환경과 CI 실행 가능성을 함께 검토한다.

## 4. Gradle plugin 정책

### Kotlin 및 Compose

```text
Kotlin plugin            2.4.10
Compose Compiler plugin  2.4.10
Compose plugin           1.11.1
```

Compose Compiler plugin은 Kotlin과 같은 버전을 사용한다.

### Android KMP

공유 library module에는 다음 plugin을 사용한다.

```text
com.android.kotlin.multiplatform.library
```

KMP module에 legacy `com.android.library` plugin을 함께 적용하지 않는다.

Android application entry point가 필요하면 공유 library와 분리된 application module로 구성한다.

## 5. Source target 계획

초기 library target은 다음과 같다.

```text
android
iosArm64
iosSimulatorArm64
jvm("desktop")
wasmJs
```

모든 target을 처음부터 release-ready로 간주하지 않는다. `docs/platform-policy.md`의 지원 등급과 component별 상태를 함께 표시한다.

## 6. 검증 상태 용어

| 상태 | 의미 |
| --- | --- |
| Not configured | Build 설정이 없음 |
| Configured | Target과 task가 생성됨 |
| Compiles | 대표 main/test compilation 성공 |
| Verified | CI 및 정의된 target 검증 완료 |
| Blocked | 알려진 외부 또는 내부 문제로 검증 불가 |

## 7. 호환성 검증 절차

Toolchain 또는 플랫폼 기준을 바꿀 때 다음을 확인한다.

1. Kotlin, Compose, AGP 및 Gradle 공식 호환 범위를 확인한다.
2. Version catalog와 CI Gradle distribution을 한 변경으로 갱신한다. wrapper를 도입할 때는 같은 변경에서 함께 갱신한다.
3. 모든 public library target을 compile한다.
4. Common test와 가능한 platform test를 실행한다.
5. Catalog target의 build 또는 run smoke test를 수행한다.
6. 생성 metadata와 publication artifact를 확인한다.
7. 독립 consumer fixture에서 공개 artifact와 전이 dependency를 resolve한다.
8. 이 문서의 상태와 알려진 문제를 갱신한다.

## 8. 소비자 호환성

BEEZ가 첫 Experimental artifact를 배포할 때 다음 소비자 조합을 추가한다.

- BEEZ가 빌드된 기준 toolchain
- 지원 범위 안의 대표 최신 toolchain
- 최소 Android SDK consumer
- Material 3가 없는 consumer
- Material 3를 별도로 사용하는 consumer

핵심 BEEZ artifact는 Material 3가 없는 consumer에서 동작해야 한다.

## 9. 열린 항목

- Desktop 운영체제별 최소 버전과 packaging JDK
- iOS Xcode 최소 및 검증 버전
- Web browser test matrix
- CI host와 native target 실행 범위
- Kotlin 및 Compose 하위 호환 범위
- Binary compatibility validator 도구
- Dependency verification 및 lock 정책
